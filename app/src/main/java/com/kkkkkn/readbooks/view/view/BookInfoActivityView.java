package com.kkkkkn.readbooks.view.view;

import android.os.Bundle;

import com.kkkkkn.readbooks.model.entity.BookInfo;
import com.kkkkkn.readbooks.model.entity.ChapterInfo;

import java.util.ArrayList;
import java.util.LinkedList;

public interface BookInfoActivityView extends BaseView {
    void syncChapterList(ArrayList<ChapterInfo> linkedList);
    void toLoginActivity();
    void toBrowsingActivity(Bundle bundle);
}
