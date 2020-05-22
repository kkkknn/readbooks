package com.kkkkkn.readbooks.util;

public interface BackgroundUtilListener {
    void success(int requestId);
    void error(int codeId);
    void timeOut(int requestId);
}
