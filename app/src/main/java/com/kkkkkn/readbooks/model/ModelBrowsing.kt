package com.kkkkkn.readbooks.model

import android.content.Context
import android.util.Base64
import android.util.Log
import com.kkkkkn.readbooks.model.clientsetting.SettingConf
import com.kkkkkn.readbooks.model.entity.ChapterInfo
import com.kkkkkn.readbooks.util.ServerConfig
import com.kkkkkn.readbooks.util.StringUtil.isEmpty
import com.kkkkkn.readbooks.util.eventBus.EventMessage
import com.kkkkkn.readbooks.util.eventBus.events.BrowsingEvent
import com.kkkkkn.readbooks.util.network.HttpUtil.Companion.instance
import okhttp3.*
import org.greenrobot.eventbus.Subscribe
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import java.io.*
import java.util.*

class ModelBrowsing : BaseModel() {
    @Subscribe
    fun syncProgress(event: BrowsingEvent) {
        when (event.message) {
            EventMessage.GET_BOOK_CHAPTER_LIST -> getChapterList(
                event.bookId,
                event.pageCount,
                event.pageSize,
                event.accountId,
                event.token
            )
            EventMessage.GET_CHAPTER_CONTENT -> getChapterContent(
                event.accountId,
                event.token,
                event.path
            )
            else -> {}
        }
    }

    /**
     * 获取指定章节内容
     * @param accountId 用户ID
     * @param token 用户token
     * @param path  章节存储路径
     */
    private fun getChapterContent(accountId: Int, token: String, path: String?) {
        val formBody = FormBody.Builder()
        if (path != null) {
            formBody.add("chapter_path", path)
        }
        val request: Request = Request.Builder()
            .url(ServerConfig.IP + ServerConfig.getChapterContent)
            .addHeader("accountId", accountId.toString())
            .addHeader("token", token)
            .post(formBody.build()) //传递请求体
            .build()
        instance?.post(request, object : Callback {
            @Throws(IOException::class)
            override fun onResponse(call: Call, response: Response) {
                val retStr = response.body!!.string()
                try {
                    val retJson = JSONObject(retStr)
                    when (retJson.getString("code")) {
                        "success" -> {
                            retJson.put("url", path)
                            callBack!!.onSuccess(1002, retJson)
                        }
                        "error" -> {
                            callBack!!.onError(-1002, retJson["data"] as String)
                        }
                        else -> {
                            callBack!!.onError(-1, "请求失败")
                        }
                    }
                } catch (e: JSONException) {
                    e.printStackTrace()
                }
            }

            override fun onFailure(call: Call, e: IOException) {
                callBack!!.onError(-1, "请求失败")
            }
        })
    }

    /**
     * 获取章节列表
     * @param book_id   图书ID
     * @param page_count    页码
     * @param page_size     每页数量
     * @param account_id    用户ID
     * @param token     用户token
     */
    private fun getChapterList(
        book_id: Int,
        page_count: Int,
        page_size: Int,
        account_id: Int,
        token: String
    ) {
        val formBody = FormBody.Builder()
        formBody.add("bookId", book_id.toString())
        formBody.add("pageCount", page_count.toString())
        formBody.add("pageSize", page_size.toString())
        val request: Request = Request.Builder()
            .url(ServerConfig.IP + ServerConfig.getChapterList)
            .addHeader("accountId", account_id.toString())
            .addHeader("token", token)
            .post(formBody.build()) //传递请求体
            .build()
        instance?.post(request, object : Callback {
            @Throws(IOException::class)
            override fun onResponse(call: Call, response: Response) {
                val retStr = response.body!!.string()
                try {
                    val retJson = JSONObject(retStr)
                    val retCode = retJson.getString("code")
                    val retData = retJson.getString("data")
                    when (retCode) {
                        "success" -> {
                            //解析返回值
                            val jsonArray = JSONArray(retData)
                            val arrayList = ArrayList<ChapterInfo>()
                            for (i in 0 until jsonArray.length()) {
                                val chapterInfo = ChapterInfo(jsonArray[i])
                                arrayList.add(chapterInfo)
                            }
                            callBack!!.onSuccess(1001, arrayList)
                        }
                        "error" -> {
                            callBack!!.onError(-1001, retData)
                        }
                        else -> {
                            callBack!!.onError(-1, "请求失败")
                        }
                    }
                } catch (e: JSONException) {
                    e.printStackTrace()
                }
            }

            override fun onFailure(call: Call, e: IOException) {
                callBack!!.onError(-1, "请求失败")
            }
        })
    }

