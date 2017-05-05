package com.share.shareapptwitter.common;

import android.app.Application;
import android.os.Build;

/**
 * Created by vaibhav.singhal on 5/5/2017.
 */

public class ShareApplication extends Application {
    private static ShareApplication mInstance;
    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;
    }

    public boolean isNeedPermission(){
        boolean needPermission = Build.VERSION.SDK_INT> Build.VERSION_CODES.LOLLIPOP_MR1;
        return needPermission;
    }
    public static ShareApplication getApplicationInstance() {
        return mInstance;
    }

}
