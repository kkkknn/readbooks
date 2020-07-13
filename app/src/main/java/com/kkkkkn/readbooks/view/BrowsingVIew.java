package com.kkkkkn.readbooks.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.text.Layout;
import android.text.StaticLayout;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.Nullable;

import java.util.ArrayList;

public class BrowsingVIew extends View {
    private String chapterContent;
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
        canvas.drawColor(Color.YELLOW);
        super.draw(canvas);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        TextPaint point=new TextPaint();
        point.setTextSize(40f);
        point.setColor(Color.BLACK);
        point.setAntiAlias(true);
        String str="阿萨达噶几杀戮空间赶快拉几个阿萨达噶几杀戮空间赶快拉几个阿萨达噶几杀戮空间赶快拉几个阿萨达噶几杀戮空间赶快拉几个阿萨达噶几杀戮空间赶快拉几个阿萨达噶几杀戮空间赶快拉几个阿萨达噶几杀戮空间赶快拉几个阿萨达噶几杀戮空间赶快拉几个阿萨达噶几杀戮空间赶快拉几个阿萨达噶几杀戮空间赶快拉几个阿萨达噶几杀戮空间赶快拉几个阿萨达噶几杀戮空间赶快拉几个阿萨达噶几杀戮空间赶快拉几个阿萨达噶几杀戮空间赶快拉几个阿萨达噶几杀戮空间赶快拉几个阿萨达噶几杀戮空间赶快拉几个阿萨达噶几杀戮空间赶快拉几个阿萨达噶几杀戮空间赶快拉几个阿萨达噶几杀戮空间赶快拉几个阿萨达噶几杀戮空间赶快拉几个阿萨达噶几杀戮空间赶快拉几个阿萨达噶几杀戮空间赶快拉几个阿萨达噶几杀戮空间赶快拉几个阿萨达噶几杀戮空间赶快拉几个";
        StaticLayout layoutopen = new StaticLayout(str,  point,  getWidth() , Layout.Alignment.ALIGN_NORMAL, 1.0F, 0.0F, true);
        //这里的参数300，表示字符串的长度，当满300时，就会换行，也可以使用“\r\n”来实现换行
        canvas.save();
        canvas.translate(0,50);
        layoutopen.draw(canvas);
        canvas.restore();
        super.onDraw(canvas);
    }
}
