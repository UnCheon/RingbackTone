package com.allo;


import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class StoreAdapter extends ArrayAdapter<Allo> {

    private int resId;
    private ArrayList<Allo> allo_list;


    private LayoutInflater Inflater;
    private Context context;
    public int listCount = 0;


    public StoreAdapter(Context context, int textViewResourceId, List<Allo> objects ) {
        super(context, textViewResourceId, objects);
        this.context = context;
        resId = textViewResourceId;
        allo_list = (ArrayList<Allo>) objects;
        listCount = allo_list.size();
        Inflater = (LayoutInflater) ((Activity) context).getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }


    @Override
    public View getView(int position, View v, ViewGroup parent) {
        ViewHolder holder;
        if (v == null) {
            v = Inflater.inflate(resId, null);
            holder = new ViewHolder();
            holder.imageBtn = (ImageView) v.findViewById(R.id.imageBtn);
            holder.rankTV = (TextView) v.findViewById(R.id.rankTV);
            holder.songTV = (TextView) v.findViewById(R.id.songTV);
            holder.artistTV = (TextView) v.findViewById(R.id.artistTV);
            holder.moreBtn = (ImageView) v.findViewById(R.id.moreBtn);


            v.setTag(holder);
        } else {
            holder = (ViewHolder) v.getTag();
        }

        Allo mAllo= allo_list.get(position);

        if (mAllo != null) {

            holder.rankTV.setText(mAllo.getRingRank());
            holder.songTV.setText(mAllo.getRingTitle());
            holder.artistTV.setText(mAllo.getRingSinger());


            final int play_position = position;

        }
        return v;
    }



    private class ViewHolder {

        ImageView imageBtn;
        TextView rankTV;
        TextView songTV;
        TextView artistTV;
        ImageView moreBtn;


    }

}