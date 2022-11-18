package com.kkkkkn.readbooks.presenter

import android.content.Context
import com.kkkkkn.readbooks.model.ModelLogin
import com.kkkkkn.readbooks.model.entity.AccountInfo
import com.kkkkkn.readbooks.util.StringUtil.checkAccountName
import com.kkkkkn.readbooks.util.StringUtil.checkAccountPassword
import com.kkkkkn.readbooks.view.view.LoginActivityView
import kotlinx.coroutines.*
import org.json.JSONObject

class PresenterLogin(context: Context?, private val loginActivityView: LoginActivityView) :
    BasePresenter(context!!, ModelLogin()) {
    private val modelLogin: ModelLogin = baseModel as ModelLogin

    /**
     * 登录,返回token 7天有效期
     * @param name 用户名
     * @param password 密码
     * @return 成功失败 boolean 类型
     */
    fun login(name: String?, password: String?) {
        if (!checkAccountName(name) || !checkAccountPassword(password)) {
            loginActivityView.showMsgDialog(-1, "用户名或密码错误，请重新输入")
            return
        }
        val job = Job()
        CoroutineScope(job).launch(Dispatchers.Main) {
            var jsonObject: JSONObject? = null
            val result = withContext(Dispatchers.IO) {
                jsonObject = modelLogin.login(name!!, password!!)
            }
            if (jsonObject != null) {
                val code = jsonObject!!.getBoolean("status")
                if (code) {
                    val info = jsonObject!!.get("data") as AccountInfo
                    setTokenCache(info.accountId, info.accountToken)
                    setAccountCache(name, password)
                    loginActivityView.showMsgDialog(1, "登录成功")
                    loginActivityView.toMainActivity()
                } else {
                    loginActivityView.showMsgDialog(-1, jsonObject!!.getString("data"))
                }
            }

        }

    }

    companion object {
        private const val TAG = "PresenterLogin"
    }
}