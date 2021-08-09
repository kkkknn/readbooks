package com.kkkkkn.readbooks.model;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.util.Log;

import androidx.annotation.NonNull;

import com.kkkkkn.readbooks.ServerConfig;
import com.kkkkkn.readbooks.model.entity.AccountInfo;
import com.kkkkkn.readbooks.model.network.HttpUtil;
import com.kkkkkn.readbooks.util.StringUtil;
import com.kkkkkn.readbooks.util.eventBus.EventMessage;
import com.kkkkkn.readbooks.util.eventBus.MessageEvent;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.Request;
import okhttp3.Response;

public class Model_Login extends BaseModel  {
    private static String TAG="Model_Login";

    private void login(final String name,final String password){
        //Md5加密
        final String password_val= StringUtil.password2md5(password);
        FormBody.Builder formBody = new FormBody.Builder();
        formBody.add("accountName", name);
        formBody.add("accountPassword",password_val);
        Request request = new Request.Builder()
                .url(ServerConfig.IP+ServerConfig.login)
                .post(formBody.build())//传递请求体
                .build();
        HttpUtil.getInstance().post(request, new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                getCallBack().onError(-1,"访问出错");
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                String str=response.body().string();
                final String token;
                final int id;
                //解析返回字符串，判断是否成功
                JSONObject jsonObject=null;
                try {
                    jsonObject=new JSONObject(str);
                    String flag_str=(String) jsonObject.get("code");
                    Object value=jsonObject.get("data");
                    switch (flag_str){
                        case "success":

                            JSONObject jsonObject1=new JSONObject((String)value);
                            token=jsonObject1.getString("token");
                            id=jsonObject1.getInt("accountId");
                            AccountInfo info=new AccountInfo();
                            info.setAccount_id(id);
                            info.setAccount_token(token);
                            getCallBack().onSuccess(1,info);
                            break;
                        case "error":
                            getCallBack().onError(-1,"登陆失败");
                            break;

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    getCallBack().onError(-1,"登录失败");
                }
            }
        });
    }

    @Subscribe
    @Override
    public void syncProgress(MessageEvent event) {
        if (event.message == EventMessage.LOGIN) {
            String[] arr = (String[]) event.value;
            if (arr == null) return;
            login(arr[0], arr[1]);
        }
    }


}
