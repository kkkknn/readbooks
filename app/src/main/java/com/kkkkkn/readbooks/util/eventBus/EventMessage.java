package com.kkkkkn.readbooks.util.eventBus;

public enum EventMessage {
    //跳转注册页
    JUMP_REG,
    //跳转主页
    JUMP_INDEX,


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


    //更新搜索结果列表
    SYNC_SEARCH_RESULT,

    //token 非法
    TOKEN_ERROR;
}
