package com.kkkkkn.readbooks.model.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.kkkkkn.readbooks.R
import com.kkkkkn.readbooks.databinding.ActivityBookInfoChapterItemBinding
import com.kkkkkn.readbooks.databinding.ActivitySearchItemBinding
import com.kkkkkn.readbooks.model.entity.ChapterInfo
import com.kkkkkn.readbooks.view.viewHolder.BookChaptersAdapterViewHolder
import com.kkkkkn.readbooks.view.viewHolder.SearchBookResultAdapterViewHolder

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

        val viewBinding = ActivityBookInfoChapterItemBinding.inflate(LayoutInflater.from(parent.context))
        val layoutParams = ViewGroup.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        viewBinding.root.layoutParams = layoutParams
        return BookChaptersAdapterViewHolder(viewBinding.root,viewType)
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