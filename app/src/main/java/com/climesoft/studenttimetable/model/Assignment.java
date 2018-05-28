package com.climesoft.studenttimetable.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.firebase.firestore.DocumentReference;


public class Assignment implements Parcelable {
    private String id;
    private String topic;
    private String date;
    private String time;
    private String subjectId;

    public Assignment(){

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

    public String getSubjectId() {
        return subjectId;
    }

    public void setSubjectId(String subject) {
        this.subjectId = subject;
    }

    protected Assignment(Parcel in) {
        id = in.readString();
        topic = in.readString();
        date = in.readString();
        time = in.readString();
        subjectId = in.readString();
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
        dest.writeString(subjectId);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<Assignment> CREATOR = new Parcelable.Creator<Assignment>() {
        @Override
        public Assignment createFromParcel(Parcel in) {
            return new Assignment(in);
        }

        @Override
        public Assignment[] newArray(int size) {
            return new Assignment[size];
        }
    };
}