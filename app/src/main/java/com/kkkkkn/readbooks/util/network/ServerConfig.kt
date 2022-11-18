package com.kkkkkn.readbooks.util.network

object ServerConfig {
    const val IP = "http://www.kkkkknn.com:30485"

    //用户模块
    const val login = "/account/login"
    const val register = "/account/register"

    //图书模块
    const val getFavoriteBook = "/book/getFavoriteBook"
    const val getChapterContent = "/book/getChapterContent"
    const val removeFavoriteBook = "/book/removeFavoriteBook"
    const val addFavoriteBook = "/book/addFavoriteBook"
    const val getChapterList = "/book/getChapterList"
    const val getBookInfo = "/book/getBookInfo"
    const val searchBook = "/book/searchBook"
    const val downloadBookImage = "/book/downloadBookImage"

    //APP模块
    const val getVersionInfo = "/app/getVersionInfo"
    const val downloadAPK = "/app/downloadAPK"
}