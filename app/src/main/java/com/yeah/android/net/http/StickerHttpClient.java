package com.yeah.android.net.http;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONException;
import com.loopj.android.http.AsyncHttpClient;
import com.yeah.android.utils.LogUtil;
import com.yeah.android.net.HttpClientUtil;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.yeah.android.model.common.ResponseData;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.Type;

import cz.msebera.android.httpclient.Header;

/**
 * Created by litingchang on 15-10-5.
 */
public class StickerHttpClient {

    private static final String TAG = StickerHttpClient.class.getSimpleName();

    private static String HOST = "http://101.201.169.77/YouryeahApi";

    public static <T> void get(String action, RequestParams requestParams, Type type,
                               StickerHttpResponseHandler<T> responseHandler) {

        LogUtil.d(TAG, "get -> action:" + action + " \n->requestParams:" + requestParams.toString());


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
                            LogUtil.e(TAG, "onSuccess -> " + e.toString());
                            responseHandler.onFailure("服务器未响应");
                            return;
                        } catch (NullPointerException e) {
                            LogUtil.e(TAG, "onFailure -> " + e.toString());
                            responseHandler.onFailure("服务器异常，请稍候重试");
                            return;
                        }

                        LogUtil.d(TAG, "onSuccess -> " + "response:" + responseString);

                        ResponseData<T> responseData = null;
                        try {
                            // api 返回的data字段 在请求错误的情况下会返回字符串。
                            responseData = JSON.parseObject(responseString, type);
                        } catch (JSONException e) {
                            LogUtil.e(TAG, "onSuccess -> " + e.toString());
                            responseHandler.onFailure("JSON解析错误");
                            return;
                        }

                        if (responseData == null) {
                            LogUtil.e(TAG, "onSuccess -> responseData is null");
                            responseHandler.onFailure("JSON解析错误");
                            return;
                        }

                        if (!responseData.isResult()) {
                            LogUtil.e(TAG, "onSuccess -> responseData isResult false");
                            responseHandler.onFailure(responseData.getMessage());
                            return;
                        }

                        responseHandler.onSuccess(responseData.getData());
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers,
                                          byte[] responseBody, Throwable error) {

                        LogUtil.e(TAG, "onFailure -> statusCode:" + statusCode);

                        String responseString;
                        try {
                            responseString = new String(responseBody, "UTF-8");
                        } catch (UnsupportedEncodingException e) {
                            LogUtil.e(TAG, "onFailure -> " + e.toString());
                            responseHandler.onFailure("服务器未响应");
                            return;
                        } catch (NullPointerException e) {
                            LogUtil.e(TAG, "onFailure -> " + e.toString());
                            responseHandler.onFailure("服务器异常，请稍候重试");
                            return;
                        }

                        responseHandler.onFailure("code:" + statusCode + "msg:" + responseString);
                    }

                    @Override
                    public void onFinish() {
                        super.onFinish();
                        responseHandler.onFinish();
                    }
                });
    }

    public static <T> void post(String action, RequestParams requestParams, Type type,
                               StickerHttpResponseHandler<T> responseHandler) {

        LogUtil.d(TAG, "post ->action:" + action + " \n->requestParams:" + requestParams.toString());

        HttpClientUtil.getHttpClient().post(HOST + action,
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
                            LogUtil.e(TAG, "onSuccess -> " + e.toString());
                            responseHandler.onFailure("服务器未响应");
                            return;
                        } catch (NullPointerException e) {
                            LogUtil.e(TAG, "onFailure -> " + e.toString());
                            responseHandler.onFailure("服务器异常，请稍候重试");
                            return;
                        }

                        LogUtil.d(TAG, "onSuccess -> " + "response:" + responseString);

                        ResponseData<T> responseData = null;
                        try {
                            // api 返回的data字段 在请求错误的情况下会返回字符串。
                            responseData = JSON.parseObject(responseString, type);
                        } catch (JSONException e) {
                            LogUtil.e(TAG, "onSuccess -> " + e.toString());
                            responseHandler.onFailure("JSON解析错误");
                            return;
                        }

                        if (responseData == null) {
                            LogUtil.e(TAG, "onSuccess -> responseData is null");
                            responseHandler.onFailure("JSON解析错误");
                            return;
                        }

                        if (!responseData.isResult()) {
                            LogUtil.e(TAG, "onSuccess -> responseData isResult false");
                            responseHandler.onFailure(responseData.getMessage());
                            return;
                        }

                        responseHandler.onSuccess(responseData.getData());
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers,
                                          byte[] responseBody, Throwable error) {

                        LogUtil.e(TAG, "onFailure -> statusCode:" + statusCode);

                        String responseString;
                        try {
                            responseString = new String(responseBody, "UTF-8");
                        } catch (UnsupportedEncodingException e) {
                            LogUtil.e(TAG, "onFailure -> " + e.toString());
                            responseHandler.onFailure("服务器异常，请稍候重试");
                            return;
                        } catch (NullPointerException e) {
                            LogUtil.e(TAG, "onFailure -> " + e.toString());
                            responseHandler.onFailure("服务器异常，请稍候重试");
                            return;
                        }

                        responseHandler.onFailure("code:" + statusCode + "msg:" + responseString);
                    }

                    @Override
                    public void onFinish() {
                        super.onFinish();
                        responseHandler.onFinish();
                    }
                });
    }

}
