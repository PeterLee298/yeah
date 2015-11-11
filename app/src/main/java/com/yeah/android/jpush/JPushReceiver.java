package com.yeah.android.jpush;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.yeah.android.YeahApp;
import com.yeah.android.activity.user.MessageListActivity;
import com.yeah.android.model.user.Message;
import com.yeah.android.utils.DBUtil;
import com.yeah.android.utils.LogUtil;

import cn.jpush.android.api.JPushInterface;

/**
 * Created by litingchang on 15-11-10.
 */
public class JPushReceiver extends BroadcastReceiver {

    private static final String TAG = JPushReceiver.class.getSimpleName();

    @Override
    public void onReceive(Context context, Intent intent) {

        LogUtil.d(TAG, "onReceive - " + intent.getAction());

        if (JPushInterface.ACTION_REGISTRATION_ID.equals(intent.getAction())) {

        }else if (JPushInterface.ACTION_MESSAGE_RECEIVED.equals(intent.getAction())) {
//            System.out.println("收到了自定义消息。消息内容是：" + bundle.getString(JPushInterface.EXTRA_MESSAGE));
            // 自定义消息不会展示在通知栏，完全要开发者写代码去处理
        } else if (JPushInterface.ACTION_NOTIFICATION_RECEIVED.equals(intent.getAction())) {
//            System.out.println("收到了通知");
            // 在这里可以做些统计，或者做些其他工作

            Bundle bundle = intent.getExtras();
            String title = bundle.getString(JPushInterface.EXTRA_NOTIFICATION_TITLE);
            String content = bundle.getString(JPushInterface.EXTRA_ALERT);

            //
            Message message = new Message();
            message.setTitle(title);
            message.setContent(content);
            DBUtil.getInstance(context).storeMessage(message);

            LogUtil.d(TAG, "app is launched:" + YeahApp.getApp().isMainActivityLaunched());

        } else if (JPushInterface.ACTION_NOTIFICATION_OPENED.equals(intent.getAction())) {
//            System.out.println("用户点击打开了通知");
            // 在这里可以自己写代码去定义用户点击后的行为
            MessageListActivity.launch(context);
        } else {
            Log.d(TAG, "Unhandled intent - " + intent.getAction());
        }
    }
}
