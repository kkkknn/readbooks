package com.kkkkkn.readbooks.util

import android.content.Context
import android.view.View
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.MultiTransformation
import com.bumptech.glide.load.model.GlideUrl
import com.bumptech.glide.load.model.LazyHeaders


object ImageUtil {


    fun loadImage(path: String?, context: Context, imageView: ImageView?) {
        if(path==null){
            return
        }

        val url = ServerConfig.IP + ServerConfig.downloadBookImage + "?urlPath=" + path
        //获取缓存内的id token
        val sharedPreferences = context.getSharedPreferences("AccountInfo", Context.MODE_PRIVATE)
        val glideUrl = GlideUrl(
            url,
            sharedPreferences.getString("accountToken", "")?.let {
                LazyHeaders.Builder()
                    .addHeader(
                        "accountId",
                        sharedPreferences.getInt("account_id", -1).toString()
                    )
                    .addHeader(
                        "token",
                        it
                    )
                    .build()
            }
        )
        Glide.with(context).load(glideUrl).into(imageView!!)
    }

}