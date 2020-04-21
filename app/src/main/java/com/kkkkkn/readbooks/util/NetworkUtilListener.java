package com.kkkkkn.readbooks.util;

import okhttp3.Response;

public interface NetworkUtilListener {
    public void Success(Response response);
    public void Error();
}
