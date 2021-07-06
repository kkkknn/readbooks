package com.kkkkkn.readbooks.model.entity;

import android.widget.ImageView;

import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;
import androidx.databinding.BindingAdapter;

import com.bumptech.glide.Glide;
import com.kkkkkn.readbooks.BR;

import java.io.Serializable;

public class BookShelfItem extends BaseObservable implements Serializable {
    private int book_id;
    private String book_name;
    private String book_img_url;
    private boolean is_update;

    @Bindable
    public int getBook_id() {
        return book_id;
    }

    public void setBook_id(int book_id) {
        this.book_id = book_id;
    }

    @Bindable
    public String getBook_name() {
        return book_name;
    }

    public void setBook_name(String book_name) {
        this.book_name = book_name;
        notifyPropertyChanged(BR.book_name);
    }

    @Bindable
    public String getBook_img_url() {
        return book_img_url;
    }

    public void setBook_img_url(String book_img_url) {
        this.book_img_url = book_img_url;
        notifyPropertyChanged(BR.book_img_url);
    }

    @Bindable
    public boolean isIs_update() {
        return is_update;
    }

    public void setIs_update(boolean is_update) {
        this.is_update = is_update;
        notifyPropertyChanged(BR.is_update);
    }

    @BindingAdapter("android:src")
    public static void setBookImage(ImageView view,String book_img_url){
        Glide.with(view.getContext()).load(book_img_url).into(view);
        System.out.println("显示图像");
    }

}
