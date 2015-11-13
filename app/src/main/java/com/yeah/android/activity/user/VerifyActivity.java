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
import com.yeah.android.model.user.VerifyConfirmResponse;
import com.yeah.android.smsverify.MobConst;
import com.yeah.android.utils.LogUtil;
import com.yeah.android.utils.StringUtils;
import com.yeah.android.utils.ToastUtil;
import com.loopj.android.http.RequestParams;
import com.yeah.android.utils.Constants;
import com.yeah.android.net.http.StickerHttpClient;
import com.yeah.android.net.http.StickerHttpResponseHandler;
import com.yeah.android.model.common.ResponseData;
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
public class VerifyActivity extends BaseActivity {

    private static final String PHONE = "verify_phone";
    private static final String VERIFY_INFO = "verify_info";
    private String mPhoneNumber;
//    private VerifyResponse mVerifyResponse;

    @InjectView(R.id.verify_phone_number)
    TextView verifyPhoneNumber;
    @InjectView(R.id.verify_input_verify)
    EditText verifyInputVerify;
    @InjectView(R.id.verify_btn_get)
    TextView verifyBtnGet;
    @InjectView(R.id.verify_btn_next)
    TextView verifyBtnNext;

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
                    dismissProgressDialog();
                    ResetPasswordActivity.launch(VerifyActivity.this,
                            mPhoneNumber);
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
        setContentView(R.layout.activity_verify);
        ButterKnife.inject(this);

        Intent intent = getIntent();
        mPhoneNumber = intent.getStringExtra(PHONE);

        verifyPhoneNumber.setText(StringUtils.makeSafe(mPhoneNumber));

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

        showProgressDialog("验证码发送中。。。");
        SMSSDK.getVerificationCode("86", mPhoneNumber);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        SMSSDK.unregisterAllEventHandler();
    }

    // 获取验证码
    @OnClick(R.id.verify_btn_get)
    public void reGetVerify() {
        if (StringUtils.isEmpty(mPhoneNumber)) {
            ToastUtil.shortToast(VerifyActivity.this, "手机号码错误，请返回上个页面重试");
            return;
        }

        showProgressDialog("验证码发送中。。。");
        SMSSDK.getVerificationCode("86", mPhoneNumber);

    }

    // 校验验证码
    @OnClick(R.id.verify_btn_next)
    public void verifyNext() {

        String verifyCode = StringUtils.clean(verifyInputVerify.getText().toString());

        if (StringUtils.isEmpty(verifyCode)) {
            ToastUtil.shortToast(VerifyActivity.this, "请输入验证码");
            return;
        }

        showProgressDialog("验证中...");

        SMSSDK.submitVerificationCode("86", mPhoneNumber, verifyCode);


    }

    public static void launch(Context context, String phoneNumber) {
        Intent intent = new Intent(context, VerifyActivity.class);
        intent.putExtra(PHONE, phoneNumber);
        context.startActivity(intent);
    }

}
