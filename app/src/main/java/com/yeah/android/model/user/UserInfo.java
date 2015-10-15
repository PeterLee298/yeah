package com.yeah.android.model.user;

/**
 * Created by litingchang on 15-10-8.
 * 用户信息
 */
public class UserInfo {

    /**
     * id : 20111114
     * phone : 17091088678
     * email :
     * password : f874834766c4ec63d4173738ec88ba31
     * name :
     * nickname :
     * avatar :
     * birthday : 1444448942000
     * horoscope : 0
     * deviceId : 0
     * ownerId : 13
     * state : 0
     * createdAt : 1444448942000
     * updatedAt : 1444448943000
     */

    private int id;
    private String phone;
    private String email;
    private String password;
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

    public void setPassword(String password) {
        this.password = password;
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

    public String getPassword() {
        return password;
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
