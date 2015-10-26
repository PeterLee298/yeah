package com.yeah.android.view.sticker;

import android.app.ActionBar;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupWindow;

import com.yeah.android.R;
import com.yeah.android.YeahApp;
import com.yeah.android.model.sticker.StickerInfo;
import com.yeah.android.utils.AppUtils;

import java.util.List;

/**
 * Created by litingchang on 15-10-24.
 */
public class StickerGroupPopWindow  extends PopupWindow{

    private Context mContext;
    private View mRootView;

    private List<StickerInfo> mStickerList;

    private StickerGroupClickListener mClickListener;

    public StickerGroupPopWindow(Context context, StickerGroupClickListener clickListener) {
        super(context);
        mContext = context;
        mClickListener = clickListener;

        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mRootView = inflater.inflate(R.layout.popwindow_sticker_group, null);

        this.setContentView(mRootView);
        this.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        this.setHeight(ViewGroup.LayoutParams.MATCH_PARENT);
        this.setFocusable(true);
        this.setAnimationStyle(R.style.Animation_AppCompat_DropDownUp);
        this.setBackgroundDrawable(new ColorDrawable(0xCC000000));

    }

    public void setStickerList(List<StickerInfo> stickerList) {
        mStickerList = stickerList;

    }




    public static interface StickerGroupClickListener{
        public void onStickerCroupItemClicke(int position);
    }


}
