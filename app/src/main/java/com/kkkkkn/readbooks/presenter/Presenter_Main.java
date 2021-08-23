package com.kkkkkn.readbooks.presenter;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.util.Log;

import com.kkkkkn.readbooks.model.BaseModel;
import com.kkkkkn.readbooks.model.Model_Main;
import com.kkkkkn.readbooks.model.entity.AccountInfo;
import com.kkkkkn.readbooks.model.entity.BookInfo;
import com.kkkkkn.readbooks.util.network.DownloadListener;
import com.kkkkkn.readbooks.util.network.DownloadUtil;
import com.kkkkkn.readbooks.util.eventBus.EventMessage;
import com.kkkkkn.readbooks.util.eventBus.MessageEvent;
import com.kkkkkn.readbooks.view.view.MainActivityView;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Objects;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class Presenter_Main extends BasePresenter implements BaseModel.CallBack {
    private static String TAG="Presenter_Main";
    private MainActivityView mainActivityView;
    private Model_Main model_main;

    public Presenter_Main(Context context, MainActivityView view) {
        super(context,new Model_Main());
        this.mainActivityView =view;
        this.model_main=(Model_Main) getBaseModel();
        this.model_main.setCallback(this);
    }

    /**
     * 获取当前用户token及用户id
     * @return 用户对象
     */
    public AccountInfo getToken(){
        return getAccountCache();
    }

    /**
     * 获取书架信息，并显示到页面上 通过eventbus发送
     *
     */
    public void getBookShelfList(){

        try {
            JSONObject jsonObject=new JSONObject();
            AccountInfo accountInfo=getAccountCache();
            jsonObject.put("accountId",accountInfo.getAccount_id());
            jsonObject.put("token",accountInfo.getAccount_token());
            //请求图书列表
            EventBus.getDefault().post(new MessageEvent(EventMessage.SYNC_BOOKSHELF,jsonObject));

        } catch (JSONException e) {
            e.printStackTrace();
            onError(-1,"获取图书列表失败");
        }
    }

    /**
     *获取当前应用版本号
     * @return 版本号字符串 "1.0.1"
     */
    private String getLocalVersion(){
        PackageManager manager = getContext().getPackageManager();
        PackageInfo info = null;
        String version="";
        try {
            info = manager.getPackageInfo(getContext().getPackageName(), 0);
            version = info.versionName;
            Log.i(TAG, "checkUpdate: "+version);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return version;
    }

    /**
     * 获取APP更新信息，通过eventbus发送
     *
     */
    public void checkUpdate(){
        try {
            JSONObject jsonObject=new JSONObject();
            AccountInfo accountInfo=getAccountCache();
            jsonObject.put("accountId",accountInfo.getAccount_id());
            jsonObject.put("token",accountInfo.getAccount_token());
            //在线获取最新版本号
            EventBus.getDefault().post(new MessageEvent(EventMessage.GET_VERSION,jsonObject));

        } catch (JSONException e) {
            e.printStackTrace();
            onError(-1,"检查更新失败");
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


    @Override
    public void onSuccess(int type, Object object) {
        switch (type){
            case 1:
                mainActivityView.syncBookShelf((ArrayList<BookInfo>) object);
                break;
            default:
                break;
        }
    }

    @Override
    public void onError(int type, Object object) {
        switch (type){
            case -1:
                Log.i(TAG, (String) object);
                break;
            case -2:
                String str=(String) object;
                if(str.equals("令牌验证失败，请重新尝试")){
                    mainActivityView.toLoginActivity();
                }
                break;
            default:
                break;
        }
    }
}
