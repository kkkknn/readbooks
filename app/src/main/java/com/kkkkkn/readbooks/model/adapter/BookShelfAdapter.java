package com.kkkkkn.readbooks.model.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;

import com.bumptech.glide.Glide;
import com.kkkkkn.readbooks.R;
import com.kkkkkn.readbooks.model.entity.BookInfo;
import com.kkkkkn.readbooks.model.entity.BookShelfItem;
import com.kkkkkn.readbooks.view.viewHolder.BookShelfAdapter_ViewHolder;

import java.util.ArrayList;

public class BookShelfAdapter extends BaseAdapter {
    private ArrayList<BookShelfItem> mArrayList;
    private Context mContext;
    private LayoutInflater mInflater;
    private int layoutId;
    private int variableId;


    public BookShelfAdapter(ArrayList<BookShelfItem> mArrayList, Context mContext, LayoutInflater mInflater, int layoutId, int variableId) {
        this.mArrayList = mArrayList;
        this.mContext = mContext;
        this.mInflater = mInflater;
        this.layoutId = layoutId;
        this.variableId = variableId;


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
        ViewDataBinding viewDataBinding=null;

        if(convertView==null){
            viewDataBinding=DataBindingUtil.inflate(mInflater,layoutId,null,false);

        }else {
            viewDataBinding=DataBindingUtil.getBinding(convertView);
        }
        viewDataBinding.setVariable(variableId,mArrayList.get(position));


        return viewDataBinding.getRoot().getRootView();
    }

}
