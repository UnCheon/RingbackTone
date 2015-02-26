package com.example.uncheon.ringbacktone;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;


/**
 * Created by baek_uncheon on 2015. 2. 23..
 */
public class FriendDetailDialog extends Dialog{
    Context mContext;
    TextView tv_nickname;
    TextView tv_phone_number;
    TextView tv_ring_to_friend;
    TextView tv_ring_to_me;
    Button btn_close;
    Button btn_play_to_friend;
    Button btn_play_to_me;
    Button btn_record;
    Button btn_upload;
    Button btn_search;

    Friend mFriend;
    RingbackTone mRingbackTone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        WindowManager.LayoutParams lpWindow = new WindowManager.LayoutParams();
        lpWindow.flags = WindowManager.LayoutParams.FLAG_DIM_BEHIND;
        lpWindow.dimAmount = 0.8f;

        getWindow().setAttributes(lpWindow);
        getWindow().setGravity(Gravity.BOTTOM);



        setContentView(R.layout.layout_friend_detail);
        setLayout();
        setData(mFriend);
        mRingbackTone = RingbackTone.getInstance();
    }

    public FriendDetailDialog(Context context) {
        super(context , android.R.style.Theme_Translucent_NoTitleBar);
    }
    public FriendDetailDialog(Context context, Friend mFriend){
        super(context , android.R.style.Theme_Translucent_NoTitleBar);
        mContext = context;
        this.mFriend = mFriend;
    }

    public void setData(Friend mFriend){
        tv_nickname.setText(mFriend.getNickname());
        tv_phone_number.setText(mFriend.getPhoneNumber());
        tv_ring_to_friend.setText(mFriend.getRingToFriendTitle());
        tv_ring_to_me.setText(mFriend.getRingToMeTitle());
    }

    Button.OnClickListener closeClickListener = new View.OnClickListener() {
        public void onClick(View v) {
            mRingbackTone.stopRingbackTone();
            dismiss();
        }
    };

    Button.OnClickListener playClickListener = new View.OnClickListener(){
        @Override
        public void onClick(View v) {
            try {
                switch (v.getId()){
                    case R.id.btn_play_to_me:
                        mRingbackTone.playRingbackTone(mFriend.getRingToMeURL());
                        break;
                    case R.id.btn_play_to_friend:
                        mRingbackTone.playRingbackTone(mFriend.getRingToFriendURL());
                        break;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    };

    Button.OnClickListener changeClickListener = new View.OnClickListener(){
        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.btn_record:
                    Log.i("change", "record");
                    break;
                case R.id.btn_upload:
                    upload();
                    Log.i("change", "upload");
                    break;
                case R.id.btn_search:
                    Log.i("change", "search");
                    break;
            }
        }
    };

    private void upload(){

        Intent intent = new Intent(getContext(), FileListActivity.class);
        intent.putExtra("friend info", mFriend);
        getContext().startActivity(intent);
    }


    private void setLayout(){
        tv_nickname = (TextView)findViewById(R.id.tv_nickname);
        tv_phone_number = (TextView)findViewById(R.id.tv_phone_number);
        tv_ring_to_friend = (TextView)findViewById(R.id.tv_ring_to_friend);
        tv_ring_to_me = (TextView)findViewById(R.id.tv_ring_to_me);
        btn_close = (Button)findViewById(R.id.btn_close);
        btn_play_to_friend = (Button)findViewById(R.id.btn_play_to_friend);
        btn_play_to_me = (Button)findViewById(R.id.btn_play_to_me);
        btn_record = (Button)findViewById(R.id.btn_record);
        btn_upload = (Button)findViewById(R.id.btn_upload);
        btn_search = (Button)findViewById(R.id.btn_search);

        btn_close.setOnClickListener(closeClickListener);
        btn_play_to_me.setOnClickListener(playClickListener);
        btn_play_to_friend.setOnClickListener(playClickListener);
        btn_record.setOnClickListener(changeClickListener);
        btn_upload.setOnClickListener(changeClickListener);
        btn_search.setOnClickListener(changeClickListener);
    }
}
