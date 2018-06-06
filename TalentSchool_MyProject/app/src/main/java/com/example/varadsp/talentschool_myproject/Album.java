package com.example.varadsp.talentschool_myproject;

/**
 * Created by Lincoln on 18/05/16.
 */
public class Album {
    private String name;
    private String numOfSongs;
    private int thumbnail;
    private String videoDescription;
    private String key;

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public Album() {
    }

    public Album(String name, String numOfSongs, int thumbnail,String videoDescription,String key) {
        this.name = name;
        this.numOfSongs = numOfSongs;
        this.thumbnail = thumbnail;
        this.videoDescription = videoDescription;
        this.key = key;
    }

    public String getVideoDescription() {
        return videoDescription;
    }

    public void setVideoDescription(String videoDescription) {
        this.videoDescription = videoDescription;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNumOfSongs() {
        return numOfSongs;
    }

    public void setNumOfSongs(String numOfSongs) {
        this.numOfSongs = numOfSongs;
    }

    public int getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(int thumbnail) {
        this.thumbnail = thumbnail;
    }
}
