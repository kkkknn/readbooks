package com.kkkkkn.readbooks.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.text.Layout;
import android.text.StaticLayout;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.Nullable;

import com.kkkkkn.readbooks.R;

import java.util.ArrayList;

public class BrowsingVIew extends View {
    private ArrayList<Bitmap> bitmapArrayList=new ArrayList<>();
    public BrowsingVIew(Context context) {
        super(context);
    }

    public BrowsingVIew(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public BrowsingVIew(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }


    @Override
    public void draw(Canvas canvas) {
        Bitmap bitmap= BitmapFactory.decodeResource(getResources(), R.drawable.browsingview);
        Bitmap bitmap2=Bitmap.createScaledBitmap(bitmap,getWidth(),getHeight(),true);
        Paint paint=new Paint();
        paint.setAntiAlias(true);
        paint.setStyle(Paint.Style.FILL);
        canvas.drawBitmap(bitmap2,0,0,paint);
        super.draw(canvas);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        TextPaint point=new TextPaint();
        point.setTextSize(40f);
        point.setColor(Color.BLACK);
        point.setAntiAlias(true);
        String str="2阿萨达噶几, ?1杀戮空间赶快拉几个阿萨达噶几杀戮空间赶快拉几个阿萨达噶几杀戮空间赶快拉几个阿萨达噶几杀戮空间赶快拉几个阿萨达噶几杀戮空间赶快拉几个阿萨达噶几杀戮空间赶快拉几个阿萨达噶几杀戮空间赶快拉几个阿萨达噶几杀戮空间赶快拉几个阿萨达噶几杀戮空间赶快拉几个阿萨达噶几杀戮空间赶快拉几个阿萨达噶几杀戮空间赶快拉几个阿萨达噶几杀戮空间赶快拉几个阿萨达噶几杀戮空间赶快拉几个阿萨达噶几杀戮空间赶快拉几个阿萨达噶几杀戮空间赶快拉几个阿萨达噶几杀戮空间赶快拉几个阿萨达噶几杀戮空间赶快拉几个阿萨达噶几杀戮空间赶快拉几个阿萨达噶几杀戮空间赶快拉几个阿萨达噶几杀戮空间赶快拉几个阿萨达噶几杀戮空间赶快拉几个阿萨达噶几杀戮空间赶快拉几个阿萨达噶几杀戮空间赶快拉几个阿萨达噶几杀戮空间赶快拉几个";
        float size=point.getTextSize();
        char[] arr=str.toCharArray();
        int num=Math.round(getWidth()/size);
        for (int i = 0; i < num; i++) {
            canvas.drawText(arr,i,1,size*i,30,point);
        }
        Log.i("TAG", "onDraw: "+num);
        /*StaticLayout layoutopen = new StaticLayout(str,  point,  getWidth() , Layout.Alignment.ALIGN_NORMAL, 1.0F, 0.0F, true);
        //这里的参数300，表示字符串的长度，当满300时，就会换行，也可以使用“\r\n”来实现换行
        canvas.save();
        canvas.translate(0,50);
        layoutopen.draw(canvas);
        canvas.restore();*/
        super.onDraw(canvas);
    }

    public ArrayList<Bitmap> getBitmapArrayList() {
        return bitmapArrayList;
    }

    public void setBitmapArrayList(ArrayList<Bitmap> bitmapArrayList) {
        this.bitmapArrayList = bitmapArrayList;
    }

    /*@Override
    public boolean onTouchEvent(MotionEvent event) {
        super.onTouchEvent(event);
        float x = event.getX();
        float y = event.getY();
        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:
                xDown = x;
                if(x<=viewWidth/3){//左
                    touchStyle = TOUCH_LEFT;
                }else if(x>viewWidth*2/3){//右
                    touchStyle = TOUCH_RIGHT;
                }else if(x>viewWidth/3 && x<viewWidth*2/3){//中
                    touchStyle = TOUCH_MIDDLE;
                }
                break;
            case MotionEvent.ACTION_MOVE:
                scrollPage(x,y);
                break;
            case MotionEvent.ACTION_UP:
                break;
        }
        return true;
    }

    private void scrollPage(float x, float y){
        touchPoint.x = x;
        touchPoint.y = y;

        if(touchStyle == TOUCH_RIGHT){
            scrollPageLeft = touchPoint.x - xDown;
        }else if(touchStyle == TOUCH_LEFT){
            scrollPageLeft =touchPoint.x - xDown - viewWidth;
        }

        if(scrollPageLeft > 0){
            scrollPageLeft = 0;
        }
        postInvalidate();
    }*/
}
