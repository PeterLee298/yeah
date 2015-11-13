package com.yeah.android.activity.user;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.fastjson.TypeReference;
import com.loopj.android.http.RequestParams;
import com.yeah.android.R;
import com.yeah.android.activity.BaseActivity;
import com.yeah.android.activity.camera.CameraManager;
import com.yeah.android.model.common.ResponseData;
import com.yeah.android.model.user.LoginResult;
import com.yeah.android.model.user.VerifyResponse;
import com.yeah.android.net.http.StickerHttpClient;
import com.yeah.android.net.http.StickerHttpResponseHandler;
import com.yeah.android.utils.Constants;
import com.yeah.android.utils.LogUtil;
import com.yeah.android.utils.StringUtils;
import com.yeah.android.utils.ToastUtil;
import com.yeah.android.utils.UserInfoManager;

import java.util.HashMap;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;
import cn.sharesdk.framework.PlatformDb;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.sina.weibo.SinaWeibo;
import cn.sharesdk.tencent.qq.QQ;
import cn.sharesdk.wechat.friends.Wechat;

/**
 * Created by litingchang on 15-10-6.
 */
public class LoginActivity extends BaseActivity implements PlatformActionListener{




    @InjectView(R.id.login_input_name)
    EditText loginInputName;
    @InjectView(R.id.login_input_password)
    EditText loginInputPassword;
    @InjectView(R.id.login_btn_login)
    TextView loginBtnLogin;
    @InjectView(R.id.login_forget_password)
    TextView loginForgetPassword;
    @InjectView(R.id.login_register)
    TextView loginRegister;
    @InjectView(R.id.bind_weibo)
    ImageView bindWeibo;
    @InjectView(R.id.bind_weixin)
    ImageView bindWeixin;
    @InjectView(R.id.bind_qq)
    ImageView bindQq;

    private VerifyResponse mVerifyResponse;

    private String mCurrentOauthPlatform;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.inject(this);

