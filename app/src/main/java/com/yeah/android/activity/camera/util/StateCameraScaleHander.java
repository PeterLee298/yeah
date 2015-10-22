package com.yeah.android.activity.camera.util;

import android.view.View;
import android.widget.ImageView;

import com.yeah.android.R;

/**
 * Created by liuchao on 10/17/15.
 */
public class StateCameraScaleHander implements View.OnClickListener{
    //点击拍照按钮
    public static final int STATE_SACLE_11 = 1001;
    //点击屏幕拍照
    public static final int STATE_SCALE_43 = 1002;

    private int mCurrentState = STATE_SACLE_11;

    private ImageView mScaleBtn;

    private OnScaleChangeListener mOnScaleChangeListener;

    public StateCameraScaleHander(ImageView imageView, OnScaleChangeListener onScaleChangeListener){
        imageView.setOnClickListener(this);
        this.mOnScaleChangeListener = onScaleChangeListener;
        this.mScaleBtn = imageView;
    }

    public void changeState(){
        if(mCurrentState == STATE_SACLE_11){
            mScaleBtn.setImageResource(R.drawable.ic_camera_ratio43);
            mCurrentState = STATE_SCALE_43;
        }else{
            mScaleBtn.setImageResource(R.drawable.ic_camera_ratio11);
            mCurrentState = STATE_SACLE_11;
        }
    }

    @Override
    public void onClick(View v) {
        changeState();
        if(mOnScaleChangeListener != null){
            mOnScaleChangeListener.onScaleChange(mCurrentState);
        }
    }

    public int getState(){
        return mCurrentState;
    }

    public static interface OnScaleChangeListener{
        public void onScaleChange(int scale);
    }

}
