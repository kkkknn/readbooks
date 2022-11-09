package com.kkkkkn.readbooks.view.viewHolder

import android.view.View
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.AppCompatTextView
import androidx.recyclerview.widget.RecyclerView
import com.kkkkkn.readbooks.R
import com.kkkkkn.readbooks.model.adapter.SearchBookResultAdapter

class SearchBookResultAdapterViewHolder(viewRoot: View, type: Int) :
    RecyclerView.ViewHolder(viewRoot) {
    var bookName: AppCompatTextView? = null
    var authorName: AppCompatTextView? = null
    var bookImg: AppCompatImageView? = null

    init {
        bookName = viewRoot.findViewById(R.id.fragment_search_item_book_name)
        authorName = viewRoot.findViewById(R.id.fragment_search_item_book_author_name)
        bookImg = viewRoot.findViewById(R.id.fragment_search_item_book_img)
    }
}