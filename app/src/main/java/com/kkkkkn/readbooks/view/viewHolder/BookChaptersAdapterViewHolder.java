package com.kkkkkn.readbooks.view.viewHolder;

import android.view.View;

import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.RecyclerView;

import com.kkkkkn.readbooks.R;

import static com.kkkkkn.readbooks.model.adapter.BookChaptersAdapter.FOOT_VIEW;

public class BookChaptersAdapterViewHolder extends RecyclerView.ViewHolder {
    public AppCompatTextView chapterName;

    public BookChaptersAdapterViewHolder(View viewRoot,int type) {
        super(viewRoot);
        if(type!=FOOT_VIEW){
            chapterName=(AppCompatTextView) viewRoot.findViewById(R.id.book_info_chapter_item_chapterName);
        }
    }

}

