package com.kkkkkn.readbooks.util.network.retrofit

import com.kkkkkn.readbooks.util.network.ServerConfig
import retrofit2.Retrofit

class RetrofitUtil private constructor() {
    private var retrofit:Retrofit = Retrofit.Builder()
        .baseUrl(ServerConfig.IP)
        .build()
    var service:NetWorkService = retrofit.create(NetWorkService::class.java)

    companion object {
        val instance: RetrofitUtil by lazy(mode = LazyThreadSafetyMode.SYNCHRONIZED) {
            RetrofitUtil();
        }
    }

}