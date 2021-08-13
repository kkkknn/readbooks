package com.kkkkkn.readbooks.view.view;

import com.kkkkkn.readbooks.model.entity.BookInfo;
import com.kkkkkn.readbooks.model.entity.ChapterInfo;

import java.util.ArrayList;
import java.util.LinkedList;

public interface BookInfoActivityView extends BaseView {
    void syncChapterList(ArrayList<ChapterInfo> linkedList);
    void toLoginActivity();
}
