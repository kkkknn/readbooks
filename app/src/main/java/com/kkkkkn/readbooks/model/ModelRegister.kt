package com.kkkkkn.readbooks.model

import com.kkkkkn.readbooks.util.ServerConfig
import com.kkkkkn.readbooks.util.StringUtil.password2md5
import com.kkkkkn.readbooks.util.eventBus.EventMessage
import com.kkkkkn.readbooks.util.eventBus.events.RegisterEvent
import com.kkkkkn.readbooks.util.network.HttpUtil.Companion.instance
import okhttp3.*
import org.greenrobot.eventbus.Subscribe
import org.json.JSONException
import org.json.JSONObject
import java.io.IOException
import java.util.*

class ModelRegister : BaseModel() {
    private fun register(name: String, password: String) {
        //Md5加密
        val passwordVal = password2md5(password)
        val formBody = FormBody.Builder()
        if(passwordVal==null){
            callBack!!.onError(-1, "参数错误")
            return
        }
        formBody.add("accountName", name)
        formBody.add("accountPassword", passwordVal)
        val request: Request = Request.Builder()
            .url(ServerConfig.IP + ServerConfig.register)
            .post(formBody.build()) //传递请求体
            .build()
        instance?.post(request, object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                callBack!!.onError(-1, "网络请求失败")
            }

            @Throws(IOException::class)
            override fun onResponse(call: Call, response: Response) {
                val valStr = response.body!!.string()
                var jsonObject: JSONObject? = null
                var flagVal: String? = null
                try {
                    jsonObject = JSONObject(valStr)
                    flagVal = jsonObject.getString("code")
                } catch (e: JSONException) {
                    e.printStackTrace()
                }
                if (flagVal == null) {
                    callBack!!.onError(-1, "网络请求失败")
                } else if (flagVal == "success") {
                    callBack!!.onSuccess(1, "注册成功")
                } else if (flagVal == "error") {
                    try {
                        val str = jsonObject!!.getString("data")
                        callBack!!.onError(-1, str)
                    } catch (e: JSONException) {
                        e.printStackTrace()
                    }
                } else {
                    callBack!!.onError(-1, "注册错误")
                }
            }
        })
    }

    @Subscribe
    fun syncProgress(event: RegisterEvent) {
        if (event.message === EventMessage.REGISTER) {
            register(event.name, event.password)
        }
    }

    companion object {
        private const val TAG = "ModelRegister"
    }
}