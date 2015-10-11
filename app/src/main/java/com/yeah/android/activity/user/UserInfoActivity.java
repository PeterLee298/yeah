package com.yeah.android.activity.user;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.ArrayMap;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.TypeReference;
import com.yeah.android.utils.LogUtil;
import com.yeah.android.utils.StringUtils;
import com.yeah.android.utils.ToastUtil;
import com.loopj.android.http.RequestParams;
import com.yeah.android.utils.Constants;
import com.yeah.android.net.http.StickerHttpClient;
import com.yeah.android.net.http.StickerHttpResponseHandler;
import com.yeah.android.utils.UserInfoManager;
import com.yeah.android.model.common.ResponseData;
import com.yeah.android.model.user.UserInfo;
import com.yeah.android.activity.BaseActivity;
import com.yeah.android.R;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

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
    private Map<String, String> updateInfo = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_userinfo);
        ButterKnife.inject(this);


        if(UserInfoManager.isLogin()) {
            setUserInfo();
        } else {
            ToastUtil.shortToast(UserInfoActivity.this, "您尚未登录");
        }

        titleBar.setRightBtnOnclickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                updateUserInfo();
            }
        });
    }


    private void setUserInfo() {

        mUserInfo = UserInfoManager.getUserInfo();

        nickname.setText(StringUtils.makeSafe(mUserInfo.getNickname()));
    }

//    private void getUserInfo(int loginId, String token) {
//        RequestParams requestParams = new RequestParams();
//        requestParams.put("appId", Constants.APP_ID);
//        requestParams.put("appKey", Constants.APP_KEY);
//        requestParams.put("loginId", loginId);
//        requestParams.put("loginToken", token);
//        StickerHttpClient.post("/account/user/info", requestParams,
//                new TypeReference<ResponseData<UserInfo>>() {
//                }.getType(),
//                new StickerHttpResponseHandler<UserInfo>() {
//
//                    @Override
//                    public void onStart() {
//                        showProgressDialog("获取用户信息...");
//                    }
//
//                    @Override
//                    public void onSuccess(UserInfo userInfo) {
//
//                        mUserInfo = userInfo;
//
//                        nickname.setText(StringUtils.makeSafe(userInfo.getNickname()));
////                        sex.setText(StringUtils.makeSafe(userInfo.get));
////                        birthday.setText(StringUtils.makeSafe(userInfo.getNickname()));
////                        constellation.setText(StringUtils.makeSafe(userInfo.getNickname()));
//                    }
//
//                    @Override
//                    public void onFailure(String message) {
//                        LogUtil.e("onFailure", message);
//                        ToastUtil.shortToast(UserInfoActivity.this, "获取用户信息失败：" + message);
//                    }
//
//                    @Override
//                    public void onFinish() {
//                        dismissProgressDialog();
//                    }
//                });
//    }

    private void updateUserInfo() {

        if(!UserInfoManager.isLogin()) {
            ToastUtil.shortToast(UserInfoActivity.this, "你尚未登录，请登录后重试");
            return;
        }

        updateInfo.put("name", "牛轰轰");
        updateInfo.put("nickname", "牛轰轰");
        updateInfo.put("avatar", "http://img5.duitang.com/uploads/item/201409/08/20140908021251_Z2y2t.thumb.700_0.jpeg");
        updateInfo.put("birthday", "1440465912123");
        updateInfo.put("horoscope", "2");

        mUserInfo.setName("牛轰轰");
        mUserInfo.setNickname("牛轰轰");
        mUserInfo.setAvatar("http://img5.duitang.com/uploads/item/201409/08/20140908021251_Z2y2t.thumb.700_0.jpeg");
        mUserInfo.setBirthday(1440465912123L);
        mUserInfo.setHoroscope(2);


//        Calendar calendar = Calendar.getInstance();
//        calendar.set();
//        calendar.getTimeInMillis();

        int loginId = UserInfoManager.getUserInfo().getId();
        String token = UserInfoManager.getToken();

        RequestParams requestParams = new RequestParams();
        requestParams.put("appId", Constants.APP_ID);
        requestParams.put("appKey", Constants.APP_KEY);
        requestParams.put("loginId", loginId);
        requestParams.put("loginToken", token);


        Set<String> keys = updateInfo.keySet();
        Iterator<String> it = keys.iterator();
        while (it.hasNext()) {
            String key = it.next();
            requestParams.put(key, updateInfo.get(key));
        }


        StickerHttpClient.post("/account/user/update", requestParams,
                new TypeReference<ResponseData<UserInfo>>() {
                }.getType(),
                new StickerHttpResponseHandler<UserInfo>() {

                    @Override
                    public void onStart() {
                        showProgressDialog("更新用户信息...");
                    }

                    @Override
                    public void onSuccess(UserInfo userInfo) {
                        ToastUtil.shortToast(UserInfoActivity.this, "更新用户信息成功");
                    }

                    @Override
                    public void onFailure(String message) {
                        LogUtil.e("onFailure", message);
                        ToastUtil.shortToast(UserInfoActivity.this, "更新用户信息失败：" + message);
                    }

                    @Override
                    public void onFinish() {
                        dismissProgressDialog();
                    }
                });
    }

    public static void launch(Context context) {
        Intent intent = new Intent(context, UserInfoActivity.class);
        context.startActivity(intent);
    }
}
