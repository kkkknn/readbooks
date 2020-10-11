package com.kkkkkn.readbooks.util.bookbrowsing;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Paint;
import android.text.TextPaint;

public class BrowsingViewInfo {
    //状态栏高度
    private int statusBarHeight;
    //绘制对象
    private Paint mPaint;
    //背景bitmap
    private Bitmap backgroundBitmap;
    //绘制文字对象
    private TextPaint mTextPaint;
    //文字大小
    private float textSize;
    //文字颜色
    private Color textColor;
    //每页文字数量
    private int linePageSum;
    //每行文字数量
    private int textLineSum;
    //绘制页面高度
    private int viewHeight;
    //绘制页面宽度
    private int viewWidth;
    private volatile static BrowsingViewInfo mBrowsingViewInfo;

    private BrowsingViewInfo(){
        mTextPaint=new TextPaint();
        mPaint=new Paint();
        //设置绘制居中
        mTextPaint.setTextAlign(Paint.Align.CENTER);
        mPaint.setTextAlign(Paint.Align.CENTER);
    }
    /*private BrowsingViewInfo(int statusBarHeight, Bitmap backgroundBitmap, float textSize, Color textColor, float viewHeight, float viewWidth) {
        this.statusBarHeight = statusBarHeight;
        this.backgroundBitmap = backgroundBitmap;
        this.textSize = textSize;
        this.textColor = textColor;
        this.viewHeight = viewHeight;
        this.viewWidth = viewWidth;
    }*/

    public static BrowsingViewInfo getInstance(){
        if(mBrowsingViewInfo==null){
            synchronized (BrowsingViewInfo.class){
                if(mBrowsingViewInfo==null){
                    //创建新对象，并赋予初始值
                    mBrowsingViewInfo=new BrowsingViewInfo();
                }
            }
        }
        return mBrowsingViewInfo;
    }
    public BrowsingViewInfo setViewSize(int width,int height){
        mBrowsingViewInfo.viewWidth=width;
        mBrowsingViewInfo.viewHeight=height;
        computeTextConfig();
        return mBrowsingViewInfo;
    }
    public BrowsingViewInfo setStatusBarHeight(int barHeight){
        this.statusBarHeight = barHeight;
        computeTextConfig();
        return mBrowsingViewInfo;
    }
    public BrowsingViewInfo setTextInfo(Color textColor,float textSize){
        mBrowsingViewInfo.textSize=textSize;
        mBrowsingViewInfo.textColor=textColor;
        mTextPaint.setTextSize(textSize);
        mTextPaint.setColor(textColor.hashCode());
        computeTextConfig();
        return mBrowsingViewInfo;
    }
    public BrowsingViewInfo setBackgroundBitmap(Bitmap backgroundBitmap){
        mBrowsingViewInfo.backgroundBitmap=backgroundBitmap;
        return mBrowsingViewInfo;
    }
    public Bitmap getBackgroundBitmap(){
        return backgroundBitmap;
    }
    public TextPaint getmTextPaint(){
        return mTextPaint;
    }

    public Paint getmPaint(){
        return mPaint;
    }
    public int getLinePageSum(){
        return linePageSum;
    }

    public int getStatusBarHeight() {
        return statusBarHeight;
    }

    public int getTextLineSum() {
        return textLineSum;
    }

    private void computeTextConfig(){
        //计算行文字数量
        textLineSum = viewWidth / (int) textSize;
        //计算页文字数量
        linePageSum = (viewHeight - statusBarHeight) / (int) textSize;
    }

}
