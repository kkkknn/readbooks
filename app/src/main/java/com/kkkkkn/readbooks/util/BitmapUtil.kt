package com.kkkkkn.readbooks.util

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import com.kkkkkn.readbooks.R
import com.kkkkkn.readbooks.model.clientsetting.SettingConf

class BitmapUtil {

    enum class BACKGROUND_CONFIG{
        WOODEN,
        GREY,
        GREEN,
        YELLOW
    }

    //单例模式
    companion object{
        val sInstance by lazy (LazyThreadSafetyMode.SYNCHRONIZED){
            BitmapUtil()
        }
    }


    //章节文字转换为bitmap ，返回多个bitmap对象 list
    fun text2bitmap(str:String?,viewWidth:Int?,viewHeight:Int?,config:SettingConf):List<Bitmap>?{
        /*if(str==null||str.isEmpty()){
            return null
        }
        val list = mutableListOf<Bitmap>()
        var backgroundBitmap:Bitmap?=null
        when(config.backgroundStyle){
            1 ->backgroundBitmap= Bitmap.createScaledBitmap(R.drawable.browsingview,viewWidth,viewHeight,Bitmap.Config.ARGB_8888)
        }
*/
        return null
    }


}