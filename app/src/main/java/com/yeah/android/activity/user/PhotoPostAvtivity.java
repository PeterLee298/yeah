package com.yeah.android.activity.user;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler.Callback;
import android.os.Message;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.TypeReference;
import com.loopj.android.http.RequestParams;
import com.mob.tools.utils.UIHandler;
import com.yeah.android.R;
import com.yeah.android.activity.BaseActivity;
import com.yeah.android.activity.camera.CameraManager;
import com.yeah.android.model.common.ResponseData;
import com.yeah.android.model.user.UploadPhotoResponse;
import com.yeah.android.net.http.StickerHttpClient;
import com.yeah.android.net.http.StickerHttpResponseHandler;
import com.yeah.android.utils.Constants;
import com.yeah.android.utils.ImageUtils;
import com.yeah.android.utils.LogUtil;
import com.yeah.android.utils.StringUtils;
import com.yeah.android.utils.ToastUtil;
import com.yeah.android.utils.UserInfoManager;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.sina.weibo.SinaWeibo;
import cn.sharesdk.wechat.friends.Wechat;
import cn.sharesdk.wechat.moments.WechatMoments;

/**
 * Created by litingchang on 15-10-20.
 */
public class PhotoPostAvtivity extends BaseActivity implements PlatformActionListener, Callback {

    private static final String TAG = PhotoPostAvtivity.class.getSimpleName();

    @InjectView(R.id.post_image)
    ImageView postImage;
    @InjectView(R.id.post_content)
    EditText postContent;
    @InjectView(R.id.post_btn)
    TextView postBtn;
    @InjectView(R.id.thirdparty_wx_friend)
    CheckBox mThirdPartyWxFriend;
    @InjectView(R.id.thirdparty_wx)
    CheckBox mThirdPartyWx;
    @InjectView(R.id.thirdparty_weibo)
    CheckBox mThirdPartyWeibo;

    private static final int MSG_TOAST = 1;
    private static final int MSG_ACTION_CCALLBACK = 2;
    private static final int MSG_CANCEL_NOTIFY = 3;