        // TODO
        loginInputName.setText("17000000001");
        loginInputPassword.setText("000000");
        ShareSDK.initSDK(this);
    }

    @OnClick(R.id.login_btn_login)
    public void login() {
        String phoneNumber = StringUtils.deleteWhitespace(loginInputName.getText().toString());
        if (StringUtils.isEmpty(phoneNumber)) {
            ToastUtil.shortToast(this, "请输入用户名，不可包含空格");
            return;
        }

        String password = StringUtils.deleteWhitespace(loginInputPassword.getText().toString());
        if (StringUtils.isEmpty(password)) {
            ToastUtil.shortToast(this, "请输入登录密码，不可包含空格");
            return;
        }

        RequestParams requestParams = new RequestParams();
        requestParams.put("appId", Constants.APP_ID);
        requestParams.put("appKey", Constants.APP_KEY);
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
                        UserInfoManager.savePassword(password);

                        UserInfoManager.login(loginResult);

                        //  登录成功后的处理
                        ToastUtil.shortToast(LoginActivity.this, "登录成功");
                        CameraManager.getInst().openCamera(LoginActivity.this);

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
        String phoneNumber = StringUtils.deleteWhitespace(loginInputName.getText().toString());
        if (StringUtils.isEmpty(phoneNumber)) {
            ToastUtil.shortToast(this, "请输入用户名，不可包含空格");
            return;
        }

        RequestParams requestParams = new RequestParams();
        requestParams.put("appId", Constants.APP_ID);
        requestParams.put("appKey", Constants.APP_KEY);
        requestParams.put("phone", phoneNumber);
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
                        ToastUtil.longToast(LoginActivity.this, "验证码发送成功:" + verifyResponse.getCode());

                        VerifyActivity.launch(LoginActivity.this, phoneNumber, mVerifyResponse);
                    }

                    @Override
                    public void onFailure(String message) {
                        LogUtil.e("onFailure", message);
                        ToastUtil.shortToast(LoginActivity.this, "验证码发送失败：" + message
                                + "\n请稍候重试");
                    }

                    @Override
                    public void onFinish() {
                        dismissProgressDialog();
                    }
                });
    }

    @OnClick(R.id.login_register)
    public void register() {
        RegistActivity.launch(LoginActivity.this);
    }
    @OnClick(R.id.bind_weibo)
    public void bindWeiBo() {

        showProgressDialog("");
        Platform weibo = ShareSDK.getPlatform(this, SinaWeibo.NAME);
        mCurrentOauthPlatform = "weibo";
        weibo.setPlatformActionListener(this);
        weibo.SSOSetting(true);
        //获取用户资料
        weibo.showUser(null);

    }
    @OnClick(R.id.bind_weixin)
    public void binWeiXin() {

        showProgressDialog("");
        Platform weixin = ShareSDK.getPlatform(this, Wechat.NAME);
        mCurrentOauthPlatform = "wechat";
        weixin.setPlatformActionListener(this);
        weixin.SSOSetting(true);
        //获取用户资料
        weixin.showUser(null);

    }
    @OnClick(R.id.bind_qq)
    public void bindQQ() {

        showProgressDialog("");
        Platform qq = ShareSDK.getPlatform(this, QQ.NAME);
        mCurrentOauthPlatform = "qq";
        qq.setPlatformActionListener(this);
        qq.SSOSetting(true);
        //获取用户资料
        qq.showUser(null);

    }


    @Override
    public void onComplete(Platform platform, int i, HashMap<String, Object> stringObjectHashMap) {
        System.out.println("thirdPartLogin--->onComplete-->");
        dismissProgressDialog();

        PlatformDb platDB = platform.getDb();
        if(mCurrentOauthPlatform.equals("weibo")){
            thirdPartLogin(stringObjectHashMap.get("id").toString(), mCurrentOauthPlatform, platDB.getToken(), stringObjectHashMap.get("name").toString(),
                    stringObjectHashMap.get("name").toString(), stringObjectHashMap.get("profile_image_url").toString(), "", "");
        }else if(mCurrentOauthPlatform.equals("wechat")){
            thirdPartLogin(stringObjectHashMap.get("openid").toString(), mCurrentOauthPlatform, platDB.getToken(), stringObjectHashMap.get("nickname").toString(),
                    stringObjectHashMap.get("nickname").toString(), stringObjectHashMap.get("headimgurl").toString(), "", "");
        }

    }

    @Override
    public void onError(Platform platform, int i, Throwable throwable) {
        dismissProgressDialog();
        throwable.printStackTrace();
        System.out.println("thirdPartLogin--->onError-->" );
    }

    @Override
    public void onCancel(Platform platform, int i) {
        dismissProgressDialog();
        System.out.println("thirdPartLogin--->onCancel-->");
    }

    public void thirdPartLogin(String oauthId, String oauthType, String oauthToken, String name, String nickname, String avatar, String email, String phone) {

        System.out.println("thirdPartLogin--->name-->" + name);

        RequestParams requestParams = new RequestParams();
        requestParams.put("appId", Constants.APP_ID);
        requestParams.put("appKey", Constants.APP_KEY);
        requestParams.put("oauthId", oauthId);
        requestParams.put("oauthType", oauthType);
        requestParams.put("oauthToken", oauthToken);
        requestParams.put("name", name);
        requestParams.put("nickname", nickname);
        requestParams.put("avatar", avatar);
        requestParams.put("email", email);
        requestParams.put("phone", phone);
        StickerHttpClient.postSync("/account/user/sso/login", requestParams,
                new TypeReference<ResponseData<LoginResult>>() {
                }.getType(),
                new StickerHttpResponseHandler<LoginResult>() {

                    @Override
                    public void onStart() {
                        showProgressDialog("登录中...");
                    }

                    @Override
                    public void onSuccess(LoginResult loginResult) {
//                        UserInfoManager.savePassword(password);

                        UserInfoManager.login(loginResult);

                        //  登录成功后的处理
                        ToastUtil.shortToast(LoginActivity.this, "登录成功");
                        CameraManager.getInst().openCamera(LoginActivity.this);
                        dismissProgressDialog();
                        LoginActivity.this.finish();
                    }

                    @Override
                    public void onFailure(String message) {
                        dismissProgressDialog();
                        ToastUtil.shortToast(LoginActivity.this, "登录失败：" + message);
                    }

                    @Override
                    public void onFinish() {
                        dismissProgressDialog();
                    }
                });

    }

    public static void launch(Context context) {
        Intent intent = new Intent(context, LoginActivity.class);
        context.startActivity(intent);
    }
}
