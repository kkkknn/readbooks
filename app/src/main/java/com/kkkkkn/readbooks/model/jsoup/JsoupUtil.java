package com.kkkkkn.readbooks.model.jsoup;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

//爬取图书接口类
public interface JsoupUtil {
    //搜索图书,返回json字符串
    String searchBook(String str) throws IOException, JSONException;
    //获取图书详情,返回json字符串
    String getBookInfo(String book_url) throws IOException, JSONException;
    //获取章节内容,返回json字符串
    JSONObject getChapterContent(String chapter_url) throws IOException, JSONException;
    //获取指定页章节列表
    String getBookChapterList(String url) throws IOException, JSONException;
    //获取来源
    int getSource();
}
