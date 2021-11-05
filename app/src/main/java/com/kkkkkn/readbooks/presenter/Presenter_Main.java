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
import org.json.JSONException;
import org.json.JSONObject;

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
     * 检查当前应用版本号
     * @return boolean 是否需要更新
     */
    private boolean checkVersion(String jsonStr){
        PackageManager manager = getContext().getPackageManager();
        try {
            JSONObject jsonObject=new JSONObject(jsonStr);
            String online_version=jsonObject.getString("version");
            PackageInfo info = manager.getPackageInfo(getContext().getPackageName(), 0);
            if(online_version.compareTo(info.versionName)>0){
                return true;
            }
        } catch (PackageManager.NameNotFoundException | JSONException e) {
            e.printStackTrace();
        }
        return false;
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
        //todo 开启前台服务，显示下载的进度

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
                if(checkVersion((String)object)){
                    JSONObject jsonObject= null;
                    String code= null;
                    String url= null;
                    String verStr= null;
                    try {
                        jsonObject = new JSONObject((String) object);
                        code = jsonObject.getString("version");
                        url = jsonObject.getString("downloadUrl");
                        verStr = jsonObject.getString("message");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    mainActivityView.showUpdateDialog(verStr,url,code);
                }
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
