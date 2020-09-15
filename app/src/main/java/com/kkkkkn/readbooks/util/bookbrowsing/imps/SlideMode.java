package com.kkkkkn.readbooks.util.bookbrowsing.imps;

import android.graphics.Canvas;

import com.kkkkkn.readbooks.util.bookbrowsing.BrowsingViewInfo;
import com.kkkkkn.readbooks.util.bookbrowsing.BrowsingViewInterface;

public class SlideMode implements BrowsingViewInterface {
    private BrowsingViewInfo info;
    public SlideMode(BrowsingViewInfo info) {
        this.info=info;
    }

    @Override
    public void drawThisPage(Canvas canvas) {

    }

    @Override
    public void drawUpPage(Canvas canvas) {

    }

    @Override
    public void drawDownPage(Canvas canvas) {

    }

    @Override
    public void initDrawSet(BrowsingViewInfo info) {

    }
}
