package com.kkkkkn.readbooks.view.viewHolder;

import android.view.View;

import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.RecyclerView;

import com.kkkkkn.readbooks.R;

import static com.kkkkkn.readbooks.model.adapter.SearchBookResultAdapter.FOOT_VIEW;

public class SearchBookResultAdapterViewHolder extends RecyclerView.ViewHolder  {
    public AppCompatTextView bookName;
    public AppCompatTextView authorName;
    public AppCompatImageView bookImg;

    public SearchBookResultAdapterViewHolder(View viewRoot,int type) {
        super(viewRoot);
        if(type!=FOOT_VIEW){
            bookName=viewRoot.findViewById(R.id.fragment_search_item_book_name);
            authorName=viewRoot.findViewById(R.id.fragment_search_item_book_author_name);
            bookImg=viewRoot.findViewById(R.id.fragment_search_item_book_img);
        }
    }
}
