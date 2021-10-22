package com.kkkkkn.readbooks.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.model.GlideUrl;
import com.bumptech.glide.load.model.LazyHeaders;
import com.kkkkkn.readbooks.ServerConfig;

import java.util.Objects;

public class ImageUtil {

    public static void loadImage(String path, Context context, ImageView imageView){
        String url= ServerConfig.IP+ServerConfig.downloadBookImage+"?urlPath="+path;
        //获取缓存内的id token
        SharedPreferences sharedPreferences=context.getSharedPreferences("AccountInfo",Context.MODE_PRIVATE);

        GlideUrl glideUrl=new GlideUrl(url,
                new LazyHeaders.Builder()
                        .addHeader("accountId", Integer.toString(sharedPreferences.getInt("account_id",-1)))
                        .addHeader("token", Objects.requireNonNull(sharedPreferences.getString("account_token", "")))
                        .build());
         Glide.with(context).load(glideUrl).into(imageView);
    }

}
