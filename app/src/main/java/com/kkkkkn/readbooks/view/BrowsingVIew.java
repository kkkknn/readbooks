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

import androidx.annotation.Nullable;

import com.kkkkkn.readbooks.R;
import com.kkkkkn.readbooks.activates.BookBrowsingActivity;

import java.util.LinkedList;
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
    //文字大小
    private float textSize = 50f;
    //文字颜色
    private int textColor = Color.BLACK;
    //每行最大显示文字数量
    private int textLineSum;
    //每页最大显示行数
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
    //绘制翻页锚点
    private LinkedList<int[]> skipList=new LinkedList<>();
    //是否结尾
    private boolean isEnd=false;
    //状态栏高度
    private int statusBarHeight;
    //绘图相关变量
    private TextPaint mTextPaint;
    private BookBrowsingActivity.BookCallback bookCallback;

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
        mViewWidth = getMeasuredWidth();
        mViewHeight = getMeasuredHeight();
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
        linePageSum = (int)((mViewHeight - statusBarHeight) / textSize);
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
                //判断是否需要变化当前页
                if(drawStyle==1){
                    //下一页变当前页
                    //是否需要转到下一页 小于6分之1不执行
                    if(Math.abs(offsetX) > mViewWidth / 6){
                        //锚点赋值
                        if(!isEnd){
                            int[] arr=new int[2];
                            arr[0]=this_arrCount;
                            arr[1]=this_lineCount;
                            skipList.add(arr);
                            last_arrCount=this_arrCount;
                            last_lineCount=this_lineCount;
                            this_arrCount=next_arrCount;
                            this_lineCount=next_lineCount;
                        }else {
                            //通知activity跳转下一章节
                            if(bookCallback!=null){
                                bookCallback.jump2nextChapter();
                            }
                        }
                    }

                }else if(drawStyle==2) {
                    //是否需要转到上一页 小于6分之1不执行
                    if((mViewWidth-Math.abs(offsetX)) > mViewWidth / 6){
                        next_arrCount=this_arrCount;
                        next_lineCount=this_lineCount;
                        this_arrCount=last_arrCount;
                        this_lineCount=last_lineCount;
                        if(!skipList.isEmpty() ){
                            skipList.removeLast();
                            if(!skipList.isEmpty()){
                                int[] arr=skipList.getLast();
                                last_arrCount=arr[0];
                                last_lineCount=arr[1];
                            }else {
                                last_lineCount=0;
                                last_arrCount=0;
                            }
                        }else{
                            last_lineCount=0;
                            last_arrCount=0;
                            //通知activity跳转下一章节
                            if(bookCallback!=null){
                                bookCallback.jump2lastChapter();
                            }
                        }
                        isEnd=false;
                    }

                }
                mClipX = -1;
                offsetX=0;
                drawStyle=0;
                performClick();
                break;
            case MotionEvent.ACTION_MOVE:
                //判断向左还是向右滑动  1左  2右
                float x = event.getX();
                if(x<0||x>mViewWidth){
                    break;
                }
                offsetX += x-mClipX;
                if(drawStyle == 0){
                    if(offsetX< 0){
                        drawStyle = 1;
                    }else if(offsetX > 0){
                        drawStyle = 2;
                        //计算初始锚点
                        offsetX=-(mViewWidth-x);
                    }
                }
                //offsetX = x;
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

        canvas.save();
        //根据drawstyle 决定绘制左边还是右边
        switch (drawStyle) {
            case 1:
                if (nextBitmap == null) {
                    nextBitmap = Bitmap.createScaledBitmap(backBitmap, mViewWidth, mViewHeight, false);
                }
                if (thisBitmap == null) {
                    thisBitmap = Bitmap.createScaledBitmap(backBitmap, mViewWidth, mViewHeight, false);
                }

                //判断是否需要绘制下一页面
                if(isEnd||offsetX>0){
                    offsetX=0;
                    //绘制当前页面
                    canvas.drawBitmap(thisBitmap, offsetX, 0, mPaint);
                    canvas.translate(offsetX, 0);
                    drawTextView(canvas,this_arrCount,this_lineCount,true);
                }else  {
                    //绘制下一页面
                    canvas.drawBitmap(nextBitmap, 0, 0, mPaint);
                    drawTextView(canvas,next_arrCount,next_lineCount,false);
                    //绘制当前页面
                    canvas.drawBitmap(thisBitmap, offsetX, 0, mPaint);
                    canvas.translate(offsetX, 0);
                    drawTextView(canvas,this_arrCount,this_lineCount,false);
                }
                break;

            case 2:
                if (lastBitmap == null) {
                    lastBitmap = Bitmap.createScaledBitmap(backBitmap, mViewWidth, mViewHeight, false);
                }
                if (thisBitmap == null) {
                    thisBitmap = Bitmap.createScaledBitmap(backBitmap, mViewWidth, mViewHeight, false);
                }
                //判断是否绘制上一页面
                if(this_lineCount>0){
                    //绘制当前页面
                    canvas.drawBitmap(thisBitmap, 0, 0, mPaint);
                    drawTextView(canvas,this_arrCount,this_lineCount,false);
                    //绘制上一页面
                    canvas.drawBitmap(lastBitmap, offsetX, 0, mPaint);
                    canvas.translate(offsetX, 0);
                    drawTextView(canvas,last_arrCount,last_lineCount,false);
                }else {
                    canvas.drawBitmap(thisBitmap, 0, 0, mPaint);
                    canvas.translate(0, 0);
                    drawTextView(canvas,this_arrCount,this_lineCount,true);
                }
                break;
            default:
                //绘制当前页面
                if (thisBitmap == null) {
                    thisBitmap = Bitmap.createScaledBitmap(backBitmap, mViewWidth, mViewHeight, false);
                }
                canvas.drawBitmap(thisBitmap, offsetX, 0, mPaint);
                canvas.translate(offsetX, 0);
                drawTextView(canvas,this_arrCount,this_lineCount,true);
                break;
        }

        canvas.restore();
    }

    //绘制浏览文字
    private void drawTextView(Canvas canvas, int count1, int count2,boolean isThis){
        int arrCount=count1;
        int lineCount=count2;
        int drawY=statusBarHeight;
        for (int i=0;i<linePageSum;i++){
            if(lineCount==contentArr.length){
                //章节结尾锚点
                if(isThis){
                    isEnd=true;
                }
                return;
            }
            String str=contentArr[lineCount];
            int drawLen=str.length()-arrCount;
            if(drawLen==0){
                lineCount++;
                continue;
            }
            while(drawLen!=0){
                if(drawLen>textLineSum){
                    canvas.drawText(str,arrCount,(arrCount+textLineSum), (float) 0,drawY,mTextPaint);
                    drawLen-=textLineSum;
                    arrCount+=textLineSum;
                    if(drawLen!=0){
                        i++;
                        if(i==linePageSum){
                            break;
                        }
                    }
                }else{
                    canvas.drawText(str,arrCount,str.length(), (float) 0,drawY,mTextPaint);
                    drawLen=0;
                    arrCount=0;
                    lineCount++;
                }
                drawY+=textSize;
            }
        }
        if(isThis){
            next_arrCount=arrCount;
            next_lineCount=lineCount;
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

    private int getStatusBarHeight(Context context) {
        Resources resources = context.getResources();
        int resourceId = resources.getIdentifier("status_bar_height", "dimen", "android");
        return resources.getDimensionPixelSize(resourceId);
    }

    public void setListener(BookBrowsingActivity.BookCallback callback){
        bookCallback=callback;
    }

}
