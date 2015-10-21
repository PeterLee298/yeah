package com.yeah.android.model.user;

/**
 * Created by litingchang on 15-10-21.
 */
public class Pageable {
    /**
     * page : 0
     * size : 10
     */

    private int page;
    private int size;

    public void setPage(int page) {
        this.page = page;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public int getPage() {
        return page;
    }

    public int getSize() {
        return size;
    }
}
