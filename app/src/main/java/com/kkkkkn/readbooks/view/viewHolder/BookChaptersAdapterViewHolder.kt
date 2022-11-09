package com.kkkkkn.readbooks.view.viewHolder

import android.view.View
import androidx.appcompat.widget.AppCompatTextView
import androidx.recyclerview.widget.RecyclerView
import com.kkkkkn.readbooks.R

class BookChaptersAdapterViewHolder(viewRoot: View, type: Int) :
    RecyclerView.ViewHolder(viewRoot) {
    var chapterName: AppCompatTextView

    init {
        chapterName =
            viewRoot.findViewById<View>(R.id.book_info_chapter_item_chapterName) as AppCompatTextView
    }
}