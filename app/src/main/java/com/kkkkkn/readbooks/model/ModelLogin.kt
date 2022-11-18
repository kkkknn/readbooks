package com.kkkkkn.readbooks.model

import com.kkkkkn.readbooks.model.entity.AccountInfo
import com.kkkkkn.readbooks.util.StringUtil.password2md5
import com.kkkkkn.readbooks.util.network.retrofit.RetrofitUtil
import org.json.JSONObject

class ModelLogin : BaseModel() {
    fun login(name: String, password: String): JSONObject {
        val retObject: JSONObject = JSONObject()
        //Md5加密
        val passwordVal = password2md5(password)
        if (passwordVal == null) {
            retObject.put("status", false)
            retObject.put("data", "密码加密出错")
            return retObject
        }
        val result = RetrofitUtil.instance.service.login(name, passwordVal).execute()
        if (result.isSuccessful) {
            val retJson = JSONObject(result.body()!!.string())
            when (retJson.getString("code")) {
                "success" -> {
                    val jsonObject1 = JSONObject(retJson.getString("data"))
                    val info = AccountInfo()
                    info.accountId = jsonObject1.getInt("accountId")
                    info.accountToken = jsonObject1.getString("token")
                    retObject.put("status", true)
                    retObject.put("data", info)
                }
                "error" -> {
                    retObject.put("status", false)
                    retObject.put("data", retJson.getString("data"))
                }
            }
        } else {
            retObject.put("status", false)
            retObject.put("data", "登录请求发送失败")
        }
        return retObject
    }


    companion object {
        private const val TAG = "ModelLogin"
    }
}