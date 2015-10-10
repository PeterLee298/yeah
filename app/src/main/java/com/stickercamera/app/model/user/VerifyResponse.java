package com.stickercamera.app.model.user;

/**
 * Created by litingchang on 15-10-10.
 * 验证码返回结果
 */
public class VerifyResponse {
    /**
     * id : 158
     * code : 501665
     * createdAt : 1444447172970
     */

    private int id;
    private String code;
    private long createdAt;

    public void setId(int id) {
        this.id = id;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public void setCreatedAt(long createdAt) {
        this.createdAt = createdAt;
    }

    public int getId() {
        return id;
    }

    public String getCode() {
        return code;
    }

    public long getCreatedAt() {
        return createdAt;
    }
}
