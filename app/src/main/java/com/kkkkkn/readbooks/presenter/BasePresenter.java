package com.kkkkkn.readbooks.presenter;

import android.content.Context;
import android.content.SharedPreferences;

import com.kkkkkn.readbooks.model.BaseModel;
import com.kkkkkn.readbooks.model.entity.AccountInfo;

public class BasePresenter {
    private Context context;
    private BaseModel baseModel;

    public BasePresenter(Context context,BaseModel baseModel) {
        this.context = context;
        this.baseModel=baseModel;
    }

    public Context getContext(){
        return context;
    }

    public void init(){
        baseModel.register();
    }

    public void release(){
        baseModel.unregister();
    }

    public BaseModel getBaseModel(){
        return baseModel;
    }

    public AccountInfo getAccountCache(){
        SharedPreferences sharedPreferences=this.context.getSharedPreferences("AccountInfo",Context.MODE_PRIVATE);
        AccountInfo accountInfo=new AccountInfo();
        accountInfo.setAccountId(sharedPreferences.getInt("account_id",-1));
        accountInfo.setAccountToken(sharedPreferences.getString("accountToken",""));
        accountInfo.setAccountName(sharedPreferences.getString("accountName",""));
        accountInfo.setAccountPassword(sharedPreferences.getString("accountPassword",""));
        return accountInfo;
    }

    public void setAccountCache(String name,String password){
        if(name==null||password==null||name.isEmpty()||password.isEmpty())return;
        SharedPreferences.Editor editor=this.context.getSharedPreferences("AccountInfo",Context.MODE_PRIVATE).edit();
        editor.putString("accountName",name);
        editor.putString("accountPassword",password);
        editor.apply();

    }

    public void setTokenCache(int id,String token){
        if(id<=0||token==null||token.isEmpty())return;
        SharedPreferences.Editor editor=this.context.getSharedPreferences("AccountInfo",Context.MODE_PRIVATE).edit();
        editor.putInt("account_id",id);
        editor.putString("accountToken",token);
        editor.apply();
    }
}
