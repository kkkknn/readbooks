package com.kkkkkn.readbooks.view.viewHolder;

import android.view.View;
import android.widget.TextView;

import com.kkkkkn.readbooks.R;

public class BookChaptersAdapter_ViewHolder {
    public TextView chapterName;
    public BookChaptersAdapter_ViewHolder(View viewRoot) {
        chapterName=(TextView) viewRoot.findViewById(R.id.book_info_chapter_item_chapterName);
    }
}

