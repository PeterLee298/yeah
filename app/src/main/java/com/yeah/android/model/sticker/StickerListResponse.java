package com.yeah.android.model.sticker;

import com.yeah.android.model.user.Pageable;

import java.util.ArrayList;

/**
 * Created by litingchang on 15-10-24.
 */
public class StickerListResponse {
    private int total;
    private Pageable pageable;
    private ArrayList<StickerListItem> content;

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public Pageable getPageable() {
        return pageable;
    }

    public void setPageable(Pageable pageable) {
        this.pageable = pageable;
    }

    public ArrayList<StickerListItem> getContent() {
        return content;
    }

    public void setContent(ArrayList<StickerListItem> content) {
        this.content = content;
    }
}
