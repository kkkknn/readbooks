package com.kkkkkn.readbooks.entity;

//图书实体类
public class Book {
    private int bookId;
    private String bookName;
    private String bookPath;
    private String authorName;
    private String updateTime;

    public Book(int bookId, String bookName, String bookPath, String authorName, String updateTime) {
        this.bookId = bookId;
        this.bookName = bookName;
        this.bookPath = bookPath;
        this.authorName = authorName;
        this.updateTime = updateTime;
    }

    public int getBookId() {
        return bookId;
    }

    public void setBookId(int bookId) {
        this.bookId = bookId;
    }

    public String getBookName() {
        return bookName;
    }

    public void setBookName(String bookName) {
        this.bookName = bookName;
    }

    public String getBookPath() {
        return bookPath;
    }

    public void setBookPath(String bookPath) {
        this.bookPath = bookPath;
    }

    public String getAuthorName() {
        return authorName;
    }

    public void setAuthorName(String authorName) {
        this.authorName = authorName;
    }

    public String getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }

    @Override
    public String toString() {
        return "Book{" +
                "bookId=" + bookId +
                ", bookName='" + bookName + '\'' +
                ", bookPath='" + bookPath + '\'' +
                ", authorName='" + authorName + '\'' +
                ", updateTime='" + updateTime + '\'' +
                '}';
    }
}
