package com.yeah.android.activity.user;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.TextView;

import com.alibaba.fastjson.TypeReference;
import com.yeah.android.activity.camera.CameraManager;
import com.yeah.android.utils.LogUtil;
import com.yeah.android.utils.StringUtils;
import com.yeah.android.utils.ToastUtil;
import com.loopj.android.http.RequestParams;
import com.yeah.android.utils.Constants;
import com.yeah.android.net.http.StickerHttpClient;
import com.yeah.android.net.http.StickerHttpResponseHandler;
import com.yeah.android.utils.UserInfoManager;
import com.yeah.android.model.common.ResponseData;
import com.yeah.android.model.user.LoginResult;
import com.yeah.android.model.user.VerifyResponse;
import com.yeah.android.activity.MainActivity;
import com.yeah.android.activity.BaseActivity;
import com.yeah.android.R;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

/**
 * Created by litingchang on 15-10-7.
 */
public class RegistActivity extends BaseActivity {

    @InjectView(R.id.register_input_name)
    EditText registerInputName;
    @InjectView(R.id.register_input_password)
    EditText registerInputPassword;
    @InjectView(R.id.register_input_password_confirm)
    EditText registerInputPasswordConfirm;
    @InjectView(R.id.register_btn_register)
    TextView registerBtnRegister;
    @InjectView(R.id.register_input_verify)
    EditText registerInputVerify;
    @InjectView(R.id.register_btn_verify)
    TextView registerBtnVerify;

    private VerifyResponse mVerifyResponse;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_regist);
        ButterKnife.inject(this);
    }

    @OnClick(R.id.register_btn_verify)
    public void verify() {
        String phoneNumber = StringUtils.deleteWhitespace(registerInputName.getText().toString());
        if (StringUtils.isEmpty(phoneNumber)) {
            ToastUtil.shortToast(this, "请输入手机号，不可包含空格");
            return;
        }

        RequestParams requestParams = new RequestParams();
        requestParams.put("appId", Constants.APP_ID);
        requestParams.put("appKey", Constants.APP_KEY);
        requestParams.put("phone", phoneNumber);
        StickerHttpClient.post("/account/verify/request/register", requestParams,
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
                        ToastUtil.longToast(RegistActivity.this, "验证码发送成功:" + verifyResponse.getCode());
                    }

                    @Override
                    public void onFailure(String message) {
                        LogUtil.e("onFailure", message);
                        ToastUtil.shortToast(RegistActivity.this, "验证码发送失败：" + message
                                + "\n请稍候重试");
                    }

                    @Override
                    public void onFinish() {
                        dismissProgressDialog();
                    }
                });

    }

    @OnClick(R.id.register_btn_register)
    public void register() {
        String phoneNumber = StringUtils.deleteWhitespace(registerInputName.getText().toString());
        if (StringUtils.isEmpty(phoneNumber)) {
            ToastUtil.shortToast(this, "请输入手机号，不可包含空格");
            return;
        }

        if(mVerifyResponse == null) {
            ToastUtil.shortToast(this, "尚未进行手机验证，请点击获取验证码");
            return;
        }

        String verifyCode = StringUtils.deleteWhitespace(registerInputVerify.getText().toString());
        if (StringUtils.isEmpty(verifyCode)) {
            ToastUtil.shortToast(this, "请输入验证码，不可包含空格");
            return;
        }



        String password = StringUtils.deleteWhitespace(registerInputPassword.getText().toString());
        if (StringUtils.makeSafe(password).length() < 6) {
            ToastUtil.shortToast(this, "请输入6位登录密码，不可包含空格");
            return;
        }

        String passwordConfirm = StringUtils.deleteWhitespace(
                registerInputPasswordConfirm.getText().toString());
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
        requestParams.put("phone", phoneNumber);
        requestParams.put("password", password);
        requestParams.put("confirmPassword", passwordConfirm);
        requestParams.put("verifyCode", verifyCode);
        requestParams.put("verifyId", mVerifyResponse.getId());

        StickerHttpClient.post("/account/user/register", requestParams,
                new TypeReference<ResponseData<LoginResult>>() {
                }.getType(),
                new StickerHttpResponseHandler<LoginResult>() {

                    @Override
                    public void onStart() {
                        showProgressDialog("注册中...");
                    }

                    @Override
                    public void onSuccess(LoginResult loginResult) {
                        UserInfoManager.savePassword(password);
                        UserInfoManager.login(loginResult);

                        // 登录成功后的处理
                        ToastUtil.shortToast(RegistActivity.this, "注册成功");

                        CameraManager.getInst().openCamera(RegistActivity.this);

                        RegistActivity.this.finish();
                    }

                    @Override
                    public void onFailure(String message) {
                        LogUtil.e("onFailure", message);
                        ToastUtil.shortToast(RegistActivity.this, "注册失败：" + message
                                + "\n请确认后重试");
                    }

                    @Override
                    public void onFinish() {
                        dismissProgressDialog();
                    }
                });
    }

    public static void launch(Context context) {
        Intent intent = new Intent(context, RegistActivity.class);
        context.startActivity(intent);
    }
}
