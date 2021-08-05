package com.kkkkkn.readbooks.presenter;

import android.content.Context;

import com.kkkkkn.readbooks.model.BaseModel;
import com.kkkkkn.readbooks.model.Model_Login;
import com.kkkkkn.readbooks.model.entity.AccountInfo;
import com.kkkkkn.readbooks.util.StringUtil;
import com.kkkkkn.readbooks.util.eventBus.EventMessage;
import com.kkkkkn.readbooks.util.eventBus.MessageEvent;
import com.kkkkkn.readbooks.view.view.LoginActivityView;

import org.greenrobot.eventbus.EventBus;

public class Presenter_Login extends BasePresenter implements BaseModel.CallBack{
    private final static String TAG="Presenter_Login";
    private LoginActivityView loginActivityView;
    private Model_Login model_login;
    private String name,password;

    public Presenter_Login(Context context, LoginActivityView loginActivityView) {
        super(context,new Model_Login());
        this.loginActivityView = loginActivityView;
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
            loginActivityView.showMsgDialog(-1,"登陆失败");
            return;
        }
        String[] arr={name,password};
        this.name=name;
        this.password=password;
        EventBus.getDefault().post(new MessageEvent(EventMessage.LOGIN,arr));
    }





    @Override
    public void onSuccess(int type, Object object) {
        switch (type){
            case 1:
                //存储ID和token
                AccountInfo info=(AccountInfo)object;
                setTokenCache(info.getAccount_id(),info.getAccount_token());
                setAccountCache(this.name,this.password);

                loginActivityView.toMainActivity();
                loginActivityView.showMsgDialog(1,"登录成功");
                break;
            case -1:
                this.name=this.password=null;
                loginActivityView.showMsgDialog(-1,(String)object);
                break;
            default:
                this.name=this.password=null;
                loginActivityView.showMsgDialog(-1,"登录失败");
                break;
        }
    }

    @Override
    public void onError(int type, Object object) {
        this.name=this.password=null;
        loginActivityView.showMsgDialog(-1,"登录失败");
    }

}
