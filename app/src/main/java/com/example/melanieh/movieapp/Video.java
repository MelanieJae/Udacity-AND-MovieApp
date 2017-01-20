package com.example.melanieh.movieapp;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by melanieh on 12/3/16.
 */

public class Video implements Parcelable {

    private String videoTitle;
    private String urlString;
    String[] videoParamStrings = {videoTitle, urlString};

    public Video(String videoTitle, String urlString) {
        this.videoTitle = videoTitle;
        this.urlString = urlString;
    }

    public String getVideoTitle() {
        return videoTitle;
    }

    public String getUrlString() {
        return urlString;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public String toString() {
        return "Video{" +
                "CREATOR=" + CREATOR +
                ", videoTitle='" + videoTitle + '\'' +
                ", urlString='" + urlString + '\'' +
                ", videoParamStrings=" + Arrays.toString(videoParamStrings) +
                '}';
    }

    // parcelable implementation

    public final Parcelable.Creator<Video> CREATOR = new Parcelable.Creator<Video>() {
        @Override
        public Video createFromParcel(Parcel parcel) {
            return new Video(parcel);
        }

        @Override
        public Video[] newArray(int i) {
            return new Video[0];
        }
    };

    public void writeToParcel(Parcel parcel, int flags) {
        parcel.writeStringArray(videoParamStrings);
    }

    private Video(Parcel in) {
        readFromParcel(in);
    }

    private void readFromParcel(Parcel in) {
        in.readStringArray(videoParamStrings);
    }
}

