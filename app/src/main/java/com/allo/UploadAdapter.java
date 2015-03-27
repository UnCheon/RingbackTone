package com.allo;


import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class UploadAdapter extends ArrayAdapter<Allo> {

    private int resId;
    private ArrayList<Allo> allo_list;

    private LayoutInflater Inflater;
    Context mContext;
    public int listCount = 0;
    FragmentUpload mFragmentUpload;

    public UploadAdapter(Context context, int textViewResourceId, List<Allo> objects, FragmentUpload mFragmentUpload) {
        super(context, textViewResourceId, objects);
        mContext = context;
        resId = textViewResourceId;
        allo_list = (ArrayList<Allo>) objects;
        listCount = allo_list.size();
        Inflater = (LayoutInflater) ((Activity) context).getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.mFragmentUpload = mFragmentUpload;
    }

    @Override
    public View getView(int position, View v, ViewGroup parent) {
        ViewHolder holder;
        if (v == null) {
            v = Inflater.inflate(resId, null);
            holder = new ViewHolder();
            holder.fileIV = (ImageView) v.findViewById(R.id.fileIV);
            holder.fileTV = (TextView) v.findViewById(R.id.fileTV);
            holder.selectSongPlayBtn = (ImageButton) v.findViewById(R.id.selectSongPlayBtn);
            holder.selectSongPlayLayout = (LinearLayout) v.findViewById(R.id.selectSongPlayLayout);

            v.setTag(holder);
        } else {
            holder = (ViewHolder) v.getTag();
        }

        Allo mAllo= allo_list.get(position);


        if (mAllo != null) {
            holder.fileTV.setText(mAllo.getRingTitle());

            if (mAllo.getIsPlaying()){
                holder.selectSongPlayBtn.setBackgroundResource(R.drawable.pause_btn);
            }else {
                holder.selectSongPlayBtn.setBackgroundResource(R.drawable.play_btn);
            }
            final int play_position = position;

            holder.selectSongPlayLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    for (int i = 0 ; i < allo_list.size(); i++) {
                        if (i != play_position) {
                            Allo allo = allo_list.get(i);
                            if (allo.getIsPlaying()) {
                                allo.setIsPlaying(false);
                                allo_list.set(i, allo);
                            }
                        }
                    }


                    Allo mAllo;
                    mAllo = allo_list.get(play_position);
                    if (mAllo.getIsPlaying()){
                        mAllo.setIsPlaying(false);
                    }else{
                        mAllo.setIsPlaying(true);
                    }
                    allo_list.set(play_position, mAllo);
                    mFragmentUpload.listViewUpdate(allo_list);
                    mFragmentUpload.playUpdate(mAllo);

                }
            });
        }
        return v;
    }



    private class ViewHolder {

        ImageView fileIV;
        TextView fileTV;
        ImageButton selectSongPlayBtn;
        LinearLayout selectSongPlayLayout;


    }

}