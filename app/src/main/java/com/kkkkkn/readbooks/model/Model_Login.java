package com.kkkkkn.readbooks.model;

import androidx.annotation.NonNull;

import com.kkkkkn.readbooks.util.ServerConfig;
import com.kkkkkn.readbooks.model.entity.AccountInfo;
import com.kkkkkn.readbooks.util.eventBus.events.LoginEvent;
import com.kkkkkn.readbooks.util.network.HttpUtil;
import com.kkkkkn.readbooks.util.StringUtil;
import com.kkkkkn.readbooks.util.eventBus.EventMessage;

import org.greenrobot.eventbus.Subscribe;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Objects;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.Request;
import okhttp3.Response;

public class Model_Login extends BaseModel  {
    private static String TAG="Model_Login";

    private void login(final String name,final String password){
        //Md5加密
        final String password_val= StringUtil.INSTANCE.password2md5(password);
        FormBody.Builder formBody = new FormBody.Builder();
        formBody.add("accountName", name);
        assert password_val != null;
        formBody.add("accountPassword",password_val);
        Request request = new Request.Builder()
                .url(ServerConfig.IP+ServerConfig.login)
                .post(formBody.build())//传递请求体
                .build();
        Objects.requireNonNull(HttpUtil.Companion.getInstance()).post(request, new Callback() {
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
                            info.setAccountId(id);
                            info.setAccountToken(token);
                            getCallBack().onSuccess(1,info);
                            break;
                        case "error":
                            getCallBack().onError(-1,(String)value);
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
    public void syncProgress(LoginEvent event) {
        if (event.getMessage() == EventMessage.LOGIN) {
            login(event.getName(), event.getPassword());
        }
    }


}