    fun getReadProgress(book_id: Int, context: Context?): Int {
        val path = getCachePath(context!!)
        val str = getProgressString(path) ?: return 0
        //未找到进度就返回0
        var jsonObject: JSONObject? = null
        var flag = 1
        try {
            jsonObject = JSONObject(str)
            flag = jsonObject.getInt(Integer.toString(book_id))
        } catch (e: JSONException) {
            e.printStackTrace()
        }
        return flag
    }

    fun setReadProgress(book_id: Int, progress: Int, context: Context?): Boolean {
        val path = getCachePath(context!!)
        val str = getProgressString(path)
        var jsonObject: JSONObject? = null
        try {
            jsonObject = if (str==null) {
                JSONObject()
            } else {
                JSONObject(str)
            }
            jsonObject.put(book_id.toString(), progress)
        } catch (e: JSONException) {
            e.printStackTrace()
            return false
        }
        return setProgressString(jsonObject.toString(), path)
    }

    /**
     * 获取阅读配置，字体，背景等
     * @return
     */
    fun getReadConfig(context: Context): SettingConf? {
        val sharedPreferences = context.getSharedPreferences("read_config", Context.MODE_PRIVATE)
        val str = sharedPreferences.getString("SettingConf", null)
        //采用序列化的方式，将SettingConf 对象写入到SharedPreferences 中
        var settingConf: SettingConf? = null
        if (!isEmpty(str)) {
            val byteArrayInputStream = ByteArrayInputStream(Base64.decode(str, Base64.DEFAULT))
            try {
                val objectInputStream = ObjectInputStream(byteArrayInputStream)
                settingConf = objectInputStream.readObject() as SettingConf
            } catch (e: IOException) {
                e.printStackTrace()
            } catch (e: ClassNotFoundException) {
                e.printStackTrace()
            }
        }
        return settingConf
    }

    /**
     * 获取阅读配置，字体，背景等
     * @return
     */
    fun setReadConfig(context: Context, conf: SettingConf?): Boolean {
        val sharedPreferences = context.getSharedPreferences("read_config", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit() //获取编辑器
        //采用序列化的方式，将SettingConf 对象写入到SharedPreferences 中
        val baoS = ByteArrayOutputStream()
        try {
            val oos = ObjectOutputStream(baoS)
            oos.writeObject(conf) //把对象写到流里
            val temp = String(Base64.encode(baoS.toByteArray(), Base64.DEFAULT))
            editor.putString("SettingConf", temp)
            editor.apply()
        } catch (e: IOException) {
            e.printStackTrace()
            return false
        }
        return true
    }

    //获取写入的json字符串
    private fun getProgressString(path: String): String? {
        val fileInputStream: FileInputStream
        var reader: BufferedReader? = null
        val content = StringBuilder()
        try {
            val file = File("$path/book_progress")
            if (!file.exists() || !file.isFile) {
                Log.e(TAG, "getProgressString: 未找到章节记录文件")
                return null
            }
            fileInputStream = FileInputStream("$path/book_progress")
            reader = BufferedReader(InputStreamReader(fileInputStream))
            var tmp: String? = ""
            while (reader.readLine().also { tmp = it } != null) {
                content.append(tmp)
            }
        } catch (e: IOException) {
            e.printStackTrace()
        } finally {
            if (reader != null) {
                try {
                    reader.close()
                } catch (e: IOException) {
                    e.printStackTrace()
                }
            }
        }
        return content.toString()
    }

    //写入读书进度的json字符串
    private fun setProgressString(str: String, path: String): Boolean {
        var flag = false
        var fileOutputStream: FileOutputStream? = null
        try {
            val file = File(path)
            if (!file.exists()) {
                file.mkdirs()
            }
            fileOutputStream = FileOutputStream("$path/book_progress")
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

    companion object {
        private const val TAG = "ModelBrowsing"
    }
}