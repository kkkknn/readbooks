package com.kkkkkn.readbooks.view.viewHolder;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.widget.AppCompatImageButton;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;

import com.kkkkkn.readbooks.R;

public class BookShelfAdapter_ViewHolder {
    public AppCompatTextView bookName;
    public AppCompatImageView bookImg;
    public View updateView;

    public BookShelfAdapter_ViewHolder(View viewRoot) {
        /*bookName = viewRoot.findViewById(R.id.bookshelf_item_book_name);*/
        bookImg = viewRoot.findViewById(R.id.bookshelf_item_book_img);
        /*updateView = viewRoot.findViewById(R.id.bookshelf_item_is_update);*/
    }
}
