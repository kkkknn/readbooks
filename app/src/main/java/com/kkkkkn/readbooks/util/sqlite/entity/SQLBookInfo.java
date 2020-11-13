package com.kkkkkn.readbooks.util.sqlite.entity;

import java.util.LinkedList;

public class SQLBookInfo {
    public int book_id;
    public String book_name;
    public String book_url;
    public String book_img_url;
    public String book_author_name;


    public int getBook_id() {
        return book_id;
    }

    public void setBook_id(int book_id) {
        this.book_id = book_id;
    }

    public String getBook_name() {
        return book_name;
    }

    public void setBook_name(String book_name) {
        this.book_name = book_name;
    }

    public String getBook_url() {
        return book_url;
    }

    public void setBook_url(String book_url) {
        this.book_url = book_url;
    }

    public String getBook_img_url() {
        return book_img_url;
    }

    public void setBook_img_url(String book_img_url) {
        this.book_img_url = book_img_url;
    }

    public String getBook_author_name() {
        return book_author_name;
    }

    public void setBook_author_name(String book_author_name) {
        this.book_author_name = book_author_name;
    }

    @Override
    public String toString() {
        return "SQLBookInfo{" +
                "book_id=" + book_id +
                ", book_name='" + book_name + '\'' +
                ", book_url='" + book_url + '\'' +
                ", book_img_url='" + book_img_url + '\'' +
                ", book_author_name='" + book_author_name + '\'' +
                '}';
    }
}
