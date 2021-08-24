package com.kkkkkn.readbooks.model;

import android.content.Context;

import androidx.annotation.NonNull;

import com.kkkkkn.readbooks.ServerConfig;
import com.kkkkkn.readbooks.model.entity.ChapterInfo;
import com.kkkkkn.readbooks.util.eventBus.events.BrowsingEvent;
import com.kkkkkn.readbooks.util.network.HttpUtil;

import org.greenrobot.eventbus.Subscribe;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.Request;
import okhttp3.Response;

public class Model_Browsing extends BaseModel{

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
        }
    }

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
        String str=getProgressString(context);
        JSONObject jsonObject= null;
        int flag=0;
        try {
            jsonObject = new JSONObject(str);
            flag=jsonObject.getInt(Integer.toString(book_id));
        } catch (JSONException e) {
            e.printStackTrace();
            flag=-1;
        }
        return flag;
    }

    public boolean setReadProgress(int book_id,int progress,Context context){
        String str=getProgressString(context);
        JSONObject jsonObject= null;
        try {
            jsonObject = new JSONObject(str);
            jsonObject.put(Integer.toString(book_id),progress);
        } catch (JSONException e) {
            e.printStackTrace();
            return false;
        }
        return setProgressString(jsonObject.toString(),context);
    }

    //获取写入的json字符串
    private String getProgressString(Context context){
        FileInputStream fileInputStream;
        BufferedReader reader = null;
        StringBuilder content = new StringBuilder();
        try {
            fileInputStream=context.openFileInput("book_progress");
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
    private boolean setProgressString(String str,Context context){
        boolean flag=false;
        FileOutputStream fileOutputStream=null;
        try {
            fileOutputStream=context.openFileOutput("book_progress",Context.MODE_PRIVATE);
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
