package com.allo;

/**
 * Created by baek_uncheon on 2015. 3. 26..
 */
public class Allo {
    String ringTitle;
    String ringSinger;
    String ringAlbum;
    String ringURL;
    String ringId;
    boolean isPlaying;

    public void setRingTitle(String ringTitle) {
        this.ringTitle = ringTitle;
    }
    public String getRingTitle(){return ringTitle;}

    public void setRingSinger(String ringSinger) {
        this.ringSinger = ringSinger;
    }
    public String getRingSinger(){return ringSinger;}

    public void setRingAlbum(String ringAlbum) {
        this.ringAlbum = ringAlbum;
    }
    public String getRingAlbum(){return ringAlbum;}

    public void setRingURL(String ringURL) {
        this.ringURL = ringURL;
    }
    public String getRingURL(){return ringURL;}

    public void setRingId(String ringId) {
        this.ringId = ringId;
    }
    public String getRingId(){return ringId;}

    public void setIsPlaying (boolean isPlaying){this.isPlaying = isPlaying;}
    public boolean getIsPlaying(){return isPlaying;}
}
