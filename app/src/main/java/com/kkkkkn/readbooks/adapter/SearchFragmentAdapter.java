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

import java.util.ArrayList;
import java.util.List;

public class SearchFragmentAdapter extends RecyclerView.Adapter<SearchFragmentAdapter.ViewHolder> {
    private List<BookInfo> mList;

    static class ViewHolder extends RecyclerView.ViewHolder{
        TextView bookName,bookAbout,bookAuthor;
        ImageView bookImg;
        public ViewHolder(View view){
            super(view);
            bookName=view.findViewById(R.id.fragment_search_item_book_name);
            bookAbout=view.findViewById(R.id.fragment_search_item_book_about);
            bookAuthor=view.findViewById(R.id.fragment_search_item_book_author_name);
            bookImg=view.findViewById(R.id.fragment_search_item_book_img);
        }

    }

    public SearchFragmentAdapter(List<BookInfo> mList) {
        this.mList = mList;
    }


    @NonNull
    @Override
    public SearchFragmentAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view=LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_search_item,parent,false);
        ViewHolder viewHolder=new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull SearchFragmentAdapter.ViewHolder holder, int position) {
        BookInfo bookInfo=mList.get(position);
        holder.bookName.setText(bookInfo.getBookName());
        holder.bookAbout.setText(bookInfo.getBookAbout());
        holder.bookAuthor.setText(bookInfo.getAuthorName());
        if(bookInfo.getBookImg()!=null){
            holder.bookImg.setImageBitmap(bookInfo.getBookImg());
        }
    }

    //动态添加图书相关
    public void addItem(BookInfo bookInfo){
        if(mList!=null){
            mList.add(bookInfo);
            notifyDataSetChanged();
        }
    }
    //清空搜索到的图书列表
    public void removeAll(){
        if(mList!=null){
            mList.clear();
            notifyDataSetChanged();
        }
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }
}
