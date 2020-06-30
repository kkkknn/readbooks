package com.kkkkkn.readbooks.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Hashtable;

//图片图像加载工具类
public class ImageLoaderUtil {
    private final static String TAG="ImageLoaderUtil";
    private volatile static ImageLoaderUtil imageLoaderUtil;
    //此处工具类 有可能线程调用，故采用线程安全的Hashtable
    private Hashtable<String, Bitmap> bitmaps=new Hashtable<>();

    private ImageLoaderUtil() {
    }

    public static ImageLoaderUtil getInstance(){
        if(imageLoaderUtil==null){
            synchronized (ImageLoaderUtil.class){
                if(imageLoaderUtil==null){
                    imageLoaderUtil=new ImageLoaderUtil();
                }
            }
        }
        return imageLoaderUtil;
    }

    public Bitmap getImage(String imgUrl, Context context){
        if(imgUrl==null||imgUrl.isEmpty()||context==null){
            return null;
        }
        //转换imgurl为md5字符串，然后进行比对
        String urlMd5=Md5Util.str2md5(imgUrl);
        if(bitmaps.containsKey(urlMd5)){
            //当前内存中含有该图片，直接加载
            return bitmaps.get(urlMd5);
        }
        //读取本地目录，查看是否含有该图片
        Bitmap sdBitmap=getImgBySDCard(urlMd5,context);
        if(sdBitmap!=null){
            return sdBitmap;
        }
        //返回网络图像
        return getImgByWeb(imgUrl,context);
    }
    //加载本地SD卡图像，成功后加载到缓存内
    private Bitmap getImgBySDCard(String fileName,Context context){
        Bitmap bitmap=null;
        File filesDir=context.getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        if(filesDir==null){
            return bitmap;
        }
        File[] files=filesDir.listFiles();
        for (File file : files) {
            if (file.isFile()) {
                if (fileName.equals(file.getName())) {
                    //条件符合，开始读取文件
                    Log.i(TAG, "getImgBySDCard: " + file.getPath());
                    bitmap = BitmapFactory.decodeFile(file.getPath());
                    bitmaps.put(fileName, bitmap);
                    return bitmap;
                }
            }
        }
        return bitmap;
    }

    //网络加载图像，成功后保存到本地和缓存内
    private Bitmap getImgByWeb(String imgUrl,Context context){
        Bitmap bitmap=null;
        try {
            URL url=new URL(imgUrl);
            HttpURLConnection connection=(HttpURLConnection) url.openConnection();
            connection.setConnectTimeout(6000);
            connection.setDoInput(true);
            connection.setUseCaches(false);
            connection.connect();
            InputStream inputStream=connection.getInputStream();
            bitmap=BitmapFactory.decodeStream(inputStream);
            inputStream.close();
            //保存到文件中
            String md5Name=Md5Util.str2md5(imgUrl);
            File dirFile=context.getExternalFilesDir(Environment.DIRECTORY_PICTURES);
            if(dirFile!=null){
                File file=new File(dirFile.getPath()+md5Name+".jpg");
                FileOutputStream out = new FileOutputStream(file);
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);
                out.flush();
                out.close();
            }
            //保存到内存中
            bitmaps.put(md5Name,bitmap);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bitmap;
    }
}
