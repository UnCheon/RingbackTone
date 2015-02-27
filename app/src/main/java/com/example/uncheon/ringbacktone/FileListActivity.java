package com.example.uncheon.ringbacktone;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.ListView;


public class FileListActivity extends Activity {

    private ListView lv_file_list;
    FileAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_file_list);

        Intent intent = getIntent();
        Friend mFriend = (Friend)intent.getSerializableExtra("friend info");

        Cursor c = this.getContentResolver().query(
                MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
                new String[] {
                        MediaStore.Audio.Media._ID,
                        MediaStore.Audio.Media.DATA,
                        MediaStore.Audio.Media.ARTIST,
                        MediaStore.Audio.Media.ALBUM,
                        MediaStore.Audio.Media.DISPLAY_NAME }, "1=1", null, null);

        Log.i("cursor", String.valueOf(c.getCount()));
        lv_file_list = (ListView)findViewById(R.id.lv_file_list);
        adapter = new FileAdapter(this, c, true, mFriend);
        lv_file_list.setAdapter(adapter);
    }



    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected  void onDestroy(){
        super.onDestroy();
        adapter.onDestroy();
    }
}
