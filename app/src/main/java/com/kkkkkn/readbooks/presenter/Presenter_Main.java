package com.kkkkkn.readbooks.presenter;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.util.Log;

import com.kkkkkn.readbooks.model.BaseModel;
import com.kkkkkn.readbooks.model.Model_Main;
import com.kkkkkn.readbooks.model.entity.AccountInfo;
import com.kkkkkn.readbooks.model.entity.BookInfo;
import com.kkkkkn.readbooks.model.network.DownloadListener;
import com.kkkkkn.readbooks.model.network.DownloadUtil;
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
        //获取缓存内的用户账户数据
        AccountInfo info=getAccountCache();
        //请求图书列表
        EventBus.getDefault().post(new MessageEvent(EventMessage.SYNC_BOOKSHELF,info));
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


    @Override
    public void onSuccess(int type, Object object) {
        switch (type){
            case 1:
                mainActivityView.updateBookShelf((ArrayList<BookInfo>) object);
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
