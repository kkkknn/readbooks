package com.kkkkkn.readbooks.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
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
    private static String filePath="/cacheChapters/";

    //保存读取到的缓存文字 章节URL ，后期可加密
    public static boolean cacheChapter(JSONArray jsonArray,final String path, final String fileName){
        if(jsonArray==null||fileName.isEmpty()){
            Log.e(TAG, "cacheChapter: 入参为空");
            return false;
        }
        FileOutputStream out = null;
        BufferedWriter writer = null;
        try{
            out = new FileOutputStream(new File(path+filePath+fileName));
            writer = new BufferedWriter(new OutputStreamWriter(out));
            for (int i = 0; i < jsonArray.length(); i++) {
                writer.write(jsonArray.getString(i));
            }
            writer.flush();
            return true;
        }catch (IOException | JSONException e){
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
    public static String[] readCacheChapter(final String path,final String fileName){
        if(fileName.isEmpty()){
            Log.e(TAG, "cacheChapter: 入参为空");
            return null;
        }
        FileInputStream inputStream = null;
        BufferedReader reader = null;
        ArrayList<String> arrayList=new ArrayList<String>();
        try{
            File file=new File(filePath+fileName);
            if(!file.exists()){
                file.mkdirs();
            }
            inputStream = new FileInputStream(path+filePath+fileName);
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
