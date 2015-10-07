package com.stickercamera.app.manager;

import com.common.util.PreferencesUtils;
import com.common.util.StringUtils;
import com.stickercamera.App;

import java.util.jar.Attributes;

/**
 * Created by litingchang on 15-10-7.
 */
public class UserInfoManager {

    private static final String TOKEN = "yeah_token";
    private static final String NAME = "yeah_name";
    private static final String PASSWORD = "yeah_password";

    private static String mToken;

    public static boolean isLogin() {
        mToken = PreferencesUtils.getString(App.getApp(), TOKEN);
        if(StringUtils.isEmpty(mToken)) {
            return false;
        }
        return true;
    }

    public static void saveToken(final String token) {
        mToken = token;
        PreferencesUtils.putString(App.getApp(), TOKEN, token);
    }

    public static String getToken() {
        mToken = PreferencesUtils.getString(App.getApp(), TOKEN);
        return mToken;
    }

    public static void saveName(final String name) {
        PreferencesUtils.putString(App.getApp(), NAME, name);
    }

    public static String getName() {
        return  PreferencesUtils.getString(App.getApp(), NAME, "");
    }

    public static void savePassword(final String password) {
        PreferencesUtils.putString(App.getApp(), NAME, password);
    }

    public static String getPassword() {
        return  PreferencesUtils.getString(App.getApp(), PASSWORD, "");
    }

    public static void loginOut() {
        mToken = "";
        PreferencesUtils.putString(App.getApp(), TOKEN, "");
        PreferencesUtils.putString(App.getApp(), PASSWORD, "");
    }

    public static void saveId(final int id) {
        PreferencesUtils.putInt(App.getApp(), "yeah_id", id);
    }

    public static int getId() {
        return  PreferencesUtils.getInt(App.getApp(), "yeah_id", 0);
    }

    public static void saveUserId(final int id) {
        PreferencesUtils.putInt(App.getApp(), "yeah_user_id", id);
    }

    public static int getUserId() {
        return  PreferencesUtils.getInt(App.getApp(), "yeah_user_id", 0);
    }
}
