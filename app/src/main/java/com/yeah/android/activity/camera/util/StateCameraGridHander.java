package com.yeah.android.activity.camera.util;

import android.view.View;
import android.widget.ImageView;
import com.yeah.android.view.CameraTopBarMenu.OnMenuClickListener;

import com.yeah.android.R;

/**
 * Created by liuchao on 10/17/15.
 */
public class StateCameraGridHander implements View.OnClickListener{
    //点击拍照按钮
    public static final int STATE_GRID_OFF = 1001;
    //点击屏幕拍照
    public static final int STATE_GRID_ON = 1002;

    private int mCurrentState = STATE_GRID_OFF;

    private ImageView mTimerBtn;

    private OnMenuClickListener mOnMenuClickListener;

    public StateCameraGridHander(ImageView imageView,OnMenuClickListener onMenuClickListener ){
        imageView.setOnClickListener(this);
        mOnMenuClickListener = onMenuClickListener;
        this.mTimerBtn = imageView;
    }

    public void changeState(){
        if(mCurrentState == STATE_GRID_OFF){
            mTimerBtn.setImageResource(R.drawable.ic_camera_topbar_menu_4_clicked);
            mCurrentState = STATE_GRID_ON;
        }else{
            mTimerBtn.setImageResource(R.drawable.ic_camera_topbar_menu_4);
            mCurrentState = STATE_GRID_OFF;
        }
    }

    @Override
    public void onClick(View v) {
        changeState();
        if(mOnMenuClickListener != null){
            mOnMenuClickListener.onGridBtnClick(mCurrentState);
        }
    }

    public int getState(){
        return mCurrentState;
    }
}
