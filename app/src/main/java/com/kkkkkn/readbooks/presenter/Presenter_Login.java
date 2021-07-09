package com.kkkkkn.readbooks.presenter;

import android.content.Context;
import android.os.Looper;

import com.kkkkkn.readbooks.model.BaseModel;
import com.kkkkkn.readbooks.model.Model_Login;
import com.kkkkkn.readbooks.model.entity.AccountInfo;
import com.kkkkkn.readbooks.util.StringUtil;
import com.kkkkkn.readbooks.util.eventBus.EventMessage;
import com.kkkkkn.readbooks.util.eventBus.MessageEvent;
import com.kkkkkn.readbooks.view.activities.LoginActivity;
import com.kkkkkn.readbooks.view.view.LoginView;

import org.greenrobot.eventbus.EventBus;

public class Presenter_Login extends BasePresenter {
    private LoginView loginView;

    public Presenter_Login(Context context,LoginView loginView) {
        super(context);
        this.loginView=loginView;
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
        //调用model层进行登录验证
        Looper.prepare();
        new Model_Login().login(name, password, new BaseModel.CallBack() {
            @Override
            public void onSuccess(int type, Object object) {
                loginView.showMsgDialog(1,"登录成功");
                loginView.toMainActivity();
            }

            @Override
            public void onError(int type, Object object) {
                loginView.showMsgDialog(11,"登录失败");
            }
        });
        Looper.loop();

    }

}
