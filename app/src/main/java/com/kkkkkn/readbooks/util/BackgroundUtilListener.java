package com.kkkkkn.readbooks.util;

public interface BackgroundUtilListener {
    void success(String  str);
    void error(int codeId);
    void timeOut(int requestId);
}
