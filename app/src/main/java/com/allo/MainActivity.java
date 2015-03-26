package com.allo;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

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
 * Created by baek_uncheon on 2015. 3. 25..
 */
public class MainActivity extends Activity{
    Context mContext;
    ContactSync mContactSync;

    ListView friendList;
    Friend myInfo;
    Friend currentFriend;


    TextView playSongTV;
    ImageView imageView;
    ImageButton playSongPlayBtn;

    private TextView myNameTV;
    private TextView myInfoTV;
    private TextView mySongTV;
    private TextView mySongArtistTV;
    private ImageButton mySongInfoBtn;
    private ImageButton mySongPlayBtn;
    LinearLayout mySongPlayLayout;
    LinearLayout myInfoLayout;

    ArrayList<Friend> friend_array;


    @Override
    protected void onCreate(Bundle saveInstanceState) {
        super.onCreate(saveInstanceState);
        setContentView(R.layout.activity_main);
        myInfo = new Friend();

        friendList = (ListView) findViewById(R.id.friendList);

        myInfoLayout = (LinearLayout) findViewById(R.id.myInfoLayout);
        myNameTV = (TextView) findViewById(R.id.myNameTV);
        myInfoTV = (TextView) findViewById(R.id.myInfoTV);
        mySongTV = (TextView) findViewById(R.id.mySongTV);
        mySongArtistTV = (TextView) findViewById(R.id.mySongArtistTV);
        mySongInfoBtn = (ImageButton) findViewById(R.id.mySongInfoBtn);
        mySongPlayBtn = (ImageButton) findViewById(R.id.mySongPlayBtn);
        mySongPlayLayout = (LinearLayout) findViewById(R.id.mySongPlayLayout);

        playSongTV = (TextView) findViewById(R.id.playSongTV);
        imageView = (ImageView) findViewById(R.id.imageView);
        playSongPlayBtn = (ImageButton) findViewById(R.id.playSongPlayBtn);



        myInfoLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, ChangeAlloActivity.class);
                startActivity(intent);
            }
        });
        mySongPlayLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currentFriend = myInfo;
                playRingbackTone();
            }
        });

        mContext = this;

        mContactSync= new ContactSync(MainActivity.this);
        syncFriends();


        mContactSync.syncLocalContacts();

        LocalBroadcastManager.getInstance(this).registerReceiver(mMessageReceiver, new IntentFilter("custom-event-name"));

    }


    public void playRingbackTone(){
        RingbackTone mRingbackTone = RingbackTone.getInstance();
        String url = mContext.getString(R.string.base_url)+currentFriend.getRingURL();
        mRingbackTone.playRingbackTone(url);
        playSongPlayBtn.setBackgroundResource(R.drawable.pause_white_btn);
        String play_title = currentFriend.getRingTitle()+" - "+currentFriend.getRingSinger();
        playSongTV.setText(play_title);
    }
    public void pauseRingbackTone(){
        RingbackTone mRingbackTone = RingbackTone.getInstance();
        mRingbackTone.pauseRingBackTone();
        playSongPlayBtn.setBackgroundResource(R.drawable.play_white_btn);
    }


    public void playSongPlayBtn(View v){
        RingbackTone mRingbackTone = RingbackTone.getInstance();
        if (mRingbackTone.isPlayingNow()){
            pauseRingbackTone();
        }else{
            playRingbackTone();
        }
    }


    public void playUpdate(Friend mFriend){
        currentFriend = mFriend;
        playRingbackTone();
    }



    private BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            // Get extra data included in the Intent
            String message = intent.getStringExtra("message");
            Log.d("receiver", "Got message: " + message);
        }
    };


    public void syncFriends(){
        AsyncHttpClient myClient;

        myClient = new AsyncHttpClient();
        myClient.setTimeout(30000);
        PersistentCookieStore myCookieStore = new PersistentCookieStore(mContext);
        myClient.setCookieStore(myCookieStore);

        ContactDBOpenHelper mContactDBOpenHelper = new ContactDBOpenHelper(mContext);
        mContactDBOpenHelper.open_writableDatabase();

        Cursor mCursor;
        mCursor = mContactDBOpenHelper.getNewContacts();

        JSONArray contact_arr = new JSONArray();

        String url = mContext.getString(R.string.base_url)+ "/main/";
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
                mainUIupdate(new String(responseBody));


            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                System.out.println(new String(responseBody));
            }
        });

    }

    public static int dpToPx(Context context, int dp) {
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        int px = Math.round(dp * (displayMetrics.xdpi / DisplayMetrics.DENSITY_DEFAULT));
        return px;
    }
    private void mainUIupdate(String responseString){
        friend_array = new ArrayList<>();

        JSONObject response_object = null;
        String status = null;

        try{
            JSONObject result_object = new JSONObject(responseString);
            response_object = result_object.getJSONObject("response");
            status = result_object.getString("status");
            if (status.equals("success")){
                JSONArray friends_object = response_object.getJSONArray("friends");
                JSONObject my_info_object = response_object.getJSONObject("my_info");

                myInfo.setNickname(my_info_object.getString("nickname"));
                myInfo.setRingTitle(my_info_object.getString("ring_to_me_title"));
                myInfo.setRingSinger(my_info_object.getString("ring_to_me_singer"));
                myInfo.setRingURL(my_info_object.getString("ring_to_me_url"));
                myInfo.setRingCount(my_info_object.getString("my_ring_count"));
                myInfo.setStartPoint(my_info_object.getString("start_point"));
                myNameTV.setText(myInfo.getNickname());
                String ring_count = "총 "+myInfo.getRingCount()+"곡";
                myInfoTV.setText(ring_count);
                mySongTV.setText(myInfo.getRingTitle());
                mySongArtistTV.setText(myInfo.getRingSinger());

                currentFriend = myInfo;
//                setRingbackTone(myInfo);
                String play_title = myInfo.getRingTitle()+" - "+myInfo.getRingSinger();
                playSongTV.setText(play_title);



                for (int i = 0; i < friends_object.length(); i++) {
                    JSONObject friend_object = friends_object.getJSONObject(i);

                    String phone_number = friend_object.getString("phone_number");
                    String nickname = friend_object.getString("nickname");
                    String friend_id = friend_object.getString("friend_id");
                    String ring_to_friend_title = friend_object.getString("ring_to_friend_title");
                    String ring_to_friend_url = friend_object.getString("ring_to_friend_url");
                    String ring_to_friend_singer;
                    try{
                        ring_to_friend_singer = friend_object.getString("ring_to_friend_singer");
                    }catch(Exception e){
                        ring_to_friend_singer = "";
                    }

//
                    Friend mFriend = new Friend();
                    mFriend.setPhoneNumber(phone_number);
                    mFriend.setNickname(nickname);
                    mFriend.setFriendId(friend_id);
                    mFriend.setRingTitle(ring_to_friend_title);
                    mFriend.setRingURL(ring_to_friend_url);
                    mFriend.setRingSinger(ring_to_friend_singer);

                    friend_array.add(mFriend);
                }

                Log.i("friend array size", String.valueOf(friends_object.length()));


                FriendAdapter adapter = new FriendAdapter(MainActivity.this, R.layout.layout_main_item, friend_array);
                friendList.setAdapter(adapter);

                int height = 70*(friends_object.length());
                Log.i("height dp ", String.valueOf(height));
                Log.i("height px ", String.valueOf(dpToPx(mContext, height)));


                ViewGroup.LayoutParams lp = friendList.getLayoutParams();
//                lp.height = dpToPx(mContext, height);
                lp.height =10000;
                friendList.setLayoutParams(lp);
//                friendList.requestLayout();


//                ViewGroup.LayoutParams params = listView.getLayoutParams();
//                params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
//                listView.setLayoutParams(params);
//                listView.requestLayout();

                ContactDBOpenHelper mContactDBOpenHelper = new ContactDBOpenHelper(mContext);
                mContactDBOpenHelper.open_writableDatabase();
                mContactDBOpenHelper.updateContacts();
                mContactDBOpenHelper.close();

            }else{
                Log.i("HttpRequest", "fail fail fail fail");
            }

        }catch (JSONException e){
            System.out.println(e);
        }
    }



    @Override
    protected void onDestroy() {
        // Unregister since the activity is about to be closed.
        // This is somewhat like [[NSNotificationCenter defaultCenter] removeObserver:name:object:]
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mMessageReceiver);
        super.onDestroy();
    }
}
