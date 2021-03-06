package com.allo;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
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
    LinearLayout headerLayout;
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

    boolean isFirstResume = true;


    @Override
    protected void onCreate(Bundle saveInstanceState) {
        super.onCreate(saveInstanceState);
        setContentView(R.layout.activity_main);
        myInfo = new Friend();

        setLayout();
        setListener();

        mContext = this;

        mContactSync= new ContactSync(MainActivity.this);
        syncFriends();

        mContactSync.syncLocalContacts();

        LocalBroadcastManager.getInstance(this).registerReceiver(mMessageReceiver, new IntentFilter("allo-state"));
    }

    private void setLayout(){
        friendList = (ListView) findViewById(R.id.friendList);
        LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.layout_main_header, null, false);
        friendList.addHeaderView(view);




        myInfoLayout = (LinearLayout) view.findViewById(R.id.myInfoLayout);
        myNameTV = (TextView) view.findViewById(R.id.myNameTV);
        myInfoTV = (TextView) view.findViewById(R.id.myInfoTV);
        mySongTV = (TextView) view.findViewById(R.id.mySongTV);
        mySongArtistTV = (TextView) view.findViewById(R.id.mySongArtistTV);
        mySongInfoBtn = (ImageButton) view.findViewById(R.id.mySongInfoBtn);
        mySongPlayBtn = (ImageButton) view.findViewById(R.id.mySongPlayBtn);
        mySongPlayLayout = (LinearLayout) view.findViewById(R.id.mySongPlayLayout);

        playSongTV = (TextView) findViewById(R.id.playSongTV);
        imageView = (ImageView) findViewById(R.id.imageView);
        playSongPlayBtn = (ImageButton) findViewById(R.id.playSongPlayBtn);
    }

    private void setListener(){
        myInfoLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, MyAlloActivity.class);
                intent.putExtra("myInfo", myInfo);
                startActivity(intent);
            }
        });
        mySongPlayLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currentFriend = myInfo;
                if (myInfo.getIsPlaying()){
                    myInfo.setIsPlaying(false);
                    mySongPlayBtn.setBackgroundResource(R.drawable.play_btn);
                    pauseRingbackTone();
                }else{
                    myInfo.setIsPlaying(true);
                    mySongPlayBtn.setBackgroundResource(R.drawable.pause_btn);
                    playRingbackTone();
                }
                listViewUIInit();
            }
        });
    }



//    Ringbacktone play & pause
    public void playRingbackTone(){
        RingbackTone mRingbackTone = RingbackTone.getInstance();
        String substring = currentFriend.getRingURL().substring(1, 8);
        String url;
        if (substring.equals("storage"))
            url = currentFriend.getRingURL();
        else
            url = mContext.getString(R.string.base_url)+currentFriend.getRingURL();


        mRingbackTone.playRingbackTone(url);
        mRingbackTone.setNowPlayingFriend(currentFriend);

        playSongPlayBtn.setBackgroundResource(R.drawable.pause_white_btn);
        String play_title = currentFriend.getRingTitle()+" - "+currentFriend.getRingSinger();
        playSongTV.setText(play_title);
    }

    public void pauseRingbackTone(){
        RingbackTone mRingbackTone = RingbackTone.getInstance();
        mRingbackTone.pauseRingBackTone();
        playBarUIInit();
    }


//    call from adapter
    public void playBarUIUpdate(Friend mFriend){
        currentFriend = mFriend;
        if (mFriend.getIsPlaying()){
            playRingbackTone();
        }else{
            pauseRingbackTone();
        }
    }



// HTTp Request

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
                mainUIUpdate(new String(responseBody));


            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                System.out.println(new String(responseBody));
            }
        });

    }


    private void mainUIUpdate(String responseString){
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

                setMyInfoUIUpdate(my_info_object);
                setFriendUIUpdate(friends_object);

                ContactDBOpenHelper mContactDBOpenHelper = new ContactDBOpenHelper(mContext);
                mContactDBOpenHelper.open_writableDatabase();
                mContactDBOpenHelper.updateContacts();
                mContactDBOpenHelper.close();

            }else{
                Log.i("HttpRequest", "fail fail fail fail");
            }

        }catch (Exception e){
            System.out.println(e);
        }
    }

