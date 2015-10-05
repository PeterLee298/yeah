package com.common.util.network;

import com.loopj.android.http.AsyncHttpClient;

/**
 * Created by litingchang on 15-9-25.
 */
public class HttpClientUtil {

    private static AsyncHttpClient asyncHttpClient;

    public static AsyncHttpClient getHttpClient() {
        if(asyncHttpClient == null) {
            synchronized (HttpClientUtil.class) {
                if (asyncHttpClient == null) {
                    asyncHttpClient = new AsyncHttpClient();
                    asyncHttpClient.setTimeout(30000);
                }
            }
        }

        return asyncHttpClient;
    }
}
