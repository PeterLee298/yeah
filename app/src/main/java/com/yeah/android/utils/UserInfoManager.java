package com.yeah.android.utils;

import com.yeah.android.YeahApp;

/**
 * Created by litingchang on 15-10-7.
 */
public class UserInfoManager {

    private static final String TOKEN = "yeah_token";
    private static final String NAME = "yeah_name";
    private static final String PASSWORD = "yeah_password";

    private static String mToken;

    public static boolean isLogin() {
        mToken = PreferencesUtils.getString(YeahApp.getApp(), TOKEN);
        if(StringUtils.isEmpty(mToken)) {
            return false;
        }
        return true;
    }

    public static void saveToken(final String token) {
        mToken = token;
        PreferencesUtils.putString(YeahApp.getApp(), TOKEN, token);
    }

    public static String getToken() {
        mToken = PreferencesUtils.getString(YeahApp.getApp(), TOKEN);
        return mToken;
    }

    public static void saveName(final String name) {
        PreferencesUtils.putString(YeahApp.getApp(), NAME, name);
    }

    public static String getName() {
        return  PreferencesUtils.getString(YeahApp.getApp(), NAME, "");
    }

    public static void savePassword(final String password) {
        PreferencesUtils.putString(YeahApp.getApp(), NAME, password);
    }

    public static String getPassword() {
        return  PreferencesUtils.getString(YeahApp.getApp(), PASSWORD, "");
    }

    public static void loginOut() {
        mToken = "";
        PreferencesUtils.putString(YeahApp.getApp(), TOKEN, "");
        PreferencesUtils.putString(YeahApp.getApp(), PASSWORD, "");
    }

    public static void saveId(final int id) {
        PreferencesUtils.putInt(YeahApp.getApp(), "yeah_id", id);
    }

    public static int getId() {
        return  PreferencesUtils.getInt(YeahApp.getApp(), "yeah_id", 0);
    }

    public static void saveUserId(final int id) {
        PreferencesUtils.putInt(YeahApp.getApp(), "yeah_user_id", id);
    }

    public static int getUserId() {
        return  PreferencesUtils.getInt(YeahApp.getApp(), "yeah_user_id", 0);
    }
}
