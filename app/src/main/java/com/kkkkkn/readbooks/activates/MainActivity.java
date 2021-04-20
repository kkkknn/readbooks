package com.kkkkkn.readbooks.activates;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.NotificationCompat;
import androidx.core.view.MenuItemCompat;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.kkkkkn.readbooks.R;
import com.kkkkkn.readbooks.adapter.BookShelfAdapter;
import com.kkkkkn.readbooks.entity.BookInfo;
import com.kkkkkn.readbooks.util.eventBus.EventMessage;
import com.kkkkkn.readbooks.util.eventBus.MessageEvent;
import com.kkkkkn.readbooks.util.jsoup.JsoupUtilImp;
import com.kkkkkn.readbooks.util.sqlite.SqlBookUtil;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;

import javax.security.auth.login.LoginException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import static android.app.NotificationManager.IMPORTANCE_DEFAULT;


/**
 * 程序主界面，每次进入的时候获取读取本地图书并进行加载
 */
public class MainActivity extends BaseActivity  {
    private final static String TAG="主界面";
    private long lastBackClick;
    private final String requestUrl="http://www.kkkkknn.com:8005/version/";;
    private String ApkDirPath="";
    private String ApkName="";
    private NotificationManager mNotifyManager;
    private Notification.Builder mBuilder=null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        //隐藏APP title
        /*ActionBar actionBar=getSupportActionBar();
        if(actionBar!=null){
            actionBar.setDisplayShowTitleEnabled(false);
        }*/

