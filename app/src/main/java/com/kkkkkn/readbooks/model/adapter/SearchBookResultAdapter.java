package com.kkkkkn.readbooks.model.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.kkkkkn.readbooks.R;
import com.kkkkkn.readbooks.model.entity.BookInfo;
import com.kkkkkn.readbooks.model.entity.ChapterInfo;
import com.kkkkkn.readbooks.util.ImageUtil;
import com.kkkkkn.readbooks.view.viewHolder.BookChaptersAdapterViewHolder;
import com.kkkkkn.readbooks.view.viewHolder.SearchBookResultAdapterViewHolder;

import java.util.ArrayList;

public class SearchBookResultAdapter  extends RecyclerView.Adapter<SearchBookResultAdapterViewHolder> {
    private ArrayList<BookInfo> resultList;
    private Context mContext;
    public static final int FOOT_VIEW=1;
    private ItemOnClickListener itemOnClickListener;

    public SearchBookResultAdapter(ArrayList<BookInfo> resultList, Context mContext) {
        this.resultList = resultList;
        this.mContext = mContext;
    }

    @NonNull
    @Override
    public SearchBookResultAdapterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if(viewType==FOOT_VIEW){
            View view = LayoutInflater.from(mContext).inflate(R.layout.activity_search_footview, parent, false);
            return new SearchBookResultAdapterViewHolder(view,viewType);
        }else{
            View view = LayoutInflater.from(mContext).inflate(R.layout.activity_search_item, parent, false);
            return new SearchBookResultAdapterViewHolder(view,viewType);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull com.kkkkkn.readbooks.view.viewHolder.SearchBookResultAdapterViewHolder holder, int position) {
        if(getItemViewType(holder.getAdapterPosition())!=FOOT_VIEW){
            BookInfo book=resultList.get(holder.getAdapterPosition());
            if(book!=null){
                holder.authorName.setText(book.getAuthorName());
                holder.bookName.setText(book.getBookName());
                ImageUtil.loadImage(book.getBookImgUrl(),mContext,holder.bookImg);

                if(itemOnClickListener!=null){
                    holder.bookName.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            itemOnClickListener.onItemClick(holder.getAdapterPosition());
                        }
                    });
                    holder.bookName.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            itemOnClickListener.onItemClick(holder.getAdapterPosition());
                        }
                    });
                    holder.bookImg.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            itemOnClickListener.onItemClick(holder.getAdapterPosition());
                        }
                    });
                }
            }

        }

    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        return resultList.size()+1;
    }

    @Override
    public int getItemViewType(int position) {
        if(position+1==resultList.size()+1){
            return FOOT_VIEW;
        }
        return super.getItemViewType(position);
    }

    public interface ItemOnClickListener{
        public void onItemClick(int position);
    }

    public void setItemOnClickListener(SearchBookResultAdapter.ItemOnClickListener listener) {
        this.itemOnClickListener = listener;
    }
}
