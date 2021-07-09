package com.kkkkkn.readbooks.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StringUtil {

    public static boolean checkAccountName(String str){
        if(str==null||str.isEmpty()){
            return false;
        }
        //正则表达式验证，仅支持英文+数字+_
        Pattern p = Pattern.compile("^([A-Za-z0-9_]{3,10})+$");
        Matcher m = p.matcher(str);
        return m.matches();
    }

    public static boolean checkAccountPassword(String str){
        if(str==null||str.isEmpty()){
            return false;
        }
        //验证是否为MD5字符串
        Pattern p = Pattern.compile("([a-fA-F0-9]{32})$");
        Matcher m = p.matcher(str);
        return m.matches();
    }


    public static String password2md5(String str){
        if(str==null||str.isEmpty()){
            return null;
        }
        MessageDigest md5 = null;
        try {
            md5 = MessageDigest.getInstance("MD5");
            byte[] bytes = md5.digest(str.getBytes());
            StringBuilder result = new StringBuilder();
            for (byte b : bytes) {
                String temp = Integer.toHexString(b & 0xff);
                if (temp.length() == 1) {
                    temp = "0" + temp;
                }
                result.append(temp);
            }
            return result.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return "";
    }
}
