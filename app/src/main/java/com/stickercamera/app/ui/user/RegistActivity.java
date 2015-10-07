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
import com.stickercamera.app.manager.UserInfoManager;
import com.stickercamera.app.model.common.ResponseData;
import com.stickercamera.app.model.user.LoginResult;
import com.stickercamera.app.ui.MainActivity;
import com.stickercamera.base.BaseActivity;
import com.yeah.stickercamera.R;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_regist);
        ButterKnife.inject(this);
    }

    @OnClick(R.id.register_btn_register)
    public void register() {
        String phoneNumber = StringUtils.deleteWhitespace(registerInputName.getText().toString());
        if(StringUtils.isEmpty(phoneNumber)) {
            ToastUtil.shortToast(this, "请输入手机号，不可包含空格");
            return;
        }

        String password = StringUtils.deleteWhitespace(registerInputPassword.getText().toString());
        if(StringUtils.isEmpty(password)) {
            ToastUtil.shortToast(this, "请输入登录密码，不可包含空格");
            return;
        }

        String passwordConfirm = StringUtils.deleteWhitespace(
                registerInputPasswordConfirm.getText().toString());
        if(StringUtils.isEmpty(passwordConfirm)) {
            ToastUtil.shortToast(this, "请输再次入登录密码，不可包含空格");
            return;
        }

        if(!StringUtils.equals(password, passwordConfirm)) {
            ToastUtil.shortToast(this, "密码不一致，请确认！");
            return;
        }

        RequestParams requestParams = new RequestParams();
        requestParams.put("appId", AppConstants.APP_ID);
        requestParams.put("appKey", AppConstants.APP_KEY);
        requestParams.put("phone", phoneNumber);
        requestParams.put("password", password);
        requestParams.put("confirmPassword", passwordConfirm);
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
                        UserInfoManager.saveName(phoneNumber);
                        UserInfoManager.savePassword(password);
                        UserInfoManager.saveToken(loginResult.getTokenKey());
                        UserInfoManager.saveId(loginResult.getId());
                        UserInfoManager.saveUserId(loginResult.getUserId());

                        // TODO 登录成功后的处理
                        ToastUtil.shortToast(RegistActivity.this, "注册成功");
                        MainActivity.launch(RegistActivity.this);

                        RegistActivity.this.finish();
                    }

                    @Override
                    public void onFailure(String message) {
                        LogUtil.e("onFailure", message);
                        ToastUtil.shortToast(RegistActivity.this, "注册失败：" + message
                            + "\n请稍候重试");
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
