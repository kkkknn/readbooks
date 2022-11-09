package com.kkkkkn.readbooks.presenter

import android.content.Context
import android.content.pm.PackageManager
import android.util.Log
import com.kkkkkn.readbooks.model.BaseModel
import com.kkkkkn.readbooks.model.ModelMain
import com.kkkkkn.readbooks.model.entity.BookInfo
import com.kkkkkn.readbooks.util.eventBus.EventMessage
import com.kkkkkn.readbooks.util.eventBus.events.MainEvent
import com.kkkkkn.readbooks.view.view.MainActivityView
import org.greenrobot.eventbus.EventBus
import org.json.JSONException
import org.json.JSONObject

class PresenterMain(context: Context?, private val mainActivityView: MainActivityView) :
    BasePresenter(context!!, ModelMain()), BaseModel.CallBack {
    private val modelMain: ModelMain = baseModel as ModelMain

    init {
        modelMain.setCallback(this)
    }//请求图书列表

    /**
     * 获取书架信息，并显示到页面上 通过eventbus发送
     *
     */
    val bookShelfList: Unit
        get() {
            val accountInfo = super.accountInfo
            if (!accountInfo.isHasToken) {
                onError(-2, "获取用户信息失败")
                return
            }
            //请求图书列表
            EventBus.getDefault().post(
                MainEvent(
                    EventMessage.GET_BOOKSHELF,
                    accountInfo.accountId,
                    accountInfo.accountToken
                )
            )
        }

    fun updateBookShelf(arrayList: ArrayList<BookInfo>): Boolean {
        return if (arrayList.isEmpty()) {
            false
        } else modelMain.updateBookShelf(arrayList, context)
    }

    /**
     * 检查当前应用版本号
     * @return boolean 是否需要更新
     */
    private fun checkVersion(jsonStr: String): Boolean {
        val manager = context.packageManager
        try {
            val jsonObject = JSONObject(jsonStr)
            val online_version = jsonObject.getString("version")
            val info = manager.getPackageInfo(context.packageName, 0)
            if (online_version.compareTo(info.versionName) > 0) {
                return true
            }
        } catch (e: PackageManager.NameNotFoundException) {
            e.printStackTrace()
        } catch (e: JSONException) {
            e.printStackTrace()
        }
        return false
    }

    /**
     * 获取APP更新信息，通过eventbus发送
     *
     */
    fun checkUpdate() {
        val accountInfo = super.accountInfo
        if (!accountInfo.isHasToken) {
            onError(-2, "获取用户信息失败")
            return
        }
        //在线获取最新版本号
        EventBus.getDefault().post(
            MainEvent(
                EventMessage.GET_VERSION,
                accountInfo.accountId,
                accountInfo.accountToken
            )
        )
    }

    /**
     * 弹窗显示更新APK
     */
    fun updateAPK(name: String?, path: String?, url: String?) {
        val accountInfo = super.accountInfo
        if (!accountInfo.isHasToken) {
            onError(-2, "获取用户信息失败")
            return
        }

        //在线获取最新版本号
        EventBus.getDefault().post(
            MainEvent(
                EventMessage.DOWNLOAD_APK,
                name,
                path,
                url,
                accountInfo.accountId,
                accountInfo.accountToken
            )
        )
    }

    override fun onSuccess(type: Int, `object`: Any) {
        when (type) {
            1001 ->                 //获取成功后，同时更新缓存书架
                mainActivityView.syncBookShelf(`object` as ArrayList<BookInfo>)
            2001 -> if (checkVersion(`object` as String)) {
                var jsonObject: JSONObject? = null
                var code: String? = null
                var url: String? = null
                var verStr: String? = null
                try {
                    jsonObject = JSONObject(`object`)
                    code = jsonObject.getString("version")
                    url = jsonObject.getString("downloadUrl")
                    verStr = jsonObject.getString("message")
                } catch (e: JSONException) {
                    e.printStackTrace()
                }
                if (verStr != null) {
                    mainActivityView.showUpdateDialog(verStr, url!!, code!!)
                }
            }
            3001 -> mainActivityView.installAPK((`object` as String))
            3002 -> mainActivityView.updateProgress(`object` as Int)
            else -> {}
        }
    }

    override fun onError(type: Int, `object`: Any) {
        when (type) {
            -1001 -> mainActivityView.syncBookShelfError(`object` as String)
            -2001, -2002, -3001 -> Log.i(
                TAG,
                (`object` as String)
            )
            -1002 -> {
                val str = `object` as String
                if (str == "令牌验证失败，请重新尝试") {
                    mainActivityView.toLoginActivity()
                }
            }
            else -> {}
        }
    }

    companion object {
        private const val TAG = "PresenterMain"
    }
}