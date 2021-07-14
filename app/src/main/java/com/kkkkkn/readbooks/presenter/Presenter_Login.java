package com.kkkkkn.readbooks.presenter;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Looper;
import android.util.Log;

import com.kkkkkn.readbooks.model.BaseModel;
import com.kkkkkn.readbooks.model.Model_Login;
import com.kkkkkn.readbooks.model.entity.AccountInfo;
import com.kkkkkn.readbooks.util.StringUtil;
import com.kkkkkn.readbooks.util.eventBus.EventMessage;
import com.kkkkkn.readbooks.util.eventBus.MessageEvent;
import com.kkkkkn.readbooks.view.activities.LoginActivity;
import com.kkkkkn.readbooks.view.view.LoginView;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONException;
import org.json.JSONObject;

public class Presenter_Login extends BasePresenter implements BaseModel.CallBack{
    private final static String TAG="Presenter_Login";
    private LoginView loginView;
    private Model_Login model_login;


    public Presenter_Login(Context context,LoginView loginView) {
        super(context,new Model_Login());
        this.loginView=loginView;
        model_login=(Model_Login)getBaseModel();
        model_login.setCallback(this);
    }

    /**
     * 登录,返回token 7天有效期
     * @param name 用户名
     * @param password 密码
     * @return 成功失败 boolean 类型
     */
    public void login(String name,String password){
        if(!StringUtil.checkAccountName(name)||!StringUtil.checkAccountPassword(password)){
            loginView.showMsgDialog(-1,"登陆失败");
            return;
        }
        String[] arr={name,password};
        EventBus.getDefault().post(new MessageEvent(EventMessage.LOGIN,arr));
    }

    public AccountInfo getAccountCache(){
        SharedPreferences sharedPreferences=getContext().getSharedPreferences("AccountInfo",Context.MODE_PRIVATE);
        AccountInfo accountInfo=new AccountInfo();
        accountInfo.setAccount_name(sharedPreferences.getString("account_name",""));
        accountInfo.setAccount_password(sharedPreferences.getString("account_password",""));
        return accountInfo;
    }

    public boolean setAccountCache(String name,String password){

    }


    @Override
    public void onSuccess(int type, Object object) {
        switch (type){
            case 1:
                //存储ID和token
                SharedPreferences.Editor editor=getContext().getSharedPreferences("AccountInfo",Context.MODE_PRIVATE).edit();
                editor.putInt();
                editor.putString();
                editor.apply();

                loginView.toMainActivity();
                loginView.showMsgDialog(1,"登录成功");
                break;
            case -1:
                loginView.showMsgDialog(-1,(String)object);
                break;
            default:
                loginView.showMsgDialog(-1,"登录失败");
                break;
        }
    }

    @Override
    public void onError(int type, Object object) {
        loginView.showMsgDialog(-1,"登录失败");
    }

}
