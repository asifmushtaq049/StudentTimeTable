package com.climesoft.studenttimetable.model;

import android.os.Parcel;
import android.os.Parcelable;

public class TimeTable implements Parcelable {
    private String id;
    private String subjectId;
    private String time;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSubjectId() {
        return subjectId;
    }

    public void setSubjectId(String subjectId) {
        this.subjectId = subjectId;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public TimeTable(){

    }

    protected TimeTable(Parcel in) {
        id = in.readString();
        subjectId = in.readString();
        time = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(subjectId);
        dest.writeString(time);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<TimeTable> CREATOR = new Parcelable.Creator<TimeTable>() {
        @Override
        public TimeTable createFromParcel(Parcel in) {
            return new TimeTable(in);
        }

        @Override
        public TimeTable[] newArray(int size) {
            return new TimeTable[size];
        }
    };
}
