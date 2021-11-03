package com.kkkkkn.readbooks.presenter;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.util.Log;

import com.kkkkkn.readbooks.model.BaseModel;
import com.kkkkkn.readbooks.model.Model_Main;
import com.kkkkkn.readbooks.model.entity.AccountInfo;
import com.kkkkkn.readbooks.model.entity.BookInfo;
import com.kkkkkn.readbooks.util.eventBus.events.MainEvent;
import com.kkkkkn.readbooks.util.eventBus.EventMessage;
import com.kkkkkn.readbooks.view.view.MainActivityView;

import org.greenrobot.eventbus.EventBus;
import java.util.ArrayList;

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
        AccountInfo accountInfo=getAccountCache();
        if(!accountInfo.isHasToken()){
            onError(-2,"获取用户信息失败");
            return;
        }
        //请求图书列表
        EventBus.getDefault().post(
                new MainEvent(
                        EventMessage.SYNC_BOOKSHELF,
                        accountInfo.getAccount_id(),
                        accountInfo.getAccount_token()));
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
        AccountInfo accountInfo=getAccountCache();
        if(!accountInfo.isHasToken()){
            onError(-2,"获取用户信息失败");
            return;
        }
        //在线获取最新版本号
        EventBus.getDefault().post(
                new MainEvent(
                        EventMessage.GET_VERSION,
                        accountInfo.getAccount_id(),
                        accountInfo.getAccount_token()));


    }

    /**
     * 弹窗显示更新APK
     */
    public void updateAPK(final String name,final String path,final String url){
        AccountInfo accountInfo=getAccountCache();
        if(!accountInfo.isHasToken()){
            onError(-2,"获取用户信息失败");
            return;
        }
        //在线获取最新版本号
        EventBus.getDefault().post(
                new MainEvent(
                        EventMessage.DOWNLOAD_APK,
                        name,
                        path,
                        url,
                        accountInfo.getAccount_id(),
                        accountInfo.getAccount_token()));


    }


    @Override
    public void onSuccess(int type, Object object) {
        switch (type){
            case 1001:
                mainActivityView.syncBookShelf((ArrayList<BookInfo>) object);
                break;
            case 2001:
                mainActivityView.showUpdateDialog((String) object);
                break;
            case 3001:
                Log.i(TAG, "onSuccess: "+object);
                break;
            case 3002:
                Log.i(TAG, "onSuccess: 进度 "+object);
            default:
                break;
        }
    }

    @Override
    public void onError(int type, Object object) {
        switch (type){
            case -1001:
            case -2001:
            case -2002:
            case -3001:
                Log.i(TAG, (String) object);
                break;
            case -1002:
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
