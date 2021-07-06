package com.kkkkkn.readbooks.viewmodel;

import android.text.Editable;
import android.util.Log;
import android.view.View;


import com.kkkkkn.readbooks.databinding.ActivityLoginBinding;
import com.kkkkkn.readbooks.model.entity.LoginInfo;
import com.kkkkkn.readbooks.util.eventBus.EventMessage;
import com.kkkkkn.readbooks.util.eventBus.MessageEvent;

import org.greenrobot.eventbus.EventBus;

public class LoginViewModel  {
    private ActivityLoginBinding activityLoginBinding;
    private LoginInfo loginInfo;

    public LoginViewModel(ActivityLoginBinding activityLoginBinding) {
        this.activityLoginBinding = activityLoginBinding;
        loginInfo=new LoginInfo();
        activityLoginBinding.setLoginInfo(loginInfo);
        activityLoginBinding.setLoginViewmodel(this);
    }


    public void loginClick(LoginInfo loginInfo){
        Log.i("TAG", "getLogin_name: "+loginInfo.getLogin_name());
        Log.i("TAG", "getLogin_password: "+loginInfo.getLogin_password());
    }

    public void jumpClick(View view){
        EventBus.getDefault().postSticky(new MessageEvent(EventMessage.JUMP_REG,null));
    }

    public void afterUserNameChanged(Editable editable) {
        loginInfo.setLogin_name(editable.toString());
    }

    public void afterPasswordChanged(Editable editable) {
        loginInfo.setLogin_password(editable.toString());

    }


}
