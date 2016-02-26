package com.stevensadler.android.blocspot.api.model;

/**
 * Created by Steven on 2/18/2016.
 */
public class PointOfInterest {

    public static String DEFAULT_GUID = "default_guid";
    public static String DEFAULT_TITLE = "default_title";
    public static float DEFAULT_LATITUDE = 38.0f;     // Point Reyes
    public static float DEFAULT_LONGITUDE = -123.0f;  // Point Reyes


    private String guid;
    private String title;
    private float latitude;
    private float longitude;

//    // constructor is a builder
//    public PointOfInterest(String guid, String title, float latitude, float longitude) {
//        this.guid = guid;
//        this.title = title;
//        this.latitude = latitude;
//        this.longitude = longitude;
//    }

    // constructor is a builder
    public PointOfInterest() {
        this.guid = DEFAULT_GUID;
        this.title = DEFAULT_TITLE;
        this.latitude = DEFAULT_LATITUDE;
        this.longitude = DEFAULT_LONGITUDE;
    }

    public PointOfInterest setGuid(String guid) {
        this.guid = guid;
        return this;
    }

    public PointOfInterest setTitle(String title) {
        this.title = title;
        return this;
    }

    public PointOfInterest setLatitude(float latitude) {
        this.latitude= latitude;
        return this;
    }

    public PointOfInterest setLongitude(float longitude) {
        this.longitude = longitude;
        return this;
    }

    public String getGuid() {
        return guid;
    }

    public String getTitle() {
        return title;
    }

    public float getLatitude() {
        return latitude;
    }

    public float getLongitude() {
        return longitude;
    }
}
