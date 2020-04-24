package com.kkkkkn.readbooks.entity;

import android.graphics.Bitmap;

//图书实体类
public class BookInfo {
    private String bookName;
    private String bookUrl;
    private String authorName;
    private Bitmap bookImg;
    private String bookAbout;
    private String bookImgUrl;

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

    public Bitmap getBookImg() {
        return bookImg;
    }

    public void setBookImg(Bitmap bookImg) {
        this.bookImg = bookImg;
    }

    @Override
    public String toString() {
        return "BookInfo{" +
                "bookName='" + bookName + '\'' +
                ", bookUrl='" + bookUrl + '\'' +
                ", authorName='" + authorName + '\'' +
                ", bookAbout='" + bookAbout + '\'' +
                '}';
    }
}
