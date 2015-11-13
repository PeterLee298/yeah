package com.yeah.android.activity.user;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.TypeReference;
import com.yeah.android.activity.camera.CameraManager;
import com.yeah.android.smsverify.MobConst;
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
import com.yeah.android.activity.BaseActivity;
import com.yeah.android.R;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import cn.smssdk.EventHandler;
import cn.smssdk.SMSSDK;

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

    private String checkedPhoneNumber;

    private boolean isCheckedPhoneNumber;


    Handler handler=new Handler(){

        @Override
        public void handleMessage(Message msg) {
            // TODO Auto-generated method stub
            super.handleMessage(msg);
            int event = msg.arg1;
            int result = msg.arg2;
            Object data = msg.obj;
            Log.e("event", "event=" + event);
            if (result == SMSSDK.RESULT_COMPLETE) {
                //短信注册成功后，返回MainActivity,然后提示新好友
                if (event == SMSSDK.EVENT_SUBMIT_VERIFICATION_CODE) {//提交验证码成功
                    register();
                } else if (event == SMSSDK.EVENT_GET_VERIFICATION_CODE){
                    dismissProgressDialog();
                    Toast.makeText(getApplicationContext(), "验证码已经发送", Toast.LENGTH_SHORT).show();
                }else if (event ==SMSSDK.EVENT_GET_SUPPORTED_COUNTRIES){//返回支持发送验证码的国家列表
                    Toast.makeText(getApplicationContext(), "获取国家列表成功", Toast.LENGTH_SHORT).show();
                }
            } else {
                ((Throwable) data).printStackTrace();
                dismissProgressDialog();
                Toast.makeText(getApplicationContext(), "验证码错误", Toast.LENGTH_SHORT).show();
            }
        }

    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_regist);
        ButterKnife.inject(this);

        SMSSDK.initSDK(this, MobConst.APP_KEY, MobConst.APP_SECREt);
        EventHandler eh=new EventHandler(){

            @Override
            public void afterEvent(int event, int result, Object data) {

                Message msg = new Message();
                msg.arg1 = event;
                msg.arg2 = result;
                msg.obj = data;
                handler.sendMessage(msg);
            }

        };
        SMSSDK.registerEventHandler(eh);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        SMSSDK.unregisterAllEventHandler();
    }

    @OnClick(R.id.register_btn_verify)
    public void verify() {
        String phoneNumber = StringUtils.deleteWhitespace(registerInputName.getText().toString());
        if (StringUtils.isEmpty(phoneNumber)) {
            ToastUtil.shortToast(this, "请输入手机号，不可包含空格");
            return;
        }

        checkedPhoneNumber = phoneNumber;

        SMSSDK.getVerificationCode("86", phoneNumber);
    }

    @OnClick(R.id.register_btn_register)
    public void checkVerify() {

        String verifyCode = StringUtils.deleteWhitespace(registerInputVerify.getText().toString());
        if (StringUtils.isEmpty(verifyCode)) {
            ToastUtil.shortToast(this, "请输入验证码，不可包含空格");
            return;
        }

        showProgressDialog("注册中...");

        SMSSDK.submitVerificationCode("86", checkedPhoneNumber, verifyCode);

        isCheckedPhoneNumber = true;
    }



    private void register() {

        String phoneNumber = StringUtils.deleteWhitespace(registerInputName.getText().toString());
        if (StringUtils.isEmpty(phoneNumber)) {
            ToastUtil.shortToast(this, "请输入手机号，不可包含空格");
            return;
        }

        if(!isCheckedPhoneNumber) {
            ToastUtil.shortToast(this, "手机未验证成功，请重新验证");
            return;
        }

        if(!phoneNumber.equals(checkedPhoneNumber)) {
            ToastUtil.shortToast(this, "手机号码改变，请重新验证");
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

        StickerHttpClient.post("/account/user/register", requestParams,
                new TypeReference<ResponseData<LoginResult>>() {
                }.getType(),
                new StickerHttpResponseHandler<LoginResult>() {

                    @Override
                    public void onStart() {
//                        showProgressDialog("注册中...");
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
