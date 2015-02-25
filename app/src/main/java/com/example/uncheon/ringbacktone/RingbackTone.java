package com.example.uncheon.ringbacktone;

import android.media.AudioManager;
import android.media.MediaPlayer;

/**
 * Created by baek_uncheon on 2015. 2. 25..
 */
public class RingbackTone {

    private MediaPlayer mMediaPlayer;

    public void playRingbackTone(String url) throws Exception{
        if (mMediaPlayer != null) {
            mMediaPlayer.release();
            mMediaPlayer = null;
        }else{
            mMediaPlayer = new MediaPlayer();
            mMediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
            mMediaPlayer.setLooping(false);
            mMediaPlayer.setDataSource(url);
            mMediaPlayer.prepare();
            mMediaPlayer.start();
        }
    }

    public void stopRingbackTone() throws Exception{
        if (mMediaPlayer != null) {
            mMediaPlayer.release();
            mMediaPlayer = null;
        }
    }
}
