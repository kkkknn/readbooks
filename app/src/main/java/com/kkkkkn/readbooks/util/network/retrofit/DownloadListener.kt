package com.kkkkkn.readbooks.util.network.retrofit

interface DownloadListener {
    fun onProgress(progress:Int)
    fun onFinish(filePath:String)
    fun onError(errStr:String)

}