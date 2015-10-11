package com.yeah.android.utils;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.widget.Toast;

/**
 * Created by litingchang on 15-10-7.
 */
public class ToastUtil {

    private static Toast sToast = null;
    private static Handler mHandler;

    private static Handler getHandler() {
        if (mHandler == null) {
            mHandler = new Handler(Looper.getMainLooper());
        }
        return mHandler;
    }


    private static void showToast(final Context context, final int resId,
                                  final int duration) {
        if (context == null) {
            return;
        }

        try {
            getHandler().post(new Runnable() {
                public void run() {

                    if (sToast == null) {
                        sToast = Toast.makeText(context.getApplicationContext(), resId, duration);
                    } else {
                        sToast.setText(resId);
                        sToast.setDuration(duration);
                    }
                    sToast.show();
                }
            });
        } catch (Exception e) {
        }
    }

    private static void showToast(final Context context, final String msg,
                                  final int duration) {
        if (context == null) {
            return;
        }

        try {
            getHandler().post(new Runnable() {
                public void run() {

                    if (sToast == null) {
                        sToast = Toast.makeText(context.getApplicationContext(), msg, duration);
                    } else {
                        sToast.setText(msg);
                        sToast.setDuration(duration);
                    }
                    sToast.show();
                }
            });
        } catch (Exception e) {
        }
    }

    public static void shortToast(Context context, int resId) {
        showToast(context, resId, Toast.LENGTH_SHORT);
    }

    public static void shortToast(final Context context, final String msg) {
        showToast(context, msg, Toast.LENGTH_SHORT);
    }

    public static void longToast(Context context, int resId) {
        showToast(context, resId, Toast.LENGTH_LONG);
    }

    public static void longToast(Context context, String msg) {
        showToast(context, msg, Toast.LENGTH_LONG);
    }

}
