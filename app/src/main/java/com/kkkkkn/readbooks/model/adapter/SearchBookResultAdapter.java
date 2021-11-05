package com.kkkkkn.readbooks.model.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.kkkkkn.readbooks.R;
import com.kkkkkn.readbooks.model.entity.BookInfo;
import com.kkkkkn.readbooks.util.ImageUtil;
import com.kkkkkn.readbooks.view.viewHolder.SearchBookResultAdapter_ViewHolder;

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
        SearchBookResultAdapter_ViewHolder viewHolder=null;
        if(convertView==null){
            convertView=mInflater.inflate(R.layout.activity_search_item,parent,false);
            viewHolder=new SearchBookResultAdapter_ViewHolder(convertView);
            convertView.setTag(viewHolder);
        }else {
            viewHolder=(SearchBookResultAdapter_ViewHolder) convertView.getTag();

        }
        BookInfo book=resultList.get(position);
        if(book!=null){
            viewHolder.authorName.setText(book.getAuthorName());
            viewHolder.bookName.setText(book.getBookName());
            ImageUtil.loadImage(book.getBookImgUrl(),mContext,viewHolder.bookImg);

        }
        return convertView;
    }

}
