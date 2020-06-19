package com.kkkkkn.readbooks.util;

import android.content.Context;

import com.kkkkkn.readbooks.entity.SearchBookInfo;

import java.util.ArrayList;

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
