package com.kkkkkn.readbooks.model;

import com.kkkkkn.readbooks.model.network.HttpUtil;

public class Model_Login extends BaseModel {
    private static String TAG="Model_Login";

    public void login(String name,String password,BaseModel.CallBack callBack){

        /*String string= HttpUtil.getInstance().login(name,password);
        if(string==null){
            callBack.onError(-1,"请求错误");
        }else {
            callBack.onSuccess(1,"成功");
            System.out.println(string);
        }*/
        HttpUtil.getInstance().login(name,password,callBack);
    }
}
