package com.kkkkkn.readbooks.view.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.adapters.TextViewBindingAdapter;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.util.Log;
import android.view.View;

import com.kkkkkn.readbooks.BR;
import com.kkkkkn.readbooks.R;
import com.kkkkkn.readbooks.databinding.ActivityLoginBinding;
import com.kkkkkn.readbooks.model.entity.LoginInfo;
import com.kkkkkn.readbooks.util.eventBus.EventMessage;
import com.kkkkkn.readbooks.util.eventBus.MessageEvent;
import com.kkkkkn.readbooks.viewmodel.LoginViewModel;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

public class LoginActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ActivityLoginBinding activityLoginBinding= DataBindingUtil.setContentView(this,R.layout.activity_login);
        new LoginViewModel(activityLoginBinding);
    }

    @Subscribe(threadMode = ThreadMode.MAIN,sticky = true)
    public void syncProgress(MessageEvent event){
        switch (event.message){
            case JUMP_REG:
                Intent intent=new Intent(getApplicationContext(), RegisterActivity.class);
                startActivity(intent);
                break;
            case JUMP_INDEX:
                System.out.println("跳转到主页");
                break;
        }
    }


}