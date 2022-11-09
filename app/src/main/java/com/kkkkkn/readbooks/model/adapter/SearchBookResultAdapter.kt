package com.kkkkkn.readbooks.model.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.kkkkkn.readbooks.R
import com.kkkkkn.readbooks.model.entity.BookInfo
import com.kkkkkn.readbooks.util.ImageUtil.loadImage
import com.kkkkkn.readbooks.view.viewHolder.SearchBookResultAdapterViewHolder
import java.util.*

class SearchBookResultAdapter(
    private val resultList: ArrayList<BookInfo>,
    private val mContext: Context
) :
    RecyclerView.Adapter<SearchBookResultAdapterViewHolder>() {
    private var itemOnClickListener: ItemOnClickListener? = null
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): SearchBookResultAdapterViewHolder {
        val view: View =
            LayoutInflater.from(mContext).inflate(R.layout.activity_search_item, parent, false)
        return SearchBookResultAdapterViewHolder(view, viewType)
    }

    override fun onBindViewHolder(holder: SearchBookResultAdapterViewHolder, position: Int) {
        val book = resultList[holder.adapterPosition]
        holder.authorName!!.text = book.authorName
        holder.bookName!!.text = book.bookName
        book.bookImgUrl?.let { loadImage(it, mContext, holder.bookImg) }
        if (itemOnClickListener != null) {
            holder.itemView.setOnClickListener {
                itemOnClickListener!!.onItemClick(
                    holder.adapterPosition
                )
            }
        }
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getItemCount(): Int {
        return resultList.size
    }

    interface ItemOnClickListener {
        fun onItemClick(position: Int)
    }

    fun setItemOnClickListener(listener: ItemOnClickListener?) {
        itemOnClickListener = listener
    }
}