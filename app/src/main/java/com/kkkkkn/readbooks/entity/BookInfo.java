package com.kkkkkn.readbooks.entity;

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
 */
public class BookInfo implements Serializable {
    private int bookId;
    private String bookName;
    private String bookUrl;
    private String authorName;
    private String bookAbout;
    private String bookImgUrl;
    private String newChapterName;
    private boolean enjoy;
    private int bookFromType;
    private String chapterPagesUrlStr;

    public String getChapterPagesUrlStr() {
        return chapterPagesUrlStr;
    }

    public void setChapterPagesUrlStr(String chapterPageUrlStr) {
        this.chapterPagesUrlStr = chapterPageUrlStr;
    }


    public int getBookId() {
        return bookId;
    }

    public void setBookId(int bookId) {
        this.bookId = bookId;
    }

    public boolean getEnjoy() {
        return enjoy;
    }

    public void setEnjoy(boolean enjoy) {
        this.enjoy = enjoy;
    }

    public int getBookFromType() {
        return bookFromType;
    }

    public void setBookFromType(int bookFromType) {
        this.bookFromType = bookFromType;
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
}
