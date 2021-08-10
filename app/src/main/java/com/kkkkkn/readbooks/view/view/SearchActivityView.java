package com.kkkkkn.readbooks.view.view;

import com.kkkkkn.readbooks.model.entity.BookInfo;

public interface SearchActivityView {
    void searchBook(String keyword);
    void toBrowsingActivity(BookInfo bookInfo);
}
