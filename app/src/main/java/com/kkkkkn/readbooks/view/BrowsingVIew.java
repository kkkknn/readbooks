package com.kkkkkn.readbooks.view;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.text.Layout;
import android.text.StaticLayout;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Scroller;

import androidx.annotation.Nullable;

import com.kkkkkn.readbooks.R;

import java.util.ArrayList;
import java.util.Arrays;

import static androidx.constraintlayout.widget.Constraints.TAG;

public class BrowsingVIew extends View {
    //当前时间字符串
    private String timeStr;
    //当前章节名字
    private String chapterNameStr;
    //当前章节/总章节字符串
    private String progressStr;
    //当前电量字符串
    private String batteryStr;
    //当前划屏位置
    private float mClipX = 0;
    //左右滑动偏移量 变量
    private float offsetX=0;
    //控件宽高
    private int mViewHeight = 0, mViewWidth = 0;
    //当前章节文字
    private char[] textContent;
    //当前章节字符串
    private String contentStr;
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

    public String getTimeStr() {
        return timeStr;
    }

    public void setTimeStr(String timeStr) {
        this.timeStr = timeStr;
    }

    public String getChapterNameStr() {
        return chapterNameStr;
    }

    public void setChapterNameStr(String chapterNameStr) {
        this.chapterNameStr = chapterNameStr;
    }

    public String getProgressStr() {
        return progressStr;
    }

    public void setProgressStr(String progressStr) {
        this.progressStr = progressStr;
    }

    public String getBatteryStr() {
        return batteryStr;
    }

    public void setBatteryStr(String batteryStr) {
        this.batteryStr = batteryStr;
    }

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
        mPaint.setShadowLayer(10f, 0, 0, Color.GRAY);
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
        contentStr=textContent;
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
                offsetX=0;
                //判断是否需要自动滑动回去
                //判断是否需要变化当前页
                if(drawStyle==1){
                    //左滑动自动绘制+下一页变当前页
                    Log.i(TAG, "onTouchEvent: 向左滑动抬起");
                    /*while(mClipX>0){
                        mClipX--;
                        invalidate();
                    }*/
                    textContentCount+=textLineSum*linePageSum;
                }else if(drawStyle==2) {
                    //右滑动自动绘制+上一页变当前页
                    Log.i(TAG, "onTouchEvent: 向右滑动抬起");
                    /*while(mClipX<mViewWidth){
                        mClipX++;
                        invalidate();
                    }*/
                    textContentCount-=textLineSum*linePageSum;
                }
                //防止坐标为负的情况出现
                /*if(textContentCount<0){
                    textContentCount=0;
                }
                //防止最后一页报错
                if(textContentCount>=textSum){
                    textContentCount-=textLineSum*linePageSum;
                }*/
                drawStyle=0;
                performClick();
                break;
            case MotionEvent.ACTION_MOVE:
                //判断向左还是向右滑动
                float x = event.getX();
                offsetX+=x-mClipX;
                if (offsetX< 0) {
                    //向左滑动
                    if (drawStyle == 0) {
                        drawStyle = 1;
                    }
                } else if (offsetX > 0) {
                    //向右滑动
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
        //判断是否需要绘制
        if(textContentCount<0||textContent==null||textContentCount>=textSum){
            Log.i(TAG, "drawBitmap: 超出长度或没有文字内容，忽略绘制");
            return;
        }
        boolean flag=false;
        //根据drawstyle 决定绘制左边还是右边
        if (drawStyle == 1) {
            flag=left_drawBitmap(canvas);
        } else if (drawStyle == 2) {
            flag=right_drawBitmap(canvas);
        }else{
            flag=true;
        }
        //绘制当前页
        if (textSum == 0 || thisBitmap == null||textContentCount<=0||flag) {
            center_drawBitmap(canvas);
        }
    }

