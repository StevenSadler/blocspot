package com.stevensadler.android.blocspot.api.model;

/**
 * Created by Steven on 2/18/2016.
 */
public class PointOfInterest {

    public static long DEFAULT_ROWID = -99l;
    public static long DEFAULT_CATEGORY_ID = -99l;
    public static String DEFAULT_GUID = "default_guid";
    public static String DEFAULT_TITLE = "default_title";
    public static String DEFAULT_NOTE = "default_note";
    public static Boolean DEFAULT_VISITED = false;
    public static float DEFAULT_LATITUDE = 38.0f;     // Point Reyes
    public static float DEFAULT_LONGITUDE = -123.0f;  // Point Reyes
    public static float DEFAULT_RADIUS = 1000f;       // meters

    private long rowId;
    private long categoryId;
    private String guid;
    private String title;
    private String note;
    private Boolean visited;
    private float latitude;
    private float longitude;
    private float radiusMeters;

//    // constructor is a builder
//    public PointOfInterest(String guid, String title, float latitude, float longitude) {
//        this.guid = guid;
//        this.title = title;
//        this.latitude = latitude;
//        this.longitude = longitude;
//    }

    // constructor is a builder
    public PointOfInterest() {
        this.rowId = DEFAULT_ROWID;
        this.categoryId = DEFAULT_CATEGORY_ID;
        this.guid = DEFAULT_GUID;
        this.title = DEFAULT_TITLE;
        this.note = DEFAULT_NOTE;
        this.visited = DEFAULT_VISITED;
        this.latitude = DEFAULT_LATITUDE;
        this.longitude = DEFAULT_LONGITUDE;
        this.radiusMeters = DEFAULT_RADIUS;
    }


    public PointOfInterest setRowId(long rowId) {
        this.rowId = rowId;
        return this;
    }

    public PointOfInterest setCategoryId(long categoryId) {
        this.categoryId = categoryId;
        return this;
    }

    public PointOfInterest setGuid(String guid) {
        this.guid = guid;
        return this;
    }

    public PointOfInterest setTitle(String title) {
        this.title = title;
        return this;
    }

    public PointOfInterest setNote(String note) {
        this.note = note;
        return this;
    }

    public PointOfInterest setVisited(Boolean visited) {
        this.visited = visited;
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

    public PointOfInterest setRadius(float radius) {
        this.radiusMeters = radius;
        return this;
    }

    public long getRowId() {
        return rowId;
    }
    public long getCategoryId() {
        return categoryId;
    }
    public String getGuid() {
        return guid;
    }
    public String getTitle() {
        return title;
    }
    public String getNote() {
        return note;
    }
    public Boolean getVisited() { return visited; }
    public float getLatitude() {
        return latitude;
    }
    public float getLongitude() {
        return longitude;
    }
    public float getRadius() {
        return radiusMeters;
    }
}
