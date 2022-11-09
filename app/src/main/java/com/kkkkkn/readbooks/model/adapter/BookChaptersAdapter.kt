package com.kkkkkn.readbooks.model.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.kkkkkn.readbooks.R
import com.kkkkkn.readbooks.model.entity.ChapterInfo
import com.kkkkkn.readbooks.view.viewHolder.BookChaptersAdapterViewHolder

class BookChaptersAdapter(
    private val chapterList: ArrayList<ChapterInfo>,
    private val mContext: Context
) :
    RecyclerView.Adapter<BookChaptersAdapterViewHolder>() {
    private var onItemClickListener: ItemOnClickListener? = null
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): BookChaptersAdapterViewHolder {
        val view = LayoutInflater.from(mContext)
            .inflate(R.layout.activity_book_info_chapter_item, parent, false)
        return BookChaptersAdapterViewHolder(view, viewType)
    }

    override fun onBindViewHolder(holder: BookChaptersAdapterViewHolder, position: Int) {
        val chapterInfo = chapterList[holder.adapterPosition]
        holder.chapterName.text = chapterInfo.chapterName
        if (onItemClickListener != null) {
            holder.itemView.setOnClickListener { onItemClickListener!!.onItemClick(holder.adapterPosition) }
        }
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getItemCount(): Int {
        return chapterList.size
    }

    override fun getItemViewType(position: Int): Int {
        return super.getItemViewType(position)
    }

    interface ItemOnClickListener {
        fun onItemClick(position: Int)
    }

    fun setItemOnClickListener(listener: ItemOnClickListener) {
        onItemClickListener = listener
    }
}