package com.yeah.android.activity.camera.ui;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import com.yeah.android.activity.BaseActivity;
import com.yeah.android.activity.user.PhotoPostAvtivity;

/**
 * Created by litingchang on 15-10-26.
 */
public class StickerListActivity extends BaseActivity {

    private static final String STICKER_GROUP_ID = "sticker_group_id";

    private int mStickerGroupId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mStickerGroupId = getIntent().getIntExtra(STICKER_GROUP_ID, 0);
    }

    public static void launch(Activity activity, int requestId, int stickerGroupId) {
        Intent intent = new Intent(activity, PhotoPostAvtivity.class);
        intent.putExtra(STICKER_GROUP_ID, stickerGroupId);
        activity.startActivityForResult(intent, requestId);
    }
}
