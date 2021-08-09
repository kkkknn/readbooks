package com.kkkkkn.readbooks.model;

import androidx.annotation.NonNull;

import com.kkkkkn.readbooks.ServerConfig;
import com.kkkkkn.readbooks.model.network.HttpUtil;
import com.kkkkkn.readbooks.util.StringUtil;
import com.kkkkkn.readbooks.util.eventBus.EventMessage;
import com.kkkkkn.readbooks.util.eventBus.MessageEvent;

import org.greenrobot.eventbus.Subscribe;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.Request;
import okhttp3.Response;

public class Model_Register extends BaseModel  {
    private static String TAG="Model_Register";

    private void register(final String name,final String password){
        //Md5加密
        final String password_val= StringUtil.password2md5(password);

        FormBody.Builder formBody = new FormBody.Builder();
        formBody.add("accountName", name);
        formBody.add("accountPassword", password_val);
        Request request = new Request.Builder()
                .url(ServerConfig.IP +ServerConfig.register)
                .post(formBody.build())//传递请求体
                .build();
        HttpUtil.getInstance().post(request, new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                getCallBack().onError(-1,"网络请求失败");
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                String val_str=response.body().string();
                JSONObject jsonObject= null;
                String flag_val=null;
                try {
                    jsonObject = new JSONObject(val_str);
                    flag_val=jsonObject.getString("code");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                if(flag_val==null){
                    getCallBack().onError(-1,"网络请求失败");
                }else if(flag_val.equals("success")){
                    getCallBack().onSuccess(1,"注册成功");
                }else if(flag_val.equals("error")){
                    try {
                        String str=jsonObject.getString("data");
                        getCallBack().onError(-1,str);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }else {
                    getCallBack().onError(-1,"注册错误");
                }

            }
        });

    }

    @Subscribe
    @Override
    public void syncProgress(MessageEvent event) {
        if (event.message == EventMessage.REGISTER) {
            String[] arr = (String[]) event.value;
            if (arr == null) return;
            register(arr[0], arr[1]);
        }
    }

}
