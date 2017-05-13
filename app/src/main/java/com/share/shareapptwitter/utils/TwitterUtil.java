package com.share.shareapptwitter.utils;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Environment;
import android.text.TextUtils;
import android.widget.Toast;

import com.twitter.sdk.android.core.Callback;
import com.twitter.sdk.android.core.Result;
import com.twitter.sdk.android.core.TwitterApiClient;
import com.twitter.sdk.android.core.TwitterCore;
import com.twitter.sdk.android.core.TwitterException;
import com.twitter.sdk.android.core.TwitterSession;
import com.twitter.sdk.android.core.models.Media;
import com.twitter.sdk.android.core.models.Tweet;
import com.twitter.sdk.android.core.services.MediaService;
import com.twitter.sdk.android.core.services.StatusesService;

import java.io.File;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;

/**
 * Created by vaibhavsinghal on 13/05/17.
 */

public class TwitterUtil {
    private Activity mContext;
    public TwitterUtil (Activity context){
        mContext = context;
    }
    public void executeTweetTask(){
        new TwitterTweetTask().execute();
    }
    class TwitterTweetTask extends AsyncTask<Void, Void, Void> {

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
                            FunctionUtils.showToast("Successfully tweeted.", mContext);
                        }
                        @Override
                        public void failure(TwitterException exception)
                        {
                            FunctionUtils.showToast("Failed to tweet.", mContext);
                        }
                    });
                }
            }

            @Override
            public void failure(TwitterException exception) {
                FunctionUtils.showToast("Get media id call failed", mContext);
            }
        });


    }
}
