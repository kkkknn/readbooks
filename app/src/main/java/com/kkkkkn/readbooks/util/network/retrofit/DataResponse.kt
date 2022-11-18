package com.kkkkkn.readbooks.util.network.retrofit

import retrofit2.Converter

class DataResponse{
    var code:Int = 0
    var message:String=""
    var data:Any? = null

    //判断是否成功
    fun isSuccess(): Boolean {
        return code == 200
    }

}