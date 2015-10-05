package com.common.util;

import android.os.*;

import com.yeah.stickercamera.BuildConfig;

import java.sql.BatchUpdateException;

/**
 * Created by litingchang on 15-10-5.
 */
public class LogUtil {

    public static void d(String tag, String msg) {
        if (!BuildConfig.DEBUG) {
            return;
        }
        android.util.Log.d(tag, "tid:" + android.os.Process.myTid() + "; -->" + msg);
    }

    public static void e(String tag, String msg) {
        if (!BuildConfig.DEBUG) {
            return;
        }
        android.util.Log.e(tag, "tid:" + android.os.Process.myTid() + "; -->" + msg);
    }


}
