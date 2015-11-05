package com.yeah.android.activity.camera;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;

import com.yeah.android.activity.camera.ui.Camera2Activity;
import com.yeah.android.activity.camera.ui.PhotoFilterActivity;
import com.yeah.android.activity.camera.ui.PhotoStickerActivity;
import com.yeah.android.model.PhotoItem;
import java.util.Stack;

/**
 * 相机管理类
 * on 15/7/6.
 */
public class CameraManager {

    private static CameraManager mInstance;
    private Stack<Activity> cameras = new Stack<Activity>();

    public static CameraManager getInst() {
        if (mInstance == null) {
            synchronized (CameraManager.class) {
                if (mInstance == null)
                    mInstance = new CameraManager();
            }
        }
        return mInstance;
    }

    //打开照相界面
    public void openCamera(Context context) {
        Intent intent = new Intent(context, Camera2Activity.class);
        context.startActivity(intent);
    }

    //判断图片是否需要裁剪
    public void processPhotoItem(Activity activity, PhotoItem photo) {
        Uri uri = photo.getImageUri().startsWith("file:") ? Uri.parse(photo
                .getImageUri()) : Uri.parse("file://" + photo.getImageUri());

        // 直接进行贴图
        Intent newIntent = new Intent(activity, PhotoStickerActivity.class);
        newIntent.setData(uri);
        activity.startActivity(newIntent);
    }

    public void filterPhotoItem(Activity activity, PhotoItem photo) {
        Uri uri = photo.getImageUri().startsWith("file:") ? Uri.parse(photo
                .getImageUri()) : Uri.parse("file://" + photo.getImageUri());
        Intent newIntent = new Intent(activity, PhotoFilterActivity.class);
        newIntent.setData(uri);
        activity.startActivity(newIntent);
    }

    public void close() {
        for (Activity act : cameras) {
            try {
                act.finish();
            } catch (Exception e) {

            }
        }
        cameras.clear();
    }

    public void addActivity(Activity act) {
        cameras.add(act);
    }

    public void removeActivity(Activity act) {
        cameras.remove(act);
    }


}
