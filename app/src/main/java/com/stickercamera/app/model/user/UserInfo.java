package com.stickercamera.app.model.user;

/**
 * Created by litingchang on 15-10-8.
 * 用户信息
 */
public class UserInfo {

    /**
     * id : 20111106
     * phone : 15615236548
     * email : 11111111
     * name : 1111111
     * nickname : 1111111111
     * avatar : 1111111
     * birthday : 1440465912000
     * horoscope : 23
     * deviceId : 0
     * ownerId : 5
     * state : 0
     * createdAt : 1442845040000
     * updatedAt : 1444405217000
     */

    private int id;
    private String phone;
    private String email;
    private String name;
    private String nickname;
    private String avatar;
    private long birthday;
    private int horoscope;
    private int deviceId;
    private int ownerId;
    private int state;
    private long createdAt;
    private long updatedAt;

    public void setId(int id) {
        this.id = id;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public void setBirthday(long birthday) {
        this.birthday = birthday;
    }

    public void setHoroscope(int horoscope) {
        this.horoscope = horoscope;
    }

    public void setDeviceId(int deviceId) {
        this.deviceId = deviceId;
    }

    public void setOwnerId(int ownerId) {
        this.ownerId = ownerId;
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

    public String getPhone() {
        return phone;
    }

    public String getEmail() {
        return email;
    }

    public String getName() {
        return name;
    }

    public String getNickname() {
        return nickname;
    }

    public String getAvatar() {
        return avatar;
    }

    public long getBirthday() {
        return birthday;
    }

    public int getHoroscope() {
        return horoscope;
    }

    public int getDeviceId() {
        return deviceId;
    }

    public int getOwnerId() {
        return ownerId;
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
