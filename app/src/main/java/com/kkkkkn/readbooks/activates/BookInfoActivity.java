package com.kkkkkn.readbooks.activates;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.kkkkkn.readbooks.R;
import com.kkkkkn.readbooks.adapter.BookChaptersAdapter;
import com.kkkkkn.readbooks.util.ImageLoaderUtil;
import com.kkkkkn.readbooks.util.jsoup.JsoupUtil;
import com.kkkkkn.readbooks.util.jsoup.JsoupUtilImp_xbqg;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class BookInfoActivity extends BaseActivity {
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
                            JSONObject jsonObject=(JSONObject)chapterArray.get(i);
                            String[] arr=new String[2];
                            arr[0]=jsonObject.getString("chapterName");
                            arr[1]=jsonObject.getString("chapterUrl");
                            chapterList.add(arr);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                    if(chaptersAdapter!=null){
                        chaptersAdapter.notifyDataSetChanged();
                    }
                    break;
                case 100:
                    Bitmap bitmap=(Bitmap) message.obj;
                    //todo::加载在线图片
                    if(bitmap!=null){
                        book_img.setImageBitmap(bitmap);
                        Log.i(TAG, "imgUrl:  图片不为空");
                    }
                    Log.i(TAG, "imgUrl:  显示图片");
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
        //设置章节点击跳转
        chapter_listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                //跳转到浏览界面，携带章节列表及点击项
                Intent intent=new Intent(getApplicationContext(),BookBrowsingActivity.class);
                Bundle bundle=new Bundle();
                bundle.putSerializable("chapterList",chapterList);
                bundle.putInt("chapterPoint",i);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
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


        new RequestThread(url).start();
    }

    private class RequestThread extends Thread{
        String url;
        public RequestThread(String str){
            this.url=str;
        }

        @Override
        public void run() {
            JsoupUtil jsoupUtil=new JsoupUtilImp_xbqg();
            try {
                String str=jsoupUtil.getBookInfo(url);
                Log.i(TAG, "run: "+str);
                //解析返回的json数据 chapterList
                JSONObject jsonObject=new JSONObject(str);
                String bookImgUrl=(String) jsonObject.getString("bookImgUrl");
                JSONArray jsonArray=(JSONArray) jsonObject.get("chapterInfo");

                //handler发送消息，同时获取章节目录
                Message msgChapter=mHandler.obtainMessage();
                msgChapter.what=90;
                msgChapter.obj=jsonArray;
                mHandler.sendMessage(msgChapter);

                Message obtainMessage=mHandler.obtainMessage();
                Bitmap bitmap=getBitmap(bookImgUrl);
                obtainMessage.what=100;
                obtainMessage.obj=bitmap;
                mHandler.sendMessage(obtainMessage);
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }


    public Bitmap getBitmap(String path) throws IOException {
        URL url = new URL(path);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setConnectTimeout(5000);
        conn.setRequestMethod("GET");
        if (conn.getResponseCode() == 200){
            InputStream inputStream = conn.getInputStream();
            return BitmapFactory.decodeStream(inputStream);
        }
        return null;
    }
}