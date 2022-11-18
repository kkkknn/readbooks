package com.kkkkkn.readbooks.presenter

import android.content.Context
import com.kkkkkn.readbooks.model.ModelRegister
import com.kkkkkn.readbooks.util.StringUtil.checkAccountName
import com.kkkkkn.readbooks.util.StringUtil.checkAccountPassword
import com.kkkkkn.readbooks.util.StringUtil.equals
import com.kkkkkn.readbooks.view.view.RegisterActivityView
import kotlinx.coroutines.*
import org.json.JSONObject

class PresenterRegister(
    context: Context?,
    private val registerActivityView: RegisterActivityView
) :
    BasePresenter(context!!, ModelRegister()){
    private val modelRegister: ModelRegister = baseModel as ModelRegister

    /**
     * 注册用户
     * @param password 用户密码
     * @param name 用户姓名
     * @return  成功失败 boolean类型
     */
    fun register(name: String?, password: String?, passwordCheck: String?) {
        if (!equals(password, passwordCheck)) {
            registerActivityView.showTip(-1, "两次输入密码不一致")
            registerActivityView.clearAccountCache()
            return
        } else if (!checkAccountName(name)) {
            registerActivityView.showTip(-2, "用户名仅支持英文、数字、下划线,长度3-10之间")
            registerActivityView.clearAccountCache()
            return
        } else if (!checkAccountPassword(password)) {
            registerActivityView.showTip(-3, "密码必须包含大小写字母和数字的组合，可以使用特殊字符，长度在8-10之间")
            registerActivityView.clearAccountCache()
            return
        }

        val job = Job()
        CoroutineScope(job).launch(Dispatchers.Main) {
            var jsonObject: JSONObject? = null
            val result = withContext(Dispatchers.IO) {
                jsonObject = modelRegister.register(name!!, password!!)
            }
            if (jsonObject != null) {
                val code = jsonObject!!.getBoolean("status")
                if (code) {
                    registerActivityView.showMsgDialog(1, jsonObject!!.getString("data"))
                    registerActivityView.back2Login()
                } else {
                    val data=jsonObject!!.getString("data")
                    registerActivityView.showTip(-1, data)
                    registerActivityView.showMsgDialog(-1, data)
                    registerActivityView.clearAccountCache()
                }
            }

        }
    }

}