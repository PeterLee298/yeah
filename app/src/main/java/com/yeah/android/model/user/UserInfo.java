package com.yeah.android.model.user;

/**
 * Created by litingchang on 15-10-8.
 * 用户信息
 */
public class UserInfo {

    /**
     * id : 20111164
     * accountId : 20111171
     * phone : 15615236547
     * email : 11111111
     * password : ab730a067e7cf0536eb5703e029c2d30
     * name : 1111111
     * nickname : 1111111111
     * avatar : 1111111
     * sex : 2
     * birthday : 1440465912000
     * horoscope : 23
     * state : 0
     * createdAt : 1447245189000
     * updatedAt : 1447253960000
     */

    private int id;
    private int accountId;
    private String phone;
    private String email;
    private String password;
    private String name;
    private String nickname;
    private String avatar;
    private int sex;
    private long birthday;
    private int horoscope;
    private int state;
    private long createdAt;
    private long updatedAt;


    public void setId(int id) {
        this.id = id;
    }

    public void setAccountId(int accountId) {
        this.accountId = accountId;
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

    public void setSex(int sex) {
        this.sex = sex;
    }

    public void setBirthday(long birthday) {
        this.birthday = birthday;
    }

    public void setHoroscope(int horoscope) {
        this.horoscope = horoscope;
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

    public int getAccountId() {
        return accountId;
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

    public int getSex() {
        return sex;
    }

    public long getBirthday() {
        return birthday;
    }

    public int getHoroscope() {
        return horoscope;
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
