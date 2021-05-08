package com.kkkkkn.readbooks.model.network;

public interface DownloadListener {
    void onSuccess();
    void onProgress(int i);
    void onError(Exception e);
}
