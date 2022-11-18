package com.kkkkkn.readbooks.model

import com.kkkkkn.readbooks.util.StringUtil.password2md5
import com.kkkkkn.readbooks.util.network.retrofit.RetrofitUtil
import okhttp3.*
import org.json.JSONObject

class ModelRegister : BaseModel() {
    fun register(name: String, password: String):JSONObject {
        val retJson=JSONObject()
        //Md5加密
        val passwordVal = password2md5(password)
        if(passwordVal==null){
            retJson.put("status",false)
            retJson.put("data","参数错误")
            return retJson
        }

        val response=RetrofitUtil.instance.service.register(name,passwordVal).execute()

        if(response.isSuccessful){
            val json=JSONObject(response.body()!!.string())

            when(json.getString("code")){
                "success"->{
                    retJson.put("status",true)
                    retJson.put("data","注册成功")
                }
                "error"->{
                    retJson.put("status",false)
                    retJson.put("data",json.getString("data"))
                }
                else->{
                    retJson.put("status",false)
                    retJson.put("data","用户名重复/注册错误")
                }
            }
        }else{
            retJson.put("status",false)
            retJson.put("data","网络请求失败")
        }
        return retJson
    }

    companion object {
        private const val TAG = "ModelRegister"
    }
}