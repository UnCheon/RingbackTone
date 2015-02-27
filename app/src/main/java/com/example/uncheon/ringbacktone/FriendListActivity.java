package com.example.uncheon.ringbacktone;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.ListView;



/**
 * Created by baek_uncheon on 2015. 1. 7..
 */
public class FriendListActivity extends Activity {


    private static final int UPDATE_FRIENDS_LIST = 0;

    private ListView lv_friend_list;
    public static final String SELECTED_PHONE = "selectedphone";
    public static final int SUCCESS = 1;
    public static final int FAIL = -1;

    Context mContext;
    MainHandler mainHandler;
    ContactSync mContactSync;
    FriendSync mFriendSync;

    @Override
    protected void onCreate(Bundle saveInstanceState) {
        super.onCreate(saveInstanceState);
        setContentView(R.layout.activity_friend_list);

        mainHandler = new MainHandler();

        lv_friend_list = (ListView) findViewById(R.id.lv_friend_list);
        mContext = this;

        mContactSync= new ContactSync(FriendListActivity.this);
        mFriendSync = new FriendSync(this, mainHandler);

        FriendsAdapter adapter = new FriendsAdapter(FriendListActivity.this, R.layout.layout_friend_item, mFriendSync.getFriendList());
        lv_friend_list.setAdapter(adapter);

        mContactSync.syncLocalContacts();
        mFriendSync.syncFriends();


    }

    @Override
    protected void onResume() {
        super.onResume();
        FriendsAdapter adapter = new FriendsAdapter(FriendListActivity.this, R.layout.layout_friend_item, mFriendSync.getFriendList());
        lv_friend_list.setAdapter(adapter);
    }

    class MainHandler extends Handler {
        @Override
        public void handleMessage(Message msg){
            super.handleMessage(msg);

            switch(msg.what){
                case UPDATE_FRIENDS_LIST:
//                    update friends list
                    Log.i("handler", "get message ");
                    FriendsAdapter adapter = new FriendsAdapter(FriendListActivity.this,
                    R.layout.layout_friend_item, mFriendSync.getFriendList());
                    lv_friend_list.setAdapter(adapter);
                    break;

            }
        }
    }
}




