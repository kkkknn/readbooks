package com.kkkkkn.readbooks.util

import android.util.Log
import org.json.JSONArray
import org.json.JSONException
import java.io.*

//章节内容缓存
object ChapterUtil {
    private const val TAG = "ChapterUtil"

    //缓存根目录
    private const val filePath = "/cacheChapters/"

    //保存读取到的缓存文字 章节URL ，后期可加密
    fun cacheChapter(
        jsonArray: JSONArray?,
        path: String,
        bookName: String,
        chapterName: String
    ): Boolean {
        if (jsonArray == null || bookName.isEmpty() || chapterName.isEmpty()) {
            Log.e(TAG, "cacheChapter: 入参为空")
            return false
        }
        val out: FileOutputStream?
        var writer: BufferedWriter? = null
        return try {
            val file = File("$path$filePath/$bookName/")
            if (!file.exists()) {
                file.mkdirs()
            }
            out = FileOutputStream(File("$path$filePath/$bookName/$chapterName"))
            writer = BufferedWriter(OutputStreamWriter(out))
            for (i in 0 until jsonArray.length()) {
                writer.write(jsonArray.getString(i))
                writer.newLine()
            }
            writer.flush()
            true
        } catch (e: IOException) {
            e.printStackTrace()
            false
        } catch (e: JSONException) {
            e.printStackTrace()
            false
        } finally {
            try {
                writer?.close()
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
    }

    //读取缓存的章节文字
    fun readCacheChapter(path: String, fileName: String?, chapterName: String?): Array<String>? {
        if (fileName==null||fileName.isEmpty()||chapterName==null||chapterName.isEmpty()) {
            Log.e(TAG, "cacheChapter: 入参为空")
            return null
        }
        val inputStream: FileInputStream?
        var reader: BufferedReader? = null
        val arrayList = ArrayList<String>()
        return try {
            val file = File("$path$filePath$fileName/$chapterName")
            if (!file.exists() || !file.isFile) {
                Log.e(TAG, "readCacheChapter: 未找到缓存文件")
                return null
            }
            Log.i(TAG, "readCacheChapter: "+file.path)
            inputStream = FileInputStream(file)
            reader = BufferedReader(InputStreamReader(inputStream))
            var temp: String?
            while (reader.readLine().also { temp = it } != null) {
                temp?.let { arrayList.add(it) }
            }
            arrayList.toTypedArray()
        } catch (e: IOException) {
            e.printStackTrace()
            null
        } finally {
            try {
                reader?.close()
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
    }
}