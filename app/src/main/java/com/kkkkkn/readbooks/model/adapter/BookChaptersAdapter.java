package com.kkkkkn.readbooks.model.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.kkkkkn.readbooks.R;
import com.kkkkkn.readbooks.model.entity.ChapterInfo;
import com.kkkkkn.readbooks.view.viewHolder.BookChaptersAdapterViewHolder;

import java.util.ArrayList;

public class BookChaptersAdapter extends RecyclerView.Adapter<BookChaptersAdapterViewHolder>{
    private final ArrayList<ChapterInfo> chapterList;
    private final Context mContext;
    private ItemOnClickListener onItemClickListener ;
    public static final int FOOT_VIEW=1;

    public BookChaptersAdapter(ArrayList<ChapterInfo> chapterList, Context mContext) {
        this.chapterList = chapterList;
        this.mContext = mContext;
    }

    @NonNull
    @Override
    public BookChaptersAdapterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if(viewType==FOOT_VIEW){
            View view = LayoutInflater.from(mContext).inflate(R.layout.activity_book_info_chapter_footview, parent, false);
            return new BookChaptersAdapterViewHolder(view,viewType);
        }else{
            View view = LayoutInflater.from(mContext).inflate(R.layout.activity_book_info_chapter_item, parent, false);
            return new BookChaptersAdapterViewHolder(view,viewType);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull com.kkkkkn.readbooks.view.viewHolder.BookChaptersAdapterViewHolder holder, int position) {
        if(getItemViewType(holder.getAdapterPosition())!=FOOT_VIEW){
            ChapterInfo chapterInfo=chapterList.get(holder.getAdapterPosition());
            if(chapterInfo!=null){
                holder.chapterName.setText(chapterInfo.getChapter_name());

                if(onItemClickListener!=null){
                    holder.chapterName.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            onItemClickListener.onItemClick(holder.getAdapterPosition());
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
        return chapterList.size()+1;
    }

    @Override
    public int getItemViewType(int position) {
        if(position+1==chapterList.size()+1){
            return FOOT_VIEW;
        }
        return super.getItemViewType(position);
    }

    public interface ItemOnClickListener{
        public void onItemClick(int position);
    }

    public void setItemOnClickListener(ItemOnClickListener listener) {
        this.onItemClickListener = listener;
    }

}
