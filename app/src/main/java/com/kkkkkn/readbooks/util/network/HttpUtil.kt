package com.kkkkkn.readbooks.util.network

import okhttp3.Callback
import okhttp3.OkHttpClient
import okhttp3.Request
import java.util.concurrent.TimeUnit

class HttpUtil private constructor() {
    private val okHttpClient: OkHttpClient = OkHttpClient().newBuilder() //访问超时
        .callTimeout(2, TimeUnit.MINUTES) //读取超时
        .readTimeout(15, TimeUnit.SECONDS) //写入超时
        .writeTimeout(15, TimeUnit.SECONDS)
        .build()

    fun post(request: Request?, callback: Callback?) {
        Thread { okHttpClient.newCall(request!!).enqueue(callback!!) }.start()
    }

    operator fun get(request: Request?, callback: Callback?) {
        Thread { okHttpClient.newCall(request!!).enqueue(callback!!) }.start()
    }

    companion object {
        @Volatile
        private var httpUtil: HttpUtil? = null
        val instance: HttpUtil?
            get() {
                if (httpUtil == null) {
                    synchronized(HttpUtil::class.java) {
                        if (httpUtil == null) {
                            httpUtil =
                                HttpUtil()
                        }
                    }
                }
                return httpUtil
            }
    }
}