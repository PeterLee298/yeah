package com.yeah.android.activity.user;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.fastjson.TypeReference;
import com.loopj.android.http.RequestParams;
import com.yeah.android.R;
import com.yeah.android.activity.BaseActivity;
import com.yeah.android.activity.camera.CameraManager;
import com.yeah.android.activity.camera.ui.PhotoStickerActivity;
import com.yeah.android.model.common.ResponseData;
import com.yeah.android.model.user.UploadPhotoResponse;
import com.yeah.android.model.user.VerifyResponse;
import com.yeah.android.net.http.StickerHttpClient;
import com.yeah.android.net.http.StickerHttpResponseHandler;
import com.yeah.android.utils.Constants;
import com.yeah.android.utils.FileUtils;
import com.yeah.android.utils.ImageUtils;
import com.yeah.android.utils.LogUtil;
import com.yeah.android.utils.StringUtils;
import com.yeah.android.utils.ToastUtil;
import com.yeah.android.utils.UserInfoManager;

import java.io.File;
import java.io.FileNotFoundException;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

/**
 * Created by litingchang on 15-10-20.
 */
public class PhotoPostAvtivity extends BaseActivity {

    private static final String TAG = PhotoPostAvtivity.class.getSimpleName();

    @InjectView(R.id.post_image)
    ImageView postImage;
    @InjectView(R.id.post_content)
    EditText postContent;
    @InjectView(R.id.post_btn)
    TextView postBtn;

    private Uri imgUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo_post);
        ButterKnife.inject(this);

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
        requestParams.put("loginId", UserInfoManager.getUserId());
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

                        CameraManager.getInst().openCamera(PhotoPostAvtivity.this);

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

    public static void launch(Context context, Uri uri) {
        Intent intent = new Intent(context, PhotoPostAvtivity.class);
        intent.setData(uri);
        context.startActivity(intent);
    }
}


