package com.kkkkkn.readbooks.view.viewHolder;

import android.view.View;
import android.widget.TextView;

import androidx.appcompat.widget.AppCompatTextView;

import com.kkkkkn.readbooks.R;

public class BookChaptersAdapter_ViewHolder {
    public AppCompatTextView chapterName;
    public BookChaptersAdapter_ViewHolder(View viewRoot) {
        chapterName=(AppCompatTextView) viewRoot.findViewById(R.id.book_info_chapter_item_chapterName);
    }
}

