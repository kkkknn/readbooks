package com.kkkkkn.readbooks.view;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.text.TextPaint;

import java.util.ArrayList;

/**
 * DCL锁实现单例
 */
public class BitmapUtil {
    private final static ArrayList<Bitmap> bitmapArrayList=new ArrayList<>();
    private static volatile BitmapUtil bitmapUtil=null;
    private static Canvas mCanvas;
    private static Paint mPaint;
    private static TextPaint mTextPaint;
    private BitmapUtil() {
        mCanvas=new Canvas();
        mPaint=new Paint();
        mTextPaint=new TextPaint();
    }

    public static BitmapUtil getInstance() {
        if(bitmapUtil==null){
            synchronized (BitmapUtil.class){
                if(bitmapUtil==null){
                    bitmapUtil=new BitmapUtil();
                }
            }
        }
        return bitmapUtil;
    }

    //添加文字到bitmap集合中
    public boolean String2Bitmap(String str,int width,int height){
        if(str.isEmpty()){
            return false;
        }
        //重置画布及背景,设置画笔相关属性
        Bitmap bitmap=Bitmap.createBitmap(width,height, Bitmap.Config.ARGB_8888);
        mCanvas.setBitmap(bitmap);
        mPaint.setAntiAlias(true);
        mPaint.setStyle(Paint.Style.FILL);
        mTextPaint.setTextSize(40f);
        mTextPaint.setColor(Color.BLACK);
        mTextPaint.setAntiAlias(true);


        return false;
    }

    //集合长度过长进行删除，防止占用太大内存
    public int slimBitmapList(){
        if(bitmapArrayList.size()>10){
            int count=bitmapArrayList.size()-10;
            for (int i = 0; i <count ; i++) {

            }
        }
        return 1;
    }

}
