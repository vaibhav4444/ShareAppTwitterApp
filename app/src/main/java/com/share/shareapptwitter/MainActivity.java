package com.share.shareapptwitter;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

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
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import io.fabric.sdk.android.Fabric;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Response;

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
                // TODO: Remove toast and use the TwitterSession's userID
                // with your app's user model
                String msg = "@" + session.getUserName() + " logged in! (#" + session.getUserId() + ")";
                Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG).show();
                new tt().execute();

            }
            @Override
            public void failure(TwitterException exception) {
                Log.d("TwitterKit", "Login with Twitter failure", exception);
            }
        });

    }
    class tt extends AsyncTask<Void, Void, Void>{

        @Override
        protected Void doInBackground(Void... voids) {
            uploadImage("asdh", session);
            return null;
        }
    }
    private void uploadImage( final String tweettext, TwitterSession twitterSession){
        TweetComposer composer = new TweetComposer();

        File f = null;

            f = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + "hh.gif");


        TwitterSession session = TwitterCore.getInstance()
                .getSessionManager().getActiveSession();

        TwitterApiClient twitterApiClient = TwitterCore.getInstance().getApiClient(session);
        MediaService ms = twitterApiClient.getMediaService();

        MediaType type = MediaType.parse("image/*");

        RequestBody body = RequestBody.create(type, f);
        MediaService mediaservice1 = twitterApiClient.getMediaService();
        //TypedFile typedFile = new TypedFile("image/*", imagefile);
        RequestBody file = RequestBody.create(MediaType.parse("image/*"), f);
        //TwitterApiClient twitterApiClient = TwitterCore.getInstance().getApiClient();
        MediaService mediaservice = twitterApiClient.getMediaService();
        Call<Media> mediaCall=  mediaservice1.upload(file, null, null);
        mediaCall.enqueue(new Callback<Media>() {
            @Override
            public void success(Result<Media> result) {
               /* runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getApplicationContext(), "posted", Toast.LENGTH_LONG).show();
                    }
                }); */
                // getting tweet in string
                String tweetStr = "tweeting";

                // post tweet only if it is not empty
                if (!TextUtils.isEmpty(tweetStr)) {
                    // EditText is not empty
                    // Check if char count is exceeding 140 limit
                    if (tweetStr.length() > 140) {
                        //Toast.makeText(PostTweetActivity.this,
                              //  "You have exceeded the 140 character limit",
                                //Toast.LENGTH_SHORT).show();
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

                    tweetCall.enqueue(new Callback<Tweet>()
                    {
                        @Override
                        public void success(Result<Tweet> result) {
                            //dismiss the progress dialog
                          //  dialog.dismiss();
                            //close the activity
                            //finish();
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(getApplicationContext(), "posted tweet call", Toast.LENGTH_LONG).show();
                                }
                            });
                        }

                        public void failure(TwitterException exception)
                        {
                            //dismiss the progress dialog
                            //dialog.dismiss();
                            // showing error to user
                            //Toast.makeText(PostTweetActivity.this,
                                   // R.string.error_msg_post_tweet, Toast.LENGTH_SHORT)
                                   // .show();
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
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // Make sure that the loginButton hears the result from any
        // Activity that it triggered.
        loginButton.onActivityResult(requestCode, resultCode, data);
    }

}
