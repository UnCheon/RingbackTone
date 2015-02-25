package com.example.uncheon.ringbacktone;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by baek_uncheon on 2015. 1. 19..
 */

public class RBDBOpenHelper {
    private static final String DATABASE_NAME = "RBManager.db";
    public static SQLiteDatabase mDB;
    private static final String TABLE_NAME = "rb_manager";
    private DatabaseHelper mDBHelper;
    private Context mContext;

    private class DatabaseHelper extends SQLiteOpenHelper {


        public DatabaseHelper(Context context) {
            super(context, DATABASE_NAME, null, 1);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL("CREATE TABLE "+TABLE_NAME+" ( _id INTEGER PRIMARY KEY AUTOINCREMENT, phone_number TEXT UNIQUE, nickname TEXT, friend_friend_id INTEGER);");
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            db.execSQL("DROP IF EXISTS "+ TABLE_NAME);
            onCreate(db);
        }
    }

    public RBDBOpenHelper(Context context){
        this.mContext = context;
    }

    public RBDBOpenHelper open_writableDatabase() throws SQLException{
        mDBHelper = new DatabaseHelper(mContext);
        mDB = mDBHelper.getWritableDatabase();
        return this;
    }

    public RBDBOpenHelper open_readableDatabase() throws SQLException{
        mDBHelper = new DatabaseHelper(mContext);
        mDB = mDBHelper.getReadableDatabase();
        return this;
    }



    public void close(){
        mDB.close();
    }

    public void deleteAll() {
        mDB.delete(TABLE_NAME, null, null);
    }

    public Cursor getFriends(){ return mDB.query(TABLE_NAME, null, null, null, null, null, null, null); }

    public void setFriends (JSONArray response_object) {
        ContentValues row;

        for (int i = 0; i < response_object.length(); i++) {
            try{
                JSONObject friend_object = response_object.getJSONObject(i);

                    String phone_number = friend_object.getString("phone_number");
                    mDB.execSQL("DELETE FROM "+TABLE_NAME+" where phone_number='"+phone_number+"';");

                    row = new ContentValues();
                    row.put("phone_number", friend_object.getString("phone_number"));
                    row.put("nickname", friend_object.getString("nickname"));
                    row.put("friend_friend_id", friend_object.getInt("friend_friend_id"));
                    mDB.insert(TABLE_NAME, null, row);

                    Log.i("friends db", "insert");
            }catch(JSONException e){}
        }
    }
}