//    Request success and UI update

    private void setMyInfoUIUpdate(JSONObject my_info_object) throws Exception{
        myInfo.setNickname(my_info_object.getString("nickname"));
        myInfo.setRingTitle(my_info_object.getString("ring_to_me_title"));
        myInfo.setRingSinger(my_info_object.getString("ring_to_me_singer"));
        myInfo.setRingURL(my_info_object.getString("ring_to_me_url"));
        myInfo.setRingCount(my_info_object.getString("my_ring_count"));
        myInfo.setStartPoint(my_info_object.getString("start_point"));
        myInfo.setIsPlaying(false);

        myNameTV.setText(myInfo.getNickname());
        String ring_count = "총 "+myInfo.getRingCount()+"곡";
        myInfoTV.setText(ring_count);
        mySongTV.setText(myInfo.getRingTitle());
        mySongArtistTV.setText(myInfo.getRingSinger());

        currentFriend = myInfo;
        setPlayBarUI();
    }

    private void setPlayBarUI(){
        String play_title = myInfo.getRingTitle()+" - "+myInfo.getRingSinger();
        playSongTV.setText(play_title);
        RingbackTone mRingbackTone = RingbackTone.getInstance();
        mRingbackTone.setNowPlayingFriend(currentFriend);
    }

    private void setFriendUIUpdate(JSONArray friends_object) throws Exception{

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

            Friend mFriend = new Friend();
            mFriend.setPhoneNumber(phone_number);
            mFriend.setNickname(nickname);
            mFriend.setFriendId(friend_id);
            mFriend.setRingTitle(ring_to_friend_title);
            mFriend.setRingURL(ring_to_friend_url);
            mFriend.setRingSinger(ring_to_friend_singer);
            mFriend.setIsPlaying(false);

            friend_array.add(mFriend);
        }
        Log.i("friend array size", String.valueOf(friends_object.length()));

        MainAdapter adapter = new MainAdapter(MainActivity.this, R.layout.layout_main_item, friend_array);
        friendList.setAdapter(adapter);
    }




//    play complete listener method
    private BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            // Get extra data included in the Intent
            Log.i("receiver", "stop stop stop");
            String message = intent.getStringExtra("message");
            if (message.equals("stop")){
                myInfoUIInit();
                listViewUIInit();
                playBarUIInit();
            }
        }
    };


//    UI init
    public void myInfoUIInit(){
        myInfo.setIsPlaying(false);
        mySongPlayBtn.setBackgroundResource(R.drawable.play_btn);
    }
    public void playBarUIInit(){
        playSongPlayBtn.setBackgroundResource(R.drawable.play_white_btn);
    }
    public void listViewUIInit(){
        for (int i = 0 ; i < friend_array.size(); i++) {
            Friend friend = friend_array.get(i);
            if (friend.getIsPlaying()) {
                friend.setIsPlaying(false);
                friend_array.set(i, friend);
            }

        }
        listViewDataUpdate(friend_array);
    }

    public void listViewDataUpdate(ArrayList<Friend> friend_list){
        MainAdapter adapter = new MainAdapter(MainActivity.this, R.layout.layout_main_item, friend_list);
        friendList.setAdapter(adapter);
    }




    //     onClick method
    public void playSongPlayBtn(View v){
        RingbackTone mRingbackTone = RingbackTone.getInstance();
        if (mRingbackTone.isPlayingNow()){
            pauseRingbackTone();
            myInfoUIInit();
            listViewUIInit();
        }else{
            playRingbackTone();
        }
    }

    public void addSongBtn(View v){
        Intent intent = new Intent(this, FragmentActivityAddAllo.class);
        startActivity(intent);
    }


//    Override method
    @Override
    protected void onResume() {
        super.onResume();
        Log.i("resume", "onResume onResume");
        if (isFirstResume){
            isFirstResume = false;
        }else{
            setResumePlayBarUI();
        }
    }

    private void setResumePlayBarUI(){
        RingbackTone mRingbackTone = RingbackTone.getInstance();
        currentFriend = mRingbackTone.getNowPlayingFriend();
        boolean isPlaying = mRingbackTone.isPlayingNow();

        String play_title = currentFriend.getRingTitle()+" - "+currentFriend.getRingSinger();
        playSongTV.setText(play_title);

        if (isPlaying)
            playSongPlayBtn.setBackgroundResource(R.drawable.pause_white_btn);
        else
            playSongPlayBtn.setBackgroundResource(R.drawable.play_white_btn);
    }


    @Override
    protected void onPause(){
        super.onPause();
        Log.i("pause", "onPause onPause");
    }

    @Override
    protected void onDestroy() {
        // Unregister since the activity is about to be closed.
        // This is somewhat like [[NSNotificationCenter defaultCenter] removeObserver:name:object:]
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mMessageReceiver);
        super.onDestroy();
    }
}
