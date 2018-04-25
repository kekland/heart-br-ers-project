package com.heartbrers.air;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;

/**
 * Created by nrgz on 12.04.2018.
 */

 //This class contains information about Tutorial video
public class TutorialsInfo {
    private String placeholderURL; //URL of placeholder (thumbnail) image
    private String videoURL; //URL of the video itself
    private String videoName; //Name of the video

    //Constructor
    public TutorialsInfo(DataSnapshot ref) {
        videoName = ref.child("videoName").getValue().toString();
        placeholderURL = ref.child("videoPlaceholder").getValue().toString();
        videoURL = ref.child("videoURL").getValue().toString();
    }

    //Getters & Setters
    public String getPlaceholderURL() {
        return placeholderURL;
    }

    public void setPlaceholderURL(String placeholderURL) {
        this.placeholderURL = placeholderURL;
    }

    public String getVideoURL() {
        return videoURL;
    }

    public void setVideoURL(String videoURL) {
        this.videoURL = videoURL;
    }

    public String getVideoName() {
        return videoName;
    }

    public void setVideoName(String videoName) {
        this.videoName = videoName;
    }

}
