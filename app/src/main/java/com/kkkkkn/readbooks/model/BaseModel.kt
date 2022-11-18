package com.kkkkkn.readbooks.model

import android.content.Context

abstract class BaseModel {

    fun getCachePath(context: Context): String {
        return context.filesDir.absolutePath + cachePath
    }

    companion object {
        private const val cachePath = "cache"
    }
}