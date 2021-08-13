package com.kkkkkn.readbooks.view.view;

import com.kkkkkn.readbooks.model.entity.BookInfo;

import java.util.ArrayList;

public interface SearchActivityView extends BaseView {
    void syncBookList(ArrayList<BookInfo> arrayList);
    void toLoginActivity();
}
