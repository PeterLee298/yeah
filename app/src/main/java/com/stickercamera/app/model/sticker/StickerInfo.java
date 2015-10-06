package com.stickercamera.app.model.sticker;

/**
 * Created by litingchang on 15-10-5.
 */
public class StickerInfo {

    /**
     * id : 20150717
     * icon : http://youryeah-test.oss-cn-beijing.aliyuncs.com/storage/4030/5281642/VgALDksC6zhwt2oCQMfBUCRHulJ2Z.jpg
     * title : ffffffew
     * description : adasdasd
     * groupId : 20150716
     * priority : 85
     * state : 0
     * createdAt : 1442576959000
     * updatedAt : 1442843410000
     */

    private int id;
    private String icon;
    private String title;
    private String description;
    private int groupId;
    private int priority;
    private int state;
    private long createdAt;
    private long updatedAt;

    public void setId(int id) {
        this.id = id;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setGroupId(int groupId) {
        this.groupId = groupId;
    }

    public void setPriority(int priority) {
        this.priority = priority;
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

    public String getIcon() {
        return icon;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public int getGroupId() {
        return groupId;
    }

    public int getPriority() {
        return priority;
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
