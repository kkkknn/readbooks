package com.kkkkkn.readbooks.adapter;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.kkkkkn.readbooks.entity.BookInfo;

import java.io.File;
import java.util.ArrayList;

public class MainFragmentAdapter extends BaseAdapter {
    private final static String Path="/sdcard/books/";
    private ArrayList<BookInfo> list;

    public MainFragmentAdapter() {
        list=getBookList();
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {


        return null;
    }


    //读取文件目录，获取图书列表
    private static ArrayList<BookInfo> getBookList(){
        ArrayList<BookInfo> rlist = new ArrayList<>();
        File rootPath=new File(Path);
        String[] booksName=rootPath.list();
        for(int i=0;i<booksName.length;i++) {
            String imgpath=Path+booksName[i]+"/bookImg.jpg";
            if(!new File(imgpath).exists()){
                imgpath="";
            }
            //BookInfo bookInfo =new BookInfo(i,booksName[i],imgpath,"cccc","ccccccc");
            //rlist.add(bookInfo);
        }
        return rlist;
    }
}
