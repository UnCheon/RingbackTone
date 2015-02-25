package com.example.uncheon.ringbacktone;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.os.Message;
import android.util.Log;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.PersistentCookieStore;
import com.loopj.android.http.RequestParams;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by baek_uncheon on 2015. 1. 27..
 */
public class FriendSync {

    private static final int UPDATE_FRIENDS_LIST = 0;

    Activity mActivity;
    Context mContext;

    AsyncHttpClient myClient;


    FriendSync(FriendListActivity activity){
        this.mActivity = activity;
        this.mContext = activity;

        myClient = new AsyncHttpClient();
        myClient.setTimeout(30000);
        PersistentCookieStore myCookieStore = new PersistentCookieStore(mContext);
        myClient.setCookieStore(myCookieStore);
    }


    public void syncFriends(final FriendListActivity.MainHandler mainHandler){

        ContactDBOpenHelper mContactDBOpenHelper = new ContactDBOpenHelper(mContext);
        mContactDBOpenHelper.open_writableDatabase();

        Cursor mCursor;
        mCursor = mContactDBOpenHelper.getNewContacts();

        JSONArray contact_arr = new JSONArray();

//        count new contacts list
        if (mCursor.getCount() == 0){
            updateCheck(mainHandler);
        }else{
            String url = mContext.getString(R.string.base_url)+ "account/sync/friends/";
            while(mCursor.moveToNext()){
                try{
                    JSONObject contact_json = new JSONObject();
                    contact_json.put("phone_number", mCursor.getString(1));
                    contact_arr.put(contact_json);

                }catch (JSONException e){
                    e.printStackTrace();
                }
            }

            mContactDBOpenHelper.close();

            RequestParams params = new RequestParams();
            params.put("contacts", contact_arr);
            myClient.post(url, params, new AsyncHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                    Log.i("HTTP RESPONSE......", new String(responseBody));
                    syncLocalFriends(responseBody, mainHandler);
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                    System.out.println(new String(responseBody));
                }
            });
        }
    }

    public void updateCheck(final FriendListActivity.MainHandler mainHandler){
        Log.i("tag", "update check");

        String url = mContext.getString(R.string.base_url)+ "account/update/check/";
        myClient.get(url, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                Log.i("HTTP RESPONSE......", new String(responseBody));
                syncLocalFriends(responseBody, mainHandler);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                System.out.println(new String(responseBody));
            }
        });
    }



    public void syncLocalFriends(byte[] responseBody, FriendListActivity.MainHandler mainHandler){
        String result_string = new String(responseBody);

        JSONArray response_object = null;
        String status = null;

        try{
            JSONObject result_object = new JSONObject(result_string);
            response_object = result_object.getJSONArray("response");
            status = result_object.getString("status");
        }catch (JSONException e){}

        if (status.equals("success")){
            ContactDBOpenHelper mContactDBOpenHelper = new ContactDBOpenHelper(mContext);
            mContactDBOpenHelper.open_writableDatabase();
            mContactDBOpenHelper.updateContacts();


            FriendDBOpenHelper mFriendDBOpenHelper = new FriendDBOpenHelper(mContext);
            mFriendDBOpenHelper.open_writableDatabase();
            mFriendDBOpenHelper.setFriends(response_object);
            mFriendDBOpenHelper.close();

            Message msg;
            msg = Message.obtain();
            msg.what = UPDATE_FRIENDS_LIST;
            mainHandler.sendMessage(msg);
        }
    }


    public ArrayList<Friend> getFriendList() {

        Cursor cursor;

        FriendDBOpenHelper mFriendDBOpenHelper= new FriendDBOpenHelper(mContext);
        mFriendDBOpenHelper.open_writableDatabase();

        cursor = mFriendDBOpenHelper.getFriends();

        ArrayList<Friend> friend_db_list = new ArrayList<Friend>();

        if (cursor.moveToFirst()) {
            do {
                Friend mFriend = new Friend();
                mFriend.setPhoneNumber(cursor.getString(1));
                mFriend.setNickname(cursor.getString(2));
                mFriend.setFriendId(cursor.getString(3));
                mFriend.setRingToMeTitle(cursor.getString(4));
                mFriend.setRingToMeURL(cursor.getString(5));
                mFriend.setRingToFriendTitle(cursor.getString(6));
                mFriend.setRingToFriendURL(cursor.getString(7));

                friend_db_list.add(mFriend);

            } while (cursor.moveToNext());
        }
        return friend_db_list;
    }
}

