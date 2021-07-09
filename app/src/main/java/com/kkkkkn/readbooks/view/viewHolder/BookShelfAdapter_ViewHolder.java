package com.kkkkkn.readbooks.view.viewHolder;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.kkkkkn.readbooks.R;

public class BookShelfAdapter_ViewHolder {
    public TextView bookName;
    public ImageView bookImg;
    public View updateView;

    public BookShelfAdapter_ViewHolder(View viewRoot) {
        bookName = viewRoot.findViewById(R.id.bookshelf_item_book_name);
        bookImg = viewRoot.findViewById(R.id.bookshelf_item_book_img);
        updateView = viewRoot.findViewById(R.id.bookshelf_item_is_update);
    }
}
