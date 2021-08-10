package com.kkkkkn.readbooks.view.activities;


import android.graphics.Color;
import android.os.Bundle;
import android.os.Looper;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.kkkkkn.readbooks.R;
import com.kkkkkn.readbooks.presenter.Presenter_Register;
import com.kkkkkn.readbooks.view.customView.CustomToast;
import com.kkkkkn.readbooks.view.view.RegisterActivityView;

public class RegisterActivity extends BaseActivity implements RegisterActivityView {
    private EditText edit_name,edit_password;
    private Button btn_reg;
    private Presenter_Register presenter_register;
    private TextView tv_tip;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        initView();
        presenter_register=new Presenter_Register(getApplicationContext(),this);
        presenter_register.init();
    }

    private void initView(){
        tv_tip=findViewById(R.id.text_reg_msg);
        edit_name=findViewById(R.id.edit_reg_name);
        edit_password=findViewById(R.id.edit_reg_password);
        btn_reg=findViewById(R.id.register_btn);
        btn_reg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tv_tip.setText("");
                final String name=edit_name.getText().toString();
                final String password=edit_password.getText().toString();
                presenter_register.register(name,password);
            }
        });
    }


    @Override
    public void showMsgDialog(int type, String msg) {
        Looper.prepare();
        if(type>0){
            CustomToast.showToast(getApplicationContext(),msg, Toast.LENGTH_SHORT,R.drawable.icon_msg_succese);

        }else {
            CustomToast.showToast(getApplicationContext(),msg,Toast.LENGTH_SHORT,R.drawable.icon_msg_error);
        }
        Looper.loop();
    }

    @Override
    public void showTip(int type, String msg) {
        if(msg==null||msg.isEmpty())return;
        final int value=type;
        final String val_str=msg;
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if(value>0){
                    tv_tip.setTextColor(Color.GREEN);
                }else if(value==0){
                    tv_tip.setTextColor(Color.YELLOW);
                }else {
                    tv_tip.setTextColor(Color.RED);
                }
                tv_tip.setText(val_str);
            }
        });

    }

    @Override
    public void back2Login() {
        this.finish();
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        presenter_register.release();
    }
}