package com.kkkkkn.readbooks.model.download;

public interface DownloadListener {
    void onSuccess();
    void onProgress(int i);
    void onError(Exception e);
}
