package com.kkkkkn.readbooks.model

import android.content.Context
import org.greenrobot.eventbus.EventBus

abstract class BaseModel {
    private var isRegister = false
    var callBack: CallBack? = null
        private set

    fun setCallback(callback: CallBack?) {
        callBack = callback
    }

    interface CallBack {
        fun onSuccess(type: Int, `object`: kotlin.Any)
        fun onError(type: Int, `object`: kotlin.Any)
    }

    fun register() {
        if (!isRegister) {
            EventBus.getDefault().register(this)
            isRegister = true
        }
    }

    fun unregister() {
        if (isRegister) {
            EventBus.getDefault().unregister(this)
            isRegister = false
        }
    }

    fun getCachePath(context: Context): String {
        return context.filesDir.absolutePath + cachePath
    }

    companion object {
        private const val cachePath = "cache"
    }
}