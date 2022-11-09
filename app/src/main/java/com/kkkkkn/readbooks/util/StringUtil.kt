package com.kkkkkn.readbooks.util

import java.security.MessageDigest
import java.security.NoSuchAlgorithmException
import java.util.regex.Pattern

object StringUtil {
    fun checkAccountName(str: String?): Boolean {
        if (str == null || str.isEmpty()) {
            return false
        }
        //正则表达式验证，仅支持英文+数字+_
        val p = Pattern.compile("^([A-Za-z0-9_]{3,10})+$")
        val m = p.matcher(str)
        return m.matches()
    }

    fun equals(str1: String?, str2: String?): Boolean {
        return if (str1 == null || str1.isEmpty() || str2 == null || str2.isEmpty()) {
            false
        } else str1 == str2
    }

    fun checkAccountPassword(str: String?): Boolean {
        if (str == null || str.isEmpty()) {
            return false
        }
        //验证是否为强密码(必须包含大小写字母和数字的组合，可以使用特殊字符，长度在8-10之间)
        val p = Pattern.compile("^(?=.*\\d)(?=.*[a-z])(?=.*[A-Z]).{8,10}$")
        val m = p.matcher(str)
        return m.matches()
    }

    fun isEmpty(str: String?): Boolean {
        return str == null || str.isEmpty()
    }

    fun password2md5(str: String?): String? {
        if (str == null || str.isEmpty()) {
            return null
        }
        var md5: MessageDigest? = null
        try {
            md5 = MessageDigest.getInstance("MD5")
            val bytes = md5.digest(str.toByteArray())
            val result = StringBuilder()
            for (b in bytes) {
                var temp =Integer.toHexString(b.toInt()and (0xFF))
                if (temp.length == 1) {
                    temp = "0$temp"
                }
                result.append(temp)
            }
            return result.toString()
        } catch (e: NoSuchAlgorithmException) {
            e.printStackTrace()
        }
        return ""
    }

    fun text2Indent(s: String?): String {
        return s?.substring(2) ?: ""
    }

    fun url2bookName(chapterUrl: String?): String? {
        if (chapterUrl == null || chapterUrl.isEmpty()) {
            return null
        }
        val pathArr = chapterUrl.split("/").toTypedArray()
        return pathArr[7]
    }

    fun url2chapterName(chapterUrl: String?): String? {
        if (chapterUrl == null || chapterUrl.isEmpty()) {
            return null
        }
        val pathArr = chapterUrl.split("/").toTypedArray()
        return pathArr[9].replace(" ", "").replace(".txt", "").split("_").toTypedArray()[1]
    }
}