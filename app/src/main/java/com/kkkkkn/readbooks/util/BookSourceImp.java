package com.kkkkkn.readbooks.util;

public interface BookSourceImp {
    //获取图书章节列表
    void getChapterList(String bookUrl);
    //获取具体章节内内容
    void getChapterContent(String chapterUrl);
    //搜索图书
    void searchBook(String str);
    //设置回调监听
    void setListener(BookSourceListener bookSourceListener);
}
