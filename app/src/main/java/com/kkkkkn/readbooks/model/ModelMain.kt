package com.kkkkkn.readbooks.model

import android.content.Context
import android.util.Log
import com.kkkkkn.readbooks.model.entity.BookInfo
import com.kkkkkn.readbooks.model.entity.BookInfo.Companion.changeObject
import com.kkkkkn.readbooks.util.ServerConfig
import com.kkkkkn.readbooks.util.StringUtil.isEmpty
import com.kkkkkn.readbooks.util.eventBus.EventMessage
import com.kkkkkn.readbooks.util.eventBus.events.MainEvent
import com.kkkkkn.readbooks.util.network.HttpUtil.Companion.instance
import okhttp3.*
import org.greenrobot.eventbus.Subscribe
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.InputStream
import java.util.*

class ModelMain : BaseModel() {
    @Subscribe
    fun syncProgress(event: MainEvent) {
        when (event.message) {
            EventMessage.GET_VERSION ->                 //获取版本号
                getVersion(event.accountId, event.token)
            EventMessage.DOWNLOAD_APK -> downloadAPK(
                event.name,
                event.path,
                event.url,
                event.accountId,
                event.token
            )
            EventMessage.GET_BOOKSHELF ->                 //获取书架
                getBookShelf(event.accountId, event.token)
            else -> {}
        }
    }

    fun updateBookShelf(bookShelf: ArrayList<BookInfo>, context: Context?): Boolean {
        val path = getCachePath(context!!)
        var jsonObject: JSONObject? = null
        try {
            jsonObject = JSONObject()
            for (info in bookShelf) {
                jsonObject.put(info.getBookId().toString(), info.getBookName())
            }
        } catch (e: JSONException) {
            e.printStackTrace()
            return false
        }
        return setBookShelfFString(jsonObject.toString(), path)
    }

    private fun setBookShelfFString(str: String, path: String): Boolean {
        var flag = false
        var fileOutputStream: FileOutputStream? = null
        try {
            val file = File(path)
            if (!file.exists()) {
                file.mkdirs()
            }
            fileOutputStream = FileOutputStream("$path/book_shelf")
            fileOutputStream.write(str.toByteArray(charset("UTF-8")))
            fileOutputStream.flush()
            flag = true
        } catch (e: IOException) {
            e.printStackTrace()
            flag = false
        } finally {
            if (fileOutputStream != null) {
                try {
                    fileOutputStream.close()
                } catch (e: IOException) {
                    e.printStackTrace()
                }
            }
        }
        return flag
    }

    private fun downloadAPK(name: String?, path: String?, url: String?, id: Int, token: String?) {
        if (isEmpty(name) || isEmpty(path) || isEmpty(url)|| isEmpty(token)) {
            Log.i(TAG, "downloadAPK: 参数错误")
            return
        }

        val request: Request = Request.Builder()
            .addHeader("accountId", id.toString())
            .addHeader("token", token!!)
            .url(ServerConfig.IP + ServerConfig.downloadAPK + "?urlPath=" + url)
            .get()
            .build()
        instance?.get(request, object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                callBack!!.onError(-3001, "下载失败")
            }

            @Throws(IOException::class)
            override fun onResponse(call: Call, response: Response) {
                if (response.body==null){
                    callBack!!.onError(-3001, "网络返回失败")
                    return
                }
                var `is`: InputStream? = null
                val buf = ByteArray(2048)
                var len = 0
                var fos: FileOutputStream? = null
                // 储存下载文件的目录
                val dir = File(path!!)
                if (!dir.exists()) {
                    dir.mkdirs()
                }
                val file = File(dir, name!!)
                try {
                    `is` = response.body!!.byteStream()
                    val total = response.body!!.contentLength()
                    fos = FileOutputStream(file)
                    var sum: Long = 0
                    var progress = 0
                    while (`is`.read(buf).also { len = it } != -1) {
                        fos.write(buf, 0, len)
                        sum += len.toLong()
                        val count = (sum * 1.0f / total * 100).toInt()
                        //下载中更新进度条 eventbus 通知
                        if (progress < count) {
                            progress = count
                            callBack!!.onSuccess(3002, progress)
                        }
                    }
                    fos.flush()
                    //下载完成
                    callBack!!.onSuccess(3001, file.absolutePath)
                } catch (e: Exception) {
                    e.printStackTrace()
                    callBack!!.onError(-3001, e.toString())
                } finally {
                    try {
                        fos?.close()
                        `is`?.close()
                    } catch (e: IOException) {
                        e.printStackTrace()
                        callBack!!.onError(-3001, e.toString())
                    }
                }
            }
        })
    }

    private fun getVersion(id: Int, token: String?) {
        if (token==null){
            callBack!!.onError(-2001, "token 为空")
            return
        }
        val request: Request = Request.Builder()
            .addHeader("accountId", id.toString())
            .addHeader("token", token)
            .url(ServerConfig.IP + ServerConfig.getVersionInfo)
            .get()
            .build()
        instance?.post(request, object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                callBack!!.onError(-2001, "访问出错")
            }

            @Throws(IOException::class)
            override fun onResponse(call: Call, response: Response) {
                //解析返回值
                val retStr = response.body!!.string()
                if (!isEmpty(retStr)) {
                    try {
                        val jsonObject = JSONObject(retStr)
                        val codeStr = jsonObject.getString("code")
                        val dataStr = jsonObject.getString("data")
                        if (codeStr == "success") {
                            callBack!!.onSuccess(2001, dataStr)
                        } else if (codeStr == "error") {
                            callBack!!.onError(-2002, dataStr)
                        }
                    } catch (e: JSONException) {
                        e.printStackTrace()
                    }
                } else {
                    callBack!!.onError(-2001, "解析失败")
                }
            }
        })
    }

    private fun getBookShelf(id: Int, token: String?) {
        if(token==null){
            callBack!!.onError(-1001, "token 为空")
            return
        }
        //okhttp post请求网络
        val formBody = FormBody.Builder()
        formBody.add("accountId", id.toString())
        val request: Request = Request.Builder()
            .addHeader("accountId", id.toString())
            .addHeader("token", token)
            .url(ServerConfig.IP + ServerConfig.getFavoriteBook)
            .post(formBody.build()) //传递请求体
            .build()
        instance?.post(request, object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                callBack!!.onError(-1001, "访问出错")
            }

            @Throws(IOException::class)
            override fun onResponse(call: Call, response: Response) {
                //解析返回值
                val retStr = response.body!!.string()
                if (!isEmpty(retStr)) {
                    try {
                        val jsonObject = JSONObject(retStr)
                        val codeStr = jsonObject.getString("code")
                        val dataStr = jsonObject.getString("data")
                        if (codeStr == "success") {
                            val jsonArray = JSONArray(dataStr)
                            val bookShelf = ArrayList<BookInfo>()
                            for (j in 0 until jsonArray.length()) {
                                val `object` = jsonArray[j] as JSONObject
                                val bookInfo = changeObject(`object`)
                                if (!bookInfo.isEmpty) {
                                    bookShelf.add(bookInfo)
                                    Log.i("TAG", "onResponse: " + bookInfo.getBookName())
                                }
                            }
                            callBack!!.onSuccess(1001, bookShelf)
                        } else if (codeStr == "error") {
                            callBack!!.onError(-1002, dataStr)
                        }
                    } catch (e: JSONException) {
                        e.printStackTrace()
                        callBack!!.onError(-1001, "请求网络书架解析失败")
                    }
                }
            }
        })
    }

    companion object {
        private const val TAG = "ModelMain"
    }
}