package com.share.shareapptwitter.constants;

import android.os.Environment;

import java.io.File;

/**
 * Created by vaibhav.singhal on 5/4/2017.
 */

public class ConstantValues {
    public static final int NUMBER_OF_PICS_TO_CAPTURE = 4;
    public static final int PERMISSION_REQUEST_CODE = 1009;
    public static final String IS_READ_PERMISSION_GRANTED = "isReadPermissionGranted";
    public static final String folderPathToSaveCapturedImages = Environment.getExternalStorageDirectory().getAbsolutePath()+ File.separator+"tweetPics";
    public static final String folderPathToSaveGIF = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + "gif";
    public static final String TWITTER_KEY = "S3PN2l06fLywzEAY4xvq4KEci";
    public static final String TWITTER_SECRET = "W0e1auhRqJukaxSfZbWFxcCEIQpediGyOjnCeX7PdqmuiU4vlp";
}
