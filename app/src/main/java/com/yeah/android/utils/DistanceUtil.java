package com.yeah.android.utils;

import com.yeah.android.YeahApp;

public class DistanceUtil {

    public static int getCameraAlbumWidth() {
        return (YeahApp.getApp().getScreenWidth() - YeahApp.getApp().dp2px(10)) / 4 - YeahApp.getApp().dp2px(4);
    }
    
    // 相机照片列表高度计算 
    public static int getCameraPhotoAreaHeight() {
        return getCameraPhotoWidth() + YeahApp.getApp().dp2px(4);
    }
    
    public static int getCameraPhotoWidth() {
        return YeahApp.getApp().getScreenWidth() / 4 - YeahApp.getApp().dp2px(2);
    }

    //活动标签页grid图片高度
    public static int getActivityHeight() {
        return (YeahApp.getApp().getScreenWidth() - YeahApp.getApp().dp2px(24)) / 3;
    }
}
