package com.kkkkkn.readbooks.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.kkkkkn.readbooks.R;
import com.kkkkkn.readbooks.entity.BookInfo;
import com.kkkkkn.readbooks.entity.MainBooks;
import com.kkkkkn.readbooks.entity.SearchBookItem;

import java.util.ArrayList;
import java.util.List;

public class SearchBookResultAdapter extends BaseAdapter {
    private ArrayList<SearchBookItem> resultList;
    private Context mContext;
    private LayoutInflater mInflater;

    public SearchBookResultAdapter(ArrayList<SearchBookItem> resultList, Context mContext) {
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
            convertView=mInflater.inflate(R.layout.fragment_search_item,parent,false);
            viewHolder=new ViewHolder();
            viewHolder.authorName=convertView.findViewById(R.id.fragment_search_item_book_author_name);
            viewHolder.bookName=convertView.findViewById(R.id.fragment_search_item_book_name);
            convertView.setTag(viewHolder);
        }else {
            viewHolder=(ViewHolder) convertView.getTag();

        }
        SearchBookItem book=resultList.get(position);
        if(book!=null){
            viewHolder.authorName.setText(book.getAuthorName());
            viewHolder.bookName.setText(book.getBookName());
        }
        return convertView;
    }

    public static class ViewHolder {
        public TextView bookName;
        public TextView authorName;
    }
}
