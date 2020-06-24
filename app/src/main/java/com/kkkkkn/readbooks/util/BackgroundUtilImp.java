package com.kkkkkn.readbooks.util;

public interface BackgroundUtilImp {
    void searchBooks(String keywordStr, int accountId,String tokenStr);
    void addFavoriteBook(String bookStr,int accountId,String tokenStr);
    void getBookInfo(String bookStr,int accountId,String tokenStr);
    void getChapterContent(String chapterStr,int accountId,String tokenStr);
    void accountLogin(String account,String password);
    void accountRegister(String account,String password);
    int getAccountId();
    String getTokenStr();
    boolean setAccountId(int id);
    boolean setTokenStr(String str);
}
