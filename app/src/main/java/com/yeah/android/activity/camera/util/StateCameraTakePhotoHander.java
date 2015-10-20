package com.yeah.android.activity.camera.util;

import android.view.View;
import android.widget.ImageView;

import com.yeah.android.R;

/**
 * Created by liuchao on 10/17/15.
 */
public class StateCameraTakePhotoHander implements View.OnClickListener{
    //点击拍照按钮
    public static final int STATE_CLICK_TAKE_BTN = 1001;
    //点击屏幕拍照
    public static final int STATE_TOUCH_SCREEN = 1002;

    private int mCurrentState = STATE_CLICK_TAKE_BTN;

    private ImageView mCameraClickBtn;

    public StateCameraTakePhotoHander(ImageView imageView){
        imageView.setOnClickListener(this);
        this.mCameraClickBtn = imageView;
    }

    public void changeState(){
        if(mCurrentState == STATE_CLICK_TAKE_BTN){
            mCameraClickBtn.setImageResource(R.drawable.ic_camera_topbar_menu_1_clicked);
            mCurrentState = STATE_TOUCH_SCREEN;
        }else{
            mCameraClickBtn.setImageResource(R.drawable.ic_camera_topbar_menu_1);
            mCurrentState = STATE_CLICK_TAKE_BTN;
        }
    }

    @Override
    public void onClick(View v) {
        changeState();
    }

    public int getState(){
        return mCurrentState;
    }
}
