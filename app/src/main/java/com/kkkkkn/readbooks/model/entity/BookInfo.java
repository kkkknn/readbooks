package com.kkkkkn.readbooks.model.entity;


import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

/**
 * 图书实体类
 *     private String bookName; 图书名字
 *     private String bookUrl;  图书连接
 *     private String authorName;   作者名字
 *     private String bookAbout;    图书介绍
 *     private String bookImgUrl;   图书图片链接
 *     private String newChapterName;   最新章节名字
 *     private String isEnjoy;     图书是否已收藏
 *     private int bookFromType;    图书来源
 *     private String chapterPagesUrlStr;   图书章节页码 网页版
 */
public class BookInfo  implements Serializable {
    private int bookId;
    private String bookName;
    private String bookUrl;
    private String authorName;
    private String bookAbout;
    private String bookImgUrl;
    private String newChapterName;
    private int chapterSum;
    private String sourceName;


    public int getBookId() {
        return bookId;
    }

    public void setBookId(int bookId) {
        this.bookId = bookId;
    }


    public String getNewChapterName() {
        return newChapterName;
    }

    public void setNewChapterName(String newChapterName) {
        this.newChapterName = newChapterName;
    }

    public String getBookImgUrl() {
        return bookImgUrl;
    }

    public void setBookImgUrl(String bookImgUrl) {
        this.bookImgUrl = bookImgUrl;
    }

    public String getBookAbout() {
        return bookAbout;
    }

    public void setBookAbout(String bookAbout) {
        this.bookAbout = bookAbout;
    }


    public String getBookName() {
        return bookName;
    }

    public void setBookName(String bookName) {
        this.bookName = bookName;
    }

    public String getBookUrl() {
        return bookUrl;
    }

    public void setBookUrl(String bookUrl) {
        this.bookUrl = bookUrl;
    }

    public String getAuthorName() {
        return authorName;
    }

    public void setAuthorName(String authorName) {
        this.authorName = authorName;
    }

    @Override
    public String toString() {
        return "SQLBookInfo{" +
                "bookName='" + bookName + '\'' +
                ", bookUrl='" + bookUrl + '\'' +
                ", authorName='" + authorName + '\'' +
                ", bookAbout='" + bookAbout + '\'' +
                ", bookImgUrl='" + bookImgUrl + '\'' +
                ", newChapterName='" + newChapterName + '\'' +
                '}';
    }
    public boolean isEmpty(){
        if(bookName.isEmpty()||bookUrl.isEmpty()){
            return true;
        }
        return false;
    }

    public int getChapterSum() {
        return chapterSum;
    }

    public void setChapterSum(int chapterSum) {
        this.chapterSum = chapterSum;
    }

    public String getSourceName() {
        return sourceName;
    }

    public void setSourceName(String sourceName) {
        this.sourceName = sourceName;
    }

    public static BookInfo changeObject(JSONObject object){
        BookInfo bookInfo=new BookInfo();
        try {
            bookInfo.authorName=object.getString("author_name");
            bookInfo.chapterSum=object.getInt("book_chapter_sum");
            bookInfo.bookAbout=object.getString("book_about");
            bookInfo.bookId=object.getInt("book_id");
            bookInfo.bookImgUrl=object.getString("book_img_url");
            bookInfo.bookName=object.getString("book_name");
            bookInfo.bookUrl=object.getString("book_url");
            bookInfo.sourceName=object.getString("source_name");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return bookInfo;
    }

}
