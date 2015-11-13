package com.yeah.android.net;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.SyncHttpClient;

/**
 * Created by litingchang on 15-9-25.
 */
public class HttpClientUtil {

    private static AsyncHttpClient asyncHttpClient;

    private static SyncHttpClient syncHttpClient;

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

    public static SyncHttpClient getSyncHttpClient() {
        if(syncHttpClient == null) {
            synchronized (HttpClientUtil.class) {
                if (syncHttpClient == null) {
                    syncHttpClient = new SyncHttpClient();
                    syncHttpClient.setTimeout(30000);
                }
            }
        }

        return syncHttpClient;
    }
}
