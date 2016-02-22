package com.stevensadler.android.blocspot.api.model;

/**
 * Created by Steven on 2/18/2016.
 */
public class PointOfInterest {

    public static String DEFAULT_GUID = "default_guid";
    public static String DEFAULT_TITLE = "default_title";

    private String guid;
    private String title;

    public PointOfInterest(String guid, String title) {
        this.guid = guid;
        this.title = title;
    }

    public PointOfInterest() {
        this.guid = DEFAULT_GUID;
        this.title = DEFAULT_TITLE;
    }

    public String getGuid() {
        return guid;
    }

    public String getTitle() {
        return title;
    }
}
