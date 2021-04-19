package com.kkkkkn.readbooks.service;

import android.app.Service;
import android.content.Intent;
import android.os.Environment;
import android.os.IBinder;

import java.io.File;

public class DownAPKService extends Service {

    private String ApkUrl="";
    private String ApkDirPath="";
    public DownAPKService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    //初始化保存目录，没有进行创建
    private void initDir(String path){
        //是否插入SD卡
        String status = Environment.getExternalStorageState();
        boolean isSDCard=status.equals(Environment.DIRECTORY_DOWNLOADS);
        if(isSDCard){
            ApkDirPath=Environment.getExternalStorageDirectory().getAbsolutePath()+path;
        }else{
            ApkDirPath=getApplicationContext().getFilesDir().getAbsolutePath()+path;
        }
        File file=new File(ApkDirPath);
        if(!file.exists()){
            file.mkdirs();
        }
    }
}