    //绘制当前阅读界面
    private void center_drawBitmap(Canvas canvas) {
        if(thisBitmap==null){
            thisBitmap = Bitmap.createScaledBitmap(backBitmap, mViewWidth, mViewHeight, true);
        }

        //计算要绘制文字数量
        int textPageSum = linePageSum * textLineSum;
        if ((textContentCount + textPageSum) >= textSum) {
            textPageSum = textSum - textPageSum;
        }

        //裁切绘制
        canvas.save();

        //canvas.clipRect(offsetX, 0, mViewWidth, mViewHeight);
        canvas.drawBitmap(thisBitmap,offsetX,0, mPaint);

        //canvas.drawRoundRect(0,0,offsetX,mViewHeight,5,5,mPaint);

        StaticLayout layout =StaticLayout.Builder.obtain(contentStr,textContentCount,textPageSum,mTextPaint,mViewWidth)
                .setAlignment(Layout.Alignment.ALIGN_NORMAL)
                .setLineSpacing(0.0F,1F)
                .setIncludePad(false)
                .build();
        canvas.translate(offsetX,0);
        layout.draw(canvas);
        canvas.restore();

        //绘制当前章节名字，时间，电量，浏览进度
        /*if(timeStr!=null&&chapterNameStr!=null&&batteryStr!=null&&progressStr!=null){
            canvas.drawText(timeStr,drawOffset,statusBarHeight-textSize,mTextPaint);
            canvas.drawText(chapterNameStr,drawOffset,mViewHeight,mTextPaint);
            canvas.drawText(batteryStr,drawOffset+(mViewWidth-textSize*batteryStr.length()),statusBarHeight-textSize,mTextPaint);
            canvas.drawText(progressStr,drawOffset+(mViewWidth-textSize*batteryStr.length()),mViewHeight,mTextPaint);
        }
*/
    }

    //左滑动绘制情况
    private boolean left_drawBitmap(Canvas canvas) {
        //计算要绘制文字数量
        int startIndex = textContentCount + linePageSum * textLineSum;
        int endIndex=startIndex+linePageSum * textLineSum;
        if(startIndex>=textSum) {
            Log.i(TAG, "left_drawBitmap: 到头了，最左边");
            return false;
        }else if(endIndex>textSum){
            endIndex=textSum-startIndex;
        }

        //绘制下一页面内容
        if (nextBitmap == null) {
            nextBitmap = Bitmap.createScaledBitmap(backBitmap, mViewWidth, mViewHeight, true);
        }
        canvas.drawBitmap(nextBitmap, 0, 0, mPaint);

        StaticLayout layout =StaticLayout.Builder.obtain(contentStr,startIndex,endIndex,mTextPaint,mViewWidth)
                .setAlignment(Layout.Alignment.ALIGN_NORMAL)
                .setLineSpacing(0.0F,1F)
                .setIncludePad(false)
                .build();

        canvas.save();
        layout.draw(canvas);
        canvas.restore();

        //绘制当前章节名字，时间，电量，浏览进度
       /* if(timeStr!=null&&chapterNameStr!=null&&batteryStr!=null&&progressStr!=null) {
            canvas.drawText(timeStr,0,statusBarHeight-textSize,mTextPaint);
            canvas.drawText(chapterNameStr,0,mViewHeight,mTextPaint);
            canvas.drawText(batteryStr,(mViewWidth-textSize*batteryStr.length()),statusBarHeight-textSize,mTextPaint);
            canvas.drawText(progressStr,(mViewWidth-textSize*batteryStr.length()),mViewHeight,mTextPaint);
        }*/
        return true;
    }

    //右滑动绘制情况
    private boolean right_drawBitmap(Canvas canvas) {
        //计算要绘制文字数量
        int startIndex = textContentCount - linePageSum * textLineSum;
        int endIndex = startIndex+linePageSum * textLineSum;
        if(startIndex<0) {
            Log.i(TAG, "right_drawBitmap: 到头了，最左边");
            return false;
        }

        if (lastBitmap == null) {
            lastBitmap = Bitmap.createScaledBitmap(backBitmap, mViewWidth, mViewHeight, true);
        }
        canvas.drawBitmap(lastBitmap, 0, 0, mPaint);


        StaticLayout layout =StaticLayout.Builder.obtain(contentStr,startIndex,endIndex,mTextPaint,mViewWidth)
                .setAlignment(Layout.Alignment.ALIGN_NORMAL)
                .setLineSpacing(0.0F,1F)
                .setIncludePad(false)
                .build();

        canvas.save();
        layout.draw(canvas);
        canvas.restore();
        //绘制当前章节名字，时间，电量，浏览进度
        /*if(timeStr!=null&&chapterNameStr!=null&&batteryStr!=null&&progressStr!=null) {
            canvas.drawText(timeStr,0,statusBarHeight-textSize,mTextPaint);
            canvas.drawText(chapterNameStr,0,mViewHeight,mTextPaint);
            canvas.drawText(batteryStr,(mViewWidth-textSize*batteryStr.length()),statusBarHeight-textSize,mTextPaint);
            canvas.drawText(progressStr,(mViewWidth-textSize*batteryStr.length()),mViewHeight,mTextPaint);

        }*/

        return true;
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




    private int getStatusBarHeight(Context context) {
        Resources resources = context.getResources();
        int resourceId = resources.getIdentifier("status_bar_height", "dimen", "android");
        return resources.getDimensionPixelSize(resourceId);
    }

}
