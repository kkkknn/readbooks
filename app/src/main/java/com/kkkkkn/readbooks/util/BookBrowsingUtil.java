package com.kkkkkn.readbooks.util;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.text.TextPaint;

import com.kkkkkn.readbooks.R;

import java.util.ArrayList;
import java.util.Calendar;

public class BookBrowsingUtil {

    public static ArrayList<Bitmap> string2bitmap(Bitmap backgroundBitmap,String content,int width,int height,int fontSize){
        if(content==null||content.isEmpty()||backgroundBitmap==null){
            return null;
        }
        ArrayList<Bitmap> arrayList=new ArrayList();
        Bitmap bitmap=Bitmap.createBitmap(backgroundBitmap);
        Canvas canvas=new Canvas(bitmap);
        int fontCount=0;
        int lineSum=height/fontSize;
        int fontSumLine=width/fontSize;
        char[] array=content.toCharArray();
        TextPaint point=new TextPaint();
        point.setTextSize(40f);
        point.setColor(Color.BLACK);
        point.setAntiAlias(true);
        while (fontCount!=array.length){
            for (int i = 0; i < lineSum; i++) {
                for (int j = 0; j < fontSumLine; j++) {
                    canvas.drawText(array,i,1,fontSumLine*j,lineSum*i,point);
                    fontCount++;
                }
            }
            arrayList.add(bitmap);
            if(fontCount!=array.length){
                bitmap=Bitmap.createBitmap(backgroundBitmap);
                canvas.setBitmap(bitmap);
            }

        }
        return arrayList;
    }

    //获取当前时间
    private static String getCurrentTime(){
        //使用Calendar获取系统时间
        Calendar calendars=Calendar.getInstance();
        String hour=String.valueOf(calendars.get(Calendar.HOUR));
        String minute=String.valueOf(calendars.get(Calendar.MINUTE));
        return hour+":"+minute;
    }
}
