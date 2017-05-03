package com.share.shareapptwitter.activity;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.share.shareapptwitter.R;
import com.twitter.sdk.android.Twitter;
import com.twitter.sdk.android.core.Callback;
import com.twitter.sdk.android.core.Result;
import com.twitter.sdk.android.core.TwitterApiClient;
import com.twitter.sdk.android.core.TwitterAuthConfig;
import com.twitter.sdk.android.core.TwitterCore;
import com.twitter.sdk.android.core.TwitterException;
import com.twitter.sdk.android.core.TwitterSession;
import com.twitter.sdk.android.core.identity.TwitterLoginButton;
import com.twitter.sdk.android.core.models.Media;
import com.twitter.sdk.android.core.models.Tweet;
import com.twitter.sdk.android.core.services.MediaService;
import com.twitter.sdk.android.core.services.StatusesService;
import com.twitter.sdk.android.tweetcomposer.TweetComposer;


import java.io.File;

import io.fabric.sdk.android.Fabric;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;

public class MainActivity extends Activity {

    // Note: Your consumer key and secret should be obfuscated in your source code before shipping.
    private static final String TWITTER_KEY = "S3PN2l06fLywzEAY4xvq4KEci";
    private static final String TWITTER_SECRET = "W0e1auhRqJukaxSfZbWFxcCEIQpediGyOjnCeX7PdqmuiU4vlp";
    private TwitterLoginButton loginButton;
    TwitterSession session;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        TwitterAuthConfig authConfig = new TwitterAuthConfig(TWITTER_KEY, TWITTER_SECRET);
        Fabric.with(this, new Twitter(authConfig));
        setContentView(R.layout.activity_main);
        File f = null;
        //try {
        f = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + "home.jpg");
        Uri myImageUri = Uri.fromFile(f);
        TweetComposer.Builder builder = new TweetComposer.Builder(this)
                .text("just setting up my Fabric.")
                .image(myImageUri);
        //builder.show();
        loginButton = (TwitterLoginButton) findViewById(R.id.twitter_login_button);
        loginButton.setCallback(new Callback<TwitterSession>() {
            @Override
            public void success(Result<TwitterSession> result) {
                // The TwitterSession is also available through:
                // Twitter.getInstance().core.getSessionManager().getActiveSession()
                 session = result.data;
                new TwitterTweetTask().execute();

            }
            @Override
            public void failure(TwitterException exception) {
               showToast("Failed to login into twitter account");
            }
        });

    }
    class TwitterTweetTask extends AsyncTask<Void, Void, Void>{

        @Override
        protected Void doInBackground(Void... voids) {
            uploadImage();
            return null;
        }
    }

    /**
     * This method will upload image.
     * File object will represent file to upload
     */
    private void uploadImage(){
        File fileObjectForGIF = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + "hh.gif");

        // retrieve active session
        TwitterSession session = TwitterCore.getInstance()
                .getSessionManager().getActiveSession();
        // create twitter client from active session
        TwitterApiClient twitterApiClient = TwitterCore.getInstance().getApiClient(session);
       // get media service object.
        MediaService mediaservice = twitterApiClient.getMediaService();
        // type of file to upload and file object
        RequestBody file = RequestBody.create(MediaType.parse("image/*"), fileObjectForGIF);
        Call<Media> mediaCall=  mediaservice.upload(file, null, null);
        mediaCall.enqueue(new Callback<Media>() {
            // on success will get media id
            @Override
            public void success(Result<Media> result) {
                // put text here that goes with GIF
                String tweetStr = "tweeting";

                // post tweet only if it is not empty
                if (!TextUtils.isEmpty(tweetStr)) {
                    // Check if char count is exceeding 140 limit
                    if (tweetStr.length() > 140) {
                        return;
                    }
                    // getting twitter api client and session
                    StatusesService statusesService =
                            TwitterCore.getInstance().getApiClient().getStatusesService();


                    Call<Tweet> tweetCall= statusesService.update
                            (       tweetStr,
                                    null, false,
                                    null, null, null,
                                    true, false,
                                    result.data.mediaIdString
                            );
                    // this call will actually upload GIF on twitter
                    tweetCall.enqueue(new Callback<Tweet>()
                    {
                        @Override
                        public void success(Result<Tweet> result) {
                            showToast("Successfully tweeted.");
                        }
                        @Override
                        public void failure(TwitterException exception)
                        {
                            showToast("Failed to tweet.");
                        }
                    });
                }
            }

            @Override
            public void failure(TwitterException exception) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getApplicationContext(), "failure", Toast.LENGTH_LONG).show();
                    }
                });
            }
        });


    }
    private void showToast(final String message){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
            }
        });
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // Make sure that the loginButton hears the result from any
        // Activity that it triggered.
        loginButton.onActivityResult(requestCode, resultCode, data);
    }

}
