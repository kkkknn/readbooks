package com.kkkkkn.readbooks.util.bookbrowsing;

import android.graphics.Canvas;

//多种翻页动画基础类
public interface BrowsingViewInterface {

    //绘制当前页
    public void drawThisPage(Canvas canvas,char[] textArr,int offset,int style);
    //绘制上一页
    public void drawUpPage(Canvas canvas,char[] textArr,int offset,int style);
    //绘制下一页
    public void drawDownPage(Canvas canvas,char[] textArr);
    //初始化相关绘制参数
    public void initDrawSet(BrowsingViewInfo info);


}
