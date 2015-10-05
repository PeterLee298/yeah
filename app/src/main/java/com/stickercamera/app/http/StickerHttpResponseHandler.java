package com.stickercamera.app.http;

/**
 * Created by litingchang on 15-10-5.
 */
public interface StickerHttpResponseHandler<T> {
    void onStart();
    void onSuccess(T response);
    void onFailure(String message);
    void onFinish();
}
