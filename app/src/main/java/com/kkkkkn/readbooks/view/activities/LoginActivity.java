package com.kkkkkn.readbooks.view.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.kkkkkn.readbooks.R;

public class LoginActivity extends AppCompatActivity {
    private EditText edit_name,edit_pwd;
    private Button submitBtn;
    private TextView jump2Rsg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
    }

    private void initView(){
        edit_name=findViewById(R.id.edit_account_name);
        edit_pwd=findViewById(R.id.edit_account_password);
        submitBtn=findViewById(R.id.login_btn);
        jump2Rsg=findViewById(R.id.jumpToRsg);
        submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //请求登录，判断是否跳转到登录页
            }
        });
        jump2Rsg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //跳转到注册页，注册完后 activitresult返回结果
                Intent intent=new Intent(getApplicationContext(),RegisterActivity.class);
                startActivityForResult(intent,2);
            }
        });
    }

}