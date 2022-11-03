package com.kkkkkn.readbooks.view.activities;


import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContract;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.appcompat.widget.AppCompatTextView;

import com.kkkkkn.readbooks.R;
import com.kkkkkn.readbooks.model.entity.AccountInfo;
import com.kkkkkn.readbooks.presenter.Presenter_Login;
import com.kkkkkn.readbooks.view.view.LoginActivityView;

import java.util.Objects;

import es.dmoral.toasty.Toasty;


public class LoginActivity extends BaseActivity implements LoginActivityView {
    private AppCompatButton btn_login;
    private AppCompatEditText edit_name,edit_password;
    private AppCompatTextView jumpText;
    private Presenter_Login presenter_login;
    private long lastBackClick;
    private ActivityResultLauncher<Intent> regActivityResultLauncher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);


        initView();
        regActivityResultLauncher=registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
            @Override
            public void onActivityResult(ActivityResult result) {
                Intent intent=result.getData();
                int resultCode = result.getResultCode();
                //判断注册是否成功
                if(resultCode==RESULT_OK && intent!=null){
                    String name=intent.getStringExtra("accountName");
                    String password=intent.getStringExtra("accountPassword");
                    edit_name.setText(name);
                    edit_password.setText(password);
                }
            }
        });
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
                String name= Objects.requireNonNull(edit_name.getText()).toString();
                String password= Objects.requireNonNull(edit_password.getText()).toString();
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
                    Toasty.success(getApplicationContext(), msg, Toast.LENGTH_SHORT, true).show();
                }else {
                    Toasty.error(getApplicationContext(), msg, Toast.LENGTH_SHORT, true).show();
                }
            }
        });

    }

    @Override
    public void toRegisterActivity() {
        regActivityResultLauncher.launch(new Intent(this, RegisterActivity.class));
    }

    @Override
    public void toMainActivity() {
        Intent intent=getIntent();
        setResult(RESULT_OK,intent);
        finish();
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