package com.yeah.android.model.user;

/**
 * Created by litingchang on 15-10-7.
 * 适用 登录、注册返回结果
 *
 */
public class LoginResult {

    /**
     * id : 20150728
     * tokenKey : 579b9a6cd45abbf7a9ad7a4be99600f9
     * userId : 20111106
     * appId : 20111104
     * state : 0
     * createdAt : 1444186160092
     * updatedAt : 1444186160092
     */

    private int id;
    private String token;
    private int accountId;
    private int appId;
    private int state;
    private long createdAt;
    private long updatedAt;
    private LoginResultExt extra;

    public void setId(int id) {
        this.id = id;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public void setAccountId(int userId) {
        this.accountId = userId;
    }

    public void setAppId(int appId) {
        this.appId = appId;
    }

    public void setState(int state) {
        this.state = state;
    }

    public void setCreatedAt(long createdAt) {
        this.createdAt = createdAt;
    }

    public void setUpdatedAt(long updatedAt) {
        this.updatedAt = updatedAt;
    }

    public int getId() {
        return id;
    }

    public String getToken() {
        return token;
    }

    public int getUserId() {
        return accountId;
    }

    public int getAccountId() {
        return appId;
    }

    public int getState() {
        return state;
    }

    public long getCreatedAt() {
        return createdAt;
    }

    public long getUpdatedAt() {
        return updatedAt;
    }

    public LoginResultExt getExtra() {
        return extra;
    }

    public void setExtra(LoginResultExt extra) {
        this.extra = extra;
    }
}
