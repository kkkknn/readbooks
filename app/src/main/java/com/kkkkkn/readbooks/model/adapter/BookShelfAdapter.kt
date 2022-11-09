package com.kkkkkn.readbooks.model.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import com.kkkkkn.readbooks.R
import com.kkkkkn.readbooks.model.entity.BookInfo
import com.kkkkkn.readbooks.util.ImageUtil.loadImage
import com.kkkkkn.readbooks.view.viewHolder.BookShelfAdapterViewHolder

class BookShelfAdapter(private val mArrayList: ArrayList<BookInfo>, private val mContext: Context) :
    BaseAdapter() {
    private val mInflater: LayoutInflater = LayoutInflater.from(mContext)

    override fun getCount(): Int {
        return mArrayList.size
    }

    override fun getItem(position: Int): Any {
        return mArrayList[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View? {
        var bookShelfAdapterViewHolder: BookShelfAdapterViewHolder? = null
        var  view = convertView
        if (view != null) {
            bookShelfAdapterViewHolder = view.tag as BookShelfAdapterViewHolder
        }else{
            view = mInflater.inflate(R.layout.activity_main_item, parent, false)
            bookShelfAdapterViewHolder = BookShelfAdapterViewHolder(view)
            view.tag = bookShelfAdapterViewHolder
        }
        val bookInfo = mArrayList[position]
        val url = bookInfo.getBookImgUrl()
        val name = bookInfo.getBookName()

        if (url != null && url.isNotEmpty()) {
            loadImage(
                url,
                mContext, bookShelfAdapterViewHolder.bookImg
            )
            //Glide.with(mContext).load(url).into(bookShelfAdapter_viewHolder.bookImg);
        }
        //bookShelfAdapter_viewHolder.updateView.setVisibility(bookInfo.isEnjoy()?View.VISIBLE:View.GONE);
        return view
    }
}