package com.share.shareapptwitter.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.share.shareapptwitter.R;
import com.share.shareapptwitter.constants.ConstantValues;
import com.share.shareapptwitter.utils.FunctionUtils;
import com.share.shareapptwitter.utils.LogUtil;
import com.share.shareapptwitter.utils.TwitterUtil;
import com.twitter.sdk.android.Twitter;
import com.twitter.sdk.android.core.Callback;
import com.twitter.sdk.android.core.Result;
import com.twitter.sdk.android.core.TwitterAuthConfig;
import com.twitter.sdk.android.core.TwitterException;
import com.twitter.sdk.android.core.TwitterSession;
import com.twitter.sdk.android.core.identity.TwitterLoginButton;

import java.io.File;

import io.fabric.sdk.android.Fabric;

/**
 * Created by vaibhav.singhal on 5/9/2017.
 */

public class BaseActivity extends Activity {
    protected File mMediaStorageDir, mGifStorage;
    public static  final String TAG = BaseActivity.class.getName();
    private TwitterLoginButton loginButton;
    private TwitterUtil mTwitterUtil;
    protected String pathToStoreGif = ConstantValues.folderPathToSaveGIF + File.separator + "gif.gif";
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        mMediaStorageDir = new File(ConstantValues.folderPathToSaveCapturedImages);
        mGifStorage = new File(ConstantValues.folderPathToSaveGIF);
        mTwitterUtil = new TwitterUtil(this);
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
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // Make sure that the loginButton hears the result from any
        // Activity that it triggered.
        if(loginButton != null) {
            loginButton.onActivityResult(requestCode, resultCode, data);
        }
    }
    protected void initialiseTwitterButton(){
        loginButton = (TwitterLoginButton) findViewById(R.id.twitter_login_button);
        loginButton.setCallback(new Callback<TwitterSession>() {
            @Override
            public void success(Result<TwitterSession> result) {
                // The TwitterSession is also available through:
                // Twitter.getInstance().core.getSessionManager().getActiveSession()
                mTwitterUtil.executeTweetTask();

            }
            @Override
            public void failure(TwitterException exception) {
                FunctionUtils.showToast("Failed to login into twitter account", BaseActivity.this);
            }
        });
    }
}
