package com.kkkkkn.readbooks.activates;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.kkkkkn.readbooks.R;

/**
 * 注册账号，注意用户名查重以及密码格式
 */
public class RegisterActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
    }
}
