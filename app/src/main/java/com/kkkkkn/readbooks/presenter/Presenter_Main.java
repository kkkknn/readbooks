package com.kkkkkn.readbooks.presenter;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.util.Log;

import com.kkkkkn.readbooks.model.Model_Main;
import com.kkkkkn.readbooks.model.entity.AccountInfo;
import com.kkkkkn.readbooks.model.network.DownloadListener;
import com.kkkkkn.readbooks.model.network.DownloadUtil;
import com.kkkkkn.readbooks.model.entity.BookInfo;
import com.kkkkkn.readbooks.model.scrap.sqlite.SqlBookUtil;
import com.kkkkkn.readbooks.util.eventBus.EventMessage;
import com.kkkkkn.readbooks.util.eventBus.MessageEvent;
import com.kkkkkn.readbooks.view.view.MainView;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Objects;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class Presenter_Main extends BasePresenter {
    private static String TAG="Presenter_Main";
    private MainView mainView;

    public Presenter_Main(Context context,MainView view) {
        super(context,new Model_Main());
        this.mainView=view;
    }

    /**
     * 获取当前用户token及用户id
     * @return 用户对象
     */
    public AccountInfo getToken(){
        SharedPreferences sharedPreferences=getContext().getSharedPreferences("AccountInfo",Context.MODE_PRIVATE);
        AccountInfo accountInfo=new AccountInfo();
        accountInfo.setAccount_id(sharedPreferences.getInt("account_id",-1));
        accountInfo.setAccount_token(sharedPreferences.getString("account_token",""));
        return accountInfo;
    }

    /**
     * 获取书架信息，并显示到页面上 通过eventbus发送
     *
     */
    public void getBookShelfList(){
        SqlBookUtil sqlBookUtil=SqlBookUtil.getInstance(getContext()).initDataBase();
        ArrayList<BookInfo> list=sqlBookUtil.getEnjoyBook();
        //发送粘性事件，防止接收不到消息
        //EventBus.getDefault().postSticky(new MessageEvent(EventMessage.SYNC_BOOKSHELF,list));
    }

    /**
     * 获取APP更新信息，通过eventbus发送
     *
     */
    public void checkUpdate(){
        //获取当前应用版本号
        try {
            PackageManager manager = getContext().getPackageManager();
            PackageInfo info = manager.getPackageInfo(getContext().getPackageName(), 0);
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
                    //EventBus.getDefault().postSticky(new MessageEvent(EventMessage.SYNC_DIALOG,jsonObject));

                }
            }
        } catch (IOException | JSONException | PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
    }

    /**
     * 弹窗显示更新APK
     */
    public void updateAPK(final String name,final String path,final String url){
        DownloadUtil.downloadFile(name,url,path,new DownloadListener() {
            @Override
            public void onSuccess() {
                //EventBus.getDefault().post(new MessageEvent(EventMessage.DOWNLOAD_SUCCESS,path+"/"+name));

            }

            @Override
            public void onProgress(int i) {
                //EventBus.getDefault().post(new MessageEvent(EventMessage.DOWNLOAD_PROGRESS,(int)i));
            }

            @Override
            public void onError(Exception e) {
                //EventBus.getDefault().post(new MessageEvent(EventMessage.DOWNLOAD_ERROR,e));

            }
        });
    }


}
