package com.stickercamera.app.ui.user;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
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
import com.stickercamera.app.manager.UserInfoManager;
import com.stickercamera.app.model.common.ResponseData;
import com.stickercamera.app.model.sticker.StickerInfo;
import com.stickercamera.app.model.user.LoginResult;
import com.stickercamera.app.ui.MainActivity;
import com.stickercamera.base.BaseActivity;
import com.yeah.stickercamera.R;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnCheckedChanged;
import butterknife.OnClick;

/**
 * Created by litingchang on 15-10-6.
 */
public class LoginActivity extends BaseActivity {

    @InjectView(R.id.login_input_name)
    EditText loginInputName;
    @InjectView(R.id.login_input_password)
    EditText loginInputPassword;
    @InjectView(R.id.login_btn_login)
    TextView loginBtnLogin;
    @InjectView(R.id.login_forget_password)
    TextView loginForgetPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.inject(this);

        titleBar.setLeftBtnOnclickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LoginActivity.this.finish();
            }
        });

        // TODO
        loginInputName.setText("15615236548");
        loginInputPassword.setText("Com2uscn");
    }

    @OnClick(R.id.login_btn_login)
    public void login() {
        String phoneNumber = StringUtils.deleteWhitespace(loginInputName.getText().toString());
        if(StringUtils.isEmpty(phoneNumber)) {
            ToastUtil.shortToast(this, "请输入用户名，不可包含空格");
            return;
        }

        String password = StringUtils.deleteWhitespace(loginInputPassword.getText().toString());
        if(StringUtils.isEmpty(password)) {
            ToastUtil.shortToast(this, "请输入登录密码，不可包含空格");
            return;
        }

        RequestParams requestParams = new RequestParams();
        requestParams.put("appId", AppConstants.APP_ID);
        requestParams.put("appKey", AppConstants.APP_KEY);
        requestParams.put("phone", phoneNumber);
        requestParams.put("password", password);
        StickerHttpClient.post("/account/user/login", requestParams,
                new TypeReference<ResponseData<LoginResult>>() {
                }.getType(),
                new StickerHttpResponseHandler<LoginResult>() {

                    @Override
                    public void onStart() {
                        showProgressDialog("登录中...");
                    }

                    @Override
                    public void onSuccess(LoginResult loginResult) {
                        UserInfoManager.saveName(phoneNumber);
                        UserInfoManager.savePassword(password);
                        UserInfoManager.saveToken(loginResult.getTokenKey());
                        UserInfoManager.saveId(loginResult.getId());
                        UserInfoManager.saveUserId(loginResult.getUserId());

                        // TODO 登录成功后的处理
                        ToastUtil.shortToast(LoginActivity.this, "登录成功");
                        MainActivity.launch(LoginActivity.this);

                        LoginActivity.this.finish();
                    }

                    @Override
                    public void onFailure(String message) {
                        LogUtil.e("onFailure", message);
                        ToastUtil.shortToast(LoginActivity.this, "登录失败：" + message);
                    }

                    @Override
                    public void onFinish() {
                        dismissProgressDialog();
                    }
                });

    }

    @OnClick(R.id.login_forget_password)
    public void resetPassword() {

    }

    public static void launch(Context context) {
        Intent intent = new Intent(context, LoginActivity.class);
        context.startActivity(intent);
    }
}
