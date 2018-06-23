package com.climesoft.studenttimetable.model;

import android.os.Parcel;
import android.os.Parcelable;

public class CustomList implements Parcelable {
    private String id;
    private String topic;
    private String date;
    private String time;

    public CustomList(){

    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    protected CustomList(Parcel in) {
        id = in.readString();
        topic = in.readString();
        date = in.readString();
        time = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(topic);
        dest.writeString(date);
        dest.writeString(time);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<CustomList> CREATOR = new Parcelable.Creator<CustomList>() {
        @Override
        public CustomList createFromParcel(Parcel in) {
            return new CustomList(in);
        }

        @Override
        public CustomList[] newArray(int size) {
            return new CustomList[size];
        }
    };
}