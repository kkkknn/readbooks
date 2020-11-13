package com.kkkkkn.readbooks.util.sqlite.entity;

public class SQLChapterInfo {
    public int chapter_id;
    public String chapter_name;
    public String chapter_url;
    public String chapter_content;
    public int book_id;
    public boolean is_download;

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

    public String getChapter_url() {
        return chapter_url;
    }

    public void setChapter_url(String chapter_url) {
        this.chapter_url = chapter_url;
    }

    public String getChapter_content() {
        return chapter_content;
    }

    public void setChapter_content(String chapter_content) {
        this.chapter_content = chapter_content;
    }

    public int getBook_id() {
        return book_id;
    }

    public void setBook_id(int book_id) {
        this.book_id = book_id;
    }

    public boolean isIs_download() {
        return is_download;
    }

    public void setIs_download(boolean is_download) {
        this.is_download = is_download;
    }

    @Override
    public String toString() {
        return "SQLChapterInfo{" +
                "chapter_id=" + chapter_id +
                ", chapter_name='" + chapter_name + '\'' +
                ", chapter_url='" + chapter_url + '\'' +
                ", chapter_content='" + chapter_content + '\'' +
                ", book_id=" + book_id +
                ", is_download=" + is_download +
                '}';
    }
}
