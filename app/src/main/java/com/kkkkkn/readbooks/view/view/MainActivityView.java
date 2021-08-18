package com.kkkkkn.readbooks.view.view;

import com.kkkkkn.readbooks.model.entity.BookInfo;

import java.util.ArrayList;

public interface MainActivityView extends BaseView {
    void syncBookShelf(ArrayList<BookInfo> list);
    void showUpdateDialog(String msg);
    void toSearchActivity();
    void toLoginActivity();
}
