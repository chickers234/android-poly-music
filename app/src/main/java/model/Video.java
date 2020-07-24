package model;

import java.io.Serializable;

public class Video implements Serializable {
    private String urlVideo;
    private String thumnail;
    private String title;
    private String decription;

    public Video() {
    }

    public Video(String urlVideo, String thumnail, String title, String decription) {
        this.urlVideo = urlVideo;
        this.thumnail = thumnail;
        this.title = title;
        this.decription = decription;
    }

    public String getUrlVideo() {
        return urlVideo;
    }

    public void setUrlVideo(String urlVideo) {
        this.urlVideo = urlVideo;
    }

    public String getThumnail() {
        return thumnail;
    }

    public void setThumnail(String thumnail) {
        this.thumnail = thumnail;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDecription() {
        return decription;
    }

    public void setDecription(String decription) {
        this.decription = decription;
    }

}