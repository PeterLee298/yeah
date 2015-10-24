package com.yeah.android.view.sticker;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.PopupWindow;

import com.yeah.android.R;

/**
 * Created by litingchang on 15-10-24.
 */
public class StickerGroupPopWindow  extends PopupWindow{

    private Context mContext;
    private View mRootView;

    private StickerGroupClickListener mClickListener;

    StickerGroupPopWindow(Context context, StickerGroupClickListener clickListener) {
        super(context);
        mContext = context;
        mClickListener = clickListener;

        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mRootView = inflater.inflate(R.layout.popwindow_sticker_group, null);
    }




    public static interface StickerGroupClickListener{
        public void onStickerCroupItemClicke(int groupId);
    }


}
