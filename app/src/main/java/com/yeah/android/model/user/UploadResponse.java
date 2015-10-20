package com.yeah.android.model.user;

/**
 * Created by litingchang on 15-10-20.
 *
 * 上传图片返回结果
 */
public class UploadResponse {
    /**
     * id : 8
     * userId : 20111106
     * url : http://youryeah-test.oss-cn-beijing.aliyuncs.com/storage/4030/5861840/ViYMIXxGRApghLFUD1Q6hccX4ifjpyC.png
     * description :
     * state : 0
     * createdAt : 1445334050125
     * updatedAt : 1445334050125
     */

    private int id;
    private int userId;
    private String url;
    private String description;
    private int state;
    private long createdAt;
    private long updatedAt;

    public void setId(int id) {
        this.id = id;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public void setDescription(String description) {
        this.description = description;
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

    public int getUserId() {
        return userId;
    }

    public String getUrl() {
        return url;
    }

    public String getDescription() {
        return description;
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
