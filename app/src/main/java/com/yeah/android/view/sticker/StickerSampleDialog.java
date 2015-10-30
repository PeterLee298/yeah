package com.yeah.android.view.sticker;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.yeah.android.R;
import com.yeah.android.model.sticker.StickerInfo;
import com.yeah.android.utils.ImageLoaderUtils;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

/**
 * Created by litingchang on 15-10-29.
 */
public class StickerSampleDialog extends Dialog {


    @InjectView(R.id.sticker_info_title)
    TextView stickerInfoTitle;
    @InjectView(R.id.sticker_sample_imageview)
    ImageView stickerSampleImageview;
    @InjectView(R.id.sticker_sample_description)
    TextView stickerSampleDescription;
    @InjectView(R.id.sticker_sample_use_btn)
    TextView stickerSampleUseBtn;
    @InjectView(R.id.sticker_sample_cancel_btn)
    TextView stickerSampleCancelBtn;

    private Context mContext;
    private StickerInfo mStickerInfo;

    private OnUseClickListener useBtnClickLisener;

    public StickerSampleDialog(Context context, StickerInfo stickerInfo) {
        super(context, R.style.sticker_info_dalog);
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        mStickerInfo = stickerInfo;

        setContentView(R.layout.dialog_sticker_sample);
        ButterKnife.inject(this);

        stickerInfoTitle.setText(mStickerInfo.getTitle());
        ImageLoader.getInstance().displayImage(mStickerInfo.getSamplePhoto(), stickerSampleImageview);
        stickerSampleDescription.setText(mStickerInfo.getDescription());
    }

    @OnClick(R.id.sticker_sample_use_btn)
    public void useBtnClicked() {
        if(useBtnClickLisener != null) {
            useBtnClickLisener.onClick(mStickerInfo);
            dismiss();
        }
    }

    @OnClick(R.id.sticker_sample_cancel_btn)
    public void cancelBtnClicked() {
        this.dismiss();
    }

    public void setOnUseBtnClickeListener(OnUseClickListener clickeListener) {
        useBtnClickLisener = clickeListener;
    }

    public interface OnUseClickListener{
        public void onClick(StickerInfo info);
    }
}
