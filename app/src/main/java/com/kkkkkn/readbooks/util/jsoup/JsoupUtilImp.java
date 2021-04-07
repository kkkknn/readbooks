package com.kkkkkn.readbooks.util.jsoup;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

public class JsoupUtilImp implements JsoupUtil {
    private static volatile JsoupUtilImp jsoupUtilImp;
    private JsoupUtil jsoupUtil;
    private static JsoupUtilImp_xbqg jsoupUtilImpXbqg;

    private JsoupUtilImp() {
        //初始化库并确认是否可用
        jsoupUtilImpXbqg=new JsoupUtilImp_xbqg();
    }

    public static JsoupUtilImp getInstance(){
        if(jsoupUtilImp==null){
            synchronized (JsoupUtilImp.class){
                if(jsoupUtilImp==null){
                    jsoupUtilImp=new JsoupUtilImp();
                }
            }
        }
        return jsoupUtilImp;
    }
    public JsoupUtilImp setSource(int type){
        switch (type){
            case 1:
                jsoupUtil=jsoupUtilImpXbqg;
                break;
            case 2:
                break;
            default:
                jsoupUtil=jsoupUtilImpXbqg;
                break;
        }
        return jsoupUtilImp;
    }


    @Override
    public String searchBook(String str) throws IOException, JSONException {
        return jsoupUtil.searchBook(str);
    }

    @Override
    public String getBookInfo(String book_url) throws IOException, JSONException {
        return jsoupUtil.getBookInfo(book_url);
    }

    @Override
    public JSONObject getChapterContent(String chapter_url) throws IOException, JSONException {
        return jsoupUtil.getChapterContent(chapter_url);
    }

    @Override
    public String getBookChapterList(String url) throws IOException, JSONException {
        return jsoupUtil.getBookChapterList(url);
    }
}
