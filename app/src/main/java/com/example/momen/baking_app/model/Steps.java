package com.example.momen.baking_app.model;

/**
 * Created by Momen on 2/6/2019.
 */

public class Steps {

    private String shortDesc;
    private String desc;
    private String videoURL;
    private String thumbnailURL;

    public Steps(){}

    public Steps(String shortDesc,String desc,String videoURL,String thumbnailURL){
        this.shortDesc = shortDesc;
        this.desc = desc;
        this.videoURL = videoURL;
        this.thumbnailURL = thumbnailURL;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public void setShortDesc(String shortDesc) {
        this.shortDesc = shortDesc;
    }

    public void setThumbnailURL(String thumbnailURL) {
        this.thumbnailURL = thumbnailURL;
    }

    public void setVideoURL(String videoURL) {
        this.videoURL = videoURL;
    }


    public String getDesc() {
        return desc;
    }

    public String getShortDesc() {
        return shortDesc;
    }

    public String getThumbnailURL() {
        return thumbnailURL;
    }

    public String getVideoURL() {
        return videoURL;
    }
}
