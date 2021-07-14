package com.kkkkkn.readbooks.model;

import com.kkkkkn.readbooks.model.network.HttpUtil;
import com.kkkkkn.readbooks.util.StringUtil;
import com.kkkkkn.readbooks.util.eventBus.MessageEvent;

import org.json.JSONException;
import org.json.JSONObject;

public class Model_Register extends BaseModel {
    private static String TAG="Model_Register";

    public void register(final String name,final String password){
        //Md5加密
        final String password_val= StringUtil.password2md5(password);
        new Thread(){
            @Override
            public void run() {
                //HttpUtil.getInstance().register(name,password_val);
            }
        }.start();

    }

    @Override
    void syncProgress(MessageEvent event) {
        CallBack callBack=getCallBack();
        if(callBack==null)return;
        switch (event.message){
            case NET_OK:
                JSONObject jsonObject= null;
                String flag_val=null;
                try {
                    jsonObject = new JSONObject((String)event.value);
                    flag_val=jsonObject.getString("code");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                switch (flag_val){
                    case "success":
                        callBack.onSuccess(1,null);
                        break;
                    case "error":
                        try {
                            String str=jsonObject.getString("data");
                            callBack.onError(-1,str);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        break;
                    default:
                        callBack.onError(-1,"注册错误");
                        break;
                }
                break;
            case NET_ERROR:
                callBack.onError(-1,event.value);
                break;
        }
    }
}
