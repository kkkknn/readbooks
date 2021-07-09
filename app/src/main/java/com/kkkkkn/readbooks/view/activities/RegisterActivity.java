package com.kkkkkn.readbooks.view.activities;


import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.kkkkkn.readbooks.R;
import com.kkkkkn.readbooks.presenter.Presenter_Register;
import com.kkkkkn.readbooks.util.StringUtil;
import com.kkkkkn.readbooks.view.view.RegisterView;

import org.greenrobot.eventbus.EventBus;

public class RegisterActivity extends BaseActivity implements RegisterView {
    private EditText edit_name,edit_password;
    private Button btn_reg;
    private Presenter_Register presenter_register;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        initView();
        presenter_register=new Presenter_Register(getApplicationContext(),this);

    }

    private void initView(){
        edit_name=findViewById(R.id.edit_reg_name);
        edit_password=findViewById(R.id.edit_reg_password);
        btn_reg=findViewById(R.id.register_btn);
        btn_reg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name=edit_name.getText().toString();
                String password= StringUtil.password2md5(edit_password.getText().toString());
                presenter_register.register(name,password);
            }
        });
    }


    @Override
    public void showMsgDialog(int type, String msg) {

    }

    @Override
    public void showTip(int type, String msg) {

    }

}