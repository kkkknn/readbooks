package com.kkkkkn.readbooks.util;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.widget.TextView;

import com.kkkkkn.readbooks.entity.BookInfo;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.Objects;

import okhttp3.Request;
import okhttp3.Response;

public class BookSourceByBQG1 implements BookSourceImp,NetworkUtilListener {
    private final static String URL="https://www.biqudu.net";
    private int netFlag=-1;
    private NetworkUtil networkUtil;
    private BookSourceListener bookSourceListener;

    @Override
    public void getChapterList(String bookUrl) {
        if(bookUrl==null||bookUrl.equals(" ")){
            return;
        }
        //开始查询

    }

    @Override
    public void getChapterContent(String chapterUrl) {
        if(chapterUrl==null||chapterUrl.equals(" ")){
            return;
        }
        //开始查询

    }

    @Override
    public void searchBook(String str) {
        if(str==null||str.equals(" ")){
            return;
        }
        //开始查询
        netFlag=0;
        networkUtil=NetworkUtil.getInstance();
        networkUtil.setListener(this);
        Request request= new Request.Builder().url(URL+"/searchbook.php?keyword="+str).build();
        networkUtil.requestPost(request);
        System.out.println("开始查询");
    }

    @Override
    public void setListener(BookSourceListener bookSourceListener) {
        this.bookSourceListener=bookSourceListener;
    }

    @Override
    public void Success(Response response) {
        switch(netFlag){
            case 0:
                try {
                    Document document = Jsoup.parse(Objects.requireNonNull(response.body()).string());
                    Elements elements=document.select("div #hotcontent .item");
                    for (Element ele:elements) {
                        System.out.println("搜索到了，开始解析");

                        //开启图书获取线程
                        new EleThread(ele).run();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }

                break;
            case 1:
                //查询所有章节

                break;
            case 2:
                //查询章节内容

                break;
        }

    }

    @Override
    public void Error() {

    }

    private class EleThread implements Runnable{
        private Element element;
        public EleThread(Element element) {
            this.element=element;
        }

        @Override
        public void run() {
            Elements elementImg=element.select(".image a img");
            Elements authorName=element.select("dl dt span");
            Elements bookName=element.select("dl dt a");
            Elements bookAbout=element.select("dl dd");

            String bookname=bookName.text();
            String bookauthor=authorName.text();
            String bookabout=bookAbout.text();
            String bookurl=URL+bookName.attr("href");
            String bookimgurl=URL+elementImg.attr("src");

            BookInfo bookInfo=new BookInfo();
            bookInfo.setAuthorName(bookauthor);
            bookInfo.setBookImgUrl(bookimgurl);
            bookInfo.setBookName(bookname);
            bookInfo.setBookUrl(bookurl);
            bookInfo.setBookAbout(bookabout);

            //请求网络图片
            Request request=new Request.Builder().url(bookimgurl).build();
            Response responseImg= null;
            try {
                responseImg = networkUtil.requestGet(request);
                Bitmap bitmap= BitmapFactory.decodeStream(Objects.requireNonNull(responseImg.body()).byteStream());
                bookInfo.setBookImg(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
            //返回请求对象
            bookSourceListener.Success(bookInfo);
        }
    }


}
