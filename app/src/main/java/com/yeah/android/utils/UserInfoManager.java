package com.yeah.android.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.yeah.android.YeahApp;
import com.yeah.android.model.common.ResponseData;
import com.yeah.android.model.user.LoginResult;
import com.yeah.android.model.user.LoginResultExt;
import com.yeah.android.model.user.UserInfo;

/**
 * Created by litingchang on 15-10-7.
 */
public class UserInfoManager {

    private static final String TAG = UserInfoManager.class.getSimpleName();

    private static final String PASSWORD = "yeah_password";

    private static final String USER_INFO = "yeah_user_info";

    private static LoginResult mLoginResult;
    private static UserInfo mUserInfo;

    public static void login(LoginResult loginResult) {
        if(loginResult == null) {
            LogUtil.e(TAG, "save error : loginResult is null");
        }
        mLoginResult = loginResult;
        String infoStr = JSON.toJSONString(loginResult);
        PreferencesUtils.putString(YeahApp.getApp(), USER_INFO, infoStr);
    }

    public static void logout() {
        mLoginResult = null;
        PreferencesUtils.putString(YeahApp.getApp(), USER_INFO, "");

    }

    public static LoginResult getLoginResult() {

        if(mLoginResult != null) {
            return mLoginResult;
        }

        String infoStr = PreferencesUtils.getString(YeahApp.getApp(), USER_INFO);
        if(StringUtils.isEmpty(infoStr)) {
            return null;
        }

        try {
            mLoginResult = JSON.parseObject(infoStr, new TypeReference<LoginResult>(){}.getType());
        } catch (Exception e) {
            return null;
        }

        return mLoginResult;
    }

    public static boolean isLogin() {

        if(mLoginResult != null && StringUtils.isEmpty(mLoginResult.getToken())) {
            return true;
        }

        mLoginResult = getLoginResult();
        if(mLoginResult == null) {
            return false;
        }

        if(StringUtils.isEmpty(mLoginResult.getToken())) {
            return false;
        }

        return true;
    }

    public static void updateUserInfo(UserInfo userInfo) {
        if(userInfo == null) {
            LogUtil.e(TAG, "save error : userInfo is null");
        }

        mUserInfo = userInfo;

        if(mLoginResult == null) {
            mLoginResult = getLoginResult();
        }

        LoginResultExt ext = new LoginResultExt();
        ext.setUser(userInfo);
        mLoginResult.setExtra(ext);
        String infoStr = JSON.toJSONString(mLoginResult);
        PreferencesUtils.putString(YeahApp.getApp(), USER_INFO, infoStr);
    }

    public static UserInfo getUserInfo() {

        if(mLoginResult == null
                || mLoginResult.getExtra() == null
                || mLoginResult.getExtra().getUser() == null) {
            mUserInfo = new UserInfo();
            mUserInfo.setNickname("设置昵称");
            mUserInfo.setNickname("设置昵称");
            mUserInfo.setBirthday(System.currentTimeMillis());
            mUserInfo.setHoroscope(1);
            mUserInfo.setAvatar("");
        } else {
            mUserInfo = mLoginResult.getExtra().getUser();
        }

        return mUserInfo;
    }

    public static int getId() {
        if(isLogin()) {
            return mLoginResult.getId();
        }

        return 0;
    }


    public static String getToken() {
        if(isLogin()) {
            return mLoginResult.getToken();
        }

        return null;
    }

    public static int getUserId() {
        if(isLogin()) {
            return mLoginResult.getUserId();
        }

        return 0;
    }



    public static void savePassword(final String password) {
        PreferencesUtils.putString(YeahApp.getApp(), PASSWORD, password);
    }

    public static String getPassword() {
        return PreferencesUtils.getString(YeahApp.getApp(), PASSWORD, "");
    }
}
