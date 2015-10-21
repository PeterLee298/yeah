package com.yeah.android.model.user;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by litingchang on 15-10-21.
 */
public class UserPhotoResponse {

    private int total;
    private Pageable pageable;
    private ArrayList<Photo> content;

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

    public ArrayList<Photo> getContent() {
        return content;
    }

    public void setContent(ArrayList<Photo> content) {
        this.content = content;
    }
}
