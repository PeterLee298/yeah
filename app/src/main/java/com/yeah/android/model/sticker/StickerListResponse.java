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
}
