package com.allo;

import android.app.Activity;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import java.io.IOException;

/**
 * Created by baek_uncheon on 2015. 2. 25..
 */
public class RingbackTone extends Activity {
    final String TAG = getClass().getSimpleName();
    private static RingbackTone uniqueInstance;
    private MediaPlayer mMediaPlayer;
    private Friend nowPlayingFriend;

    private final String base_url = "http://128.199.97.46:8080";

    private RingbackTone(){}

    public static RingbackTone getInstance(){
        if(uniqueInstance == null)
            uniqueInstance = new RingbackTone();


        return uniqueInstance;
    }

    public void setNowPlayingFriend(Friend mFriend){
        nowPlayingFriend = mFriend;
    }
    public Friend getNowPlayingFriend(){
        return nowPlayingFriend;
    }






    public void playRingbackTone(String url){

        if (mMediaPlayer != null){
            if (mMediaPlayer.isPlaying()){
                mMediaPlayer.stop();
            }
            mMediaPlayer.release();
            mMediaPlayer = null;
        }

        mMediaPlayer = new MediaPlayer();
        mMediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        mMediaPlayer.setLooping(false);
        try {

            mMediaPlayer.setDataSource(url);
            mMediaPlayer.prepare();
        } catch (IOException e) {
            e.printStackTrace();
        }
        mMediaPlayer.start();
        mMediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                sendMessage();
            }
        });

    }


    public void pauseRingBackTone(){
        if(mMediaPlayer != null){
            if (mMediaPlayer.isPlaying()){
                mMediaPlayer.pause();
            }
        }
    }





    public void stopRingbackTone(){
        if (mMediaPlayer != null){
            if (mMediaPlayer.isPlaying()){
                mMediaPlayer.stop();
            }
            mMediaPlayer.release();
            mMediaPlayer = null;
        }
        sendMessage();
    }

    public boolean isPlayingNow(){
        if (mMediaPlayer != null)
            return mMediaPlayer.isPlaying();
        else
            return false;
    }

    private void sendMessage() {
        Log.d("sender", "Broadcasting message");
        Intent intent = new Intent("allo-state");
        // You can also include some extra data.
        intent.putExtra("message", "stop");
        LocalBroadcastManager.getInstance(this).sendBroadcast(intent);

    }
}
