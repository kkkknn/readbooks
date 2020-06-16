package com.kkkkkn.readbooks.activates;

import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

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
    private long lastBackClick;

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
                        public void success(String str) {
                            //解析返回字符串，判断调用是否成功
                            Log.i(TAG, "success: "+str);
                        }

                        @Override
                        public void error(int codeId) {
                            //登录失败，弹窗重新进行登录
                            Log.i(TAG, "error: "+codeId);

                        }

                        @Override
                        public void timeOut(int requestId) {
                            //网络超时，弹窗重新进行登录
                            Log.i(TAG, "timeOut: "+requestId);
                        }
                    };
                    BackgroundUtil backgroundUtil=BackgroundUtil.getInstance(LoginActivity.this).setListener(listener);
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

    //监听返回键，连续按2次直接退出程序

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
