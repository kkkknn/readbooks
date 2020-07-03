package com.kkkkkn.readbooks.activates;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.kkkkkn.readbooks.R;
import com.kkkkkn.readbooks.util.BackgroundUtil;
import com.kkkkkn.readbooks.util.BackgroundUtilListener;

public class BookBrowsingActivity extends AppCompatActivity implements BackgroundUtilListener {
    private final static String TAG="BookBrowsingActivity";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_browsing);

        //获取携带信息
        Intent intent=getIntent();
        String name=intent.getStringExtra("chapterName");
        String url=intent.getStringExtra("chapterUrl");
        if(name==null||url==null||url.isEmpty()){
            finish();
        }
        //请求服务器
        BackgroundUtil backgroundUtil=BackgroundUtil.getInstance(getApplicationContext()).setListener(this);
        String token=backgroundUtil.getTokenStr();
        int id=backgroundUtil.getAccountId();
        if(id==0||token==null||token.isEmpty()){
            finish();
        }
        backgroundUtil.getChapterContent(url,id,token);

    }

    @Override
    public void success(int codeId, String str) {
        if(codeId==BackgroundUtil.CHAPTER){
            //开始解析json字符串
            Log.i(TAG, "success: "+str);
            //发送handle消息渲染更新界面
        }
    }

    @Override
    public void error(int codeId) {

    }

    @Override
    public void timeOut(int requestId) {

    }
}