    private Uri imgUri;
    private int mThirdPartyPublishNum = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo_post);
        ButterKnife.inject(this);

        ShareSDK.initSDK(this);

        imgUri = getIntent().getData();

        ImageUtils.asyncLoadImage(this, imgUri, new ImageUtils.LoadImageCallback() {
            @Override
            public void callback(Bitmap result) {
                postImage.setImageBitmap(result);
            }
        });
    }

    @OnClick(R.id.post_btn)
    public void postPhoto() {

        String contentStr = StringUtils.clean(postContent.getText().toString());

        if(StringUtils.isEmpty(contentStr)) {
            ToastUtil.longToast(PhotoPostAvtivity.this, "请输入描述文字");
            return;
        }

        File file = new File(imgUri.getPath());

        if(file == null || !file.exists()) {
            ToastUtil.longToast(PhotoPostAvtivity.this, "读取图片失败");
            return;
        }

        RequestParams requestParams = new RequestParams();
        requestParams.put("appId", Constants.APP_ID);
        requestParams.put("appKey", Constants.APP_KEY);
        requestParams.put("loginId", UserInfoManager.getId());
        requestParams.put("loginToken", UserInfoManager.getToken());
        requestParams.put("description", contentStr);
        try {
            requestParams.put("upload", file);
        } catch (FileNotFoundException e) {
            ToastUtil.longToast(PhotoPostAvtivity.this, "网络初始化失败");
            return;
        }

        StickerHttpClient.post("/account/user/photo/upload", requestParams,
                new TypeReference<ResponseData<UploadPhotoResponse>>() {
                }.getType(),
                new StickerHttpResponseHandler<UploadPhotoResponse>() {

                    @Override
                    public void onStart() {
                        showProgressDialog("发布中...");
                    }

                    @Override
                    public void onSuccess(UploadPhotoResponse response) {
                        // TODO 验证码
                        ToastUtil.longToast(PhotoPostAvtivity.this, "发布成功");

                        thirdPartyPublish(contentStr, response.getUrl());

                        LogUtil.d(TAG, response.getUrl());
                    }

                    @Override
                    public void onFailure(String message) {
                        LogUtil.e("onFailure", message);
                        ToastUtil.shortToast(PhotoPostAvtivity.this, "发布失败：" + message
                                + "\n请稍候重试");
                    }

                    @Override
                    public void onFinish() {
                        dismissProgressDialog();
                    }
                });
    }

    private void thirdPartyPublish(String text, String url){
        System.out.println("thirdPartyPublish--->");
        if(mThirdPartyWeibo.isChecked()){
            mThirdPartyPublishNum ++;
            SinaWeibo.ShareParams sp = new SinaWeibo.ShareParams();
            sp.text = text;
            sp.imageUrl = url;

            //初始化新浪分享平台
            Platform pf = ShareSDK.getPlatform(PhotoPostAvtivity.this, SinaWeibo.NAME);
            //设置分享监听
            pf.setPlatformActionListener(this);
            //执行分享
            pf.share(sp);
        }
        if(mThirdPartyWx.isChecked()){
            mThirdPartyPublishNum ++;
            Wechat.ShareParams sp = new Wechat.ShareParams();
            sp.text = text;
            sp.imageUrl = url;
            sp.shareType = Platform.SHARE_IMAGE;
            Platform pf = ShareSDK.getPlatform(PhotoPostAvtivity.this, Wechat.NAME);
            pf.setPlatformActionListener(this);
            pf.share(sp);
        }
        if(mThirdPartyWxFriend.isChecked()){
            mThirdPartyPublishNum ++;
            WechatMoments.ShareParams sp = new WechatMoments.ShareParams();
            sp.text = text;
            sp.imageUrl = url;
            sp.shareType = Platform.SHARE_IMAGE;
            Platform pf = ShareSDK.getPlatform(PhotoPostAvtivity.this, WechatMoments.NAME);
            pf.setPlatformActionListener(this);
            pf.share(sp);
        }
        checkpublishState2Camera();
    }

    public void checkpublishState2Camera(){
        if(mThirdPartyPublishNum <= 0){
            CameraManager.getInst().openCamera(PhotoPostAvtivity.this);
        }
    }

    public static void launch(Context context, Uri uri) {
        Intent intent = new Intent(context, PhotoPostAvtivity.class);
        intent.setData(uri);
        context.startActivity(intent);
    }

    @Override
    public void onCancel(Platform platform, int action) {
        mThirdPartyPublishNum --;
        Message msg = new Message();
        msg.what = MSG_ACTION_CCALLBACK;
        msg.arg1 = 3;
        msg.arg2 = action;
        msg.obj = platform;
        UIHandler.sendMessage(msg, this);
    }

    @Override
    public void onComplete(Platform platform, int action, HashMap<String, Object> arg2) {
        mThirdPartyPublishNum --;
        Message msg = new Message();
        msg.what = MSG_ACTION_CCALLBACK;
        msg.arg1 = 1;
        msg.arg2 = action;
        msg.obj = platform;
        UIHandler.sendMessage(msg, this);
    }

    @Override
    public void onError(Platform platform, int action, Throwable t) {
        mThirdPartyPublishNum --;
        t.printStackTrace();

        Message msg = new Message();
        msg.what = MSG_ACTION_CCALLBACK;
        msg.arg1 = 2;
        msg.arg2 = action;
        msg.obj = t;
        UIHandler.sendMessage(msg, this);
    }

    @Override
    public boolean handleMessage(Message msg) {
        switch(msg.what) {
            case MSG_TOAST: {
                String text = String.valueOf(msg.obj);
                Toast.makeText(PhotoPostAvtivity.this, text, Toast.LENGTH_SHORT).show();
            }
            break;
            case MSG_ACTION_CCALLBACK: {
                switch (msg.arg1) {
                    case 1: { // 成功, successful notification
                        showNotification(2000, getString(R.string.share_completed));
                    }
                    break;
                    case 2: { // 失败, fail notification
                        String expName = msg.obj.getClass().getSimpleName();
                        if ("WechatClientNotExistException".equals(expName)
                                || "WechatTimelineNotSupportedException".equals(expName)) {
                            showNotification(2000, getString(R.string.wechat_client_inavailable));
                        }
                        else if ("GooglePlusClientNotExistException".equals(expName)) {
                            showNotification(2000, getString(R.string.google_plus_client_inavailable));
                        }
                        else if ("QQClientNotExistException".equals(expName)) {
                            showNotification(2000, getString(R.string.qq_client_inavailable));
                        }
                        else {
                            showNotification(2000, getString(R.string.share_failed));
                        }
                    }
                    break;
                    case 3: { // 取消, cancel notification
                        showNotification(2000, getString(R.string.share_canceled));
                    }
                    break;
                }
                checkpublishState2Camera();
            }
            break;
            case MSG_CANCEL_NOTIFY: {
                NotificationManager nm = (NotificationManager) msg.obj;
                if (nm != null) {
                    nm.cancel(msg.arg1);
                }
            }
            break;
        }
        return false;
    }

    // 在状态栏提示分享操作,the notification on the status bar
    private void showNotification(long cancelTime, String text) {
        try {
            Context app = getApplicationContext();
            NotificationManager nm = (NotificationManager) app
                    .getSystemService(Context.NOTIFICATION_SERVICE);
            final int id = Integer.MAX_VALUE / 13 + 1;
            nm.cancel(id);

            long when = System.currentTimeMillis();
            Notification notification = new Notification(R.drawable.ic_launcher, text, when);
            PendingIntent pi = PendingIntent.getActivity(app, 0, new Intent(), 0);
            notification.setLatestEventInfo(app, "sharesdk test", text, pi);
            notification.flags = Notification.FLAG_AUTO_CANCEL;
            nm.notify(id, notification);

            if (cancelTime > 0) {
                Message msg = new Message();
                msg.what = MSG_CANCEL_NOTIFY;
                msg.obj = nm;
                msg.arg1 = id;
                UIHandler.sendMessageDelayed(msg, cancelTime, this);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}


