package com.share.shareapptwitter.activity;

import android.app.Activity;
import android.os.Bundle;
import android.widget.Toast;

import com.share.shareapptwitter.constants.ConstantValues;
import com.share.shareapptwitter.utils.LogUtil;

import java.io.File;

/**
 * Created by vaibhav.singhal on 5/9/2017.
 */

public class BaseActivity extends Activity {
    protected File mMediaStorageDir, mGifStorage;
    public static  final String TAG = BaseActivity.class.getName();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mMediaStorageDir = new File(ConstantValues.folderPathToSaveCapturedImages);
        mGifStorage = new File(ConstantValues.folderPathToSaveGIF);
        if(!mGifStorage.exists()){
            boolean isSuccess = mGifStorage.mkdir();
            if(isSuccess){
                LogUtil.i(TAG, "Folder created");
            }
            else{
                Toast.makeText(this, "Failed to create folder for storing gif", Toast.LENGTH_LONG).show();
            }
        }
        if(!mMediaStorageDir.exists()){
            boolean isSuccess = mMediaStorageDir.mkdir();
            if(isSuccess){
                LogUtil.i(TAG, "Folder created");
            }
            else{
                Toast.makeText(this, "Failed to create folder for storing pics", Toast.LENGTH_LONG).show();
            }
        }
    }
}
