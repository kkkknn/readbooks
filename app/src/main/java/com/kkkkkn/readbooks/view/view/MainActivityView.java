package com.kkkkkn.readbooks.view.view;

import android.view.View;

import com.kkkkkn.readbooks.model.entity.BookInfo;

import java.util.ArrayList;

public interface MainActivityView extends BaseView {
    void syncBookShelf(ArrayList<BookInfo> list);
    void showUpdateDialog( String msg, String url, String version);
    void toSearchActivity();
    void toLoginActivity();
    void updateProgress(int progress);
    void installAPK(String filePath);
}
