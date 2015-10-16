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
import com.yeah.android.activity.MainActivity;
import com.yeah.android.activity.camera.CameraManager;
import com.yeah.android.activity.camera.ui.CameraActivity;
import com.yeah.android.model.common.ResponseData;
import com.yeah.android.model.user.LoginResult;
import com.yeah.android.net.http.StickerHttpClient;
import com.yeah.android.net.http.StickerHttpResponseHandler;
import com.yeah.android.utils.Constants;
import com.yeah.android.utils.LogUtil;
import com.yeah.android.utils.ObjectUtils;
import com.yeah.android.utils.StringUtils;
import com.yeah.android.utils.ToastUtil;
import com.yeah.android.utils.UserInfoManager;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

/**
 * Created by litingchang on 15-10-14.
 */
public class ResetPasswordActivity extends BaseActivity {

    public static final String VERIFY_PHONE = "verify_phone";
    public static final String VERIFY_ID = "verify_id";
    public static final String VERIFY_CODE = "verify_code";

    private String mPhone;
    private int mVerifyId;
    private String mVerifyCode;

    @InjectView(R.id.reset_input_password)
    EditText resetInputPassword;
    @InjectView(R.id.reset_input_password_confirm)
    EditText resetInputPasswordConfirm;
    @InjectView(R.id.reset_btn)
    TextView resetBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password);
        ButterKnife.inject(this);

        Intent intent = getIntent();
        mPhone = intent.getStringExtra(VERIFY_PHONE);
        mVerifyId = intent.getIntExtra(VERIFY_ID, 0);
        mVerifyCode = intent.getStringExtra(VERIFY_CODE);
    }


    @OnClick(R.id.reset_btn)
    public void resetPassword() {

        String password = StringUtils.deleteWhitespace(resetInputPassword.getText().toString());
        if (StringUtils.makeSafe(password).length() < 6) {
            ToastUtil.shortToast(this, "请输入6位登录密码，不可包含空格");
            return;
        }

        String passwordConfirm = StringUtils.deleteWhitespace(resetInputPasswordConfirm.getText().toString());
        if (StringUtils.makeSafe(password).length() < 6) {
            ToastUtil.shortToast(this, "请再次输入6位登录密码，不可包含空格");
            return;
        }

        if (!StringUtils.equals(password, passwordConfirm)) {
            ToastUtil.shortToast(this, "密码不一致，请确认！");
            return;
        }

        RequestParams requestParams = new RequestParams();
        requestParams.put("appId", Constants.APP_ID);
        requestParams.put("appKey", Constants.APP_KEY);
        requestParams.put("password", password);
        requestParams.put("phone", mPhone);
        requestParams.put("verifyId", mVerifyId);
        requestParams.put("verifyCode", mVerifyCode);

        StickerHttpClient.post("/account/user/password/reset", requestParams,
                new TypeReference<ResponseData<LoginResult>>() {
                }.getType(),
                new StickerHttpResponseHandler<LoginResult>() {

                    @Override
                    public void onStart() {
                        showProgressDialog("密码重置中...");
                    }

                    @Override
                    public void onSuccess(LoginResult loginResult) {
                        UserInfoManager.savePassword(password);
                        UserInfoManager.login(loginResult);

                        ToastUtil.shortToast(ResetPasswordActivity.this, "密码重置成功，请重新登录");

                        LoginActivity.launch(ResetPasswordActivity.this);
                    }

                    @Override
                    public void onFailure(String message) {
                        LogUtil.e("onFailure", message);
                        ToastUtil.shortToast(ResetPasswordActivity.this, "密码重置失败：" + message
                                + "\n请确认后重试");
                    }

                    @Override
                    public void onFinish() {
                        dismissProgressDialog();
                    }
                });
    }

    public static void launch(Context context, String phone, int verifyId, String verifyCode) {
        Intent intent = new Intent(context, ResetPasswordActivity.class);
        intent.putExtra(VERIFY_PHONE, phone);
        intent.putExtra(VERIFY_ID, verifyId);
        intent.putExtra(VERIFY_CODE, verifyCode);
        context.startActivity(intent);
    }


}
