package com.share.shareapptwitter.utils;

import android.app.Activity;
import android.content.Context;
import android.hardware.Camera;
import android.widget.Toast;

/**
 * Created by vaibhavsinghal on 10/05/17.
 */

public class FunctionUtils {
    public static  final String TAG = FunctionUtils.class.getName();
    public static void showToast(final String message, final Activity context){
        context.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(context, message, Toast.LENGTH_LONG).show();
            }
        });
    }
    public static int getFrontCamId() {
        int cameraCount = 0;
        Camera cam = null;
        Camera.CameraInfo cameraInfo = new Camera.CameraInfo();
        cameraCount = Camera.getNumberOfCameras();
        for (int camIdx = 0; camIdx < cameraCount; camIdx++) {
            Camera.getCameraInfo(camIdx, cameraInfo);
            if (cameraInfo.facing == Camera.CameraInfo.CAMERA_FACING_FRONT) {
               return camIdx;
            }
        }

        return -1;
    }
}
