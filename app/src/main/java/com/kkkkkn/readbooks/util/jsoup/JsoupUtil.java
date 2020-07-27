package com.kkkkkn.readbooks.util.jsoup;

import org.json.JSONException;

import java.io.IOException;

//爬取图书接口类
public interface JsoupUtil {
    //搜索图书,返回json字符串
    String searchBook(String str) throws IOException, JSONException;
    //获取图书详情,返回json字符串
    String getBookInfo(String book_url) throws IOException, JSONException;
    //获取章节内容,返回json字符串
    String getChapterContent(String chapter_url) throws IOException, JSONException;
    //
}
