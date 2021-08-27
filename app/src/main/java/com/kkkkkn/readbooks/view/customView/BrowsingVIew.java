     package com.kkkkkn.readbooks.view.customView;

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
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.kkkkkn.readbooks.R;
import com.kkkkkn.readbooks.view.activities.BookBrowsingActivity;

import java.util.ArrayList;
public class BrowsingVIew extends View {
    private final static String TAG="BrowsingVIew";
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
    //当前章节字符串
    private String[] contentArr;
    //文字大小
    private float textSize = 40f;
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
    //当前页起止标志
    private int thisPage_flag=0;
    //当前页bitmap
    private static Bitmap thisBitmap = null;
    //绘制翻页锚点
    private ArrayList<int[]> skipList=new ArrayList<>();
    //状态栏高度
    private int statusBarHeight;
    //绘图相关变量
    private TextPaint mTextPaint;
    private BookBrowsingActivity.BookCallback bookCallback;
    private float read_progress;

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

    public int getlineFlag(){
        return thisPage_flag;
    }

    public void setProgress(float progress){
        this.read_progress=progress;
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
        statusBarHeight = getStatusBarHeight(context)+30;
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

        //重新计算当前章节文字数量及相关信息
        setChapterFlags();
    }

    public void setTextColor(int textColor) {
        mTextPaint.setColor(textColor);
        this.textColor = textColor;
    }

    //根据章节计算页面数量和标志位置并添加到list中
    private void setChapterFlags(){
        if(this.textLineSum==0){
            return;
        }
        skipList.clear();
        thisPage_flag=0;
        //计算页面绘制完成后的锚点
        int line_count=0;
        int[] arr=new int[2];
        skipList.add(arr);
        for (int i = 0; i < contentArr.length; i++) {
            String str=contentArr[i];
            double line=Math.ceil((double)str.length()/this.textLineSum);
            if(line>0){
                line_count+=line;
            }

            if(line_count>=this.linePageSum){
                //多出的行数
                int line_offset=(line_count-this.linePageSum);
                arr=new int[2];
                arr[0]=line_offset*this.textLineSum;
                arr[1]=i;
                skipList.add(arr);
                line_count=0;
            }
        }

    }

    public void setTextContent(String[] content) {
        this.contentArr=content;

        //根据章节重新设置3个页面的进度
        mClipX = -1;
        offsetX=0;
        drawStyle=0;

        //根据章节计算页面数量和标志位置并添加到list中
        setChapterFlags();

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
                //首先判断滑动距离是否超出宽度1/6
                if(Math.abs(offsetX) > mViewWidth / 6){
                    if(drawStyle==1){
                        //锚点赋值
                        if((thisPage_flag+1)<skipList.size()){
                            thisPage_flag++;

                        }else if(bookCallback!=null){
                            //通知activity跳转下一章节
                            bookCallback.jump2nextChapter();
                        }
                    }else if(drawStyle==2){
                        if(thisPage_flag>0){
                            thisPage_flag--;
                        }else if(bookCallback!=null){
                            //通知activity跳转上一章节
                            bookCallback.jump2lastChapter();
                        }
                    }
                }else {
                    if(event.getX()>=mViewWidth/3*2){
                        showSlide(false,(int) event.getX());
                        Toast.makeText(getContext(),"下一页面",Toast.LENGTH_SHORT).show();
                    }else if(event.getX()<=mViewWidth/3){
                        showSlide(true,(int)event.getX());
                        Toast.makeText(getContext(),"上一页面",Toast.LENGTH_SHORT).show();
                    }else {
                        //落点在中央，显示阅读设置view
                        bookCallback.showSetting();
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
                        //offsetX=-(mViewWidth-x);
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
    private void showSlide(boolean type,int x){
        if(type){
            //左滑动
            while (offsetX>-mViewWidth){
                offsetX--;
                //重新绘制界面
                invalidate();
            }
        }else{
            //右滑动
            while (offsetX<mViewWidth){
                offsetX++;
                //重新绘制界面
                invalidate();
            }
        }
    }
    //绘制阅读界面 2个页面  1，当前页面  2，根据当前手势判断绘制上一页/下一页
    private void drawBitmap(Canvas canvas) {
        //判断是否需要绘制
        if(contentArr==null||contentArr.length==0){
            Log.i(TAG, "drawBitmap: 超出长度或没有文字内容，忽略绘制");
            //todo  展示加载框

            return;
        }

        //初始化bitmap
        if(thisBitmap==null&&mViewWidth>0&&mViewHeight>0){
            thisBitmap=Bitmap.createScaledBitmap(backBitmap, mViewWidth, mViewHeight, false);
        }

        canvas.save();
        //根据drawstyle 决定绘制左边还是右边
        switch (drawStyle) {
            case 1:
                //判断是否需要绘制下一页面
                if((thisPage_flag+1)==skipList.size()||offsetX>0){
                    //绘制当前页面
                    canvas.drawBitmap(thisBitmap, 0, 0, mPaint);
                    canvas.translate(0, 0);
                    int[] arr=skipList.get(thisPage_flag);
                    drawTextView(canvas,arr[0],arr[1],true);
                }else {
                    //绘制下一页面
                    canvas.drawBitmap(thisBitmap, 0, 0, mPaint);
                    int[] arr=skipList.get(thisPage_flag+1);
                    drawTextView(canvas,arr[0],arr[1],false);
                    //绘制当前页面
                    canvas.drawBitmap(thisBitmap, offsetX, 0, mPaint);
                    canvas.translate(offsetX, 0);
                    int[] arr2=skipList.get(thisPage_flag);
                    drawTextView(canvas,arr2[0],arr2[1],true);
                }
                break;

            case 2:
                //判断是否绘制上一页面
                if(thisPage_flag>0){
                    //绘制当前页面
                    canvas.drawBitmap(thisBitmap, 0, 0, mPaint);
                    int[] arr2=skipList.get(thisPage_flag);
                    drawTextView(canvas,arr2[0],arr2[1],true);
                    //绘制上一页面
                    canvas.drawBitmap(thisBitmap, offsetX-mViewWidth, 0, mPaint);
                    canvas.translate(offsetX-mViewWidth, 0);
                    int[] arr=skipList.get(thisPage_flag-1);
                    drawTextView(canvas,arr[0],arr[1],false);
                }else {
                    canvas.drawBitmap(thisBitmap, 0, 0, mPaint);
                    canvas.translate(0, 0);
                    int[] arr2=skipList.get(thisPage_flag);
                    drawTextView(canvas,arr2[0],arr2[1],true);
                }
                break;
            default:
                canvas.drawBitmap(thisBitmap, offsetX, 0, mPaint);
                canvas.translate(offsetX, 0);
                int[] arr2=skipList.get(thisPage_flag);
                drawTextView(canvas,arr2[0],arr2[1],true);
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
