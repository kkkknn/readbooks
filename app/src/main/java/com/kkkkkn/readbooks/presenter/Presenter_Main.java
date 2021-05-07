package com.kkkkkn.readbooks.presenter;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.util.Log;

import com.kkkkkn.readbooks.model.download.DownloadListener;
import com.kkkkkn.readbooks.model.download.DownloadUtil;
import com.kkkkkn.readbooks.model.entity.BookInfo;
import com.kkkkkn.readbooks.model.sqlite.SqlBookUtil;
import com.kkkkkn.readbooks.util.eventBus.EventMessage;
import com.kkkkkn.readbooks.util.eventBus.MessageEvent;

import org.greenrobot.eventbus.EventBus;
import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import org.json.JSONObject;

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

public class Presenter_Main {
    private static String TAG="Presenter_Main";
    private volatile static Presenter_Main presenter_main=null;

    private Presenter_Main() {
    }

    public static Presenter_Main getInstance(){
        if(presenter_main==null){
            synchronized (Presenter_Browsing.class){
                if(presenter_main==null){
                    presenter_main=new Presenter_Main();
                }
            }
        }
        return presenter_main;
    }

    //获取书架信息，并显示到页面上 通过eventbus发送
    public void getBookShelfList(Context context){
        SqlBookUtil sqlBookUtil=SqlBookUtil.getInstance(context).initDataBase();
        ArrayList<BookInfo> list=sqlBookUtil.getEnjoyBook();
        //发送粘性事件，防止接收不到消息
        EventBus.getDefault().postSticky(new MessageEvent(EventMessage.SYNC_BOOKSHELF,list));
    }

    //获取APP更新信息，通过eventbus发送
    public void checkUpdate(Context context){
        //获取当前应用版本号
        try {
            PackageManager manager = context.getPackageManager();
            PackageInfo info = manager.getPackageInfo(context.getPackageName(), 0);
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
                    EventBus.getDefault().post(new MessageEvent(EventMessage.SYNC_DIALOG,jsonObject));

                }
            }
        } catch (IOException | JSONException | PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
    }

    //更新APK
    public void downloadAPK(final String name,final String path,final String url){
        DownloadUtil.downloadFile(name,url,path,new DownloadListener() {
            @Override
            public void onSuccess() {
                EventBus.getDefault().post(new MessageEvent(EventMessage.DOWNLOAD_SUCCESS,path+"/"+name));

            }

            @Override
            public void onProgress(int i) {
                EventBus.getDefault().post(new MessageEvent(EventMessage.DOWNLOAD_PROGRESS,(int)i));
            }

            @Override
            public void onError(Exception e) {
                EventBus.getDefault().post(new MessageEvent(EventMessage.DOWNLOAD_ERROR,e));

            }
        });
    }


}
