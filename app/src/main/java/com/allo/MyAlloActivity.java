package com.allo;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
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

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by baek_uncheon on 2015. 3. 26..
 */
public class MyAlloActivity extends Activity {
    Friend myInfo;
    Friend currentFriend;
    TextView nameTv;
    TextView mySongTV;
    TextView mySongArtistTV;

    ImageButton mySongInfoBtn;
    ImageButton mySongPlayBtn;
    LinearLayout mySongPlayLayout;

    ListView alloList;

    ImageView imageView;
    TextView playSongTV;
    ImageButton playSongPlayBtn;

    ArrayList<Allo> allo_list_array ;


    @Override
    protected void onCreate(Bundle saveInstanceState) {
        super.onCreate(saveInstanceState);
        setContentView(R.layout.activity_my_allo);

        Intent intent = getIntent();
        myInfo = (Friend) intent.getSerializableExtra("myInfo");

        setLayout();
        setPlayBarUI();

        nameTv.setText(myInfo.getNickname());
        mySongTV.setText(myInfo.getRingTitle());
        mySongArtistTV.setText(myInfo.getRingSinger());

        LocalBroadcastManager.getInstance(this).registerReceiver(mMessageReceiver, new IntentFilter("allo-state"));

        getAlloList();

    }

// on create
    private void setLayout(){
        alloList = (ListView)findViewById(R.id.friendList);
        LayoutInflater inflater = (LayoutInflater) getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.layout_my_allo_header, null, false);
        alloList.addHeaderView(view);

        nameTv = (TextView) findViewById(R.id.nameTV);
        mySongTV = (TextView) view.findViewById(R.id.mySongTV);
        mySongArtistTV = (TextView) view.findViewById(R.id.mySongArtistTV);

        mySongInfoBtn = (ImageButton) view.findViewById(R.id.mySongInfoBtn);
        mySongPlayBtn = (ImageButton) view.findViewById(R.id.mySongPlayBtn);

        mySongPlayLayout = (LinearLayout) view.findViewById(R.id.mySongPlayLayout);
        mySongPlayLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currentFriend = myInfo;
                if (myInfo.getIsPlaying()){
                    myInfoUIInit();
//                    myInfo.setIsPlaying(false);
//                    mySongPlayBtn.setBackgroundResource(R.drawable.play_btn);
                    pauseRingbackTone();
                }else{
                    myInfo.setIsPlaying(true);
                    mySongPlayBtn.setBackgroundResource(R.drawable.pause_btn);
                    playRingbackTone();
                }
                listViewUIInit();
            }
        });



        imageView = (ImageView)findViewById(R.id.imageView);
        playSongTV = (TextView)findViewById(R.id.playSongTV);
        playSongPlayBtn = (ImageButton)findViewById(R.id.playSongPlayBtn);
    }

    private void setPlayBarUI(){
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


//    Ringbacktone play & pause
    private void playRingbackTone(){
        RingbackTone mRingbackTone = RingbackTone.getInstance();
        String substring = currentFriend.getRingURL().substring(1, 8);
        String url;
        if (substring.equals("storage"))
            url = currentFriend.getRingURL();
        else
            url = getApplicationContext().getString(R.string.base_url)+currentFriend.getRingURL();

        mRingbackTone.playRingbackTone(url);
        mRingbackTone.setNowPlayingFriend(currentFriend);

        playSongPlayBtn.setBackgroundResource(R.drawable.pause_white_btn);
        String play_title = currentFriend.getRingTitle()+" - "+currentFriend.getRingSinger();
        playSongTV.setText(play_title);

    }
    private void pauseRingbackTone(){
        RingbackTone mRingbackTone = RingbackTone.getInstance();
        mRingbackTone.pauseRingBackTone();
        playSongPlayBtn.setBackgroundResource(R.drawable.play_white_btn);
    }




    public void playUpdate(Allo mAllo){
        Friend mFriend = new Friend();
        mFriend.setRingTitle(mAllo.getRingTitle());
        mFriend.setRingSinger(mAllo.getRingSinger());
        mFriend.setRingURL(mAllo.getRingURL());
        currentFriend = mFriend;
        if (mAllo.getIsPlaying()){
            playRingbackTone();
        }else{
            pauseRingbackTone();
        }
    }
    public void listViewUpdate(ArrayList<Allo> allo_list){
        MyAlloAdapter adapter = new MyAlloAdapter(MyAlloActivity.this, R.layout.layout_my_allo_item, allo_list, this);
        alloList.setAdapter(adapter);
    }

    public void myInfoUIInit(){
        Log.i("MyInFoUIINit", "click");
        myInfo.setIsPlaying(false);
        mySongPlayBtn.setBackgroundResource(R.drawable.play_btn);
    }

    public void listViewUIInit(){
        for (int i = 0 ; i < allo_list_array .size(); i++) {
            Allo allo = allo_list_array .get(i);
            if (allo.getIsPlaying()) {
                allo.setIsPlaying(false);
                allo_list_array .set(i, allo);
            }

        }
        listViewUpdate(allo_list_array );
    }

    public void playBarUIInit(){
        playSongPlayBtn.setBackgroundResource(R.drawable.play_white_btn);
    }
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

//    Http Request
    private void getAlloList(){
        AsyncHttpClient myClient;

        myClient = new AsyncHttpClient();
        myClient.setTimeout(30000);
        PersistentCookieStore myCookieStore = new PersistentCookieStore(getApplicationContext());
        myClient.setCookieStore(myCookieStore);

        String url = getApplicationContext().getString(R.string.base_url)+ "/ringback/tones/";

        myClient.get(url, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                Log.i("HTTP RESPONSE......", new String(responseBody));
                alloUIupdate(new String(responseBody));
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                System.out.println(new String(responseBody));
            }
        });
    }

    private void alloUIupdate(String responseString){
        allo_list_array = new ArrayList<>();

        JSONArray allo_objects = null;
        String status = null;

        try{
            JSONObject result_object = new JSONObject(responseString);
            allo_objects = result_object.getJSONArray("response");
            status = result_object.getString("status");
            if (status.equals("success")){


                for (int i = 0; i < allo_objects.length(); i++) {
                    JSONObject allo_object = allo_objects.getJSONObject(i);

                    String title = allo_object.getString("title");
                    String ring_tone_id = allo_object.getString("ring_tone_id");
                    String ring_tone_url = allo_object.getString("ring_tone_url");

                    String ring_tone_singer;
                    try{
                        ring_tone_singer = allo_object.getString("ring_tone_singer");
                    }catch(Exception e){
                        ring_tone_singer = "";
                    }
                    Allo mAllo = new Allo();
                    mAllo.setRingTitle(title);
                    mAllo.setRingURL(ring_tone_url);
                    mAllo.setRingSinger(ring_tone_singer);
                    mAllo.setRingId(ring_tone_id);
                    mAllo.setIsPlaying(false);

                    allo_list_array.add(mAllo);
                }

                MyAlloAdapter adapter = new MyAlloAdapter(MyAlloActivity.this, R.layout.layout_my_allo_item, allo_list_array, this);
                alloList.setAdapter(adapter);
            }else{
                Log.i("HttpRequest", "fail fail fail fail");
            }
        }catch (JSONException e){
            System.out.println(e);
        }
    }

//    OnClick Method
    public void backBtn(View v){
        finish();
    }

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
        finish();
    }

    @Override
    protected void onDestroy() {
        // Unregister since the activity is about to be closed.
        // This is somewhat like [[NSNotificationCenter defaultCenter] removeObserver:name:object:]
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mMessageReceiver);
        super.onDestroy();
    }
}
