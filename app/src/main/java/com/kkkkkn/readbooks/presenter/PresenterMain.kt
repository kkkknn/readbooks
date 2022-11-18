package com.kkkkkn.readbooks.presenter

import android.content.Context
import android.content.pm.PackageManager
import android.util.Log
import com.kkkkkn.readbooks.model.ModelMain
import com.kkkkkn.readbooks.model.entity.BookInfo
import com.kkkkkn.readbooks.util.network.retrofit.DownloadListener
import com.kkkkkn.readbooks.view.view.MainActivityView
import kotlinx.coroutines.*
import org.json.JSONException
import org.json.JSONObject

class PresenterMain(context: Context?, private val mainActivityView: MainActivityView) :
    BasePresenter(context!!, ModelMain()){
    private val modelMain: ModelMain = baseModel as ModelMain

    /**
     * 获取书架信息，并显示到页面上 通过eventbus发送
     *
     */
    val bookShelfList: Unit
        get() {
            val accountInfo = super.accountInfo
            if (!accountInfo.isHasToken) {
                mainActivityView.showMsgDialog(-1,"获取用户信息失败")
                mainActivityView.toLoginActivity()
                return
            }
            //协程处理
            val job = Job()
            CoroutineScope(job).launch(Dispatchers.Main) {
                var jsonObject: JSONObject? = null
                val result = withContext(Dispatchers.IO) {
                    jsonObject = modelMain.getBookShelf(accountInfo.accountId, accountInfo.accountToken)
                }
                if (jsonObject != null) {
                    val code = jsonObject!!.getBoolean("status")
                    if (code) {
                        mainActivityView.syncBookShelf( jsonObject!!.get("data") as ArrayList<BookInfo>)
                    } else {
                        mainActivityView.syncBookShelfError(jsonObject!!.getString("data"))
                    }
                }

            }

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
    private fun checkVersion(jsonObject: JSONObject): Boolean {
        val manager = context.packageManager
        try {
            val online_version = jsonObject.getString("version")
            val info = manager.getPackageInfo(context.packageName, 0)
            if (online_version > info.versionName) {
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
            mainActivityView.showMsgDialog(-1,"获取用户信息失败")
            mainActivityView.toLoginActivity()
            return
        }
        //协程处理
        val job = Job()
        CoroutineScope(job).launch(Dispatchers.Main) {
            var jsonObject: JSONObject
            val result = withContext(Dispatchers.IO) {
                jsonObject = modelMain.getVersion(accountInfo.accountId, accountInfo.accountToken)
            }
            val code = jsonObject.getBoolean("status")
            if (code) {
                val json = JSONObject(jsonObject.getString("data"))
                Log.i(TAG, "checkUpdate: $json")
                if(checkVersion(json)){
                    mainActivityView.showUpdateDialog(
                        json.getString("message"),
                        json.getString("downloadUrl"),
                        json.getString("version")
                    )
                }
            } else {
                mainActivityView.showMsgDialog(-1, jsonObject.getString("data"))
            }

        }
    }

    /**
     * 弹窗显示更新APK
     */
    fun updateAPK(name: String?, path: String?, url: String?) {
        val accountInfo = super.accountInfo
        if (!accountInfo.isHasToken) {
            mainActivityView.showMsgDialog(-1,"获取用户信息失败")
            mainActivityView.toLoginActivity()
            return
        }

        //协程处理
        val job = Job()
        CoroutineScope(job).launch(Dispatchers.Main) {
            val result = withContext(Dispatchers.IO) {
                modelMain.downloadAPK(
                    name,
                    path,
                    url,
                    accountInfo.accountId,
                    accountInfo.accountToken,
                    object : DownloadListener{
                        override fun onProgress(progress: Int) {
                            mainActivityView.updateProgress(progress)
                        }

                        override fun onFinish(filePath: String) {
                            mainActivityView.installAPK(filePath)
                        }

                        override fun onError(errStr: String) {
                            mainActivityView.showMsgDialog(-1, errStr)
                        }

                    }
                )
            }
        }

    }


    companion object {
        private const val TAG = "PresenterMain"
    }
}