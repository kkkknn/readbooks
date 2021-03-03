package com.kkkkkn.readbooks.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.kkkkkn.readbooks.R;
import com.kkkkkn.readbooks.entity.BookInfo;
import com.kkkkkn.readbooks.entity.MainBooks;

import java.io.File;
import java.util.ArrayList;

public class BookShelfAdapter extends BaseAdapter {
    private ArrayList<MainBooks> mArrayList;
    private Context mContext;
    private LayoutInflater mInflater;

    public BookShelfAdapter(Context context, ArrayList<MainBooks> lxrs) {
        mContext = context;
        this.mArrayList = lxrs;
        mInflater = LayoutInflater.from(context);
        Log.i("TAG", "BookShelfAdapter: "+mArrayList.toString());
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
        ViewHolder viewHolder=null;
        if(convertView==null){
            convertView=mInflater.inflate(R.layout.activity_main_item,parent,false);
            viewHolder=new ViewHolder();
            viewHolder.bookImg=(ImageView)convertView.findViewById(R.id.book_img);
            viewHolder.bookName=(TextView) convertView.findViewById(R.id.book_name);
            viewHolder.updateSum=(TextView)convertView.findViewById(R.id.book_replaceSum);
            convertView.setTag(viewHolder);
        }else {
            viewHolder=(ViewHolder) convertView.getTag();

        }

        MainBooks mainBooks=mArrayList.get(position);
        if(mainBooks!=null){
            viewHolder.bookImg.setImageResource(R.drawable.bookimg);
            viewHolder.bookName.setText(mainBooks.getName());
            if(mainBooks.isUpdate()){
                viewHolder.updateSum.setVisibility(View.VISIBLE);
                int sum=mainBooks.getReplaceSum();
                if(sum<=99){
                    viewHolder.updateSum.setText(Integer.toString(sum));
                }else{
                    viewHolder.updateSum.setText("99+");
                }
            }else{
                viewHolder.updateSum.setVisibility(View.GONE);
            }
        }

        return convertView;
    }

    public static class ViewHolder {
        public TextView bookName;
        public ImageView bookImg;
        public TextView updateSum;
    }

}
