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
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.kkkkkn.readbooks.R;
import com.kkkkkn.readbooks.adapter.BookChaptersAdapter;
import com.kkkkkn.readbooks.entity.BookInfo;
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
import java.util.LinkedList;
import java.util.List;

public class BookInfoActivity extends BaseActivity {
    private final static String TAG="BookInfoActivity";
    private TextView book_name,author_name;
    private ImageView book_img;
    private ArrayList<String[]> chapterList=new ArrayList<>();;
    private ListView chapter_listView;
    private BookChaptersAdapter chaptersAdapter;
    private boolean isRun=false;
    private LinkedList<String> linkedList=new LinkedList<>();
    private Handler mHandler=new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message message) {
            switch (message.what){
                case 100:
                    //更新当前页面数据
                    JSONArray array=(JSONArray) message.obj;
                    for (int i = 0; i < array.length(); i++) {
                        try {
                            JSONObject jsonObject=(JSONObject)array.get(i);
                            String[] arr=new String[2];
                            arr[0]=(String) jsonObject.get("chapterName");
                            arr[1]=(String) jsonObject.get("chapterUrl");
                            chapterList.add(arr);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                    if(chaptersAdapter!=null){
                        chaptersAdapter.notifyDataSetChanged();
                    }
                    Log.i(TAG, "handleMessage: 获取到了  666666666");
                    isRun=false;
                    break;
            }
            return false;
        }
    });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_info);

        initView();

        //查找图书信息是否存在
        Intent intent=getIntent();
        BookInfo bookInfo=(BookInfo)intent.getSerializableExtra("bookInfo");

        //获取图书信息
        if(bookInfo.isEmpty()){
            Log.i(TAG, "onCreate: 2222222222");
            finish();
            return;
        }
        author_name.setText(bookInfo.getAuthorName());
        book_name.setText(bookInfo.getBookName());
        Glide.with(getApplicationContext()).load(bookInfo.getBookImgUrl()).into(book_img);
        new RequestThread(bookInfo.getBookUrl()).start();
    }

    //初始化绑定控件
    private void initView(){
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
        chapter_listView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView absListView, int i) {

            }

            @Override
            public void onScroll(AbsListView absListView, int i, int i1, int i2) {
                if(i+i1==i2){
                    Log.i(TAG, "onScroll: 滑动到底部");
                    //开始请求下一页面章节
                    if(linkedList.size()>0&&!isRun){
                        isRun=true;
                        new NextPageRequestThread().start();
                    }
                }
            }
        });
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
                chapterList.clear();
                String str=jsoupUtil.getBookInfo(url);
                //解析返回的json数据 chapterList
                JSONObject jsonObject=new JSONObject(str);
                JSONArray jsonArray=(JSONArray) jsonObject.get("chapterPages");
                for (int i=0;i<jsonArray.length();i++){
                    JSONObject object=(JSONObject)jsonArray.get(i);
                    linkedList.add((String) object.get("chapterPageUrl"));
                }
                if(!isRun){
                    isRun=true;
                    new NextPageRequestThread().start();
                }
            } catch (IOException | JSONException e) {
                e.printStackTrace();
            }
        }
    }

    private class NextPageRequestThread extends Thread{
        @Override
        public void run() {
            JsoupUtil jsoupUtil=new JsoupUtilImp_xbqg();
            try {
                //取当前页
                String jsStr=jsoupUtil.getBookChapterList(linkedList.getFirst());
                if(!jsStr.isEmpty()){
                    linkedList.removeFirst();
                    JSONObject jsonObject2=new JSONObject(jsStr);
                    //handler发送消息，同时获取章节目录
                    Message msgChapter=mHandler.obtainMessage();
                    msgChapter.what=100;
                    msgChapter.obj=(JSONArray) jsonObject2.get("chapters");
                    mHandler.sendMessage(msgChapter);
                }

            } catch (IOException | JSONException e) {
                e.printStackTrace();
            }
        }
    }

}