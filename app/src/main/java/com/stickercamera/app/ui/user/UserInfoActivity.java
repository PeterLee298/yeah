package com.stickercamera.app.ui.user;

import android.os.Bundle;
import android.widget.RelativeLayout;
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
import com.stickercamera.app.model.user.UserInfo;
import com.stickercamera.base.BaseActivity;
import com.yeah.stickercamera.R;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by litingchang on 15-10-8.
 * <p>
 * 用户信息
 */
public class UserInfoActivity extends BaseActivity {

    @InjectView(R.id.nickname)
    TextView nickname;
    @InjectView(R.id.info_nickname_root)
    RelativeLayout infoNicknameRoot;
    @InjectView(R.id.sex)
    TextView sex;
    @InjectView(R.id.info_sex_root)
    RelativeLayout infoSexRoot;
    @InjectView(R.id.birthday)
    TextView birthday;
    @InjectView(R.id.info_birthday_root)
    RelativeLayout infoBirthdayRoot;
    @InjectView(R.id.constellation)
    TextView constellation;
    @InjectView(R.id.info_constellation_root)
    RelativeLayout infoConstellationRoot;

    private UserInfo mUserInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_userinfo);
        ButterKnife.inject(this);


        if(UserInfoManager.isLogin()) {
            int loginId = UserInfoManager.getUserId();
            String token = UserInfoManager.getToken();
            getUserInfo(loginId, token);
        } else {
            ToastUtil.shortToast(UserInfoActivity.this, "您尚未登录");
        }


    }


    private void getUserInfo(int loginId, String token) {
        RequestParams requestParams = new RequestParams();
        requestParams.put("appId", AppConstants.APP_ID);
        requestParams.put("appKey", AppConstants.APP_KEY);
        requestParams.put("loginId", loginId);
        requestParams.put("loginToken", token);
        StickerHttpClient.post("/account/user/login", requestParams,
                new TypeReference<ResponseData<UserInfo>>() {
                }.getType(),
                new StickerHttpResponseHandler<UserInfo>() {

                    @Override
                    public void onStart() {
                        showProgressDialog("获取用户信息...");
                    }

                    @Override
                    public void onSuccess(UserInfo userInfo) {

                        mUserInfo = userInfo;

                        nickname.setText(StringUtils.makeSafe(userInfo.getNickname()));
//                        sex.setText(StringUtils.makeSafe(userInfo.get()));
//                        nickname.setText(StringUtils.makeSafe(userInfo.getNickname()));
//                        nickname.setText(StringUtils.makeSafe(userInfo.getNickname()));
                    }

                    @Override
                    public void onFailure(String message) {
                        LogUtil.e("onFailure", message);
                        ToastUtil.shortToast(UserInfoActivity.this, "获取用户信息失败：" + message);
                    }

                    @Override
                    public void onFinish() {
                        dismissProgressDialog();
                    }
                });
    }

    private void updateUserInfo() {

    }
}
