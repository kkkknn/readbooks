package com.kkkkkn.readbooks.model.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.kkkkkn.readbooks.R;
import com.kkkkkn.readbooks.model.entity.ChapterInfo;
import com.kkkkkn.readbooks.view.viewHolder.BookChaptersAdapter_ViewHolder;

import java.util.ArrayList;

public class BookChaptersAdapter extends BaseAdapter {
    private ArrayList<ChapterInfo> chapterList;
    private Context mContext;
    private LayoutInflater mInflater;

    public BookChaptersAdapter(ArrayList<ChapterInfo> chapterList, Context mContext) {
        this.chapterList = chapterList;
        this.mContext = mContext;
        this.mInflater = LayoutInflater.from(mContext);
    }

    @Override
    public int getCount() {
        return chapterList.size();
    }

    @Override
    public Object getItem(int position) {
        return chapterList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        BookChaptersAdapter_ViewHolder viewHolder=null;
        if(convertView==null){
            convertView=mInflater.inflate(R.layout.activity_book_info_chapter_item,parent,false);
            viewHolder=new BookChaptersAdapter_ViewHolder(convertView);
            convertView.setTag(viewHolder);
        }else {
            viewHolder=(BookChaptersAdapter_ViewHolder) convertView.getTag();

        }

        ChapterInfo chapterInfo=chapterList.get(position);
        if(chapterInfo!=null){
            viewHolder.chapterName.setText(chapterInfo.getChapter_name());
        }

        return convertView;
    }


}
