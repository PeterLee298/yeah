package com.yeah.android.utils;

import android.hardware.Camera;

import com.yeah.android.impl.ICameraLightBack;

import java.util.List;

/**
 * Created by liuchao on 10/13/15.
 */
public class CameraUtils {

    /**
     * 闪光灯开关   开->关->自动
     *
     * @param mCamera
     */
    public static void turnLight(Camera mCamera,ICameraLightBack iCameraLightBack) {
        if (mCamera == null || mCamera.getParameters() == null
                || mCamera.getParameters().getSupportedFlashModes() == null) {
            return;
        }
        Camera.Parameters parameters = mCamera.getParameters();
        String flashMode = mCamera.getParameters().getFlashMode();
        List<String> supportedModes = mCamera.getParameters().getSupportedFlashModes();
        if (Camera.Parameters.FLASH_MODE_OFF.equals(flashMode)
                && supportedModes.contains(Camera.Parameters.FLASH_MODE_ON)) {//关闭状态
            parameters.setFlashMode(Camera.Parameters.FLASH_MODE_ON);
            mCamera.setParameters(parameters);
            if(iCameraLightBack != null){
                iCameraLightBack.onFlashOn();
            }
//            flashBtn.setImageResource(R.drawable.camera_flash_on);
        } else if (Camera.Parameters.FLASH_MODE_ON.equals(flashMode)) {//开启状态
            if (supportedModes.contains(Camera.Parameters.FLASH_MODE_AUTO)) {
                parameters.setFlashMode(Camera.Parameters.FLASH_MODE_AUTO);
                if(iCameraLightBack != null){
                    iCameraLightBack.onFlashAuto();
                }
//                flashBtn.setImageResource(R.drawable.camera_flash_auto);
                mCamera.setParameters(parameters);
            } else if (supportedModes.contains(Camera.Parameters.FLASH_MODE_OFF)) {
                parameters.setFlashMode(Camera.Parameters.FLASH_MODE_OFF);
                if(iCameraLightBack != null){
                    iCameraLightBack.onFlashOff();
                }
//                flashBtn.setImageResource(R.drawable.camera_flash_off);
                mCamera.setParameters(parameters);
            }
        } else if (Camera.Parameters.FLASH_MODE_AUTO.equals(flashMode)
                && supportedModes.contains(Camera.Parameters.FLASH_MODE_OFF)) {
            parameters.setFlashMode(Camera.Parameters.FLASH_MODE_OFF);
            mCamera.setParameters(parameters);
            if(iCameraLightBack != null){
                iCameraLightBack.onFlashOff();
            }
//            flashBtn.setImageResource(R.drawable.camera_flash_off);
        }
    }
}
