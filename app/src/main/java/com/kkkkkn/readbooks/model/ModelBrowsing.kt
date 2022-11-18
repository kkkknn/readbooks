package com.kkkkkn.readbooks.model

import android.content.Context
import android.util.Base64
import android.util.Log
import com.kkkkkn.readbooks.model.clientsetting.SettingConf
import com.kkkkkn.readbooks.model.entity.ChapterInfo
import com.kkkkkn.readbooks.util.StringUtil.isEmpty
import com.kkkkkn.readbooks.util.network.retrofit.RetrofitUtil
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import java.io.*
import java.util.*

class ModelBrowsing : BaseModel() {

    /**
     * 获取指定章节内容
     * @param accountId 用户ID
     * @param token 用户token
     * @param path  章节存储路径
     */
    fun getChapterContent(accountId: Int, token: String, path: String?): JSONObject {
        val retJsonObject: JSONObject = JSONObject()

        if (path == null) {
            retJsonObject.put("status", false)
            retJsonObject.put("data", "目录为空，请求错误")
            return retJsonObject
        }

        val response = RetrofitUtil.instance.service.getChapterContent(
            accountId.toString(),
            token,
            path.toString()
        ).execute()

        if (response.isSuccessful) {
            val retJson = JSONObject(response.body()!!.string())
            when (retJson.getString("code")) {
                "success" -> {
                    retJsonObject.put("status", true)
                    retJsonObject.put("data", retJson.getString("data"))
                    retJsonObject.put("url", path)
                }
                "error" -> {
                    retJsonObject.put("status", false)
                    retJsonObject.put("data", retJson.getString("data"))
                }
                else -> {
                    retJsonObject.put("status", false)
                    retJsonObject.put("data", "解析错误")
                }
            }
        } else {
            retJsonObject.put("status", false)
            retJsonObject.put("data", "网络请求失败")
        }
        return retJsonObject
    }

    /**
     * 获取章节列表
     * @param book_id   图书ID
     * @param page_count    页码
     * @param page_size     每页数量
     * @param account_id    用户ID
     * @param token     用户token
     */
    fun getChapterList(
        book_id: Int,
        page_count: Int,
        page_size: Int,
        account_id: Int,
        token: String
    ): JSONObject {
        val jsonObject: JSONObject = JSONObject()

        val response = RetrofitUtil.instance.service.getChapterList(
            account_id.toString(),
            token,
            book_id.toString(),
            page_count.toString(),
            page_size.toString()
        ).execute()

        if (response.isSuccessful) {
            val retJson = JSONObject(response.body()!!.string())
            when (retJson.getString("code")) {
                "success" -> {
                    //解析返回值
                    val jsonArray = JSONArray(retJson.getString("data"))
                    val arrayList = ArrayList<ChapterInfo>()
                    for (i in 0 until jsonArray.length()) {
                        val chapterInfo = ChapterInfo(jsonArray[i])
                        arrayList.add(chapterInfo)
                    }
                    jsonObject.put("status", true)
                    jsonObject.put("data", arrayList)
                }
                "error" -> {
                    jsonObject.put("status", false)
                    jsonObject.put("data", retJson.getString("data"))
                }
                else -> {
                    jsonObject.put("status", false)
                    jsonObject.put("data", "解析错误")
                }
            }
        } else {
            jsonObject.put("status", false)
            jsonObject.put("data", "网络请求失败")
        }
        return jsonObject
    }

    fun getReadProgress(book_id: Int, context: Context?): Int {
        val path = getCachePath(context!!)
        val str = getProgressString(path) ?: return 0
        //未找到进度就返回0
        val jsonObject: JSONObject?
        var flag = 1
        try {
            jsonObject = JSONObject(str)
            flag = jsonObject.getInt(book_id.toString())
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
            jsonObject = if (str == null) {
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