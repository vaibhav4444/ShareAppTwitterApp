package com.share.shareapptwitter.constants;

import android.os.Environment;

import java.io.File;

/**
 * Created by vaibhav.singhal on 5/4/2017.
 */

public class ConstantValues {
    public static final int NUMBER_OF_PICS_TO_CAPTURE = 10;
    public static final int PERMISSION_REQUEST_CODE = 1009;
    public static final String IS_READ_PERMISSION_GRANTED = "isReadPermissionGranted";
    public static final String folderPathToSaveCapturedImages = Environment.getExternalStorageDirectory().getAbsolutePath()+ File.separator+"tweetPics";
}
