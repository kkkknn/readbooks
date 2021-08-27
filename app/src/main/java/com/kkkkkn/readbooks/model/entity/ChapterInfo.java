package com.kkkkkn.readbooks.model.entity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

public class ChapterInfo implements Serializable,Comparable<ChapterInfo> {
    private int chapter_id;
    private String chapter_name;
    private String chapter_path;

    public ChapterInfo(Object object) {
        //jsonobject 强制赋值
        JSONObject jsonObject=(JSONObject) object;
        try {
            chapter_id=jsonObject.getInt("chapter_id");
            chapter_name=jsonObject.getString("chapter_name");
            chapter_path=jsonObject.getString("chapter_url");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public int getChapter_id() {
        return chapter_id;
    }

    public void setChapter_id(int chapter_id) {
        this.chapter_id = chapter_id;
    }

    public String getChapter_name() {
        return chapter_name;
    }

    public void setChapter_name(String chapter_name) {
        this.chapter_name = chapter_name;
    }

    public String getChapter_path() {
        return chapter_path;
    }

    public void setChapter_path(String chapter_path) {
        this.chapter_path = chapter_path;
    }


    @Override
    public int compareTo(ChapterInfo chapterInfo) {
        return chapter_id-chapterInfo.getChapter_id();
    }
}
