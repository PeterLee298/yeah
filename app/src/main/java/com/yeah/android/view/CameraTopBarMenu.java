package com.yeah.android.view;

import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;

import com.yeah.android.R;
import com.yeah.android.activity.camera.util.StateCameraGridHander;
import com.yeah.android.activity.camera.util.StateCameraTakePhotoHander;
import com.yeah.android.activity.camera.util.StateCameraTimerHander;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by liuchao on 10/15/14.
 */

public class CameraTopBarMenu extends PopupWindow implements View.OnClickListener{

    private OnMenuClickListener mOnMenuClickListener;
    @InjectView(R.id.camera_topbar_menu_1_layout)
    RelativeLayout mBtn1Layout;
    @InjectView(R.id.camera_topbar_menu_1)
    ImageView mBtn1;
    @InjectView(R.id.camera_topbar_menu_2)
    ImageView mTimeBtn;
    @InjectView(R.id.camera_topbar_menu_3)
    ImageView mFlashBtn;
    @InjectView(R.id.camera_topbar_menu_4)
    ImageView mGridBtn;

    private StateCameraTakePhotoHander mStateCameraTakePhotoHander;
    private StateCameraTimerHander mStateCameraTimerHander;
    private StateCameraGridHander mStateCameraGridHander;

    public CameraTopBarMenu(View contentView, OnMenuClickListener onMenuClickListener) {
        super(contentView, LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT, false);
        ButterKnife.inject(this, contentView);
        this.mOnMenuClickListener = onMenuClickListener;
        mFlashBtn.setOnClickListener(this);
        mStateCameraTakePhotoHander = new StateCameraTakePhotoHander(mBtn1Layout, mBtn1);
        mStateCameraTimerHander = new StateCameraTimerHander(mTimeBtn);
        mStateCameraGridHander = new StateCameraGridHander(mGridBtn, onMenuClickListener);
    }

    public int getCameraTakeState(){
        if(mStateCameraTakePhotoHander != null){
            return mStateCameraTakePhotoHander.getState();
        }
        return StateCameraTakePhotoHander.STATE_CLICK_TAKE_BTN;
    }

    public int getCameraTimerState(){
        if(mStateCameraTimerHander != null){
            return mStateCameraTimerHander.getState();
        }
        return StateCameraTimerHander.STATE_TIMER_OFF;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.camera_topbar_menu_3:
                if(mOnMenuClickListener != null){
                    mOnMenuClickListener.onFlashBtnClick((ImageView)v);
                }
                break;
        }
    }

    public static interface OnMenuClickListener{
        public void onFlashBtnClick(ImageView flashBtn);
        public void onGridBtnClick(int state);
    }

}
