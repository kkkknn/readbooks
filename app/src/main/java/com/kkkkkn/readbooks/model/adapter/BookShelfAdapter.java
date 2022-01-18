package com.kkkkkn.readbooks.model.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;


import com.bumptech.glide.Glide;
import com.kkkkkn.readbooks.R;
import com.kkkkkn.readbooks.model.entity.BookInfo;
import com.kkkkkn.readbooks.model.entity.BookShelfItem;
import com.kkkkkn.readbooks.util.ImageUtil;
import com.kkkkkn.readbooks.view.viewHolder.BookChaptersAdapter_ViewHolder;
import com.kkkkkn.readbooks.view.viewHolder.BookShelfAdapter_ViewHolder;

import java.util.ArrayList;

public class BookShelfAdapter extends BaseAdapter {
    private ArrayList<BookInfo> mArrayList;
    private Context mContext;
    private LayoutInflater mInflater;


    public BookShelfAdapter(ArrayList<BookInfo> mArrayList, Context mContext) {
        this.mArrayList = mArrayList;
        this.mContext = mContext;
        this.mInflater = LayoutInflater.from(mContext);
    }

    @Override
    public int getCount() {
        return mArrayList.size();
    }

    @Override
    public Object getItem(int position) {
        return mArrayList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        BookShelfAdapter_ViewHolder bookShelfAdapter_viewHolder=null;

        if(convertView==null){
            convertView=mInflater.inflate(R.layout.activity_main_item,parent,false);
            bookShelfAdapter_viewHolder=new BookShelfAdapter_ViewHolder(convertView);
            convertView.setTag(bookShelfAdapter_viewHolder);
        }else {
            bookShelfAdapter_viewHolder=(BookShelfAdapter_ViewHolder) convertView.getTag();
        }
        BookInfo bookInfo=mArrayList.get(position);
        if(bookInfo!=null){
            String url=bookInfo.getBookImgUrl();
            String name=bookInfo.getBookName();
            if(name!=null&&!name.isEmpty()){
                //bookShelfAdapter_viewHolder.bookName.setText(bookInfo.getBookName());
            }
            if(url!=null&&!url.isEmpty()){
                ImageUtil.loadImage(url,mContext,bookShelfAdapter_viewHolder.bookImg);
                //Glide.with(mContext).load(url).into(bookShelfAdapter_viewHolder.bookImg);
            }
            //bookShelfAdapter_viewHolder.updateView.setVisibility(bookInfo.isEnjoy()?View.VISIBLE:View.GONE);
        }


        return convertView;
    }

}
