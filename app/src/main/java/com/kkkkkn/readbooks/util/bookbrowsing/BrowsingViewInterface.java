package com.kkkkkn.readbooks.util.bookbrowsing;

import android.graphics.Canvas;

//多种翻页动画基础类
public interface BrowsingViewInterface {

    //绘制当前页
    public void drawThisPage(Canvas canvas);
    //绘制上一页
    public void drawUpPage(Canvas canvas);
    //绘制下一页
    public void drawDownPage(Canvas canvas);
    //初始化相关绘制参数
    public void initDrawSet(BrowsingViewInfo info);


}
