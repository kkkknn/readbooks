package com.kkkkkn.readbooks.view.activities;

import android.app.AlertDialog;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.Settings;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.FileProvider;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.kkkkkn.readbooks.R;
import com.kkkkkn.readbooks.model.adapter.BookShelfAdapter;
import com.kkkkkn.readbooks.model.entity.BookInfo;
import com.kkkkkn.readbooks.presenter.Presenter_Main;
import com.kkkkkn.readbooks.util.eventBus.EventMessage;
import com.kkkkkn.readbooks.util.eventBus.MessageEvent;
import com.kkkkkn.readbooks.model.sqlite.SqlBookUtil;

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
import java.util.Objects;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;


/**
 * 程序主界面，每次进入的时候获取读取本地图书并进行加载
 */
public class MainActivity extends BaseActivity  {
    private final static String TAG="主界面";
    private Presenter_Main presenterMain;
    private long lastBackClick;
    private final String requestUrl="http://www.kkkkknn.com:8005/version/";
    private String ApkDirPath="";
    private String ApkName="";
    private NotificationManager mNotifyManager;
    private Notification.Builder mBuilder=null;
    private GridView mGridView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initView();

        Presenter_Main.getInstance().getBookShelfList(getApplicationContext());

        new Thread(){
            @Override
            public void run() {
                Presenter_Main.getInstance().checkUpdate(getApplicationContext());
            }
        }.start();
    }
    private void initView(){
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        //隐藏APP title
        /*ActionBar actionBar=getSupportActionBar();
        if(actionBar!=null){
            actionBar.setDisplayShowTitleEnabled(false);
        }*/
        mGridView=findViewById(R.id.main_booksGridView);
        final SwipeRefreshLayout swipeRefreshLayout=findViewById(R.id.main_SwipeRefreshLayout);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                Presenter_Main.getInstance().getBookShelfList(getApplicationContext());;
                swipeRefreshLayout.setRefreshing(false);
            }
        });

        //初始化通知栏
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
        mNotifyManager.notify(1,mBuilder.build());
    }


    private void syncBookShelf(ArrayList<BookInfo> object){
        if(mGridView.getAdapter()==null&& (ArrayList<BookInfo>) object !=null){
            BookShelfAdapter mAdapter = new BookShelfAdapter(getApplicationContext(), (ArrayList<BookInfo>) object);
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
        }else if((ArrayList<BookInfo>) object !=null){
            BookShelfAdapter mAdapter = new BookShelfAdapter(getApplicationContext(), (ArrayList<BookInfo>) object);
            mGridView.setAdapter(mAdapter);
        }

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

    @Subscribe(threadMode = ThreadMode.MAIN,sticky = true)
    public void syncProgress(MessageEvent event){
        switch(event.message){
            case DOWNLOAD_PROGRESS:
                mBuilder.setProgress(100,(int)event.value,false);
                mNotifyManager.notify(1,mBuilder.build());
                Log.i(TAG, "syncProgress: "+(int)event.value);
                break;
            case DOWNLOAD_SUCCESS:
                //下载完成后 调用安装APK
                File apk=new File((String) event.value);
                //File apk=new File("/sdcard/app-debug (1).apk");
                Log.i(TAG, "syncProgress: "+(String) event.value);
                if(apk.exists()){
                    installApk(apk);
                }
                break;
            case SYNC_DIALOG:
                showUpdateDialog(event.value);
                break;
            case SYNC_BOOKSHELF:
                Log.i(TAG, "syncProgress: 接收到了");
                ArrayList<BookInfo> list=(ArrayList<BookInfo>) event.value;
                syncBookShelf(list);
                break;
        }

    }

    private void showUpdateDialog(Object object){
        JSONObject jsonObject=(JSONObject)object;
        String code= null;
        String url= null;
        String verStr= null;
        try {
            code = jsonObject.getString("version");
            url = jsonObject.getString("downloadUrl");
            verStr = jsonObject.getString("message");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        if(code!=null&&url!=null&&verStr!=null){
            AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
            builder.setTitle("检测到新版本");
            builder.setMessage(verStr);
            builder.setIcon(R.mipmap.ic_launcher);
            builder.setCancelable(false);            //点击对话框以外的区域是否让对话框消失

            //设置正面按钮
            final String name = code+".apk";
            final String path = Environment.getExternalStorageDirectory().getPath();
            final String finalUrl = url;
            builder.setPositiveButton("立即更新", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                    Log.i(TAG, "onClick: "+name+"||"+path+"||"+finalUrl);
                    Presenter_Main.getInstance().downloadAPK(name,path,finalUrl);
                }
            });
            //设置反面按钮
            builder.setNegativeButton("稍后再说", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
            AlertDialog alertdialog = builder.create();
            alertdialog.show();
        }
    }


    //安装APK
    private void installApk(File apk){
        if (!apk.exists()) {
            Log.i(TAG, "installApk: apk不存在");
            return;
        }

        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        if (apk.getName().endsWith(".apk")) {
            try {
                //兼容7.0
                Uri uri;
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    boolean hasInstallPermission = getPackageManager().canRequestPackageInstalls();
                    if(!hasInstallPermission){
                        startInstallPermissionSettingActivity();
                    }
                    uri = FileProvider.getUriForFile(getApplicationContext(), getPackageName() + ".fileprovider", apk);//通过FileProvider创建一个content类型的Uri
                    intent.setDataAndType(uri, "application/vnd.android.package-archive"); // 对应apk类型

                } else if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.N){// 适配Android 7系统版本
                    uri = FileProvider.getUriForFile(getApplicationContext(), getPackageName() + ".fileprovider", apk);//通过FileProvider创建一个content类型的Uri
                    intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION); //添加这一句表示对目标应用临时授权该Uri所代表的文件
                    intent.setDataAndType(uri, "application/vnd.android.package-archive"); // 对应apk类型

                }else{
                    intent.setDataAndType(Uri.fromFile(apk), "application/vnd.android.package-archive");
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
            //弹出安装界面
                startActivity(intent);
        } else {
            Log.i(TAG, "installApk: 不是apk文件!");
        }

    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void startInstallPermissionSettingActivity() {
        //注意这个是8.0新API
        Intent intent = new Intent(Settings.ACTION_MANAGE_UNKNOWN_APP_SOURCES);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    //初始化保存目录，没有进行创建
    private String getDir(){
        //是否插入SD卡
        String status = Environment.getExternalStorageState();
        boolean isSDCard=status.equals(Environment.DIRECTORY_DOWNLOADS);
        if(isSDCard){
            ApkDirPath=getApplicationContext().getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS).getAbsolutePath();
        }else{
            ApkDirPath=getApplicationContext().getFilesDir().getAbsolutePath();
        }
        File file=new File(ApkDirPath);
        if(!file.exists()){
            file.mkdirs();
        }
        return ApkDirPath;
    }

    @Override
    protected void onRestart() {
        super.onRestart();

        //返回时重新获取书架数据
        Presenter_Main.getInstance().getBookShelfList(getApplicationContext());;
    }
}
