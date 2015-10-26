package com.yeah.android.model.sticker;

import com.yeah.android.model.user.Pageable;

import java.util.ArrayList;

/**
 * Created by litingchang on 15-10-24.
 */
public class StickerResponse {
    private int total;
    private Pageable pageable;
    private ArrayList<StickerInfo> content;

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

    public ArrayList<StickerInfo> getContent() {
        return content;
    }

    public void setContent(ArrayList<StickerInfo> content) {
        this.content = content;
    }
}
