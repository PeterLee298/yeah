package com.yeah.android.activity.camera.util;

import android.view.View;
import android.widget.ImageView;

import com.yeah.android.R;

/**
 * Created by liuchao on 10/17/15.
 */
public class StateCameraTimerHander implements View.OnClickListener{
    //点击拍照按钮
    public static final int STATE_TIMER_OFF = 1001;
    //点击屏幕拍照
    public static final int STATE_TIMER_ON = 1002;

    private int mCurrentState = STATE_TIMER_OFF;

    private ImageView mTimerBtn;

    public StateCameraTimerHander(ImageView imageView){
        imageView.setOnClickListener(this);
        this.mTimerBtn = imageView;
    }

    public void changeState(){
        if(mCurrentState == STATE_TIMER_OFF){
            mTimerBtn.setImageResource(R.drawable.ic_camera_topbar_menu_2_clicked);
            mCurrentState = STATE_TIMER_ON;
        }else{
            mTimerBtn.setImageResource(R.drawable.ic_camera_topbar_menu_2);
            mCurrentState = STATE_TIMER_OFF;
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
