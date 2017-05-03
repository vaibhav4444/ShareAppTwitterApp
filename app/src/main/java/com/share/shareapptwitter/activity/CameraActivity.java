package com.share.shareapptwitter.activity;

import android.app.Activity;
import android.content.res.Configuration;
import android.hardware.Camera;
import android.view.SurfaceHolder;
import android.widget.Toast;

import java.io.IOException;

/**
 * Created by vaibhav.singhal on 5/3/2017.
 */

public class CameraActivity extends Activity  implements SurfaceHolder.Callback {
    private Camera camera = null;
    private boolean previewing = false;
    @Override
    public void surfaceCreated(SurfaceHolder surfaceHolder) {
        try {
            // camera = Camera.open(findFrontFacingCamera());
            camera = Camera.open();

            // camera.setDisplayOrientation(90);
        } catch (RuntimeException e) {
            Toast.makeText(getApplicationContext(), "Device camera  is not working properly, please try after sometime.", Toast.LENGTH_LONG).show();
        }

    }

    @Override
    public void surfaceChanged(SurfaceHolder surfaceHolder, int i, int i1, int i2) {
        if (previewing) {
            camera.stopPreview();
            previewing = false;
        }
        try {
            Camera.Parameters params = camera.getParameters();
            if (this.getResources().getConfiguration().orientation != Configuration.ORIENTATION_LANDSCAPE) {
                camera.setDisplayOrientation(90);
            }
            // parameters.setRotation(90);
            //params.set("rotation", 270);
            params.set("orientation", "portrait");
            camera.setParameters(params);
            camera.setPreviewDisplay(cameraSurfaceHolder);
            camera.startPreview();
            previewing = true;
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder surfaceHolder) {
        // TODO Auto-generated method stub
        camera.stopPreview();
        camera.release();
        camera = null;
        previewing = false;

    }
}
