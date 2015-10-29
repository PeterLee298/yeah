package com.yeah.android.view.sticker;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.view.Window;

import com.yeah.android.R;
import com.yeah.android.model.sticker.StickerInfo;

import butterknife.OnClick;

/**
 * Created by litingchang on 15-10-29.
 */
public class StickerSampleDialog extends Dialog implements View.OnClickListener{


    private Context mContext;
    private StickerInfo mStickerInfo;

    private View.OnClickListener useBtnClickLisener;

    public StickerSampleDialog(Context context, StickerInfo stickerInfo) {
        super(context, R.style.sticker_info_dalog);
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        mStickerInfo = stickerInfo;

        setContentView(R.layout.dialog_sticker_sample);



    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            default:
                break;
        }
    }

    public void setOnUseBtnClickeListener(View.OnClickListener clickeListener) {
        useBtnClickLisener = clickeListener;
    }
}
