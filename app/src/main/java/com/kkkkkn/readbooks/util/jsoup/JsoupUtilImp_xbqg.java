package com.kkkkkn.readbooks.util.jsoup;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;

public class JsoupUtilImp_xbqg implements JsoupUtil {
    //来源网址
    private final static String URL="http://www.xbiquge.la";

    @Override
    public String searchBook(String str) throws IOException, JSONException {
        Document document=Jsoup.connect(URL+"/modules/article/waps.php")
                .data("searchkey",str)
                .ignoreContentType(true)
                .timeout(8000)
                .post();
        JSONObject retObject=new JSONObject();
        JSONArray jsonArray=new JSONArray();
        Elements elements=document.body().select(".grid>tbody tr");
        Log.i("TAG", "searchBook: "+elements.size());
        for (int i = 1; i < elements.size(); i++) {
            Element element=elements.get(i);
            Elements items=element.select("td");
            JSONObject jsonObject=new JSONObject();
            jsonObject.put("authorName",items.get(2).text());
            jsonObject.put("bookName",items.get(0).select("a").text());
            jsonObject.put("bookUrl",items.get(0).select("a").attr("href"));
            jsonArray.put(jsonObject);
        }
        retObject.put("data",jsonArray);
        return retObject.toString();
    }

    @Override
    public String getBookInfo(String book_url) throws IOException, JSONException {
        Document document=Jsoup.connect(book_url)
                .timeout(8000)
                .ignoreContentType(true)
                .get();
        JSONObject retObject=new JSONObject();
        String name=document.body().select("#info h1").text();
        retObject.put("bookName",name);
        retObject.put("bookUrl",book_url);
        Elements elements=document.body().select("#info p");
        String author_name=elements.get(0).text().split("：")[1];
        String update_time=elements.get(2).text().split("：")[1];
        String latest_chapter=elements.get(3).select("a").text();
        String img_url=document.body().select("#fmimg img").attr("src");
        retObject.put("bookImgUrl",img_url);
        retObject.put("authorName",author_name);
        retObject.put("updateTime",update_time);
        retObject.put("latestChapter",latest_chapter);
        //循环添加所有章节
        Elements elementsChapters=document.body().select("#list>dl>dd");
        JSONArray jsonArray=new JSONArray();
        for (int i=0;i<elementsChapters.size();i++) {
            Elements item=elementsChapters.get(i).getElementsByTag("a");
            String chapterUrl=item.attr("href");
            String chapterName=item.text();
            JSONObject object=new JSONObject();
            object.put("chapterName",chapterName);
            object.put("chapterUrl",chapterUrl);
            jsonArray.put(i,object);

        }
        retObject.put("chapterInfo",jsonArray);
        return retObject.toString();
    }

    @Override
    public String getChapterContent(String chapter_url) throws IOException, JSONException {
        Document document=Jsoup.connect(URL+chapter_url)
                .timeout(8000)
                .ignoreContentType(true)
                .get();
        JSONObject retObject=new JSONObject();
        String chapter_name=document.body().select(".bookname>h1").text();
        retObject.put("chapterName",chapter_name);
        String contentStr=document.body().select("#content").text();
        String adStr=document.body().select("#content>p").text();
        String valueStr=contentStr.replace(adStr,"");
        retObject.put("chapterContent",valueStr);
        return retObject.toString();
    }
}
