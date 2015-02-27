package com.example.uncheon.ringbacktone;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CursorAdapter;
import android.widget.TextView;

import com.loopj.android.http.RequestParams;

import java.io.File;
import java.io.FileNotFoundException;

/**
 * Created by baek_uncheon on 2015. 2. 26..
 */
public class FileAdapter extends CursorAdapter{
    Context mContext;
    Friend mFriend;
    RingbackTone mRinbackTone;

    public FileAdapter(Context context, Cursor c, boolean autoRequery) {
        super(context, c, autoRequery);
    }
    public FileAdapter(Context context, Cursor c, boolean autoRequery, Friend mFriend) {
        super(context, c, autoRequery);
        mContext =context;
        this.mFriend = mFriend;
        mRinbackTone = RingbackTone.getInstance();
    }

    @Override
    public View newView(final Context context, final Cursor cursor, ViewGroup parent) {
        ViewHolder holder;
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.layout_file_list_item, null);
        holder = new ViewHolder();
        holder.tv_file_name = (TextView)view.findViewById(R.id.tv_file_name);
        holder.btn_play_file = (Button)view.findViewById(R.id.btn_play_file);
        view.setTag(holder);
        final int positionInt = cursor.getPosition();

        view.setOnClickListener(new Button.OnClickListener(){
            @Override
            public void onClick(View v) {
                Log.i("on item click", "click click click");
                cursor.moveToPosition(positionInt);
                uploadDialog(cursor);

            }
        });
        return view;
    }

    private void uploadDialog(final Cursor cursor){
        String message = "Do you want to change "+mFriend.getPhoneNumber()+"'s ringbacktone to " + cursor.getString(4) + "?";
        AlertDialog.Builder alt_bld = new AlertDialog.Builder(mContext);
        alt_bld.setMessage(message).setCancelable(
                false).setPositiveButton("Yes",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // Action for 'Yes' Button

                        chageRingbackTone(cursor);
                    }
                }).setNegativeButton("No",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // Action for 'NO' Button
                        dialog.cancel();
                    }
                });
        AlertDialog alert = alt_bld.create();
        alert.setTitle("Allo");
        alert.setIcon(R.drawable.ic_launcher);
        alert.show();
    }

    private void chageRingbackTone(Cursor cursor)
    {

        File file = new File(cursor.getString(1));
        RequestParams params = new RequestParams();
        String url = mContext.getString(R.string.base_url) + "ringback/tone/upload/friend/";
        try{
            params.put("ringback_tone", file);
            params.put("title", cursor.getString(4));
            params.put("friend_id", mFriend.getFriendId());
            Log.i("friend id", mFriend.getFriendId());
        }catch (FileNotFoundException e){}

        AlloHTTPConnect allo;
        allo = new AlloHTTPConnect(mContext);
        allo.postFileUploadConnect(url, params);


    }



    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        ViewHolder holder;
        holder = (ViewHolder)view.getTag();
        holder.tv_file_name.setText(cursor.getString(4));
        PlayFileOnClickListener listener;
        listener = new PlayFileOnClickListener(cursor.getString(1));
        holder.btn_play_file.setOnClickListener(listener);
    }


    private class ViewHolder {
        TextView tv_file_name;
        Button btn_play_file;
    }

    private class PlayFileOnClickListener implements View.OnClickListener {
        String file_path;
        public PlayFileOnClickListener(String file_path){
            this.file_path = file_path;
        }
        @Override
        public void onClick(View v){
            switch (v.getId()) {
                case R.id.btn_play_file:
                    mRinbackTone.playRingbackTone(file_path);
            }
        }
    }

    protected void onDestroy(){
        mRinbackTone.stopRingbackTone();
    }
}
