package com.kkkkkn.readbooks.activates;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.kkkkkn.readbooks.R;
import com.kkkkkn.readbooks.util.BackgroundUtil;
import com.kkkkkn.readbooks.util.BackgroundUtilListener;

import org.json.JSONException;
import org.json.JSONObject;

public class BookBrowsingActivity extends BaseActivity implements BackgroundUtilListener {
    private final static String TAG="BookBrowsingActivity";
    private Handler mHandler= new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            return false;
        }
    });
    
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
            try {
                JSONObject jsonObject=new JSONObject(str);
                String code=jsonObject.getString("code");
                String data=jsonObject.getString("chapterContent");
            } catch (JSONException e) {
                e.printStackTrace();
            }

            //发送handle消息渲染更新界面
            Message message=mHandler.obtainMessage();
            message.what=22;
        }
    }

    @Override
    public void error(int codeId) {

    }

    @Override
    public void timeOut(int requestId) {

    }
}