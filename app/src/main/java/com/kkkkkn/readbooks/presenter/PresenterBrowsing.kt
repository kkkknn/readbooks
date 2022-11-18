package com.kkkkkn.readbooks.presenter

import android.content.Context
import android.util.Log
import com.kkkkkn.readbooks.model.ModelBrowsing
import com.kkkkkn.readbooks.model.clientsetting.SettingConf
import com.kkkkkn.readbooks.model.entity.AccountInfo
import com.kkkkkn.readbooks.model.entity.ChapterInfo
import com.kkkkkn.readbooks.util.ChapterUtil.cacheChapter
import com.kkkkkn.readbooks.util.ChapterUtil.readCacheChapter
import com.kkkkkn.readbooks.util.StringUtil.url2bookName
import com.kkkkkn.readbooks.util.StringUtil.url2chapterName
import com.kkkkkn.readbooks.view.view.BrowsingActivityView
import kotlinx.coroutines.*
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject

class PresenterBrowsing(
    context: Context?,
    private val browsingActivityView: BrowsingActivityView
) :
    BasePresenter(context!!, ModelBrowsing()){
    private val modelBrowsing: ModelBrowsing = super.baseModel as ModelBrowsing

    /**
     * 获取阅读设置
     * @return
     */
    fun loadConfig(): SettingConf {
        //获取配置信息
        var settingConf: SettingConf?
        settingConf = modelBrowsing.getReadConfig(context)
        if (settingConf == null) {
            settingConf = SettingConf()
        }
        return settingConf
    }

    //获取章节列表 ，页码数
    fun getChapterList(book_id: Int, chapter_count: Int) {
        val accountInfo: AccountInfo = super.accountInfo
        if (!accountInfo.isHasToken) {
            Log.e(TAG, "getChapterList: 获取用户信息失败")
            browsingActivityView.toLoginActivity()
            return
        }
        //协程处理
        val job= Job()
        CoroutineScope(job).launch (Dispatchers.Main){
            var jsonObject:JSONObject
            val result= withContext(Dispatchers.IO){
                jsonObject=modelBrowsing.getChapterList(book_id,
                    chapter_count / PAGE_SIZE + 1,
                    PAGE_SIZE,
                    accountInfo.accountId,
                    accountInfo.accountToken!!)
            }
            val code= jsonObject.getBoolean("status")
            if(code){
                browsingActivityView.syncChapterList(jsonObject.get("data") as ArrayList<ChapterInfo>)
            }else{
                browsingActivityView.showMsgDialog(-1, jsonObject.getString("data"))
            }

        }

    }

    //获取章节内容
    fun getChapterContent(chapterUrl: String?) {
        val accountInfo: AccountInfo = super.accountInfo
        if (!accountInfo.isHasToken) {
            Log.e(TAG, "getChapterList: 获取用户信息失败")
            browsingActivityView.toLoginActivity()
            return
        }

        //协程处理
        val job= Job()
        CoroutineScope(job).launch (Dispatchers.Main){
            //显示加载框
            browsingActivityView.setLoading(true)
            //读取判断是否有缓存
            var arr: Array<String?>? = null
            var filePath: String? = null
            var chapterName: String? = null
            filePath = url2bookName(chapterUrl)
            chapterName = url2chapterName(chapterUrl)
            arr = readCacheChapter(context.filesDir.absolutePath, filePath, chapterName)
            if (arr == null) {
                var jsonObject:JSONObject
                val result= withContext(Dispatchers.IO){
                    jsonObject=modelBrowsing.getChapterContent(
                        accountInfo.accountId,
                        accountInfo.accountToken!!,
                        chapterUrl
                    )
                }
                val code= jsonObject.getBoolean("status")
                if(code){
                    //写入读取到的章节缓存
                    val jsonArray: JSONArray
                    try {
                        val jsonUrl = jsonObject.getString("url")
                        jsonArray = jsonObject.getJSONArray("data")
                        val bookName = url2bookName(jsonUrl)
                        val jsonName = url2chapterName(jsonUrl)
                        if (!cacheChapter(
                                jsonArray, context.filesDir.absolutePath,
                                bookName!!, jsonName!!
                            )
                        ) {
                            Log.e(TAG, "onSuccess:  缓存 $jsonName 章节失败")
                        } else {
                            Log.i(TAG, "onSuccess: 缓存章节成功")
                        }
                        browsingActivityView.syncReadView(jsonArray)
                        browsingActivityView.setLoading(false)
                    } catch (e: JSONException) {
                        e.printStackTrace()
                    }
                }else{

                    browsingActivityView.setLoading(false)
                    browsingActivityView.showMsgDialog(-1, jsonObject.getString("data"))
                }
            } else {
                Log.i(TAG, "getChapterContent: 读取的缓存内容")
                val jsonArray = JSONArray()
                for (str in arr) {
                    jsonArray.put(str)
                }
                browsingActivityView.syncReadView(jsonArray)
                browsingActivityView.setLoading(false)
            }

        }

    }

    fun chapterCount2listCount(count: Int): Int {
        return if (count / PAGE_SIZE > 0) {
            Log.i(TAG, "chapterCount2listCount: " + count % PAGE_SIZE)
            count % PAGE_SIZE
        } else {
            Log.i(TAG, "chapterCount2listCount: $count")
            count
        }
    }

    /**
     * 获取保存的章节进度
     * @param book_id 图书id
     * @return  缓存的阅读进度
     */
    fun getBookProgress(book_id: Int): Int {
        return modelBrowsing.getReadProgress(book_id, context)
    }

    /**
     * 设置图书的阅读章节进度
     * @param book_id   图书id
     * @param progress  章节进度
     * @return  是否成功
     */
    fun setBookProgress(book_id: Int, progress: Int): Boolean {
        return modelBrowsing.setReadProgress(book_id, progress, context)
    }


    fun setReadConfig(settingConf: SettingConf?) {
        modelBrowsing.setReadConfig(context, settingConf)
    }

    companion object {
        private const val PAGE_SIZE = 20
        private const val TAG = "PresenterBrowsing"
    }
}