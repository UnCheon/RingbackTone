package com.example.uncheon.ringbacktone;


import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class FriendsAdapter extends ArrayAdapter<Friend> {

    private int resId;
    private ArrayList<Friend> friend_list;
    private LayoutInflater Inflater;
    private Context context;
    public int listCount = 0;
    FriendDetailDialog mFriendDetailDialog;

    public FriendsAdapter(Context context, int textViewResourceId, List<Friend> objects) {
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
            holder.tv_name = (TextView) v.findViewById(R.id.tv_name);
            holder.tv_phonenumber = (TextView) v.findViewById(R.id.tv_phonenumber);
            v.setTag(holder);
        } else {
            holder = (ViewHolder) v.getTag();
        }

        Friend mFriend = friend_list.get(position);

        if (mFriend != null) {
            holder.tv_name.setText(mFriend.getNickname());
            holder.tv_phonenumber.setText(mFriend.getPhoneNumber());
        }

        final int positionInt = position;
        v.setOnClickListener(new Button.OnClickListener(){
            @Override
            public void onClick(View v) {
                detailInfo(positionInt);
            }
        });

        return v;
    }

    public void detailInfo(int position){
        Friend mFriend;
        mFriend = friend_list.get(position);
        mFriendDetailDialog = new FriendDetailDialog(context, mFriend);
        mFriendDetailDialog.show();
    }




    private class ViewHolder {
        TextView tv_name;
        TextView tv_phonenumber;
    }

}