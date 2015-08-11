package com.imagination.technologies.ora.app.beans;

import android.os.Parcel;
import android.os.Parcelable;

public class MyLocation implements Parcelable {
    private String Longitude;
    private String Zipcode;
    private String ZipClass;
    private String County;
    private String City;
    private String State;
    private String Latitude;

    public String getLongitude() {
        return Longitude;
    }

    public void setLongitude(String longitude) {
        Longitude = longitude;
    }

    public String getZipcode() {
        return Zipcode;
    }

    public void setZipcode(String zipcode) {
        Zipcode = zipcode;
    }

    public String getZipClass() {
        return ZipClass;
    }

    public void setZipClass(String zipClass) {
        ZipClass = zipClass;
    }

    public String getCounty() {
        return County;
    }

    public void setCounty(String county) {
        County = county;
    }

    public String getCity() {
        return City;
    }

    public void setCity(String city) {
        City = city;
    }


    public String getState() {
        return State;
    }

    public void setState(String state) {
        State = state;
    }

    public String getLatitude() {
        return Latitude;
    }

    public void setLatitude(String latitude) {
        Latitude = latitude;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel out, int flags) {
        out.writeString(Longitude);
        out.writeString(Zipcode);
        out.writeString(ZipClass);
        out.writeString(County);
        out.writeString(City);
        out.writeString(State);
        out.writeString(Latitude);
    }

    public static final Creator<MyLocation> CREATOR
            = new Creator<MyLocation>() {
        public MyLocation createFromParcel(Parcel in) {
            return new MyLocation(in);
        }

        public MyLocation[] newArray(int size) {
            return new MyLocation[size];
        }
    };

    private MyLocation(Parcel in) {
        Longitude = in.readString();
        Zipcode = in.readString();
        ZipClass = in.readString();
        County= in.readString();
        City = in.readString();
        State = in.readString();
        Latitude = in.readString();
    }
}
