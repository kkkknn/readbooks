package com.kkkkkn.readbooks.view.viewHolder;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;

import com.kkkkkn.readbooks.R;

public class SearchBookResultAdapter_ViewHolder {
    public AppCompatTextView bookName;
    public AppCompatTextView authorName;
    public AppCompatImageView bookImg;

    public SearchBookResultAdapter_ViewHolder(View viewRoot) {
        bookName=viewRoot.findViewById(R.id.fragment_search_item_book_name);
        authorName=viewRoot.findViewById(R.id.fragment_search_item_book_author_name);
        bookImg=viewRoot.findViewById(R.id.fragment_search_item_book_img);
    }
}
