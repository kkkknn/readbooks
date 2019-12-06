package com.kkkkkn.readbooks.util;

public class JsoupUtil {
    private static JsoupUtil mjsoupUtil = null;

    private JsoupUtil() {

    }

    public synchronized JsoupUtil getJsoupUtil(){
        if(mjsoupUtil==null){
            mjsoupUtil=new JsoupUtil();
        }
        return mjsoupUtil;
    }


}
