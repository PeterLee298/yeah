package com.yeah.android.model.sticker;

/**
 * Created by litingchang on 15-10-24.
 */
public class StickerHot {
    /**
     * id : 20150747
     * groupId : 20150722
     * categoryId : 20150757
     * priority : 30
     * state : 1
     * createdAt : 1445529299000
     * updatedAt : 1445533137000
     */

    private int id;
    private int groupId;
    private int categoryId;
    private int priority;
    private int state;
    private long createdAt;
    private long updatedAt;
    private ExtraGroup extra;

    public void setId(int id) {
        this.id = id;
    }

    public void setGroupId(int groupId) {
        this.groupId = groupId;
    }

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
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

    public int getGroupId() {
        return groupId;
    }

    public int getCategoryId() {
        return categoryId;
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

    public ExtraGroup getExtra() {
        return extra;
    }

    public void setExtra(ExtraGroup extra) {
        this.extra = extra;
    }
}
