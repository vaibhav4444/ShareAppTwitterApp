package com.share.shareapptwitter.common;

import android.app.Application;
import android.os.Build;

import com.crashlytics.android.Crashlytics;
import com.share.shareapptwitter.constants.ConstantValues;
import com.twitter.sdk.android.Twitter;
import com.twitter.sdk.android.core.TwitterAuthConfig;

import io.fabric.sdk.android.Fabric;

/**
 * Created by vaibhav.singhal on 5/5/2017.
 */

public class ShareApplication extends Application {
    private static ShareApplication mInstance;
    @Override
    public void onCreate() {
        super.onCreate();
        TwitterAuthConfig authConfig = new TwitterAuthConfig(ConstantValues.TWITTER_KEY, ConstantValues.TWITTER_SECRET);
        Fabric.with(this, new Twitter(authConfig));
        Fabric.with(this, new Crashlytics());
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
