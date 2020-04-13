package com.kkkkkn.readbooks.util;

//网络请求工具类
public class NetworkUtil {
    private static NetworkUtil networkUtil;

    private NetworkUtil() {
    }

    public static NetworkUtil getInstance(){
        if(networkUtil==null){
            synchronized (NetworkUtil.class){
                if(networkUtil==null){
                    networkUtil=new NetworkUtil();
                }
            }
        }
        return networkUtil;
    }

    //1.请求服务器


    //2.设置服务器返回监听


    //3.网络关闭


    //4.网络状态恢复



}
