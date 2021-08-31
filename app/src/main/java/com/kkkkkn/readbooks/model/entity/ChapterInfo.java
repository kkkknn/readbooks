package com.kkkkkn.readbooks.model.entity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

public class ChapterInfo implements Serializable,Comparable<ChapterInfo> {
    private int chapter_id;
    private int chapter_num;
    private String chapter_name;
    private String chapter_path;

    public ChapterInfo(Object object) {
        //jsonobject 强制赋值
        JSONObject jsonObject=(JSONObject) object;
        try {
            chapter_id=jsonObject.getInt("chapter_id");
            //拆分章节字符串，取出名字和章节数
            String str=jsonObject.getString("chapter_name");
            String[] arr=str.split("_");
            chapter_num=Integer.parseInt(arr[0]);
            chapter_name=arr[1];
            chapter_path=jsonObject.getString("chapter_url");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public int getChapter_id() {
        return chapter_id;
    }

    public String getChapter_name() {
        return chapter_name;
    }


    public String getChapter_path() {
        return chapter_path;
    }

    public int getChapter_num() {
        return chapter_num;
    }

    @Override
    public int compareTo(ChapterInfo chapterInfo) {
        return chapter_num-chapterInfo.getChapter_num();
    }
}
