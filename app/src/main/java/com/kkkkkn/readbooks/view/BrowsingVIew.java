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
    private char[] textContent;
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
    private static Bitmap lastBitmap;
    //下一页bitmap
    private static Bitmap nextBitmap;
    //当前页bitmap
    private static Bitmap thisBitmap;
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
        mTextPaint.setTextSize(textSize);
        this.textSize = textSize;
        //重新计算行数和每行字数
        computeValue();
    }

    public void setTextColor(int textColor) {
        mTextPaint.setColor(textColor);
        this.textColor = textColor;
    }

    //计算绘制相关变量
    private void computeValue() {
        //计算页面 绘制的总字数，行数，每行字数
        textLineSum = mViewWidth / (int) textSize;
        linePageSum = (mViewHeight - statusBarHeight) / (int) textSize;

    }

    public void setTextContent(String textContent) {
        this.textContent = textContent.toCharArray();
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
                //判断是否需要自动滑动回去
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

        //根据drawstyle 决定绘制左边还是右边
        if (drawStyle == 1) {
            left_drawBitmap(canvas);
        } else if (drawStyle == 2) {
            right_drawBitmap(canvas);
        }

        //绘制当前页
        center_drawBitmap(canvas);
    }

    //绘制当前阅读界面
    private void center_drawBitmap(Canvas canvas) {
        //无文字情况直接退出
        if (textSum == 0) {
            Log.i(TAG, "center_drawBitmap: 直接退出，芜湖");
            return;
        }
        if(thisBitmap==null){
            thisBitmap = Bitmap.createScaledBitmap(backBitmap, mViewWidth, mViewHeight, true);
        }
        //计算要绘制文字数量
        int textPageSum = linePageSum * textLineSum;
        int drawLineNum = linePageSum;
        //绘制偏移量
        int drawOffset=0;
        //判断是否需要裁切绘制
        canvas.save();
        if ((textContentCount + textPageSum) > textSum) {
            textPageSum = textSum - textPageSum;
            drawLineNum = textPageSum % textLineSum == 0 ? textPageSum / textLineSum : textPageSum / textLineSum + 1;
        }

        if(drawStyle==1){
            canvas.clipRect(0, 0, mClipX, mViewHeight);
            Log.i(TAG, "center_drawBitmap: 左滑动");
            drawOffset=-(int)(mViewWidth-mClipX);
        }else if(drawStyle==2){
            canvas.clipRect(mClipX, 0, mViewWidth, mViewHeight);
            Log.i(TAG, "center_drawBitmap: 右滑动");
            drawOffset=(int)mClipX;
        }
        canvas.drawBitmap(thisBitmap, 0, 0, mPaint);
        //绘制文字
        for (int i = 0; i < drawLineNum; i++) {
            canvas.drawText(textContent, textContentCount+textLineSum*i, textLineSum, drawOffset, (float) (textSize * i + statusBarHeight), mTextPaint);
        }
        canvas.restore();
    }

    //左滑动绘制情况
    private void left_drawBitmap(Canvas canvas) {
        Log.i(TAG, "left_drawBitmap: 左滑动绘制");
        //判断文字情况，无文字直接推出绘制
        if (textSum == 0 || thisBitmap == null) {
            Log.i(TAG, "left_drawBitmap: 无文字情况，退出");
            return;
        }
        //绘制下一页面内容
        if (nextBitmap == null) {
            nextBitmap = Bitmap.createScaledBitmap(backBitmap, mViewWidth, mViewHeight, true);
        }
        canvas.drawBitmap(nextBitmap, 0, 0, mPaint);
        //计算要绘制文字数量
        int textPageSum = linePageSum * textLineSum;
        int drawLineNum = linePageSum;
        if ((textContentCount + textPageSum) > textSum) {
            textPageSum = textSum - textPageSum;
            drawLineNum = textPageSum % textLineSum == 0 ? textPageSum / textLineSum : textPageSum / textLineSum + 1;
        }
        int draw_textIndex = textContentCount + linePageSum * textLineSum;
        //绘制文字
        for (int i = 0; i < drawLineNum; i++) {
            canvas.drawText(textContent, draw_textIndex, textLineSum, 0, textSize * i + statusBarHeight, mTextPaint);
            draw_textIndex += textLineSum;
        }

    }

    //右滑动绘制情况
    private void right_drawBitmap(Canvas canvas) {
        Log.i(TAG, "right_drawBitmap: 右滑动绘制");
        //计算要绘制文字数量
        int textPageSum = linePageSum * textLineSum;
        int drawLineNum = linePageSum;
        //判断文字情况，无文字直接推出绘制
        if (textSum == 0 || thisBitmap == null||textContentCount<textPageSum) {
            Log.i(TAG, "left_drawBitmap: 无文字情况，退出");
            return;
        }
        if (lastBitmap == null) {
            lastBitmap = Bitmap.createScaledBitmap(backBitmap, mViewWidth, mViewHeight, true);
        }
        canvas.drawBitmap(lastBitmap, 0, 0, mPaint);

        if ((textContentCount + textPageSum) > textSum) {
            textPageSum = textSum - textPageSum;
            drawLineNum = textPageSum % textLineSum == 0 ? textPageSum / textLineSum : textPageSum / textLineSum + 1;
        }
        int draw_textIndex = textContentCount - linePageSum * textLineSum;
        //绘制文字
        for (int i = 0; i < drawLineNum; i++) {
            canvas.drawText(textContent, textLineSum*i, textLineSum, 0, textSize * i + statusBarHeight, mTextPaint);
            draw_textIndex += textLineSum;

        }

    }


    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        mViewHeight = h;
        mViewWidth = w;
        super.onSizeChanged(w, h, oldw, oldh);
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        drawBitmap(canvas);
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
