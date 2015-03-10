package com.example.uncheon.ringbacktone;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.os.IBinder;
import android.util.Log;

public class BackgroundService extends Service{

    RingbackTone mRingbackTone;
    Friend mFriend;

	AudioManager audioManager = null;
	int currVol;


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

        new AsyncFFTHook(getApplicationContext()).execute("start", "start", "start");


        return START_STICKY;
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
}
