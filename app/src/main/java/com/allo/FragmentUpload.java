package com.allo;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by baek_uncheon on 2015. 3. 26..
 */
public class FragmentUpload extends Fragment {
    public ViewPager mPager;
    ListView uploadLv;
    ImageView imageView;
    TextView playSongTV;
    ImageButton playSongPlayBtn;

    Friend currentFriend;
    Context mContext;

    ArrayList<Allo> allo_list_array;

    public FragmentUpload(){

    }
    public FragmentUpload(Context context){
        mContext = context;
    }

    public Context getContext(){
        return mContext;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_upload, null);
        setLayout(view);
        setPlayBarUI();
        ArrayList<Allo> allo_list = getAlloList();
        UploadAdapter adapter = new UploadAdapter(mContext, R.layout.layout_upload_item, allo_list, this);
        uploadLv.setAdapter(adapter);
        LocalBroadcastManager.getInstance(mContext).registerReceiver(mMessageReceiver, new IntentFilter("allo-state"));
        return view;
    }

    private void setLayout(View view){
        uploadLv = (ListView)view.findViewById(R.id.uploadLv);
        imageView = (ImageView)view.findViewById(R.id.imageView);
        playSongTV = (TextView)view.findViewById(R.id.playSongTV);
        playSongPlayBtn = (ImageButton)view.findViewById(R.id.playSongPlayBtn);
        playSongPlayBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                playSongPlayBtn();
            }
        });
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

    private void playRingbackTone(){
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
    private void pauseRingbackTone(){
        RingbackTone mRingbackTone = RingbackTone.getInstance();
        mRingbackTone.pauseRingBackTone();
        playSongPlayBtn.setBackgroundResource(R.drawable.play_white_btn);
    }


    private void playSongPlayBtn(){
        RingbackTone mRingbackTone = RingbackTone.getInstance();
        if (mRingbackTone.isPlayingNow()){
            pauseRingbackTone();
            listViewUIInit();
        }else{
            playRingbackTone();
        }
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
        UploadAdapter adapter = new UploadAdapter(mContext, R.layout.layout_upload_item, allo_list, this);
        uploadLv.setAdapter(adapter);
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


    private ArrayList<Allo> getAlloList(){
        allo_list_array = new ArrayList<>();
        Cursor c = mContext.getContentResolver().query(
                MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
                new String[] {
                        MediaStore.Audio.Media._ID,
                        MediaStore.Audio.Media.DATA,
                        MediaStore.Audio.Media.ARTIST,
                        MediaStore.Audio.Media.ALBUM,
                        MediaStore.Audio.Media.DISPLAY_NAME }, "1=1", null, null);

        while (c.moveToNext()){
            Allo allo= new Allo();
            allo.setRingTitle(c.getString(4));
            allo.setRingURL(c.getString(1));
            allo.setRingSinger(c.getString(2));
            allo.setRingAlbum(c.getString(3));
            allo.setIsPlaying(false);
            allo_list_array.add(allo);
        }

        return allo_list_array;
    }

    private BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            // Get extra data included in the Intent
            Log.i("receiver", "stop stop stop");
            String message = intent.getStringExtra("message");
            if (message.equals("stop")){
                listViewUIInit();
                playBarUIInit();
            }
        }
    };

    @Override
    public void onDestroy() {
        // Unregister since the activity is about to be closed.
        // This is somewhat like [[NSNotificationCenter defaultCenter] removeObserver:name:object:]
        LocalBroadcastManager.getInstance(mContext).unregisterReceiver(mMessageReceiver);
        super.onDestroy();
    }

}
