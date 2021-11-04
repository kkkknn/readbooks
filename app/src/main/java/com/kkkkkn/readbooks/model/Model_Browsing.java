package com.kkkkkn.readbooks.model;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Base64;
import android.util.Log;

import androidx.annotation.NonNull;

import com.kkkkkn.readbooks.ServerConfig;
import com.kkkkkn.readbooks.model.clientsetting.SettingConf;
import com.kkkkkn.readbooks.model.entity.ChapterInfo;
import com.kkkkkn.readbooks.util.StringUtil;
import com.kkkkkn.readbooks.util.eventBus.events.BrowsingEvent;
import com.kkkkkn.readbooks.util.network.HttpUtil;

import org.greenrobot.eventbus.Subscribe;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class Model_Browsing extends BaseModel{
    private static final String cachePath="cache";
    private static final String TAG="Model_Browsing";
    @Subscribe
    public void syncProgress(BrowsingEvent event) {
        switch (event.message){
            case GET_BOOK_CHAPTER_LIST:
                getChapterList(event.bookId,
                        event.pageCount,
                        event.pageSize,
                        event.accountId,
                        event.token);
                break;
            case GET_CHAPTER_CONTENT:
                getChapterContent(event.accountId,
                        event.token,
                        event.path);
                break;
        }
    }

    /**
     * 获取指定章节内容
     * @param accountId 用户ID
     * @param token 用户token
     * @param path  章节存储路径
     */
    private void getChapterContent(int accountId, String token,final String path) {
        FormBody.Builder formBody = new FormBody.Builder();
        formBody.add("chapter_path", path);

        Request request = new Request.Builder()
                .url(ServerConfig.IP+ServerConfig.getChapterContent)
                .addHeader("accountId",Integer.toString(accountId))
                .addHeader("token",token)
                .post(formBody.build())//传递请求体
                .build();

        HttpUtil.getInstance().post(request, new Callback() {
            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                String ret_str=response.body().string();
                try {
                    JSONObject ret_json=new JSONObject(ret_str);
                    String ret_code=ret_json.getString("code");
                    if(ret_code.equals("success")){
                        ret_json.put("url",path);
                        getCallBack().onSuccess(1002,ret_json);
                    }else if(ret_code.equals("error")){
                        getCallBack().onError(-1002,(String) ret_json.get("data"));
                    }else{
                        getCallBack().onError(-1,"请求失败");
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                getCallBack().onError(-1,"请求失败");
            }
        });
    }

    /**
     * 获取章节列表
     * @param book_id   图书ID
     * @param page_count    页码
     * @param page_size     每页数量
     * @param account_id    用户ID
     * @param token     用户token
     */
    private void getChapterList(int book_id,int page_count,int page_size,int account_id,String token){
        FormBody.Builder formBody = new FormBody.Builder();
        formBody.add("bookId", Integer.toString(book_id));
        formBody.add("pageCount", Integer.toString(page_count));
        formBody.add("pageSize", Integer.toString(page_size));

        Request request = new Request.Builder()
                .url(ServerConfig.IP+ServerConfig.getChapterList)
                .addHeader("accountId",Integer.toString(account_id))
                .addHeader("token",token)
                .post(formBody.build())//传递请求体
                .build();

        HttpUtil.getInstance().post(request, new Callback() {
            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                String ret_str=response.body().string();
                try {
                    JSONObject ret_json=new JSONObject(ret_str);
                    String ret_code=ret_json.getString("code");
                    String ret_data=ret_json.getString("data");
                    if(ret_code.equals("success")){
                        //解析返回值
                        JSONArray jsonArray=new JSONArray(ret_data);
                        ArrayList<ChapterInfo> arrayList=new ArrayList<>();
                        for (int i = 0; i < jsonArray.length(); i++) {
                            ChapterInfo chapterInfo=new ChapterInfo(jsonArray.get(i));
                            arrayList.add(chapterInfo);
                        }
                        getCallBack().onSuccess(1001,arrayList);
                    }else if(ret_code.equals("error")){
                        getCallBack().onError(-1001,ret_data);
                    }else{
                        getCallBack().onError(-1,"请求失败");
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                getCallBack().onError(-1,"请求失败");
            }
        });

    }

    public int getReadProgress(int book_id,Context context) {
        String path=context.getFilesDir().getAbsolutePath()+cachePath;
        String str=getProgressString(path);
        //未找到进度就返回0
        if(StringUtil.isEmpty(str)) {
            return 0;
        }
        JSONObject jsonObject= null;
        int flag=1;
        try {
            jsonObject = new JSONObject(str);
            flag=jsonObject.getInt(Integer.toString(book_id));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return flag;
    }

    public boolean setReadProgress(int book_id,int progress,Context context){
        String path=context.getFilesDir().getAbsolutePath()+cachePath;
        String str=getProgressString(path);
        JSONObject jsonObject= null;
        try {
            if(StringUtil.isEmpty(str)){
                jsonObject = new JSONObject();
            }else {
                jsonObject = new JSONObject(str);
            }
            jsonObject.put(Integer.toString(book_id),progress);
        } catch (JSONException e) {
            e.printStackTrace();
            return false;
        }
        return setProgressString(jsonObject.toString(),path);
    }

    /**
     * 获取阅读配置，字体，背景等
     * @return
     */
    public SettingConf getReadConfig(Context context){
        SharedPreferences sharedPreferences=context.getSharedPreferences("read_config",Context.MODE_PRIVATE);
        String str=sharedPreferences.getString("SettingConf",null);
        //采用序列化的方式，将SettingConf 对象写入到SharedPreferences 中
        SettingConf settingConf=null;
        if(!StringUtil.isEmpty(str)){
            ByteArrayInputStream byteArrayInputStream=new ByteArrayInputStream(Base64.decode(str,Base64.DEFAULT));
            try {
                ObjectInputStream objectInputStream=new ObjectInputStream(byteArrayInputStream);
                settingConf=(SettingConf) objectInputStream.readObject();
            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
        return settingConf;
    }

    /**
     * 获取阅读配置，字体，背景等
     * @return
     */
    public boolean setReadConfig(Context context,SettingConf conf){
        SharedPreferences sharedPreferences=context.getSharedPreferences("read_config",Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();//获取编辑器
        //采用序列化的方式，将SettingConf 对象写入到SharedPreferences 中
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try {
            ObjectOutputStream oos = new ObjectOutputStream(baos);
            oos.writeObject(conf);//把对象写到流里
            String temp = new String(Base64.encode(baos.toByteArray(), Base64.DEFAULT));
            editor.putString("SettingConf", temp);
            editor.apply();
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    //获取写入的json字符串
    private String getProgressString(String path){
        FileInputStream fileInputStream;
        BufferedReader reader = null;
        StringBuilder content = new StringBuilder();
        try {
            File file=new File(path+"/book_progress");
            if(!file.exists()||!file.isFile()){
                Log.e(TAG, "getProgressString: 未找到章节记录文件");
                return null;
            }
            fileInputStream=new FileInputStream(path+"/book_progress");
            reader = new BufferedReader(new InputStreamReader(fileInputStream));
            String tmp = "";
            while((tmp = reader.readLine()) != null){
                content.append(tmp);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if(reader != null){
                try{
                    reader.close();
                }catch (IOException e){
                    e.printStackTrace();
                }
            }
        }
        return content.toString();
    }

    //写入读书进度的json字符串
    private boolean setProgressString(String str,String path){
        boolean flag=false;
        FileOutputStream fileOutputStream=null;
        try {
            File file=new File(path);
            if(!file.exists())
            {
                file.mkdirs();
            }
            fileOutputStream=new FileOutputStream(path+"/book_progress");
            fileOutputStream.write(str.getBytes("UTF-8"));
            fileOutputStream.flush();
            flag=true;
        } catch (IOException e) {
            e.printStackTrace();
            flag=false;
        }finally {
            if(fileOutputStream!=null){
                try {
                    fileOutputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return flag;
    }


}
