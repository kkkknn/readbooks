package com.kkkkkn.readbooks.model;

import android.util.Log;

import androidx.annotation.NonNull;

import com.kkkkkn.readbooks.util.ServerConfig;
import com.kkkkkn.readbooks.model.entity.BookInfo;
import com.kkkkkn.readbooks.util.eventBus.events.MainEvent;
import com.kkkkkn.readbooks.util.network.HttpUtil;
import com.kkkkkn.readbooks.util.StringUtil;

import org.greenrobot.eventbus.Subscribe;
import org.json.JSONArray;
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
import okhttp3.FormBody;
import okhttp3.Request;
import okhttp3.Response;

public class Model_Main extends BaseModel {
    private final static String TAG="Model_Main";


    @Subscribe
    public void syncProgress(MainEvent event) {
        switch (event.message){
            case SYNC_BOOKSHELF:
                //获取书架
                getBookShelf(event.accountId,event.token);
                break;
            case GET_VERSION:
                //获取版本号
                getVersion(event.accountId,event.token);
                break;
            case DOWNLOAD_APK:
                downloadAPK(event.name,event.path,event.url,event.accountId,event.token);
                break;
        }
    }

    private void downloadAPK(final String name,final String path,String url,int id,String token) {
        if(StringUtil.isEmpty(name)||StringUtil.isEmpty(path)||StringUtil.isEmpty(url)){
            Log.i(TAG, "downloadAPK: 参数错误");
            return;
        }
        Request request = new Request.Builder()
                .addHeader("accountId",Integer.toString(id))
                .addHeader("token",token)
                .url(ServerConfig.IP+ServerConfig.downloadAPK+"?urlPath="+url)
                .get()
                .build();
        HttpUtil.getInstance().get(request, new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                getCallBack().onError(-3001,"下载失败");
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                InputStream is = null;
                byte[] buf = new byte[2048];
                int len = 0;
                FileOutputStream fos = null;
                // 储存下载文件的目录
                File dir = new File(path);
                if (!dir.exists()) {
                    dir.mkdirs();
                }
                File file = new File(dir, name);
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
                            getCallBack().onSuccess(3002, progress);
                        }
                    }
                    fos.flush();
                    //下载完成
                    getCallBack().onSuccess(3001, file.getAbsolutePath());
                } catch (Exception e) {
                    e.printStackTrace();
                    getCallBack().onError(-3001, e.toString());
                }finally {
                    try {
                        if (fos != null) {
                            fos.close();
                        }
                        if (is != null) {
                            is.close();
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                        getCallBack().onError(-3001, e.toString());
                    }

                }

            }
        });
    }

    private void getVersion(int id, String token){
        Request request = new Request.Builder()
                .addHeader("accountId",Integer.toString(id))
                .addHeader("token",token)
                .url(ServerConfig.IP+ServerConfig.getVersionInfo)
                .get()
                .build();
        HttpUtil.getInstance().post(request, new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                getCallBack().onError(-2001,"访问出错");
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                //解析返回值
                String ret_str=response.body().string();
                if(!StringUtil.isEmpty(ret_str)){
                    try {
                        JSONObject jsonObject = new JSONObject(ret_str);
                        String code_str = jsonObject.getString("code");
                        String data_str = jsonObject.getString("data");
                        if (code_str.equals("success")) {
                            getCallBack().onSuccess(2001, data_str);
                        } else if (code_str.equals("error")) {
                            getCallBack().onError(-2002, data_str);
                        }
                    }catch (JSONException e) {
                        e.printStackTrace();
                    }

                }else {
                    getCallBack().onError(-2001,"解析失败");
                }

            }
        });
    }

    private void getBookShelf(int id,String token){
        //okhttp post请求网络
        FormBody.Builder formBody = new FormBody.Builder();
        formBody.add("accountId", Integer.toString(id));
        Request request = new Request.Builder()
                .addHeader("accountId",Integer.toString(id))
                .addHeader("token",token)
                .url(ServerConfig.IP+ServerConfig.getFavoriteBook)
                .post(formBody.build())//传递请求体
                .build();
        HttpUtil.getInstance().post(request, new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                getCallBack().onError(-1001,"访问出错");
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                //解析返回值
                String ret_str=response.body().string();
                if(!StringUtil.isEmpty(ret_str)){
                    try {
                        JSONObject jsonObject=new JSONObject(ret_str);
                        String code_str=jsonObject.getString("code");
                        String data_str=jsonObject.getString("data");
                        if(code_str.equals("success")){
                            JSONArray jsonArray=new JSONArray(data_str);
                            ArrayList<BookInfo> book_shelf=new ArrayList<>();
                            for (int j = 0; j < jsonArray.length(); j++) {
                                JSONObject object=(JSONObject) jsonArray.get(j);
                                BookInfo bookInfo=BookInfo.changeObject(object);
                                if(!bookInfo.isEmpty()){
                                    book_shelf.add(bookInfo);
                                    Log.i("TAG", "onResponse: "+bookInfo.getBookName());
                                }
                            }
                            getCallBack().onSuccess(1001,book_shelf);
                        }else if(code_str.equals("error")){
                            getCallBack().onError(-1002,data_str);
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                        getCallBack().onError(-1001,"解析失败");
                    }
                }

            }
        });
    }


}
