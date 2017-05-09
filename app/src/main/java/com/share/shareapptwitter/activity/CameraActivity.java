package com.share.shareapptwitter.activity;

import android.app.Activity;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.drawable.Drawable;
import android.hardware.Camera;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.share.shareapptwitter.constants.ConstantValues;
import com.share.shareapptwitter.R;
import com.share.shareapptwitter.gif.AnimatedGifEncoder;
import com.share.shareapptwitter.utils.LogUtil;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by vaibhav.singhal on 5/3/2017.
 *
 * links :
 * define call back surface methods : http://stackoverflow.com/questions/10482057/android-camera-surface-view/10482872#10482872
 * define surface , surface view : http://stackoverflow.com/questions/17198520/what-is-surfaceview-surfaceholder-surface-camera-api-android
 *
 */

public class CameraActivity extends BaseActivity  implements SurfaceHolder.Callback {
    public static  final String TAG = CameraActivity.class.getName();
    private Camera camera = null;
    private boolean previewing = false;
    // this view will display video captured by camera
    private SurfaceView mCameraSurfaceView = null;
    // used to interact with surface view & get various callback regardign changes to surface view.
    private SurfaceHolder mCameraSurfaceHolder = null;
    // to check whether camera is ready to take pic again
    private boolean mIsSafeToTakePic = true;

