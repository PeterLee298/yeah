package com.yeah.android.view;

import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;
import android.widget.PopupWindow;

import com.yeah.android.R;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by liuchao on 10/15/14.
 */

public class CameraTopBarMenu extends PopupWindow implements View.OnClickListener{

    private OnMenuClickListener mOnMenuClickListener;
    @InjectView(R.id.camera_topbar_menu_1)
    ImageView mBtn1;
    @InjectView(R.id.camera_topbar_menu_2)
    ImageView mTimeBtn;
    @InjectView(R.id.camera_topbar_menu_3)
    ImageView mFlashBtn;
    @InjectView(R.id.camera_topbar_menu_4)
    ImageView mGridBtn;

    public CameraTopBarMenu(View contentView, OnMenuClickListener onMenuClickListener){
        super(contentView, LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT, false);
        ButterKnife.inject(this, contentView);
        this.mOnMenuClickListener = onMenuClickListener;
        mBtn1.setOnClickListener(this);
        mTimeBtn.setOnClickListener(this);
        mFlashBtn.setOnClickListener(this);
        mGridBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.camera_topbar_menu_1:
                if(mOnMenuClickListener != null){
                    mOnMenuClickListener.onBtn1Click();
                }
                break;
            case R.id.camera_topbar_menu_2:
                if(mOnMenuClickListener != null){
                    mOnMenuClickListener.onTimeBtnClick();
                }
                break;
            case R.id.camera_topbar_menu_3:
                if(mOnMenuClickListener != null){
                    mOnMenuClickListener.onFlashBtnClick((ImageView)v);
                }
                break;
            case R.id.camera_topbar_menu_4:
                if(mOnMenuClickListener != null){
                    mOnMenuClickListener.onGridBtnClick();
                }
                break;
        }
    }

    public static interface OnMenuClickListener{
        public void onBtn1Click();
        public void onTimeBtnClick();
        public void onFlashBtnClick(ImageView flashBtn);
        public void onGridBtnClick();
    }

}
