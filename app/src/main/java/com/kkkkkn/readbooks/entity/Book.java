package com.kkkkkn.readbooks.entity;

//图书实体类
public class Book {
    private int bookId;
    private String bookName;
    private String bookPath;

    public Book(int bookId, String bookName, String bookPath) {
        this.bookId = bookId;
        this.bookName = bookName;
        this.bookPath = bookPath;
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

    @Override
    public String toString() {
        return "Book{" +
                "bookId=" + bookId +
                ", bookName='" + bookName + '\'' +
                ", bookPath='" + bookPath + '\'' +
                '}';
    }

}
