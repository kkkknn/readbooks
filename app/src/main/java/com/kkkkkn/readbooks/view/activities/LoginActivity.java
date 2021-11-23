package com.kkkkkn.readbooks.view.activities;


import android.content.Intent;
import android.os.Bundle;
import android.os.Looper;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.kkkkkn.readbooks.R;
import com.kkkkkn.readbooks.model.entity.AccountInfo;
import com.kkkkkn.readbooks.presenter.Presenter_Login;
import com.kkkkkn.readbooks.view.customView.CustomToast;
import com.kkkkkn.readbooks.view.view.LoginActivityView;


public class LoginActivity extends BaseActivity implements LoginActivityView {
    private Button btn_login;
    private EditText edit_name,edit_password;
    private TextView jumpText;
    private Presenter_Login presenter_login;
    private long lastBackClick;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        initView();
        presenter_login=new Presenter_Login(getApplicationContext(),this);
        presenter_login.init();

    }
    private void initView(){
        jumpText=findViewById(R.id.jumpToRsg);
        btn_login=findViewById(R.id.login_btn);
        edit_name=findViewById(R.id.edit_account_name);
        edit_password=findViewById(R.id.edit_account_password);
        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name=edit_name.getText().toString();
                String password= edit_password.getText().toString();
                presenter_login.login(name,password);
            }
        });
        jumpText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                toRegisterActivity();
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        flushEditView();
    }

    @Override
    public void showMsgDialog(final int type, final String msg) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if(type>0){
                    //不显示成功toast，
                    //CustomToast.showToast(getApplicationContext(),msg,Toast.LENGTH_SHORT,R.drawable.icon_msg_succese);
                }else {
                    CustomToast.showToast(getApplicationContext(),msg,Toast.LENGTH_SHORT,R.drawable.icon_msg_error);
                }
            }
        });

    }

    @Override
    public void toRegisterActivity() {
        startActivityForResult(new Intent(getApplicationContext(), RegisterActivity.class),1);
    }

    @Override
    public void toMainActivity() {
        startActivity(new Intent(getApplicationContext(), MainActivity.class));
    }

    @Override
    public void flushEditView() {
        if(presenter_login!=null){
            AccountInfo info=presenter_login.getAccountCache();
            edit_name.setText(info.getAccount_name());
            edit_password.setText(info.getAccount_password());
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(presenter_login!=null){
            presenter_login.release();
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode==KeyEvent.KEYCODE_BACK){
            long nowBackClick=System.currentTimeMillis();
            if(lastBackClick!=0&&(nowBackClick-lastBackClick)<1500){
                //程序退出
                this.exitAll();
            }else{
                //500ms以上，弹窗不处理
                lastBackClick=nowBackClick;
                Toast.makeText(getApplicationContext(),"请再按一次以退出程序",Toast.LENGTH_SHORT).show();
            }
            return false;
        }
        return super.onKeyDown(keyCode, event);
    }
}