package com.yeah.android.activity.user;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.TextView;

import com.alibaba.fastjson.TypeReference;
import com.loopj.android.http.RequestParams;
import com.yeah.android.R;
import com.yeah.android.activity.BaseActivity;
import com.yeah.android.activity.camera.CameraManager;
import com.yeah.android.model.common.ResponseData;
import com.yeah.android.model.user.FeedbackResponse;
import com.yeah.android.model.user.LoginResult;
import com.yeah.android.net.http.StickerHttpClient;
import com.yeah.android.net.http.StickerHttpResponseHandler;
import com.yeah.android.utils.Constants;
import com.yeah.android.utils.LogUtil;
import com.yeah.android.utils.StringUtils;
import com.yeah.android.utils.ToastUtil;
import com.yeah.android.utils.UserInfoManager;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

/**
 * Created by litingchang on 15-11-1.
 */
public class FeedbackActivity extends BaseActivity {

    @InjectView(R.id.feedback_content)
    EditText feedbackContent;
    @InjectView(R.id.feedback_btn)
    TextView feedbackBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback);
        ButterKnife.inject(this);

    }

    @OnClick(R.id.feedback_btn)
    public void feedback() {
        String content = StringUtils.clean(feedbackContent.getText().toString());

        if(StringUtils.isEmpty(content)) {
            ToastUtil.shortToast(FeedbackActivity.this, "请输入反馈内容");
            return;
        }

        RequestParams requestParams = new RequestParams();
        requestParams.put("appId", Constants.APP_ID);
        requestParams.put("appKey", Constants.APP_KEY);
        requestParams.put("loginId", UserInfoManager.getUserId());
        requestParams.put("loginToken", UserInfoManager.getToken());
        requestParams.put("content", content);
        StickerHttpClient.post("/account/user/feedback/create", requestParams,
                new TypeReference<ResponseData<FeedbackResponse>>() {
                }.getType(),
                new StickerHttpResponseHandler<FeedbackResponse>() {

                    @Override
                    public void onStart() {
                        showProgressDialog("发送中...");
                    }

                    @Override
                    public void onSuccess(FeedbackResponse response) {
                        ToastUtil.shortToast(FeedbackActivity.this, "反馈成功");
                        feedbackContent.setText("");
                    }

                    @Override
                    public void onFailure(String message) {
                        LogUtil.e("onFailure", message);
                        ToastUtil.shortToast(FeedbackActivity.this, "反馈失败：" + message);
                    }

                    @Override
                    public void onFinish() {
                        dismissProgressDialog();
                    }
                });

    }

    public static void launch(Context context) {
        Intent intent = new Intent(context, FeedbackActivity.class);
        context.startActivity(intent);
    }
}
