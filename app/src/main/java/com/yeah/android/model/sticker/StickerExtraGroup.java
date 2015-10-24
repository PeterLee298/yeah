package com.yeah.android.model.sticker;

/**
 * Created by litingchang on 15-10-24.
 */
public class StickerExtraGroup {
    /**
     * id : 20150721
     * icon : http://youryeah-test.oss-cn-beijing.aliyuncs.com/storage/4030/5461660/VguBDXxGRAGEKotBAFFk5Fo2SV8vcbl9.png
     * title : 星座宝宝
     * description : 可爱的星座宝宝
     * stickerCount : 19
     * priority : 12
     * state : 1
     * createdAt : 1443594458000
     * updatedAt : 1443595857000
     */

    private int id;
    private String icon;
    private String title;
    private String description;
    private int stickerCount;
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

    public void setStickerCount(int stickerCount) {
        this.stickerCount = stickerCount;
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

    public int getStickerCount() {
        return stickerCount;
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
