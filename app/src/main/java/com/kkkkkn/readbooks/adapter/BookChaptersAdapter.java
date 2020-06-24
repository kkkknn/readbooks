package com.kkkkkn.readbooks.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

public class BookChaptersAdapter extends BaseAdapter {
    private ArrayList<String> chapterList;
    private Context mContext;
    private LayoutInflater mInflater;

    public BookChaptersAdapter(ArrayList<String> chapterList, Context mContext) {
        this.chapterList = chapterList;
        this.mContext = mContext;
        this.mInflater = LayoutInflater.from(mContext);
    }

    @Override
    public int getCount() {
        return 0;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        return null;
    }

    private class ViewHolder{
        public TextView chapterName;
    }
}
