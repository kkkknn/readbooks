package com.kkkkkn.readbooks.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;

//章节内容缓存
public class ChapterUtil {
    private final static String TAG="ChapterUtil";
    //缓存根目录
    private static String filePath="/cacheFiles/";

    //保存读取到的缓存文字 图书ID+章节ID，后期可加密
    public static boolean cacheChapter(Context context,String[] lineStr,final String fileName){
        if(context==null||lineStr==null||fileName.isEmpty()){
            Log.e(TAG, "cacheChapter: 入参为空");
            return false;
        }
        FileOutputStream out = null;
        BufferedWriter writer = null;
        try{
            out = context.openFileOutput(filePath+fileName, Context.MODE_PRIVATE);
            writer = new BufferedWriter(new OutputStreamWriter(out));
            for (String s : lineStr) {
                writer.write(s);
            }
            writer.flush();
            return true;
        }catch (IOException e){
            e.printStackTrace();
            return false;
        }finally {
            try{
                if(writer != null){
                    writer.close();
                }
            }catch (IOException e){
                e.printStackTrace();
            }
        }
    }

    //读取缓存的章节文字
    public static String[] readCacheChapter(Context context,String fileName){
        if(context==null||fileName.isEmpty()){
            Log.e(TAG, "cacheChapter: 入参为空");
            return null;
        }
        FileInputStream inputStream = null;
        BufferedReader reader = null;
        ArrayList<String> arrayList=new ArrayList<String>();
        try{
            inputStream = context.openFileInput(filePath+fileName);
            reader = new BufferedReader(new InputStreamReader(inputStream));
            String temp="";
            while ((temp=reader.readLine())!=null){
                arrayList.add(temp);
            }
            return (String[])arrayList.toArray();
        }catch (IOException e){
            e.printStackTrace();
            return null;
        }finally {
            try{
                if(reader != null){
                    reader.close();
                }
            }catch (IOException e){
                e.printStackTrace();
            }
        }
    }

}
