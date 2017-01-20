package com.example.melanieh.movieapp;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by melanieh on 12/3/16.
 */

public class Review implements Parcelable{

    String author;
    String title;
    String[] reviewParamStrings = {author, title};

    public Review(String author, String title) {
        this.author = author;
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public String getText() {
        return title;
    }

    // parcelable implementation

    public final Parcelable.Creator<Review> CREATOR = new Parcelable.Creator<Review>() {
        @Override
        public Review createFromParcel(Parcel parcel) {
            return new Review(parcel);
        }

        @Override
        public Review[] newArray(int i) {
            return new Review[0];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel parcel, int flags) {
        parcel.writeStringArray(reviewParamStrings);
    }

    private Review(Parcel in) {
        readFromParcel(in);
    }

    private void readFromParcel(Parcel in) {
        in.readStringArray(reviewParamStrings);
    }
}
