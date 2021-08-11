package com.kkkkkn.readbooks.view.view;

import com.kkkkkn.readbooks.model.entity.BookInfo;

import java.util.ArrayList;

public interface SearchActivityView {
    void syncBookList(ArrayList<BookInfo> arrayList);
    void toLoginActivity();
}
