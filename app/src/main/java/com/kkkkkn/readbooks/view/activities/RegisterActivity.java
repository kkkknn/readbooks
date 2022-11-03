package com.kkkkkn.readbooks.view.activities;


import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Looper;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.appcompat.widget.AppCompatTextView;

import com.kkkkkn.readbooks.R;
import com.kkkkkn.readbooks.presenter.Presenter_Register;
import com.kkkkkn.readbooks.view.view.RegisterActivityView;

import java.util.Objects;

import es.dmoral.toasty.Toasty;

public class RegisterActivity extends BaseActivity implements RegisterActivityView {
    private AppCompatEditText edit_name,edit_password,edit_password_check;
    private Presenter_Register presenter_register;
    private AppCompatTextView tv_account_tip,tv_password_tip,tv_password_check_tip;
    private String cacheAccount,cachePassword;
    private final View.OnFocusChangeListener onFocusChangeListener=new View.OnFocusChangeListener() {
        @Override
        public void onFocusChange(View view, boolean b) {
            if(!b){
                return;
            }
            int id=view.getId();
            if(id==R.id.edit_reg_name){
                tv_account_tip.setText("");
                tv_account_tip.setVisibility(View.GONE);
            }else if(id==R.id.edit_reg_password){
                tv_password_tip.setText("");
                tv_password_tip.setVisibility(View.GONE);
            }else if(id==R.id.edit_reg_password_check){
                tv_password_check_tip.setText("");
                tv_password_check_tip.setVisibility(View.GONE);
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        initView();
        presenter_register=new Presenter_Register(getApplicationContext(),this);
        presenter_register.init();
    }

    private void initView(){
        tv_account_tip=findViewById(R.id.text_reg_name_tip);
        tv_password_tip=findViewById(R.id.text_reg_password_tip);
        tv_password_check_tip=findViewById(R.id.text_reg_password_check_tip);
        edit_name=findViewById(R.id.edit_reg_name);
        edit_password=findViewById(R.id.edit_reg_password);
        edit_password_check=findViewById(R.id.edit_reg_password_check);
        AppCompatButton btn_reg = findViewById(R.id.register_btn);
        btn_reg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tv_account_tip.setText("");
                tv_password_tip.setText("");
                tv_password_check_tip.setText("");
                final String name= Objects.requireNonNull(edit_name.getText()).toString();
                final String password= Objects.requireNonNull(edit_password.getText()).toString();
                final String passwordCheck= Objects.requireNonNull(edit_password_check.getText()).toString();
                cacheAccount=name;
                cachePassword=password;
                presenter_register.register(name,password,passwordCheck);
            }
        });
        edit_name.setOnFocusChangeListener(onFocusChangeListener);
        edit_password.setOnFocusChangeListener(onFocusChangeListener);
        edit_password_check.setOnFocusChangeListener(onFocusChangeListener);
    }


    @Override
    public void showMsgDialog(final int type, final String msg) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if(type>0){
                    Toasty.success(getApplicationContext(), msg, Toast.LENGTH_SHORT, true).show();
                }else {
                    Toasty.error(getApplicationContext(), msg, Toast.LENGTH_SHORT, true).show();
                }
            }
        });

    }

    @Override
    public void showTip(int type, String msg) {
        if(msg==null||msg.isEmpty())return;
        final int value=type;
        final String val_str=msg;
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                switch (value){
                    case -1:
                        tv_password_check_tip.setVisibility(View.VISIBLE);
                        tv_password_check_tip.setText(val_str);
                        break;
                    case -2:
                        tv_account_tip.setVisibility(View.VISIBLE);
                        tv_account_tip.setText(val_str);
                        break;
                    case -3:
                        edit_password.setVisibility(View.VISIBLE);
                        edit_password.setText(val_str);
                        break;
                }
            }
        });

    }

    @Override
    public void back2Login() {
        Intent intent=getIntent();
        intent.putExtra("accountName", cacheAccount);
        intent.putExtra("accountPassword", cachePassword);
        setResult(RESULT_OK,intent);
        finish();
    }

    @Override
    public void clearAccountCache() {
        cacheAccount=null;
        cachePassword=null;
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        presenter_register.release();
    }
}