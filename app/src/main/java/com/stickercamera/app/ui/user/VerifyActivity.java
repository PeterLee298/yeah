package com.stickercamera.app.ui.user;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.TextView;

import com.alibaba.fastjson.TypeReference;
import com.common.util.LogUtil;
import com.common.util.StringUtils;
import com.common.util.ToastUtil;
import com.loopj.android.http.RequestParams;
import com.stickercamera.AppConstants;
import com.stickercamera.app.http.StickerHttpClient;
import com.stickercamera.app.http.StickerHttpResponseHandler;
import com.stickercamera.app.model.common.ResponseData;
import com.stickercamera.app.model.user.VerifyResponse;
import com.stickercamera.base.BaseActivity;
import com.yeah.stickercamera.R;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

/**
 * Created by litingchang on 15-10-7.
 */
public class VerifyActivity extends BaseActivity {

    private static final String PHONE = "verify_phone";
    private static final String VERIFY_INFO = "verify_info";
    private String mPhoneNumber;
    private VerifyResponse mVerifyResponse;

    @InjectView(R.id.verify_phone_number)
    TextView verifyPhoneNumber;
    @InjectView(R.id.verify_input_verify)
    EditText verifyInputVerify;
    @InjectView(R.id.verify_btn_get)
    TextView verifyBtnGet;
    @InjectView(R.id.verify_btn_next)
    TextView verifyBtnNext;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verify);
        ButterKnife.inject(this);

        Intent intent = getIntent();
        mPhoneNumber = intent.getStringExtra(PHONE);
        mVerifyResponse = (VerifyResponse)intent.getSerializableExtra(VERIFY_INFO);

        verifyPhoneNumber.setText(StringUtils.makeSafe(mPhoneNumber));

    }

    @OnClick(R.id.verify_btn_get)
    public void reGetVerify() {
        if (StringUtils.isEmpty(mPhoneNumber)) {
            ToastUtil.shortToast(VerifyActivity.this, "手机号码错误，请返回上个页面重试");
            return;
        }

        RequestParams requestParams = new RequestParams();
        requestParams.put("appId", AppConstants.APP_ID);
        requestParams.put("appKey", AppConstants.APP_KEY);
        requestParams.put("phone", mPhoneNumber);
        StickerHttpClient.post("/account/verify/request", requestParams,
                new TypeReference<ResponseData<VerifyResponse>>() {
                }.getType(),
                new StickerHttpResponseHandler<VerifyResponse>() {

                    @Override
                    public void onStart() {
                        showProgressDialog("验证码发送中...");
                    }

                    @Override
                    public void onSuccess(VerifyResponse verifyResponse) {
                        // TODO 验证码
                        mVerifyResponse = verifyResponse;
                        ToastUtil.longToast(VerifyActivity.this, "验证码发送成功:" + verifyResponse.getCode());
                    }

                    @Override
                    public void onFailure(String message) {
                        LogUtil.e("onFailure", message);
                        ToastUtil.shortToast(VerifyActivity.this, "验证码发送失败：" + message
                                + "\n请稍候重试");
                    }

                    @Override
                    public void onFinish() {
                        dismissProgressDialog();
                    }
                });

    }

    @OnClick(R.id.verify_btn_next)
    public void verifyNext() {

    }

    public static void launch(Context context, String phoneNumber, VerifyResponse verifyResponse) {
        Intent intent = new Intent(context, VerifyActivity.class);
        intent.putExtra(PHONE, phoneNumber);
        intent.putExtra(VERIFY_INFO, verifyResponse);
        context.startActivity(intent);
    }

}
