package com.share.shareapptwitter.utils;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;

import com.indesign.viewer.indesignviewer.common.app.ViewerApplication;
import com.indesign.viewer.indesignviewer.common.constants.Constants;
import com.share.shareapptwitter.common.ShareApplication;
import com.share.shareapptwitter.constants.ConstantValues;


public class PermissionsHelper {
    private static Activity mActivity;
    private String [] permissionArray = {Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA};
    public PermissionsHelper(Activity activity){
        mActivity = activity;
    }
    public void checkPermissionStatus(){
        if (!isPermissionRequiredForAppGranted()) {
            ActivityCompat.requestPermissions(mActivity,
                   permissionArray,
                    ConstantValues.PERMISSION_REQUEST_CODE);
        }
    }
   /* public boolean isReadStoragePermissionGranted(){
        return verifyPermission(Manifest.permission.READ_EXTERNAL_STORAGE);
    }
    public boolean isWriteStoragePermissionGranted(){
        return verifyPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE);
    } */
    public boolean isPermissionRequiredForAppGranted(){
        return verifyPermission();
    }
    private boolean verifyPermission(){
        boolean isPermissionsGranted = false;
        if(!ShareApplication.getApplicationInstance().isNeedPermission()){
            return true;
        }
        int permissionStatus;

        for(String permissionName : permissionArray){
            permissionStatus = ContextCompat.checkSelfPermission(mActivity, permissionName);
            if(permissionStatus == PackageManager.PERMISSION_GRANTED){
                isPermissionsGranted = true;
            }
            else{
                isPermissionsGranted = false;
                return isPermissionsGranted;
            }
        }

        return isPermissionsGranted;
    }
    public boolean verifyPermissions(@NonNull int[] grantResults) {
        if (grantResults.length < 1) {
            return false;
        }
        for (int result : grantResults) {
            if (result != PackageManager.PERMISSION_GRANTED) {
                return false;
            }
        }
        return true;
    }
}
