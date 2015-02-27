package com.example.uncheon.ringbacktone;

import android.app.Activity;
import android.content.Context;
import android.util.Log;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.PersistentCookieStore;
import com.loopj.android.http.RequestParams;

import org.apache.http.Header;

/**
 * Created by baek_uncheon on 2015. 2. 26..
 */
public class AlloHTTPConnect {
    Context mContext;


    public AlloHTTPConnect(Context context){
        mContext = context;
    }

    public void getAlloHttpConnect(){

    }

    public void postFileUploadConnect(String url, RequestParams params){
        AsyncHttpClient myClient;
        myClient = new AsyncHttpClient();
        PersistentCookieStore myCookieStore = new PersistentCookieStore(mContext);
        myClient.setCookieStore(myCookieStore);
        myClient.post(url, params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                Log.i("HTTP RESPONSE......", new String(responseBody));
                FriendSync mFriendSync;
                mFriendSync = new FriendSync(mContext);
                mFriendSync.syncLocalFriends(responseBody);

                Activity mActivity = (Activity)mContext;
                mActivity.finish();
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                System.out.println(new String(responseBody));
            }
        });
    }
}
