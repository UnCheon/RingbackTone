package com.example.uncheon.ringbacktone;

import android.media.AudioManager;
import android.media.MediaPlayer;
import android.util.Log;

import java.io.IOException;

/**
 * Created by baek_uncheon on 2015. 2. 25..
 */
public class RingbackTone {
    private static RingbackTone uniqueInstance;
    private RingbackTone(){}

    public static RingbackTone getInstance(){
        if(uniqueInstance == null)
            uniqueInstance = new RingbackTone();

        return uniqueInstance;
    }

    private MediaPlayer mMediaPlayer;

    public void playRingbackTone(String ring_url){
        String url = "http://128.199.97.46:8080"+ring_url;
        Log.i("ring url", url);
        if (mMediaPlayer != null) {
            mMediaPlayer.release();
            mMediaPlayer = null;
        }else{
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
        }
    }

    public void stopRingbackTone(){
        if (mMediaPlayer != null) {
            mMediaPlayer.release();
            mMediaPlayer = null;
        }
    }
}
