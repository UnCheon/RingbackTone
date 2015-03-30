package com.allo;


import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class MainAdapter extends ArrayAdapter<Friend> {

    private int resId;
    private ArrayList<Friend> friend_list;


    private LayoutInflater Inflater;
    private Context context;
    public int listCount = 0;
    FriendDetailDialog mFriendDetailDialog;

    public MainAdapter(Context context, int textViewResourceId, List<Friend> objects) {
        super(context, textViewResourceId, objects);
        this.context = context;
        resId = textViewResourceId;
        friend_list = (ArrayList<Friend>) objects;
        listCount = friend_list.size();
        Inflater = (LayoutInflater) ((Activity) context).getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }


    @Override
    public int getCount(){
        return super.getCount();
    }

    @Override
    public Friend getItem(int arg0) {
        return super.getItem(arg0);
    }

    @Override
    public long getItemId(int position){
        return super.getItemId(position);
    }



    @Override
    public View getView(int position, View v, ViewGroup parent) {
        ViewHolder holder;
        if (v == null) {
            v = Inflater.inflate(resId, null);
            holder = new ViewHolder();
            holder.fName = (TextView) v.findViewById(R.id.fName);
            holder.fTel = (TextView) v.findViewById(R.id.fTel);
            holder.fSongTV = (TextView) v.findViewById(R.id.fSongTV);
            holder.fSongArtistTV = (TextView) v.findViewById(R.id.fSongArtistTV);
            holder.fSongInfoBtn = (ImageButton) v.findViewById(R.id.fSongInfoBtn);
            holder.inSongPlayBtn = (ImageButton) v.findViewById(R.id.inSongPlayBtn);
            holder.inSongPlayLayout = (LinearLayout) v.findViewById(R.id.inSongPlayLayout);

            v.setTag(holder);
        } else {
            holder = (ViewHolder) v.getTag();
        }

        Friend mFriend = friend_list.get(position);

        if (mFriend != null) {
            holder.fName.setText(mFriend.getNickname());
            holder.fTel.setText(mFriend.getPhoneNumber());
            holder.fSongTV.setText(mFriend.getRingTitle());
            holder.fSongArtistTV.setText(mFriend.getRingSinger());
            holder.fTel.setText(mFriend.getPhoneNumber());

            if (mFriend.getIsPlaying()){
                holder.inSongPlayBtn.setBackgroundResource(R.drawable.pause_btn);
            }else{
                holder.inSongPlayBtn.setBackgroundResource(R.drawable.play_btn);
            }


            final int play_position = position;
            holder.inSongPlayLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    for (int i = 0 ; i < friend_list.size(); i++) {
                        if (i != play_position) {
                            Friend friend = friend_list.get(i);
                            if (friend.getIsPlaying()) {
                                friend.setIsPlaying(false);
                                friend_list.set(i, friend);
                            }
                        }
                    }

                    Friend mFriend;
                    mFriend = friend_list.get(play_position);

                    if (mFriend.getIsPlaying()){
                        mFriend.setIsPlaying(false);
                    }else{
                        mFriend.setIsPlaying(true);
                    }
                    friend_list.set(play_position, mFriend);

                    ((MainActivity)context).listViewDataUpdate(friend_list);
                    ((MainActivity)context).myInfoUIInit();
                    ((MainActivity)context).playBarUIUpdate(mFriend);

                }
            });
        }
        return v;
    }

    public void detailInfo(int position){
        Friend mFriend;
        mFriend = friend_list.get(position);
        mFriendDetailDialog = new FriendDetailDialog(context, mFriend);
        mFriendDetailDialog.show();
    }

    private class ViewHolder {
        TextView fName;
        TextView fTel;
        TextView fSongTV;
        TextView fSongArtistTV;
        ImageButton fSongInfoBtn;
        ImageButton inSongPlayBtn;
        LinearLayout inSongPlayLayout;
    }

}