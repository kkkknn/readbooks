package com.kkkkkn.readbooks.model.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.kkkkkn.readbooks.R;
import com.kkkkkn.readbooks.model.entity.BookInfo;
import com.kkkkkn.readbooks.view.viewHolder.BookShelfAdapter_ViewHolder;

import java.util.ArrayList;

public class BookShelfAdapter extends BaseAdapter {
    private ArrayList<BookInfo> mArrayList;
    private Context mContext;
    private LayoutInflater mInflater;

    public BookShelfAdapter(Context context, ArrayList<BookInfo> lxrs) {
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
        BookShelfAdapter_ViewHolder viewHolder=null;
        if(convertView==null){
            convertView=mInflater.inflate(R.layout.activity_main_item,parent,false);
            viewHolder=new BookShelfAdapter_ViewHolder();
            viewHolder.bookImg=(ImageView)convertView.findViewById(R.id.book_img);
            viewHolder.bookName=(TextView) convertView.findViewById(R.id.book_name);
            viewHolder.updateSum=(TextView)convertView.findViewById(R.id.book_replaceSum);
            convertView.setTag(viewHolder);
        }else {
            viewHolder=(BookShelfAdapter_ViewHolder) convertView.getTag();

        }

        BookInfo books=mArrayList.get(position);
        if(books!=null){
            Glide.with(mContext).load(books.getBookImgUrl()).into(viewHolder.bookImg);
            viewHolder.bookName.setText(books.getBookName());
            viewHolder.updateSum.setVisibility(View.GONE);
            /*if(books.isUpdate()){
                viewHolder.updateSum.setVisibility(View.VISIBLE);
                int sum=books.getReplaceSum();
                if(sum<=99){
                    viewHolder.updateSum.setText(Integer.toString(sum));
                }else{
                    viewHolder.updateSum.setText("99+");
                }
            }else{

            }*/

        }

        return convertView;
    }

    public static class ViewHolder {
        public TextView bookName;
        public ImageView bookImg;
        public TextView updateSum;
    }

}
