package com.kkkkkn.readbooks.model

import android.content.Context
import android.util.Log
import com.kkkkkn.readbooks.model.entity.BookInfo
import com.kkkkkn.readbooks.model.entity.BookInfo.Companion.changeObject
import com.kkkkkn.readbooks.util.network.ServerConfig
import com.kkkkkn.readbooks.util.StringUtil.isEmpty
import com.kkkkkn.readbooks.util.network.retrofit.DownloadListener
import com.kkkkkn.readbooks.util.network.retrofit.RetrofitUtil
import okhttp3.*
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.InputStream
import java.util.*

class ModelMain : BaseModel() {

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

    fun downloadAPK(name: String?, path: String?, url: String?, id: Int, token: String?, listener: DownloadListener) {
        if (isEmpty(name) || isEmpty(path) || isEmpty(url) || isEmpty(token)) {
            listener.onError("参数错误")
            return
        }

        RetrofitUtil.instance.service.downloadApk(
            id.toString(),
            token!!,
            ServerConfig.IP+ServerConfig.downloadAPK+"?urlPath=$url"
        ).enqueue(object :retrofit2.Callback<ResponseBody> {
            override fun onFailure(call: retrofit2.Call<ResponseBody>, t: Throwable) {
                listener.onError("下载失败")
            }

            override fun onResponse(
                call: retrofit2.Call<ResponseBody>,
                response: retrofit2.Response<ResponseBody>
            ) {
                if (response.body() == null) {
                    listener.onError("网络返回失败")
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
                    `is` = response.body()!!.byteStream()
                    val total = response.body()!!.contentLength()
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
                            listener.onProgress(progress)
                        }
                    }
                    fos.flush()
                    //下载完成
                    listener.onFinish( file.absolutePath)
                } catch (e: Exception) {
                    e.printStackTrace()
                    listener.onError(e.toString())
                } finally {
                    try {
                        fos?.close()
                        `is`?.close()
                    } catch (e: IOException) {
                        e.printStackTrace()
                        listener.onError(e.toString())
                    }
                }
            }

        })

    }

    fun getVersion(id: Int, token: String?):JSONObject {
        val jsonObject:JSONObject= JSONObject()
        if (token == null) {
            jsonObject.put("status",false)
            jsonObject.put("data","token 为空")
            return jsonObject
        }
        val response = RetrofitUtil.instance.service.getVersionInfo(
            id.toString(),
            token
        ).execute()
        if(response.isSuccessful){
            //解析返回值
            val retObject = JSONObject(response.body()!!.string())
            when(retObject.getString("code")){
                "success"-> {
                    jsonObject.put("status",true)
                    jsonObject.put("data",retObject.getString("data"))
                }
                "error" ->{
                    jsonObject.put("status",false)
                    jsonObject.put("data",retObject.getString("data"))
                }
            }
        }else {
            jsonObject.put("status",false)
            jsonObject.put("data","访问出错")
        }
        return jsonObject
    }

    fun getBookShelf(id: Int, token: String?):JSONObject {
        val jsonObject=JSONObject()
        if (token == null) {
            jsonObject.put("status",false)
            jsonObject.put("data","token 为空")
            return jsonObject
        }

        val response = RetrofitUtil.instance.service.getFavoriteBook(
            id.toString(),
            token,
            id.toString()
        ).execute()

        if(response.isSuccessful){
            val json=JSONObject(response.body()!!.string())
            when(json.getString("code")){
                "success"->{
                    val jsonArray = JSONArray(json.getString("data"))
                    val bookShelf = ArrayList<BookInfo>()
                    for (j in 0 until jsonArray.length()) {
                        val `object` = jsonArray[j] as JSONObject
                        val bookInfo = changeObject(`object`)
                        if (!bookInfo.isEmpty) {
                            bookShelf.add(bookInfo)
                            Log.i("TAG", "onResponse: " + bookInfo.getBookName())
                        }
                    }
                    jsonObject.put("status",true)
                    jsonObject.put("data",bookShelf)
                }
                "error"->{
                    jsonObject.put("status",false)
                    jsonObject.put("data",jsonObject.getString("data"))
                }
            }

        }else{

            jsonObject.put("status",false)
            jsonObject.put("data","访问出错")
        }

        return jsonObject
    }

    companion object {
        private const val TAG = "ModelMain"
    }
}