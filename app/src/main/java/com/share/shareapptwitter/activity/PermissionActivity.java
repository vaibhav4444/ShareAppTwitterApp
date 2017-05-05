package com.share.shareapptwitter.activity;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;

import com.share.shareapptwitter.R;
import com.share.shareapptwitter.common.ShareApplication;
import com.share.shareapptwitter.constants.ConstantValues;
import com.share.shareapptwitter.utils.LogUtil;
import com.share.shareapptwitter.utils.PermissionsHelper;


public class PermissionActivity extends Activity {
    private PermissionsHelper mPermissionHelper;
    private boolean mShowRationale = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_permissions);
        mPermissionHelper = new PermissionsHelper(this);
        if(ShareApplication.getApplicationInstance().isNeedPermission() &&
                !mPermissionHelper.isPermissionRequiredForAppGranted()){
            requestPermissions();
        }
        else {
            launchMainActivity(true);
        }

    }
    @TargetApi(Build.VERSION_CODES.M)
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String[] permissions,
                                           int[] grantResults) {
        if (requestCode == ConstantValues.PERMISSION_REQUEST_CODE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                launchMainActivity(true);
            }
            else if(grantResults[0] == PackageManager.PERMISSION_DENIED) {
                mShowRationale = shouldShowRequestPermissionRationale(permissions[0]);
                if (!mShowRationale) {
                    // user denied flagging NEVER ASK AGAIN
                    // you can either enable some fall back,
                    // disable features of your app
                    // or open another dialog explaining
                    // again the permission and directing to
                    // the app setting
                    LogUtil.d(PermissionActivity.class.getSimpleName(),"Never ask again as clicked");
                    // dont show dialog as when all doc fragment is displayed it will show dialog
                    launchMainActivity(false);
                }
                else{
                   // FunctionUtils.showDialogForReadPermissionDenied(PermissionActivity.this);
                }
            }
            // launch main activity/ disclaimer activity irrespective of permission given or not.

        }
    }
    public void launchMainActivity(boolean isPermissionGranted){
        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra(ConstantValues.IS_READ_PERMISSION_GRANTED, isPermissionGranted);
        startActivity(intent);
        PermissionActivity.this.finish();
    }
    public void requestPermissions(){
        mPermissionHelper.checkPermissionStatus();
    }
}
