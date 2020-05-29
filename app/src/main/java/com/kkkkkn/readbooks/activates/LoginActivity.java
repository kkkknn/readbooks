package com.kkkkkn.readbooks.activates;

import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.kkkkkn.readbooks.R;
import com.kkkkkn.readbooks.util.BackgroundUtil;
import com.kkkkkn.readbooks.util.BackgroundUtilListener;
import com.kkkkkn.readbooks.util.Md5Util;
import com.kkkkkn.readbooks.view.MessageDialog;

import java.io.IOException;

import okhttp3.Response;
import okhttp3.ResponseBody;

/**
 * 登录账号页面，记录返回的token令牌，并载入缓存，每次请求都要加入此token
 */
public class LoginActivity extends BaseActivity {
    private final static String TAG="LoginActivity";
    private EditText text_name,text_password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        //初始化控件
        initView();
    }

    private void initView(){
        Button btn_login=findViewById(R.id.btn_login);
        TextView register_text = findViewById(R.id.edit_register);
        text_name=findViewById(R.id.user_name_login);
        text_password=findViewById(R.id.user_password_login);

        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String account=text_name.getText().toString();
                String password=Md5Util.str2md5(text_password.getText().toString());
                Log.i(TAG, "onClick: account:"+account+"  password:"+password);
                if(password == null || account.equals("") || password.equals("")){
                    DialogInterface.OnClickListener listener1=new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Log.i(TAG, "onClick: 点击了确定");
                            dialog.dismiss();

                        }
                    };
                    //相关数据为空，弹窗提示，重新提交
                    MessageDialog.showDialog(LoginActivity.this,"提示","相关数据为空，请重新填写",listener1,null);

                }else{
                    //获取到相关数据，开始进行登录，调用接口类访问服务器进行登录
                    BackgroundUtilListener listener=new BackgroundUtilListener() {
                        @Override
                        public void success(int requestId) {
                            //登录成功，返回主页，并缓存相关数据
                        }

                        @Override
                        public void error(int codeId) {
                            //登录失败，弹窗重新进行登录

                        }

                        @Override
                        public void timeOut(int requestId) {
                            //网络超时，弹窗重新进行登录

                        }
                    };
                    BackgroundUtil backgroundUtil=BackgroundUtil.getInstance(LoginActivity.this,listener);
                    backgroundUtil.accountLogin(account,password);




                }
            }
        });
        register_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogInterface.OnClickListener listener1=new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Log.i(TAG, "onClick: 点击了确定");
                        dialog.dismiss();

                    }
                };
                //弹窗提示，此功能暂未开发，等待后续版本添加
                MessageDialog.showDialog(LoginActivity.this,"提示","此功能暂未开发，等待后续版本添加",listener1,null);
            }
        });
    }
}
