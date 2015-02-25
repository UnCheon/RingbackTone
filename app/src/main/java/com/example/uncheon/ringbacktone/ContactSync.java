package com.example.uncheon.ringbacktone;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;
import android.util.Log;

import java.util.ArrayList;

/**
 * Created by baek_uncheon on 2015. 1. 27..
 */
public class ContactSync {

    private static final int UPDATE_FRIENDS_LIST = 0;

    Activity mActivity;
    Context mContext;

    ContactSync(FriendListActivity activity){
        this.mActivity = activity;
        this.mContext = activity;
    }

    public void syncLocalContacts() {
        ContactDBOpenHelper mContactDBOpenHelper = new ContactDBOpenHelper(mContext);
        mContactDBOpenHelper.open_writableDatabase();

        ArrayList<Contact> contact_list = getContactList();
        ArrayList<Contact> contact_db_list = getContactDBList();

        Log.i("before sync", String.valueOf(contact_db_list.size())+"  : "+String.valueOf(contact_list.size()));

        String phone_number;
        Boolean is_new;

        for (int i = 0 ; i < contact_list.size() ; i++) {
            phone_number = contact_list.get(i).getPhonenum();
            is_new = true;
            for (int j = 0 ; j < contact_db_list.size() ; j++){
                if (phone_number.equals(contact_db_list.get(j).getPhonenum())) {
                    is_new = false;
                }
            }
            if(is_new){
                Log.i("tag", "is new ");
                mContactDBOpenHelper.setContact(phone_number, is_new);
            }
        }
        mContactDBOpenHelper.close();
    }


    public ArrayList<Contact> getContactDBList() {

        Cursor cursor;

        ContactDBOpenHelper mContactDBOpenHelper = new ContactDBOpenHelper(mContext);
        mContactDBOpenHelper.open_writableDatabase();


        cursor = mContactDBOpenHelper.getAllContacts();

        ArrayList<Contact> contact_db_list = new ArrayList<Contact>();

        if (cursor.moveToFirst()) {
            do {
                Contact aContact = new Contact();
                aContact.setPhonenum(cursor.getString(1));
                aContact.setNickname("");
                aContact.setIsNew(Boolean.parseBoolean(cursor.getString(2)));

                contact_db_list.add(aContact);

            } while (cursor.moveToNext());
        }

        return contact_db_list;

    }





    public ArrayList<Contact> getContactList() {


        Uri uri = ContactsContract.CommonDataKinds.Phone.CONTENT_URI;

        String[] projection = new String[] {
                ContactsContract.CommonDataKinds.Phone.CONTACT_ID,
                ContactsContract.CommonDataKinds.Phone.NUMBER,
                ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME };

        String[] selectionArgs = null;

        String sortOrder = ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME
                + " COLLATE LOCALIZED ASC";

        Cursor contactCursor = mActivity.managedQuery(uri, projection, null,
                selectionArgs, sortOrder);

        ArrayList<Contact> contact_list = new ArrayList<Contact>();

        if (contactCursor.moveToFirst()) {
            do {
                String phone_number = contactCursor.getString(1).replace("-", "");

                if (phone_number.startsWith("+82")) {
                    phone_number = phone_number.replace(
                            "+82", "0");
                }


                Contact aContact = new Contact();
                aContact.setPhonenum(phone_number);
                aContact.setNickname(contactCursor.getString(2));

                contact_list.add(aContact);
            } while (contactCursor.moveToNext());
        }
        return contact_list;
    }

}

