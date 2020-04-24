package com.kkkkkn.readbooks.activates;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.kkkkkn.readbooks.R;
import com.kkkkkn.readbooks.util.Md5Util;

import java.io.IOException;

import okhttp3.Response;
import okhttp3.ResponseBody;

public class LoginActivity extends BaseActivity {
    private final static String TAG="LoginActivity";
    EditText text_name,text_password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        //初始化控件
        initView();
    }

    private void initView(){
        Button btn_login=findViewById(R.id.btn_login);
        text_name=findViewById(R.id.user_name);
        text_password=findViewById(R.id.user_password);

        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i(TAG, "onClick: "+text_name.getText()+"||"+ Md5Util.str2md5(text_password.getText().toString()));
            }
        });
    }
}
