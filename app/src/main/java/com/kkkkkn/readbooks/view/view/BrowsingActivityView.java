package com.kkkkkn.readbooks.view.view;

import com.kkkkkn.readbooks.model.entity.ChapterInfo;

import org.json.JSONArray;

import java.util.ArrayList;

public interface BrowsingActivityView extends BaseView {
    void syncChapterList(ArrayList<ChapterInfo> list);
    void toLoginActivity();
    void syncReadView(JSONArray jsonArray);
    void setLoading(boolean type);
}
