package com.kkkkkn.readbooks.presenter

import android.content.Context
import android.util.Log
import com.kkkkkn.readbooks.model.BaseModel
import com.kkkkkn.readbooks.model.ModelBrowsing
import com.kkkkkn.readbooks.model.clientsetting.SettingConf
import com.kkkkkn.readbooks.model.entity.AccountInfo
import com.kkkkkn.readbooks.model.entity.ChapterInfo
import com.kkkkkn.readbooks.util.ChapterUtil.cacheChapter
import com.kkkkkn.readbooks.util.ChapterUtil.readCacheChapter
import com.kkkkkn.readbooks.util.StringUtil.url2bookName
import com.kkkkkn.readbooks.util.StringUtil.url2chapterName
import com.kkkkkn.readbooks.util.eventBus.EventMessage
import com.kkkkkn.readbooks.util.eventBus.events.BrowsingEvent
import com.kkkkkn.readbooks.view.view.BrowsingActivityView
import org.greenrobot.eventbus.EventBus
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject

class PresenterBrowsing(
    context: Context?,
    private val browsingActivityView: BrowsingActivityView
) :
    BasePresenter(context!!, ModelBrowsing()), BaseModel.CallBack {
    private val modelBrowsing: ModelBrowsing = super.baseModel as ModelBrowsing

    init {
        modelBrowsing.setCallback(this)
    }

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
            onError(-2, "获取用户信息失败")
            return
        }
        EventBus.getDefault().post(
            BrowsingEvent(
                EventMessage.GET_BOOK_CHAPTER_LIST,
                accountInfo.accountToken!!,
                accountInfo.accountId,
                book_id,
                PAGE_SIZE,
                chapter_count / PAGE_SIZE + 1
            )
        )
    }

    //获取章节内容
    fun getChapterContent(chapterUrl: String?) {
        val accountInfo: AccountInfo = super.accountInfo
        if (!accountInfo.isHasToken) {
            onError(-2, "获取用户信息失败")
            return
        }
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
            //请求网络获取文章内容
            EventBus.getDefault().post(
                BrowsingEvent(
                    EventMessage.GET_CHAPTER_CONTENT,
                    accountInfo.accountToken!!,
                    accountInfo.accountId,
                    chapterUrl
                )
            )
        } else {
            Log.i(TAG, "getChapterContent: 读取的缓存内容")
            val jsonArray = JSONArray()
            for (str in arr) {
                jsonArray.put(str)
            }
            arr = null
            browsingActivityView.syncReadView(jsonArray)
            browsingActivityView.setLoading(false)
        }
    }

    fun chapterCount2listCount(count: Int): Int {
        return if (count / PAGE_SIZE > 0) {
            Log.i(TAG, "chapterCount2listCount: " + count % PAGE_SIZE)
            count % PAGE_SIZE
        } else {
            Log.i(
                TAG,
                "chapterCount2listCount: $count"
            )
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

    override fun onSuccess(type: Int, `object`: Any) {
        when (type) {
            1001 -> browsingActivityView.syncChapterList(`object` as ArrayList<ChapterInfo>)
            1002 -> {
                //写入读取到的章节缓存
                val jsonObject = `object` as JSONObject
                val jsonArray: JSONArray?
                val chapterUrl: String?
                try {
                    chapterUrl = jsonObject.getString("url")
                    jsonArray = jsonObject.getJSONArray("data")
                    val bookName = url2bookName(chapterUrl)
                    val chapterName = url2chapterName(chapterUrl)
                    if (!cacheChapter(
                            jsonArray, context.filesDir.absolutePath,
                            bookName!!, chapterName!!
                        )
                    ) {
                        Log.e(TAG, "onSuccess:  缓存章节失败")
                    } else {
                        Log.i(TAG, "onSuccess: 缓存章节成功")
                    }
                    browsingActivityView.syncReadView(jsonArray!!)
                    browsingActivityView.setLoading(false)
                } catch (e: JSONException) {
                    e.printStackTrace()
                }
            }
        }
    }

    override fun onError(type: Int, `object`: Any) {
        when (type) {
            -1001 -> browsingActivityView.showMsgDialog(type, (`object` as String))
            -1002 -> {
                browsingActivityView.setLoading(false)
                browsingActivityView.showMsgDialog(type, (`object` as String))
            }
            -2 -> browsingActivityView.toLoginActivity()
            else -> {}
        }
    }

    fun setReadConfig(settingConf: SettingConf?) {
        modelBrowsing.setReadConfig(context, settingConf)
    }

    companion object {
        private const val PAGE_SIZE = 20
        private const val TAG = "PresenterBrowsing"
    }
}