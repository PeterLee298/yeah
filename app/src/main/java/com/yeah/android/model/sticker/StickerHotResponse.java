package com.yeah.android.model.sticker;

import com.yeah.android.model.user.Pageable;
import com.yeah.android.model.user.Photo;

import java.util.ArrayList;

/**
 * Created by litingchang on 15-10-24.
 */
public class StickerHotResponse {
    private int total;
    private Pageable pageable;
    private ArrayList<StickerHot> content;

    public ArrayList<StickerHot> getContent() {
        return content;
    }

    public void setContent(ArrayList<StickerHot> content) {
        this.content = content;
    }

    public Pageable getPageable() {
        return pageable;
    }

    public void setPageable(Pageable pageable) {
        this.pageable = pageable;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }
}
