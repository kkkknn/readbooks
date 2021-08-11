package com.kkkkkn.readbooks.util.eventBus;

public enum EventMessage {

    //网络相关
    NET_ERROR,
    NET_OK,

    //登录相关
    LOGIN,
    //注册相关
    REGISTER,

    //更新书架
    SYNC_BOOKSHELF,
    //更新窗口
    SYNC_DIALOG,
    //检查更新
    CHECK_VERSION,
    //准备下载
    DOWNLOAD_APK,
    //更新进度
    DOWNLOAD_PROGRESS,
    //下载完成
    DOWNLOAD_SUCCESS,
    //下载失败
    DOWNLOAD_ERROR,

    //搜索图书
    SEARCH_BOOK,
    //搜索失败
    SEARCH_BOOK_ERROR,
    //搜索成功
    SEARCH_BOOK_SUCCESS,

    //获取章节列表
    GET_BOOK_CHAPTER_LIST,
    GET_BOOK_CHAPTER_LIST_SUCCESS,
    GET_BOOK_CHAPTER_LIST_ERROR,

    //收藏图书
    ADD_BOOK,
    //收藏成功
    ADD_BOOK_SUCCESS,
    //收藏失败
    ADD_BOOK_ERROR,


    //更新搜索结果列表
    SYNC_SEARCH_RESULT,

    //token 非法
    TOKEN_ERROR;
}
