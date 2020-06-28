package com.kkkkkn.readbooks.activates;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.kkkkkn.readbooks.R;
import com.kkkkkn.readbooks.adapter.BookChaptersAdapter;
import com.kkkkkn.readbooks.util.BackgroundUtil;
import com.kkkkkn.readbooks.util.BackgroundUtilListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class BookInfoActivity extends AppCompatActivity implements BackgroundUtilListener {
    private final static String TAG="BookInfoActivity";
    private TextView book_name,author_name;
    private ImageView book_img;
    private ArrayList<String[]> chapterList=new ArrayList<>();;
    private ListView chapter_listView;
    private BookChaptersAdapter chaptersAdapter;
    private Handler mHandler=new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message message) {
            switch (message.what){
                case 90:
                    JSONArray chapterArray=(JSONArray) message.obj;
                    chapterList.clear();
                    for (int i = 0; i < chapterArray.length(); i++) {
                        try {
                            JSONArray chapterInfo=(JSONArray) chapterArray.get(i);
                            String[] chapterArr=new String[2];
                            chapterArr[0]=chapterInfo.getString(0);
                            chapterArr[1]=chapterInfo.getString(1);
                            chapterList.add(chapterArr);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                    if(chaptersAdapter!=null){
                        chaptersAdapter.notifyDataSetChanged();
                    }
                    break;
                case 100:
                    String imgUrl=(String) message.obj;
                    Log.i(TAG, "imgUrl: "+imgUrl);
                    break;
            }
            return false;
        }
    });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_info);

        //绑定控件
        book_name=findViewById(R.id.bookInfo_bookName);
        author_name=findViewById(R.id.bookInfo_authorName);
        book_img=findViewById(R.id.bookInfo_bookImg);
        chapter_listView=findViewById(R.id.bookInfo_chapters_listView);
        chaptersAdapter=new BookChaptersAdapter(chapterList,getApplicationContext());
        chapter_listView.setAdapter(chaptersAdapter);
        //查找图书信息是否存在
        Intent intent=getIntent();
        Bundle bundle=intent.getExtras();
        if(bundle==null){
            //不存在直接结束程序，或者弹窗显示
            finish();
            return;
        }
        String url=bundle.getString("bookUrl");
        String authorName=bundle.getString("bookAuthorName");
        String bookName=bundle.getString("bookName");
        //获取图书信息
        if(url==null||url.isEmpty()||authorName==null||authorName.isEmpty()||bookName==null||bookName.isEmpty()){
            finish();
            return;
        }
        author_name.setText(authorName);
        book_name.setText(bookName);

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
        try {
            JSONObject jsonObject = new JSONObject(str);
            String code=jsonObject.getString("code");
            if(code.isEmpty()||code.equals("error")){
                //返回错误，弹出提示
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getApplicationContext(),"获取信息失败",Toast.LENGTH_SHORT).show();
                    }
                });
                return;
            }
            JSONObject dataObject=(JSONObject) jsonObject.get("data");
            String bookImgUrl=(String) dataObject.getString("bookImg");
            int sourceId=dataObject.getInt("bookSourceId");
            JSONArray chapterArray=(JSONArray)dataObject.get("chapterInfo");
            //handler发送消息，同时获取章节目录和图书图片
            Message msgImg=mHandler.obtainMessage();
            msgImg.what=(100);
            msgImg.obj=bookImgUrl;
            mHandler.sendMessage(msgImg);

            Message msgChapter=mHandler.obtainMessage();
            msgChapter.what=(90);
            msgChapter.obj=chapterArray;
            mHandler.sendMessage(msgChapter);

        } catch (JSONException e) {
            e.printStackTrace();
            Log.e(TAG, "success: json 解析错误");
        }


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