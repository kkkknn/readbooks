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


public class BrowsingVIew extends View {
    private final static String TAG="BrowsingVIew";
    //当前时间字符串
    private String timeStr;
    //当前章节名字
    private String chapterNameStr;
    //当前章节/总章节字符串
    private String progressStr;
    //当前章节
    private int mChapterCount;
    //总章节数量
    private int mChapterLen;
    //当前电量字符串
    private String batteryStr;
    //当前划屏位置
    private float mClipX = 0;
    //左右滑动偏移量 变量
    private float offsetX=0;
    //控件宽高
    private int mViewHeight = 0, mViewWidth = 0;
    //当前章节字符串
    private String[] contentArr;
    //当前页面显示章节进度    textContent的count
    private int textContentCount = 0;
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
    private int last_arrCount;
    private int last_lineCount;
    //下一页bitmap
    private static Bitmap nextBitmap;
    private int next_arrCount;
    private int next_lineCount;
    //当前页bitmap
    private static Bitmap thisBitmap;
    private int this_arrCount;
    private int this_lineCount;
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

        //计算页面 绘制的总字数，行数，每行字数
        textLineSum = mViewWidth / (int) textSize;
        linePageSum = (mViewHeight - statusBarHeight) / (int) textSize;
    }

    public void setTextColor(int textColor) {
        mTextPaint.setColor(textColor);
        this.textColor = textColor;
    }

    public void setTextContent(String[] content) {
        this.contentArr=content;
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
        if(contentArr==null||contentArr.length==0){
            Log.i(TAG, "drawBitmap: 超出长度或没有文字内容，忽略绘制");
            return;
        }
        /*boolean flag=false;
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
        }*/
        drawTextView(canvas);
    }

    //绘制浏览文字
    private void drawTextView(Canvas canvas){
        int arrCount;
        int lineCount;
        float drawX=0,drawY=statusBarHeight;
        switch (drawStyle){
            case 0:
                drawX=offsetX;
                if(thisBitmap==null){
                    thisBitmap = Bitmap.createScaledBitmap(backBitmap, mViewWidth, mViewHeight, false);
                }
                canvas.drawBitmap(thisBitmap, drawX, 0, mPaint);
                canvas.translate(drawX,0);
                arrCount=this_arrCount;
                lineCount=this_lineCount;
                Log.i(TAG, "drawTextView: 绘制当前界面");
                break;
            case 1:
                if(nextBitmap==null){
                    nextBitmap = Bitmap.createScaledBitmap(backBitmap, mViewWidth, mViewHeight, false);
                }
                drawX=0;
                canvas.drawBitmap(nextBitmap, 0, 0, mPaint);
                arrCount=next_arrCount;
                lineCount=next_lineCount;

                Log.i(TAG, "drawTextView: 绘制下一界面");
                break;
            case 2:
                if(lastBitmap==null){
                    lastBitmap = Bitmap.createScaledBitmap(backBitmap, mViewWidth, mViewHeight, false);
                }
                drawX=0;
                canvas.drawBitmap(lastBitmap, 0, 0, mPaint);
                arrCount=last_arrCount;
                lineCount=last_lineCount;
                Log.i(TAG, "drawTextView: 绘制上一界面");
                break;
            default:
                return;
        }
        canvas.save();
        //绘制当前页面
        for (int i=0;i<linePageSum;i++){
            if(lineCount==contentArr.length){
                return;
            }
            String str=contentArr[lineCount];
            int drawLen=str.length();
            if(drawLen==0){
                Log.i(TAG, "drawTextView: contentArr[i] 长度为0 ");
                lineCount++;
                continue;
            }

            while(drawLen!=0){
                if(drawLen>=textLineSum){
                    Log.i(TAG, "drawTextView111: "+str+"||"+arrCount+"||"+drawLen+"||"+textLineSum);

                    canvas.drawText(str,arrCount,(arrCount+textLineSum),drawX,drawY,mTextPaint);
                    drawLen-=textLineSum;
                    arrCount+=textLineSum;
                    Log.i(TAG, "drawTextView: while绘制，111："+drawLen);
                }else{
                    Log.i(TAG, "drawTextView: "+arrCount+"||"+textLineSum);
                    canvas.drawText(str,arrCount,str.length(),drawX,drawY,mTextPaint);
                    drawLen=0;
                    lineCount++;
                    Log.i(TAG, "drawTextView: while绘制，222："+drawLen);
                }
                drawY+=textSize;
            }
        }

        canvas.restore();

        return;
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
