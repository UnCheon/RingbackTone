package com.example.uncheon.ringbacktone;

/**
 * Created by baek_uncheon on 2015. 1. 29..
 */
public class Friend {
    String nickname;
    String phone_number;
    String friend_id;
    String ring_to_me_url;
    String ring_to_me_title;
    String ring_to_friend_url;
    String ring_to_friend_title;

    Boolean is_new;



    Friend(){}

    Friend(String phone_number, String nickname, String friend_id, String ring_to_me_title, String ring_to_me_url, String ring_to_friend_title, String ring_to_friend_url, boolean is_new){
        this.phone_number = phone_number;
        this.nickname = nickname;
        this.friend_id = friend_id;
        this.ring_to_me_title = ring_to_me_title;
        this.ring_to_me_url = ring_to_me_url;
        this.ring_to_friend_title = ring_to_friend_title;
        this.ring_to_friend_url = ring_to_friend_url;
        this.is_new = is_new;
    }

    public String getNickname() {
        return nickname;
    }
    public void setNickname(String nickname) {	this.nickname = nickname;}

    public String getPhoneNumber() {
        return phone_number;
    }
    public void setPhoneNumber(String phone_number) {
        this.phone_number = phone_number;
    }

    public String getFriendId(){ return friend_id; }
    public void setFriendId(String friend_id){ this.friend_id = friend_id; }

    public String getRingToMeTitle(){ return ring_to_me_title; }
    public void setRingToMeTitle(String ring_to_me_title){ this.ring_to_me_title = ring_to_me_title; }

    public String getRingToMeURL(){ return ring_to_me_url; }
    public void setRingToMeURL(String ring_to_me_url){ this.ring_to_me_url = ring_to_me_url; }

    public String getRingToFriendTitle(){ return ring_to_friend_title; }
    public void setRingToFriendTitle(String ring_to_friend_title){ this.ring_to_friend_title = ring_to_friend_title; }

    public String getRingToFriendURL(){ return ring_to_friend_url; }
    public void setRingToFriendURL(String ring_to_friend_url){ this.ring_to_friend_url = ring_to_friend_url; }

    public boolean getIsNew(){ return is_new; }
    public void setIsNew(boolean is_new){ this.is_new= is_new; }


}
