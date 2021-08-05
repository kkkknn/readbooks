package com.kkkkkn.readbooks.view.view;

import com.kkkkkn.readbooks.model.entity.BookInfo;

import java.util.ArrayList;

public interface MainActivityView {
    void updateBookShelf(ArrayList<BookInfo> list);
    void showUpdateDialog(String msg);
    void toSearchActivity();
    void toBrowsingActivity();
    void toLoginActivity();
}
