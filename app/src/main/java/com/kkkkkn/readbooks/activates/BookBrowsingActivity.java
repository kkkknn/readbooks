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
import org.jsoup.helper.ChangeNotifyingArrayList;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class BookBrowsingActivity extends BaseActivity implements BackgroundUtilListener {
    private final static String TAG="BookBrowsingActivity";
    private ArrayList<String[]> chapterList=new ArrayList<>();
    private int arrayCount;
    private String chapterUrl;
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
        Bundle bundle=getIntent().getExtras();
        if(bundle==null){
            //没有携带信息
            finish();
        }else{
            //序列化处理，防止转换警告
            Object obj=(Object) bundle.getSerializable("chapterList");
            if (obj instanceof ArrayList<?>) {
                for (Object o : (List<?>) obj) {
                    chapterList.add((String[]) o);
                }
            }
            arrayCount=bundle.getInt("chapterPoint");
        }
        //请求服务器
        BackgroundUtil backgroundUtil=BackgroundUtil.getInstance(getApplicationContext()).setListener(this);
        String token=backgroundUtil.getTokenStr();
        int id=backgroundUtil.getAccountId();
        if(id==0||token==null||token.isEmpty()){
            finish();
        }
        backgroundUtil.getChapterContent(chapterList.get(arrayCount)[1],id,token);
    }

    @Override
    public void success(int codeId, String str) {
        if(codeId==BackgroundUtil.CHAPTER){
            String code="",data="";//开始解析json字符串
            Log.i(TAG, "success: "+str);
            try {
                JSONObject jsonObject=new JSONObject(str);
                code=jsonObject.getString("code");
                if(code.equals("success")){
                    data=jsonObject.getString("chapterContent");
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            if(!data.isEmpty()){
                //发送handle消息渲染更新界面
                Message message=mHandler.obtainMessage();
                message.what=22;
            }
        }
    }

    @Override
    public void error(int codeId) {

    }

    @Override
    public void timeOut(int requestId) {

    }
}