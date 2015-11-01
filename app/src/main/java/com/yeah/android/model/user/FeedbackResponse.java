package com.yeah.android.model.user;

/**
 * Created by litingchang on 15-11-1.
 */
public class FeedbackResponse {
    /**
     * id : 21
     * userId : 20111106
     * content : fasdfasdfasdfasdfasdf
     * state : 0
     * createdAt : 1446389484420
     * updatedAt : 1446389484420
     */

    private int id;
    private int userId;
    private String content;
    private int state;
    private long createdAt;
    private long updatedAt;

    public void setId(int id) {
        this.id = id;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public void setContent(String content) {
        this.content = content;
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

    public String getContent() {
        return content;
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
