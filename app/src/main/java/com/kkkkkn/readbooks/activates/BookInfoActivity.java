package com.kkkkkn.readbooks.activates;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.kkkkkn.readbooks.R;
import com.kkkkkn.readbooks.util.BackgroundUtil;
import com.kkkkkn.readbooks.util.BackgroundUtilListener;

public class BookInfoActivity extends AppCompatActivity implements BackgroundUtilListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_info);

        //绑定控件
        TextView book_name=findViewById(R.id.bookInfo_bookName);
        TextView author_name=findViewById(R.id.bookInfo_authorName);
        ImageView book_img=findViewById(R.id.bookInfo_bookImg);

        //查找图书信息是否存在
        Intent intent=getIntent();
        Bundle bundle=intent.getExtras();
        if(bundle==null){
            //不存在直接结束程序，或者弹窗显示
            finish();
            return;
        }
        String url=bundle.getString("bookUrl");
        //获取图书信息
        if(url==null||url.isEmpty()){
            finish();
            return;
        }
        //获取id和token字符串
        BackgroundUtil backgroundUtil=BackgroundUtil.getInstance(getApplicationContext());
        int id = backgroundUtil.getAccountId();
        String token = backgroundUtil.getTokenStr();
        if(id==0||token==null){
            //弹出提示登录信息有误，请重新登录
            finish();
            return;
        }
        //获取图书信息
        backgroundUtil.setListener(this);
        backgroundUtil.getBookInfo(url,id,token);
    }

    @Override
    public void success(String str) {
        Log.i("yyy", "success: 成功");
        //获取章节并填入信息 com.penpower.ppsignsi

    }

    @Override
    public void error(int codeId) {

        Log.i("yyy", "success: 失败");
    }

    @Override
    public void timeOut(int requestId) {

        Log.i("yyy", "success: 超时");
    }
}