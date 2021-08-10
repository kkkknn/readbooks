package com.kkkkkn.readbooks.view.view;

import com.kkkkkn.readbooks.model.entity.BookInfo;

import java.util.ArrayList;

public interface SearchActivityView {
    void toBrowsingActivity(BookInfo bookInfo);
    void syncBookList(ArrayList<BookInfo> arrayList);
}
