package com.kkkkkn.readbooks.util.network.retrofit

import com.kkkkkn.readbooks.util.network.ServerConfig
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.*


interface NetWorkService {
    //登录
    @FormUrlEncoded
    @POST(ServerConfig.login)
    fun login(
        @Field("accountName") name:String,
        @Field("accountPassword") password:String
    ):Call<ResponseBody>

    @FormUrlEncoded
    @POST(ServerConfig.addFavoriteBook)
    fun addFavoriteBook(
        @Header("accountId") userId: String,
        @Header("token") userToken: String,
        @Field("book_id") bookId:String,
        @Field("account_id") accountId:String
    ): Call<ResponseBody>

    @FormUrlEncoded
    @POST(ServerConfig.getChapterList)
    fun getChapterList(
        @Header("accountId") userId: String,
        @Header("token") userToken: String,
        @Field("bookId") bookId:String,
        @Field("pageCount") pageCount:String,
        @Field("pageSize") pageSize:String

    ):Call<ResponseBody>

    @FormUrlEncoded
    @POST(ServerConfig.getChapterContent)
    fun getChapterContent(
        @Header("accountId") userId: String,
        @Header("token") userToken: String,
        @Field("chapter_path") chapterPath: String
    ):Call<ResponseBody>


    @GET
    fun downloadApk(
        @Header("accountId") userId: String,
        @Header("token") userToken: String,
        @Url fileUrl: String
    ): Call<ResponseBody>

    @GET(ServerConfig.getVersionInfo)
    fun getVersionInfo(
        @Header("accountId") userId: String,
        @Header("token") userToken: String
    ):Call<ResponseBody>

    @FormUrlEncoded
    @POST(ServerConfig.getFavoriteBook)
    fun getFavoriteBook(
        @Header("accountId") userId: String,
        @Header("token") userToken: String,
        @Field("accountId") accountId:String
    ):Call<ResponseBody>

    @FormUrlEncoded
    @POST(ServerConfig.register)
    fun register(
        @Field("accountName") accountId: String,
        @Field("accountPassword") accountPassword:String
    ):Call<ResponseBody>

    @FormUrlEncoded
    @POST(ServerConfig.searchBook)
    fun searchBook(
        @Header("accountId") userId: String,
        @Header("token") userToken: String,
        @Field("str") str:String,
        @Field("pageCount") pageCount:String,
        @Field("pageSize") pageSize:String,

    ):Call<ResponseBody>
}