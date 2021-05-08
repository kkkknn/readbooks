package com.kkkkkn.readbooks.model.network;

import android.util.Log;

import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Objects;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class DownloadUtil {
    private static final String TAG="DownloadUtil";

    public static void downloadFile(final String fileName,final String fileUrl,final String filePath,final DownloadListener listener){
        if(fileUrl==null||fileUrl.isEmpty()){
            Log.i(TAG, "downloadAPK: 下载链接为空");
            return;
        }
        //okhttp 进行下载，并通过eventbus发送消息通知UI更新通知栏下载进度
        OkHttpClient client=new OkHttpClient();
        Request down_request = new Request.Builder()
                .url(fileUrl)
                .build();
        //拆分APK名字 http://www.aaa.com:8005/download/xxx.apk
        final String ApkName=fileUrl.split("download")[1].replace("/","");
        Log.i(TAG, "onMessageEvent: "+ApkName);
        client.newCall(down_request).enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                Log.i(TAG, "onFailure: 下载失败");
                listener.onError(e);
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                InputStream is = null;
                byte[] buf = new byte[2048];
                int len = 0;
                FileOutputStream fos = null;
                // 储存下载文件的目录
                File dir = new File(filePath);
                if (!dir.exists()) {
                    dir.mkdirs();
                }
                File file = new File(dir, fileName);
                try {
                    is = Objects.requireNonNull(response.body()).byteStream();
                    long total = Objects.requireNonNull(response.body()).contentLength();
                    fos = new FileOutputStream(file);
                    long sum = 0;
                    int progress=0;
                    while ((len = is.read(buf)) != -1) {
                        fos.write(buf, 0, len);
                        sum += len;
                        int count = (int) (sum * 1.0f / total * 100);
                        //下载中更新进度条 eventbus 通知
                        if(progress<count){
                            progress=count;
                            listener.onProgress(progress);
                        }
                    }
                    fos.flush();
                    //下载完成
                    listener.onSuccess();
                    Log.i(TAG, "onResponse: DOWNLOAD_SUCCESS");
                } catch (Exception e) {
                    listener.onError(e);
                }finally {
                    try {
                        if (is != null) {
                            is.close();
                        }
                        if (fos != null) {
                            fos.close();
                        }
                    } catch (IOException e) {
                        listener.onError(e);
                    }

                }

            }
        });
    }
}
