package com.kkkkkn.readbooks.view.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import com.kkkkkn.readbooks.R;

public class RegisterActivity extends AppCompatActivity {
    private EditText edit_pwd,edit_name;
    private Button btn_submit;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        initView();
    }
    private void initView(){
        
    }
}