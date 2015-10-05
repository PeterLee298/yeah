package com.stickercamera.app.http;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.common.util.network.HttpClientUtil;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.stickercamera.app.personal.model.ResponseData;
import com.stickercamera.app.sticker.StickerInfo;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.Type;

import cz.msebera.android.httpclient.Header;

/**
 * Created by litingchang on 15-10-5.
 */
public class StickerHttpClient {

    private static String HOST = "http://101.201.169.77/YouryeahApi";

    public static <T> void get(String action, RequestParams requestParams, Type type,
                               StickerHttpResponseHandler<T> responseHandler) {

        HttpClientUtil.getHttpClient().get(HOST + action,
                requestParams, new AsyncHttpResponseHandler() {

                    @Override
                    public void onStart() {
                        super.onStart();
                        responseHandler.onStart();
                    }

                    @Override
                    public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                        String responseString;
                        try {
                            responseString = new String(responseBody, "UTF-8");
                        } catch (UnsupportedEncodingException e) {
                            e.printStackTrace();
                            responseHandler.onFailure("服务器未响应");
                            return;
                        }

                        ResponseData<T> responseData = JSON.parseObject(responseString,
                                type);

                        if(responseData == null) {
                            responseHandler.onFailure("JSON解析错误");
                            return;
                        }

                        if(!responseData.isResult()) {
                            responseHandler.onFailure(responseData.getMessage());
                            return;
                        }

                        responseHandler.onSuccess(responseData.getData());
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers,
                                          byte[] responseBody, Throwable error) {

                        String responseString;
                        try {
                            responseString = new String(responseBody, "UTF-8");
                        } catch (UnsupportedEncodingException e) {
                            responseHandler.onFailure("服务器未响应");
                            return;
                        }

                        ResponseData<StickerInfo> responseData = JSON.parseObject(responseString,
                                type);

                        if(responseData == null) {
                            responseHandler.onFailure("JSON解析错误");
                            return;
                        }

                        responseHandler.onFailure(responseData.getMessage());
                    }

                    @Override
                    public void onFinish() {
                        super.onFinish();
                        responseHandler.onFinish();
                    }
                });

    }

}
