package com.kkkkkn.readbooks.util.eventBus

enum class EventMessage {
    //获取章节进度
    GET_BOOK_CHAPTER_FLAG,  //写入章节进度
    SET_BOOK_CHAPTER_FLAG,  //登录相关
    LOGIN,  //注册相关
    REGISTER,  //获取书架
    GET_BOOKSHELF,  //更新窗口
    SYNC_DIALOG,  //获取最新版本信息
    GET_VERSION,  //准备下载
    DOWNLOAD_APK,  //更新进度
    DOWNLOAD_PROGRESS,  //下载完成
    DOWNLOAD_SUCCESS,  //搜索图书
    SEARCH_BOOK,  //获取章节列表
    GET_BOOK_CHAPTER_LIST,  //收藏图书
    ADD_BOOK,  //更新搜索结果列表
    SYNC_SEARCH_RESULT,  //获取章节内容
    GET_CHAPTER_CONTENT
}