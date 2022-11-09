package com.kkkkkn.readbooks.model

import com.kkkkkn.readbooks.model.entity.AccountInfo
import com.kkkkkn.readbooks.util.ServerConfig
import com.kkkkkn.readbooks.util.StringUtil.password2md5
import com.kkkkkn.readbooks.util.eventBus.EventMessage
import com.kkkkkn.readbooks.util.eventBus.events.LoginEvent
import com.kkkkkn.readbooks.util.network.HttpUtil.Companion.instance
import okhttp3.*
import org.greenrobot.eventbus.Subscribe
import org.json.JSONException
import org.json.JSONObject
import java.io.IOException
import java.util.*

class ModelLogin : BaseModel() {
    private fun login(name: String, password: String) {
        //Md5加密
        val passwordVal = password2md5(password)
        val formBody = FormBody.Builder()
        if(passwordVal==null){
            callBack!!.onError(-1, "访问出错")
            return
        }
        formBody.add("accountName", name)
        formBody.add("accountPassword", passwordVal)
        val request: Request = Request.Builder()
            .url(ServerConfig.IP + ServerConfig.login)
            .post(formBody.build()) //传递请求体
            .build()
        instance?.post(request, object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                callBack!!.onError(-1, "访问出错")
            }

            @Throws(IOException::class)
            override fun onResponse(call: Call, response: Response) {
                val str = response.body!!.string()
                val token: String
                val id: Int
                //解析返回字符串，判断是否成功
                var jsonObject: JSONObject? = null
                try {
                    jsonObject = JSONObject(str)
                    val flagStr = jsonObject["code"] as String
                    val value = jsonObject["data"]
                    when (flagStr) {
                        "success" -> {
                            val jsonObject1 = JSONObject(value as String)
                            token = jsonObject1.getString("token")
                            id = jsonObject1.getInt("accountId")
                            val info = AccountInfo()
                            info.accountId = id
                            info.accountToken = token
                            callBack!!.onSuccess(1, info)
                        }
                        "error" -> callBack!!.onError(-1, value as String)
                    }
                } catch (e: JSONException) {
                    e.printStackTrace()
                    callBack!!.onError(-1, "登录失败")
                }
            }
        })
    }

    @Subscribe
    fun syncProgress(event: LoginEvent) {
        if (event.message === EventMessage.LOGIN) {
            login(event.name, event.password)
        }
    }

    companion object {
        private const val TAG = "ModelLogin"
    }
}