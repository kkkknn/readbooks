package com.kkkkkn.readbooks.util.sqlite.inf;

import java.util.ArrayList;

public interface SQLiteUtil {
    //添加下载图书数据
    public boolean addBook();
    //删除下载图书数据
    public boolean deleteBook();
    //查询下载图书数据
    public ArrayList<String> selectBook();

}