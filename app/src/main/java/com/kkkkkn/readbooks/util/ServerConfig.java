package com.kkkkkn.readbooks.util;

public class ServerConfig {
    public final static String IP="http://81.70.239.217:8005";

    //用户模块
    public final static String login="/account/login";
    public final static String register="/account/register";

    //图书模块
    public final static String getFavoriteBook="/book/getFavoriteBook";
    public final static String getChapterContent="/book/getChapterContent";
    public final static String removeFavoriteBook="/book/removeFavoriteBook";
    public final static String addFavoriteBook="/book/addFavoriteBook";
    public final static String getChapterList="/book/getChapterList";
    public final static String getBookInfo="/book/getBookInfo";
    public final static String searchBook="/book/searchBook";
    public final static String downloadBookImage="/book/downloadBookImage";

    //APP模块
    public final static String getVersionInfo="/app/getVersionInfo";
    public final static String downloadAPK="/app/downloadAPK";

}
