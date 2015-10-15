package com.yeah.android.model.user;

/**
 * Created by litingchang on 15-10-14.
 *
 * 校验验证码结果
 */
public class VerifyConfirmResponse {

    /**
     * id : 192
     * contact : 17091088678
     * code : 895514
     * state : 0
     * createdAt : 1444754904000
     * updatedAt : 1444754904000
     */

    private int id;
    private String contact;
    private String code;
    private int state;
    private long createdAt;
    private long updatedAt;

    public void setId(int id) {
        this.id = id;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public void setCode(String code) {
        this.code = code;
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

    public String getContact() {
        return contact;
    }

    public String getCode() {
        return code;
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
}