        GridView mGridView=findViewById(R.id.main_booksGridView);
        SwipeRefreshLayout swipeRefreshLayout=findViewById(R.id.main_SwipeRefreshLayout);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                SqlBookUtil sqlBookUtil=SqlBookUtil.getInstance(getApplicationContext()).initDataBase();
                ArrayList<BookInfo> list=sqlBookUtil.getEnjoyBook();
                if(list!=null){
                    BookShelfAdapter mAdapter = new BookShelfAdapter(getApplicationContext(),list);
                    mGridView.setAdapter(mAdapter);
                }
                swipeRefreshLayout.setRefreshing(false);
            }
        });

        //读取本地数据库，获取已加入收藏的图书 并添加到主页相应位置
        SqlBookUtil sqlBookUtil=SqlBookUtil.getInstance(getApplicationContext()).initDataBase();
        ArrayList<BookInfo> list=sqlBookUtil.getEnjoyBook();
        BookShelfAdapter mAdapter = new BookShelfAdapter(getApplicationContext(),list);
        mGridView.setAdapter(mAdapter);
        mGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                BookInfo bookInfo=(BookInfo) adapterView.getAdapter().getItem(i);
                if(bookInfo!=null){
                    jump2ReadView(bookInfo);
                    Log.i(TAG, "onItemClick: "+bookInfo.getBookId());
                }
            }
        });


        initNotification();

    }

    //初始化通知栏
    private void initNotification(){
        mNotifyManager =(NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        //ChannelId为"1",ChannelName为"Channel1"
        NotificationChannel channel = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            mBuilder=new Notification.Builder(this,"1");
        }else {
            mBuilder=new Notification.Builder(this);
        }
        mBuilder.setSmallIcon(R.mipmap.ic_launcher);
        mBuilder.setContentTitle("正在下载");
        mBuilder.setContentText("Download in progress");
        mNotifyManager.notify(1,mBuilder.build());
    }

    @Override
    protected void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
        //EventBus.getDefault().post(new MessageEvent(EventMessage.CHECK_VERSION,null));
    }

    @Override
    protected void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }

    //跳转到阅读页面
    private void jump2ReadView(BookInfo info){
        //根据图书ID确定当前读书进度
        SqlBookUtil sqlBookUtil=SqlBookUtil.getInstance(getApplicationContext()).initDataBase();
        String str=sqlBookUtil.getReadProgress(info.getBookId());
        int pageCount=0;
        int chapterCount=0;
        int lineFlag=0;
        try {
            if(str!=null){
                JSONObject jsonObject=new JSONObject(str);
                pageCount=jsonObject.getInt("chapterPageCount");
                chapterCount=jsonObject.getInt("chapterCount");
                lineFlag=jsonObject.getInt("chapterLineCount");
                Log.i(TAG, "jump2ReadView: pageCount "+pageCount+" || "+chapterCount+" || "+lineFlag);
            }

            //跳转到阅读页面
            Intent intent=new Intent(getApplicationContext(),BookBrowsingActivity.class);
            Bundle bundle=new Bundle();
            bundle.putSerializable("bookInfo",info);
            bundle.putInt("chapterFlag",chapterCount);
            bundle.putInt("lineFlag",lineFlag);
            bundle.putInt("pageFlag",pageCount);
            intent.putExtras(bundle);
            startActivity(intent);

        } catch ( JSONException e) {
            e.printStackTrace();
        }
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.navigation,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.navigation_notifications:
                Toast.makeText(this,"点击了1",Toast.LENGTH_SHORT).show();
                EventBus.getDefault().post(new MessageEvent(EventMessage.CHECK_VERSION,null));

                break;
            case R.id.navigation_dashboard:
                //点击了搜索框，开始跳转
                Intent intent=new Intent(MainActivity.this,SearchActivity.class);
                startActivity(intent);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void syncProgress(MessageEvent event){
        if(event.message==EventMessage.DOWNLOAD_PROGRESS){
            mBuilder.setProgress(100,(int)event.value,false);
            mNotifyManager.notify(1,mBuilder.build());
            Log.i(TAG, "syncProgress: "+(int)event.value);
        }else if(event.message==EventMessage.DOWNLOAD_SUCCESS){
            //todo 跳转安装新版本APK
            String path=(String) event.value;
            Intent  intent = new Intent(this,SearchActivity.class);
            PendingIntent pendingIntent = PendingIntent.getActivity(this,0,intent,0);
            mBuilder.setContentIntent(pendingIntent);
        }
    }

    @Subscribe(threadMode = ThreadMode.BACKGROUND)
    public void onMessageEvent(MessageEvent event){
        if(event.message== EventMessage.CHECK_VERSION){
            //获取当前应用版本号
            try {
                PackageManager manager = this.getPackageManager();
                PackageInfo info = manager.getPackageInfo(this.getPackageName(), 0);
                String version = info.versionName;
                Log.i(TAG, "checkUpdate: "+version);

                //在线获取最新版本号
                OkHttpClient client = new OkHttpClient();
                Request request = new Request.Builder()
                        .url("http://www.kkkkknn.com:8005/version/")
                        .build();
                Response response = client.newCall(request).execute();
                if(response.code()==200){
                    String responseStr= Objects.requireNonNull(response.body()).string();
                    Log.i(TAG, "checkUpdate: "+responseStr);
                    //转换为json对象进行解析
                    JSONObject jsonObject=new JSONObject(responseStr);
                    String versionStr=jsonObject.getString("version");
                    if(!version.equals(versionStr)){

                        String url=jsonObject.getString("downloadUrl");
                        EventBus.getDefault().post(new MessageEvent(EventMessage.DOWNLOAD_APK,url));
                    }
                }
            } catch (IOException | JSONException | PackageManager.NameNotFoundException e) {
                e.printStackTrace();
            }
        }else if(event.message== EventMessage.DOWNLOAD_APK){
            //todo 弹窗显示是否下载APK
            String url=(String) event.value;
            //okhttp 进行下载，并通过eventbus发送消息通知UI更新通知栏下载进度
            OkHttpClient client=new OkHttpClient();
            Request down_request = new Request.Builder()
                    .url(url)
                    .build();
            initDir("");//初始化下载目录
            //拆分APK名字 http://www.aaa.com:8005/download/xxx.apk
            ApkName=url.split("download")[1];
            Log.i(TAG, "onMessageEvent: "+ApkName);
            client.newCall(down_request).enqueue(new Callback() {
                @Override
                public void onFailure(@NotNull Call call, @NotNull IOException e) {
                    Log.i(TAG, "onFailure: 下载失败");
                }

                @Override
                public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                    InputStream is = null;
                    byte[] buf = new byte[2048];
                    int len = 0;
                    FileOutputStream fos = null;
                    //储存下载文件的目录
                    File file = new File(ApkDirPath, ApkName);
                    try {
                        is = response.body().byteStream();
                        long total = response.body().contentLength();
                        fos = new FileOutputStream(file);
                        long sum = 0;
                        while ((len = is.read(buf)) != -1) {
                            fos.write(buf, 0, len);
                            sum += len;
                            int progress = (int) (sum * 1.0f / total * 100);
                            //下载中更新进度条 eventbus 通知
                            if(progress%5==0){
                                EventBus.getDefault().post(new MessageEvent(EventMessage.DOWNLOAD_PROGRESS,progress));
                            }

                        }
                        fos.flush();
                        //下载完成
                        EventBus.getDefault().post(new MessageEvent(EventMessage.DOWNLOAD_SUCCESS,file));
                        Log.i(TAG, "onResponse: DOWNLOAD_SUCCESS");
                    } catch (Exception e) {
                        EventBus.getDefault().post(new MessageEvent(EventMessage.DOWNLOAD_ERROR,e));
                    }finally {
                        try {
                            if (is != null) {
                                is.close();
                            }
                            if (fos != null) {
                                fos.close();
                            }
                        } catch (IOException e) {

                        }

                    }

                }
            });
        }
    }

    //初始化保存目录，没有进行创建
    private void initDir(String path){
        //是否插入SD卡
        String status = Environment.getExternalStorageState();
        boolean isSDCard=status.equals(Environment.DIRECTORY_DOWNLOADS);
        if(isSDCard){
            ApkDirPath=getApplicationContext().getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS).getAbsolutePath()+path;
        }else{
            ApkDirPath=getApplicationContext().getFilesDir().getAbsolutePath()+path;
        }
        File file=new File(ApkDirPath);
        if(!file.exists()){
            file.mkdirs();
        }
    }
}