    public byte[][] globalData  = new byte[1000][];
    public int counter = 0;
    public int glo = 0;
    private Button btnCapture;
    private CameraActivity mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);
        btnCapture = (Button) findViewById(R.id.btnIdCapture);
        mContext = this;
        btnCapture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CaptureThread captureThread = new CaptureThread();
                captureThread.start();
            }
        });

        mCameraSurfaceView = (SurfaceView) findViewById(R.id.surfaceView);
        mCameraSurfaceHolder = mCameraSurfaceView.getHolder();
        mCameraSurfaceHolder.addCallback(this);
    }

    @Override
    public void surfaceCreated(SurfaceHolder surfaceHolder) {
        try {
            // camera = Camera.open(findFrontFacingCamera());
            camera = Camera.open();

            // camera.setDisplayOrientation(90);
        } catch (RuntimeException e) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(getApplicationContext(), "Device camera  is not working properly, please try after sometime.", Toast.LENGTH_LONG).show();
                }
            });

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
            camera.setPreviewDisplay(mCameraSurfaceHolder);
            camera.startPreview();
            previewing = true;
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }
    Camera.ShutterCallback cameraShutterCallback = new Camera.ShutterCallback()
    {
        @Override
        public void onShutter()
        {
            // TODO Auto-generated method stub
        }
    };

    Camera.PictureCallback cameraPictureCallbackRaw = new Camera.PictureCallback()
    {
        @Override
        public void onPictureTaken(byte[] data, Camera camera)
        {
            // TODO Auto-generated method stub
        }
    };

    Camera.PictureCallback cameraPictureCallbackJpeg = new Camera.PictureCallback() {
        @Override
        public void onPictureTaken(byte[] data, Camera camera) {

            globalData[counter] = data;
            counter++;
            camera.startPreview();
            mIsSafeToTakePic =true;


        }
    };


    @Override
    public void surfaceDestroyed(SurfaceHolder surfaceHolder) {
        // TODO Auto-generated method stub
        camera.stopPreview();
        camera.release();
        camera = null;
        previewing = false;

    }
    //code to capture image need to replace it with Async task
    class CaptureThread extends Thread {

        @Override
        public void run() {
            LogUtil.d(TAG, "run()");
            // TakeScreenshot();
            Log.d(TAG, "In run to click- after capture");

            int count = 0;
            // code to play sound while capturing images
            //MediaPlayer mp = MediaPlayer.create(getApplicationContext(), R.raw.click);
            //mp.setLooping(true);

            while (count < ConstantValues.NUMBER_OF_PICS_TO_CAPTURE) {
                try {
                    Log.d(TAG, "camera clicked number ->"+count );

                    if(mIsSafeToTakePic) {
                        Log.d(TAG,"enter safe - running media");
                        //mp.start();


                        camera.takePicture(cameraShutterCallback,
                                cameraPictureCallbackRaw,
                                cameraPictureCallbackJpeg);
                        mIsSafeToTakePic = false;
                        count++;
                        final int finalCount = count;
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(mContext, "Image clicked:"+finalCount, Toast.LENGTH_LONG).show();
                            }
                        });

                    }
                    Thread.sleep(500);
                } catch (Exception exception) {
                    exception.printStackTrace();
                }
            }
           // mp.stop();
            generatePng(globalData);
            glo = 0; // images will be save from 0 in pics folder
            try {
            } catch (Exception e) {
                e.printStackTrace();
            }
            createGif();


        }

        public void generatePng(byte[][] global) {

            for (int i = 0; i < ConstantValues.NUMBER_OF_PICS_TO_CAPTURE; i++) {
                // Matrix matrix = new Matrix();
                // matrix.postRotate();
                // createa matrix for the manipulation


                Bitmap cameraBitmap = BitmapFactory.decodeByteArray(global[i], 0, global[i].length);
                int wid = cameraBitmap.getWidth();
                int hgt = cameraBitmap.getHeight();

                Matrix matrix = new Matrix();
                // resize the bit map
                // matrix.postScale(scaleWidth, scaleHeight);
                // rotate the Bitmap
                matrix.postRotate(270);

                Bitmap newImage = Bitmap.createBitmap
                        (cameraBitmap,0,0,wid, hgt, matrix,true);

                Canvas canvas = new Canvas(newImage);

                canvas.drawBitmap(newImage, 0f, 0f, null);

                //Drawable drawable = getResources().getDrawable(R.drawable.overlay111);
                // drawable.
                //  drawable.setBounds(40, 40, drawable.getIntrinsicWidth() + 40, drawable.getIntrinsicHeight() + 40);
               // drawable.setBounds(0, 0, canvas.getWidth(),canvas.getHeight());

                //drawable.draw(canvas);




                File myImage = new File(ConstantValues.folderPathToSaveCapturedImages + File.separator + glo + ".png");
                Log.d("naval", "File path :" + myImage);
                glo++;

                try {
                    FileOutputStream out = new FileOutputStream(myImage);
                    newImage.compress(Bitmap.CompressFormat.JPEG, 80, out);


                    out.flush();
                    out.close();
                } catch (FileNotFoundException e) {
                    Log.d("In Saving File", e + "");
                } catch (IOException e) {
                    Log.d("In Saving File", e + "");
                }
            }
            counter =0;
            // camera.startPreview();

            //   cnt.setVisibility(ImageView.VISIBLE);
            //   cnt.setImageResource(R.drawable.thr);
        }


        public void createGif() {
            // Toast.makeText(getApplicationContext(), "We are done", Toast.LENGTH_SHORT).show();
            //  cnt.setVisibility(ImageView.VISIBLE);
            //  cnt.setImageResource(R.drawable.thr);
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(mContext, "creating gif", Toast.LENGTH_LONG).show();
                }
            });

            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            AnimatedGifEncoder encoder = new AnimatedGifEncoder();
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inSampleSize = 3 ;

            encoder.setDelay(100);
            encoder.setRepeat(0);
            encoder.start(bos);
            for (int i = 0; i < ConstantValues.NUMBER_OF_PICS_TO_CAPTURE; i++) {
                Bitmap bMap = BitmapFactory.decodeFile(ConstantValues.folderPathToSaveCapturedImages + File.separator + i + ".png", options);
                Log.d("naval", "added image");

                encoder.addFrame(bMap);
            }
            encoder.finish();

            writeToFile(bos.toByteArray());
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(mContext, "gif created", Toast.LENGTH_LONG).show();
                }
            });


            Thread.interrupted();


            /*try {
                Log.d("naval - lets sleep", "Turn again to click");
                Thread.sleep(1000);

                SocketServerThread sc = new SocketServerThread();
                sc.initialize();
                sc.start();


            } catch (Exception e) {
                e.printStackTrace();
            }

            MainActivity.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    // cnt.setImageResource(R.drawable.save);
                    cnt.setVisibility(ImageView.INVISIBLE);

                }
            }); */

        }

        public void writeToFile(byte[] array) {
            try {
                //String path = Environment.getExternalStorageDirectory() + "/gif/gif.gif";
                String pathToStoreGif = ConstantValues.folderPathToSaveGIF + File.separator + "gif.gif";
                FileOutputStream stream = new FileOutputStream(pathToStoreGif);
                stream.write(array);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
