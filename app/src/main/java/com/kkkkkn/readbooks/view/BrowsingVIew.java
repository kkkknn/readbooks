package com.kkkkkn.readbooks.view;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Scroller;

import androidx.annotation.Nullable;

import com.kkkkkn.readbooks.R;

import java.util.ArrayList;

import static androidx.constraintlayout.widget.Constraints.TAG;

public class BrowsingVIew extends View {
    private ArrayList<Bitmap> bitmapArrayList = new ArrayList<>();
    //当前划屏位置
    private float mClipX = 0;
    //控件宽高
    private int mViewHeight = 0, mViewWidth = 0;
    //当前章节文字
    private String textContent;
    //当前页面显示章节进度    textContent的count
    private int textContentCount = 0;
    //当前章节总字数
    private int textSum;
    //文字大小
    private float textSize = 40f;
    //文字颜色
    private int textColor = Color.BLACK;
    //每行文字数量
    private int textLineSum;
    //每页行数
    private int linePageSum;
    //展示模式相关 true 左滑动绘制下一页  false 右滑动绘制上一页
    private int drawStyle = 0;
    //是否需要进行吸附处理
    private boolean isAdsorb = false;
    //上一页bitmap
    private Bitmap lastBitmap;
    //下一页bitmap
    private Bitmap nextBitmap;
    //状态栏高度
    private int statusBarHeight;
    //绘图相关变量
    private TextPaint mTextPaint;
    private Paint mPaint;
    private Bitmap backBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.browsingview);

    public BrowsingVIew(Context context) {
        super(context);
        initView(context);
    }

    public BrowsingVIew(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    public BrowsingVIew(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);

    }

    //初始化view
    private void initView(Context context) {
        mViewWidth = getWidth();
        mViewHeight = getHeight();
        statusBarHeight = getStatusBarHeight(context);
        mTextPaint = new TextPaint();
        mPaint = new Paint();

        mPaint.setAntiAlias(true);
        mPaint.setStyle(Paint.Style.FILL);
        mTextPaint.setTextSize(textSize);
        mTextPaint.setColor(textColor);
        mTextPaint.setAntiAlias(true);
    }

    public void setTextSize(float textSize) {
        this.textSize = textSize;
        //重新计算行数和每行字数
        computeValue();
    }

    public void setTextColor(int textColor) {
        this.textColor = textColor;
    }

    //计算绘制相关变量
    private void computeValue() {
        //计算页面 绘制的总字数，行数，每行字数
        textLineSum = mViewWidth / (int) textSize;
        linePageSum = (mViewHeight - statusBarHeight) / (int) textSize;

    }

    public void setTextContent(String textContent) {
        this.textContent = textContent;
        textSum = textContent.toCharArray().length;
    }

    @Override
    public boolean performClick() {
        return super.performClick();
    }

    //手势判断
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mClipX = event.getX();
                break;
            case MotionEvent.ACTION_UP:
                mClipX = -1;
                drawStyle = 0;
                performClick();
                break;
            case MotionEvent.ACTION_MOVE:
                //判断向左还是向右滑动
                float x = event.getX();
                if (mClipX - x > 0) {
                    //向左滑动
                    Log.i(TAG, "onTouchEvent: 向左滑动");
                    if (drawStyle == 0) {
                        drawStyle = 1;
                    }

                } else if (mClipX - x < 0) {
                    //向右滑动
                    Log.i(TAG, "onTouchEvent: 向右滑动");
                    if (drawStyle == 0) {
                        drawStyle = 2;
                    }
                }
                mClipX = x;
                break;
        }
        //重新绘制界面
        invalidate();
        return true;
    }

    //绘制阅读界面 2个页面  1，当前页面  2，根据当前手势判断绘制上一页/下一页
    private void drawBitmap(Canvas canvas) {
        //判断绘制模式
        if (drawStyle == 1) {
            //首先绘制下一页
            left_drawBitmap(canvas);
        } else if (drawStyle == 2) {
            //首先绘制上一页
            right_drawBitmap(canvas);
        }
        //绘制背景

        //计算要绘制的文字

        //绘制文字
        for (int i = 0; i < bitmapArrayList.size(); i++) {
            canvas.save();
            //仅裁切最上层图像
            if (i == bitmapArrayList.size() - 1) {
                canvas.clipRect(0, 0, mClipX, mViewHeight);
            }

            canvas.drawBitmap(bitmapArrayList.get(i), 0, 0, null);
            canvas.restore();
        }

    }

    //左滑动绘制情况
    private void left_drawBitmap(Canvas canvas) {
        //判断文字情况，无文字直接推出绘制
        if (textContent == null || textContent.isEmpty()) {
            return;
        }
        //绘制下一页面内容
        if (lastBitmap == null) {
            lastBitmap = Bitmap.createScaledBitmap(backBitmap, mViewWidth, mViewHeight, true);
            canvas.drawBitmap(lastBitmap, 0, 0, mPaint);
            //计算要绘制文字数量
            int textPageSum = linePageSum * textLineSum;
            int drawLineNum = linePageSum;
            if ((textContentCount + textPageSum) > textSum) {
                textPageSum = textSum - textPageSum;
                drawLineNum = textPageSum % textLineSum == 0 ? textPageSum / textLineSum : textPageSum / textLineSum + 1;
            }
            //绘制文字
            for(int i=0;i<drawLineNum;i++){
                //canvas.drawText();
            }

        }

    }

    //右滑动绘制情况
    private void right_drawBitmap(Canvas canvas) {

    }


    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        mViewHeight = h;
        mViewWidth = w;
        super.onSizeChanged(w, h, oldw, oldh);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        drawBitmap(canvas);
        super.onDraw(canvas);
    }


    //章节生成bitmap并保存到对象中
    public boolean chapter2Bitmap(String chapterContent) {
        if (chapterContent == null || chapterContent.isEmpty()) {
            return false;
        }
        Log.i(TAG, "chapter2Bitmap: " + chapterContent);
        Bitmap mBitmap = null;
        Paint mPaint = new Paint();
        Canvas mCanvas = new Canvas();
        TextPaint mTextPaint = new TextPaint();

        mPaint.setAntiAlias(true);
        mPaint.setStyle(Paint.Style.FILL);
        mTextPaint.setTextSize(40f);
        mTextPaint.setColor(Color.BLACK);
        mTextPaint.setAntiAlias(true);
        int height = getStatusBarHeight(getContext());
        //将字体大小向下取整
        int size = (int) mTextPaint.getTextSize();
        char[] arr = chapterContent.toCharArray();
        //总字数
        int textSum = arr.length;
        //每行字数
        int textLine_sum = mViewWidth / size;
        //每页行数
        int linePage_sum = mViewHeight / size;
        //总行数
        int lineSum_page = textSum / textLine_sum == 0 ? textSum / textLine_sum : textSum / textLine_sum + 1;
        int lineCount = 0;
        //绘制文字到bitmap中
        while (lineCount != lineSum_page - 1) {
            //创建新页面并绘制背景
            mBitmap = Bitmap.createScaledBitmap(backBitmap, mViewWidth, mViewHeight, true);
            mCanvas.drawBitmap(mBitmap, 0, 0, mPaint);
            //绘制一页
            for (int i = 0; i < linePage_sum; i++) {
                if (lineCount == lineSum_page - 1) {
                    break;
                }
                int drawCount = Math.min((textSum - i * textLine_sum), textLine_sum);
                mCanvas.drawText(arr, lineCount * textLine_sum, drawCount, 0, size * i + height, mPaint);
                lineCount++;
            }
            //保存当前页面到list
            bitmapArrayList.add(mBitmap);
            invalidate();
        }

        Log.i(TAG, "chapter2Bitmap: 绘制完成" + bitmapArrayList.size());
        return true;
    }

    private Bitmap testDraw2() {
        Canvas canvas = new Canvas();
        Bitmap mBitmap = Bitmap.createScaledBitmap(backBitmap, mViewWidth, mViewHeight, true);
        Paint paint = new Paint();
        paint.setAntiAlias(true);
        paint.setStyle(Paint.Style.FILL);
        canvas.drawBitmap(mBitmap, 0, 0, paint);
        TextPaint point = new TextPaint();
        point.setTextSize(40f);
        point.setColor(Color.BLACK);
        point.setAntiAlias(true);
        int height = getStatusBarHeight(getContext());
        String str = "生命中的痛苦就像是盐的咸味一样，就这么多。而我们所能感受和体验的程度，取决于我们将它放在多大的容器里。你的心量越小，烦恼就多了，心量大了，能承受的就多了，生活就不那么苦了。用敞亮的心去看待世界，世界就是闪闪发光的，相反，用阴暗的心去看待世界，世界就永无发光之日。";
        //将字体大小向下取整
        int size = (int) point.getTextSize();
        char[] arr = str.toCharArray();
        int num = getWidth() / size;
        //行数需要向上取整， 不能强制转换
        int line = arr.length / num == 0 ? arr.length / num : arr.length / num + 1;
        for (int i = 0; i < line; i++) {
            int count = arr.length - i * num;
            canvas.drawText(arr, i * num, Math.min(count, num), 0, size * i + height, point);
        }
        canvas.save();
        canvas.restore();
        return mBitmap;
    }

    public void testDraw() {
        bitmapArrayList.add(testDraw2());
        bitmapArrayList.add(BitmapFactory.decodeResource(getResources(), R.drawable.bookshelf));
        invalidate();
    }

    private int getStatusBarHeight(Context context) {
        Resources resources = context.getResources();
        int resourceId = resources.getIdentifier("status_bar_height", "dimen", "android");
        return resources.getDimensionPixelSize(resourceId);
    }

}
