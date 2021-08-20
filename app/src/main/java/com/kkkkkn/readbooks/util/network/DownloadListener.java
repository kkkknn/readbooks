package com.kkkkkn.readbooks.util.network;

public interface DownloadListener {
    void onSuccess();
    void onProgress(int i);
    void onError(Exception e);
}
