package com.kkkkkn.readbooks.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.kkkkkn.readbooks.R;
import com.kkkkkn.readbooks.entity.BookInfo;

import java.util.ArrayList;

public class SearchBookResultAdapter extends BaseAdapter {
    private ArrayList<BookInfo> resultList;
    private Context mContext;
    private LayoutInflater mInflater;

    public SearchBookResultAdapter(ArrayList<BookInfo> resultList, Context mContext) {
        this.resultList = resultList;
        this.mInflater=LayoutInflater.from(mContext);
        this.mContext = mContext;
    }

    @Override
    public int getCount() {
        return resultList.size();
    }

    @Override
    public Object getItem(int position) {
        return resultList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder=null;
        if(convertView==null){
            convertView=mInflater.inflate(R.layout.activity_search_item,parent,false);
            viewHolder=new ViewHolder();
            viewHolder.authorName=convertView.findViewById(R.id.fragment_search_item_book_author_name);
            viewHolder.bookName=convertView.findViewById(R.id.fragment_search_item_book_name);
            viewHolder.bookImg=convertView.findViewById(R.id.fragment_search_item_book_img);
            convertView.setTag(viewHolder);
        }else {
            viewHolder=(ViewHolder) convertView.getTag();

        }
        BookInfo book=resultList.get(position);
        if(book!=null){
            viewHolder.authorName.setText(book.getAuthorName());
            viewHolder.bookName.setText(book.getBookName());
            Glide.with(mContext).load(book.getBookImgUrl()).into(viewHolder.bookImg);;
        }
        return convertView;
    }

    public static class ViewHolder {
        public TextView bookName;
        public TextView authorName;
        public ImageView bookImg;
    }
}
