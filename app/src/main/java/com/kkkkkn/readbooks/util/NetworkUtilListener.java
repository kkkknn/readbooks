package com.kkkkkn.readbooks.util;

import okhttp3.Response;

interface NetworkUtilListener {
    void Success(Response response);
    void Error();
}
