package com.allo;

import java.io.Serializable;

/**
 * Created by baek_uncheon on 2015. 1. 29..
 */
public class Friend implements Serializable{
    String nickname;
    String phone_number;
    String friend_id;
    String ring_url;
    String ring_title;
    String ring_singer;
    String ring_album;
    String start_point;
    String ring_count;
    boolean isPlaying;


    String ring_to_me_url;
    String ring_to_me_title;
    String ring_to_friend_url;
    String ring_to_friend_title;
    String ring_to_friend_singer;


    Boolean is_new;


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

    public String getRingTitle(){ return ring_title; }
    public void setRingTitle(String ring_title){ this.ring_title = ring_title; }

    public String getRingURL(){ return ring_url; }
    public void setRingURL(String ring_url){ this.ring_url = ring_url; }

    public String getRingSinger(){ return ring_singer; }
    public void setRingSinger(String ring_singer){ this.ring_singer = ring_singer; }

    public String getRingAlbum(){return ring_album;}
    public void setRingAlbum(String ring_album){this.ring_album = ring_album;}

    public String getRingCount(){return ring_count;}
    public String setRingCount(String ring_count){return this.ring_count=ring_count;}

    public String getStartPoint(){return start_point;}
    public void setStartPoint(String start_point){this.start_point=start_point;}

    public boolean getIsPlaying(){return isPlaying;}
    public void setIsPlaying(boolean isPlaying){this.isPlaying=isPlaying;}



    public String getRingToMeTitle(){ return ring_to_me_title; }
    public void setRingToMeTitle(String ring_to_me_title){ this.ring_to_me_title = ring_to_me_title; }

    public String getRingToMeURL(){ return ring_to_me_url; }
    public void setRingToMeURL(String ring_to_me_url){ this.ring_to_me_url = ring_to_me_url; }

    public String getRingToFriendTitle(){ return ring_to_friend_title; }
    public void setRingToFriendTitle(String ring_to_friend_title){ this.ring_to_friend_title = ring_to_friend_title; }

    public String getRingToFriendURL(){ return ring_to_friend_url; }
    public void setRingToFriendURL(String ring_to_friend_url){ this.ring_to_friend_url = ring_to_friend_url; }

    public String getRingToFriendSinger(){ return ring_to_friend_singer; }
    public void setRingToFriendSinger(String ring_to_friend_singer){ this.ring_to_friend_singer = ring_to_friend_singer; }


    public boolean getIsNew(){ return is_new; }
    public void setIsNew(boolean is_new){ this.is_new= is_new; }


}
