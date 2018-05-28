package com.climesoft.studenttimetable.model;

import android.os.Parcel;
import android.os.Parcelable;

public class Subject implements Parcelable {

    public Subject(){

    }

    private String id;

    private String name;

    private String code;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    protected Subject(Parcel in) {
        id = in.readString();
        name = in.readString();
        code = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(name);
        dest.writeString(code);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<Subject> CREATOR = new Parcelable.Creator<Subject>() {
        @Override
        public Subject createFromParcel(Parcel in) {
            return new Subject(in);
        }

        @Override
        public Subject[] newArray(int size) {
            return new Subject[size];
        }
    };


    @Override
    public String toString(){
        return this.name.toUpperCase() + " " + this.code.toUpperCase();
    }
}
