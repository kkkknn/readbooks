package com.kkkkkn.readbooks.view.customView.flipping

import android.content.Context
import android.content.res.Resources
import android.graphics.Bitmap
import android.util.Log
import okhttp3.internal.notify
import okhttp3.internal.wait
import java.util.*

class LoadBitmapTask private constructor(context: Context) :Runnable{
    private val bitmapList:LinkedList<Bitmap> = LinkedList<Bitmap>()
    private var mResources: Resources? = null
    //默认最大值长度5
    private val bitmapMaxSize:Int=5
    private var thread:Thread?=null
    private var mStop: Boolean = false


    init {
        mResources=context.resources

    }

    //单例模式
    companion object{
        const val TAG: String="LoadBitmapTask"
        var instance:LoadBitmapTask?=null

        fun getInstance(context: Context):LoadBitmapTask{
            if(instance==null){
                synchronized(LoadBitmapTask::class){
                    if(instance==null){
                        instance=LoadBitmapTask(context)
                    }
                }
            }
            return instance!!
        }
    }


    //循环，保证5个文字bitmap存在
    override fun run() {
        while (true) {
            synchronized(this) {

                // check if ask thread stopping
                if (mStop) {
                    clearBitmap()
                    return
                }

                // load bitmap only when no cached bitmap in queue
                val size: Int = bitmapList.size
                if (size < bitmapMaxSize) {
                    //todo 通过回调函数通知网络获取上下文字
                    for (i in 0 until bitmapMaxSize) {
                        Log.d(TAG, "Load Queue:$i in background!")
                        //bitmapList.push(getRandomBitmap())
                    }
                }

                // wait to be awaken
                try {
                    wait()
                } catch (_: InterruptedException) {
                }
            }
        }
    }

    @Synchronized
    fun start() {
        if (thread == null || !thread!!.isAlive) {
            mStop = false
            thread = Thread(this)
            thread!!.start()
        }
    }

    fun stop() {
        synchronized(this) {
            mStop = true
            notify()
        }

        var i = 0
        while (i < 3 && thread!!.isAlive) {
            Log.d(LoadBitmapTask.TAG, "Waiting thread to stop ...")
            try {
                Thread.sleep(500)
            } catch (_: InterruptedException) {
            }
            ++i
        }
        if (thread!!.isAlive) {
            Log.d(LoadBitmapTask.TAG, "Thread is still alive after waited 1.5s!")
        }
    }


    private fun clearBitmap() {
        for (i in bitmapList.indices) {
            bitmapList[i].recycle()
        }
        bitmapList.clear()
    }
}