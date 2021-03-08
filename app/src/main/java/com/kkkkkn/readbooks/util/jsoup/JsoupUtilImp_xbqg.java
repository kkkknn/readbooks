package com.kkkkkn.readbooks.util.jsoup;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.safety.Whitelist;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.net.SocketTimeoutException;
import java.util.Arrays;
import java.util.LinkedList;

public class JsoupUtilImp_xbqg implements JsoupUtil {
    //来源网址 http://m.paoshuzw.com/
    private final static String URL="http://m.paoshuzw.com";
    private final static int TIMEOUT =8000;

    @Override
    public String searchBook(String str) throws IOException, JSONException {
        Document document=Jsoup.connect(URL+"/modules/article/waps.php")
                .data("searchkey",str)
                .ignoreContentType(true)
                .timeout(TIMEOUT)
                .post();
        JSONObject retObject=new JSONObject();
        JSONArray jsonArray=new JSONArray();
        Elements elements=document.body().select(".block");

        for (int i = 0; i < elements.size(); i++) {
            Element element=elements.get(i);
            String bookImgUrl=element.select(".block_img a img").attr("src");
            String bookName=element.select(".block_txt h2 a").text();
            String bookUrl=element.select(".block_txt h2 a").attr("href");
            String bookAuthor=element.select(".block_txt p").get(3).text().split("：")[1];
            String newChapterName=element.select(".block_txt p").last().select("a").text();
            JSONObject jsonObject=new JSONObject();
            jsonObject.put("authorName",bookAuthor);
            jsonObject.put("bookName",bookName);
            jsonObject.put("bookUrl",bookUrl);
            jsonObject.put("bookImgUrl",bookImgUrl);
            jsonObject.put("newChapterName",newChapterName);
            jsonArray.put(jsonObject);
        }
        retObject.put("data",jsonArray);
        return retObject.toString();
    }

    @Override
    public String getBookInfo(String book_url) throws IOException, JSONException {
        Document document=Jsoup.connect(URL+book_url)
                .timeout(TIMEOUT)
                .ignoreContentType(true)
                .get();
        JSONObject retObject=new JSONObject();
        String name=document.body().select(".block_txt2 h2>a").text();
        retObject.put("bookName",name);
        retObject.put("bookUrl",book_url);
        Elements elements=document.body().select(".block_txt2 p");
        String author_name=elements.get(2).text().split("：")[1];
        String update_time=elements.get(5).text().split("：")[1];
        String latest_chapter=elements.get(3).select("a").text();
        String img_url=document.body().select(".block_img2 img").attr("src");
        retObject.put("bookImgUrl",img_url);
        retObject.put("authorName",author_name);
        retObject.put("updateTime",update_time);
        retObject.put("latestChapter",latest_chapter);
        String about=document.body().select(".intro_info").html().split("<br>")[0];
        retObject.put("bookAbout",about);

        //循环添加所有分页地址
        Elements elementsPages=document.body().select(".listpage .middle select option");
        JSONArray jsonArray=new JSONArray();
        for (int i=0;i<elementsPages.size();i++) {
            String url=elementsPages.get(i).attr("value");
            JSONObject object=new JSONObject();
            object.put("chapterPage",i);
            object.put("chapterPageUrl",url);
            jsonArray.put(i,object);
        }
        retObject.put("chapterPages",jsonArray);
        return retObject.toString();
    }

    @Override
    public JSONObject getChapterContent(String chapter_url) throws IOException, JSONException {
        Document document=Jsoup.connect(URL+chapter_url)
                .timeout(TIMEOUT)
                .ignoreContentType(true)
                .get();
        JSONObject retObject=new JSONObject();
        String chapter_name=document.body().select("#_bqgmb_h1").text();
        retObject.put("chapterName",chapter_name);
        //过滤字符串
        String contentHtml=document.body().select("#nr1").html();
        //String valStr=Jsoup.clean(contentHtml,"", Whitelist.none(), new Document.OutputSettings().prettyPrint(false));
        String contentStr=contentHtml.replaceAll("&nbsp;&nbsp;&nbsp;&nbsp;","    ");
        String[] strArr=contentStr.split("<br>");
        String[] result1=new String[strArr.length-2];
        System.arraycopy(strArr,1,result1,0,result1.length);
        LinkedList<String> linkedList = new LinkedList<>(Arrays.asList(result1));

        //判断当前页和总页数
        while (true){
            String url=document.body().select("#pb_next").attr("href");
            String[] arr=url.split("_\\d*?_\\d*?\\.html*?");
            if(arr.length==1){
                break;
            }
            Log.i("123", "getChapterContent: "+Arrays.toString(arr));;
            document=Jsoup.connect(URL+url)
                    .timeout(TIMEOUT)
                    .ignoreContentType(true)
                    .get();

            contentHtml=document.body().select("#nr1").html();
            //valStr=Jsoup.clean(contentHtml,"", Whitelist.none(), new Document.OutputSettings().prettyPrint(false));
            contentStr=contentHtml.replaceAll("&nbsp;&nbsp;&nbsp;&nbsp;","    ");
            //strArr=valStr.split("&nbsp;&nbsp;&nbsp;&nbsp;");
            strArr=contentStr.split("<br>");
            String[] result2=new String[strArr.length-2];
            System.arraycopy(strArr,1,result2,0,result2.length);
            linkedList.addAll(Arrays.asList(result2));
        }
        retObject.put("chapterContent",linkedList.toArray(new String[0]));
        Log.i("TAG", "getChapterContent: "+linkedList.size());
        return retObject;
    }

    @Override
    public String getBookChapterList(String url) throws IOException, JSONException {
        Document document = Jsoup.connect(URL+url)
                    .timeout(TIMEOUT)
                    .ignoreContentType(true)
                    .get();
        JSONObject retObject=new JSONObject();
        JSONArray jsonArray=new JSONArray();
        Elements lists=document.body().select(".cover .chapter").last().select("li a");
        for(int i=0;i<lists.size();i++){
            JSONObject object=new JSONObject();
            String chapterName=lists.get(i).text();
            String chapterUrl=lists.get(i).attr("href");
            object.put("chapterName",chapterName);
            object.put("chapterUrl",chapterUrl);
            jsonArray.put(i,object);
        }
        retObject.put("chapters",jsonArray);
        return retObject.toString();
    }
}
