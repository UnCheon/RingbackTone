package com.allo;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.os.IBinder;
import android.util.Log;

public class BackgroundService extends Service implements AudioManager.OnAudioFocusChangeListener{

    RingbackTone mRingbackTone;
    Friend mFriend;

	AudioManager audioManager = null;
	int currVol;

    final String TAG = getClass().getSimpleName();

	@Override
	public IBinder onBind(Intent arg0) {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public void onCreate() {
		super.onCreate();

		audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        currVol = audioManager.getStreamVolume(AudioManager.STREAM_VOICE_CALL);
        Log.i("currvol 1 ", ""+currVol);
        audioManager.setStreamVolume(AudioManager.STREAM_VOICE_CALL, 0, AudioManager.FLAG_PLAY_SOUND);
        audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC)/2, AudioManager.FLAG_PLAY_SOUND);

		Log.i("service", "onCreate");

	}
	
	@Override
	public int onStartCommand(Intent intent, int flags, int startId){
		Log.i("service", "onStart");
        String phone_number = intent.getStringExtra("phone_number");

        FriendDBOpenHelper mFriendDBOpenHelper = new FriendDBOpenHelper(this);
        mFriendDBOpenHelper.open_readableDatabase();
        mFriend = mFriendDBOpenHelper.getFriendWithPhoneNumber(phone_number);
        mFriendDBOpenHelper.close();

        mRingbackTone = RingbackTone.getInstance();
        mRingbackTone.playRingbackTone("http://128.199.97.46:8080"+mFriend.getRingToFriendURL());

//        new AsyncFFTHook(getApplicationContext()).execute("start", "start", "start");


        return START_STICKY;
	}
	

    private void audioFocusTest(){

    }


	@Override
	public void onDestroy() {
		super.onDestroy();
        Log.i("Allo", "onDestroy");

        mRingbackTone.stopRingbackTone();

        audioManager.setMode(AudioManager.MODE_CURRENT);
        audioManager.setMicrophoneMute(false);
        new AsyncFFTHook(getApplicationContext()).cancel(true);

    }

    @Override
    public void onAudioFocusChange(int focusChange) {
        switch (focusChange) {
            case AudioManager.AUDIOFOCUS_GAIN:
                // resume playback
//                if (mMediaPlayer == null) initMediaPlayer();
//                else if (!mMediaPlayer.isPlaying()) mMediaPlayer.start();
//                mMediaPlayer.setVolume(1.0f, 1.0f);
                Log.i(TAG, "audio focus gain");
                break;

            case AudioManager.AUDIOFOCUS_LOSS:
                // Lost focus for an unbounded amount of time: stop playback and release media player
//                if (mMediaPlayer.isPlaying()) mMediaPlayer.stop();
//                mMediaPlayer.release();
//                mMediaPlayer = null;
                Log.i(TAG, "audio focus loss");
                break;

            case AudioManager.AUDIOFOCUS_LOSS_TRANSIENT:
                // Lost focus for a short time, but we have to stop
                // playback. We don't release the media player because playback
                // is likely to resume
//                if (mMediaPlayer.isPlaying()) mMediaPlayer.pause();
                Log.i(TAG, "audio focus loss transient");
                break;

            case AudioManager.AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK:
                // Lost focus for a short time, but it's ok to keep playing
                // at an attenuated level
//                if (mMediaPlayer.isPlaying()) mMediaPlayer.setVolume(0.1f, 0.1f);
                Log.i(TAG, "audio focus loss transient can duck");
                break;
        }
    }
}
