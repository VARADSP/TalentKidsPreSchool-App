package com.example.varadsp.talentschool_myproject;

/**
 * Created by VARAD on 22-03-2018.
 */

public class VideoClass {
    String videoName;
    String videoUrl;
    String key;

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public VideoClass(){

    }

    public VideoClass(String videoName, String videoUrl,String key) {
        this.videoName = videoName;
        this.videoUrl = videoUrl;
        this.key = key;

    }

    public String getVideoName() {
        return videoName;
    }

    public void setVideoName(String videoName) {
        this.videoName = videoName;
    }

    public String getVideoUrl() {
        return videoUrl;
    }

    public void setVideoUrl(String videoUrl) {
        this.videoUrl = videoUrl;
    }
}
