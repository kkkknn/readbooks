package com.kkkkkn.readbooks.view.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.os.Bundle;
import android.text.Editable;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.kkkkkn.readbooks.R;
import com.kkkkkn.readbooks.databinding.ActivityRegisterBinding;
import com.kkkkkn.readbooks.model.entity.LoginInfo;
import com.kkkkkn.readbooks.model.entity.RegisterInfo;
import com.kkkkkn.readbooks.util.eventBus.EventMessage;
import com.kkkkkn.readbooks.util.eventBus.MessageEvent;
import com.kkkkkn.readbooks.viewmodel.RegisterViewModel;

import org.greenrobot.eventbus.EventBus;

public class RegisterActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        ActivityRegisterBinding activityRegisterBinding= DataBindingUtil.setContentView(this,R.layout.activity_register);
        new RegisterViewModel(activityRegisterBinding);
    }



}