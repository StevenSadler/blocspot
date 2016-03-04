package com.stevensadler.android.blocspot.api.model;

/**
 * Created by Steven on 3/2/2016.
 */
public class Category {

    public static long DEFAULT_ROWID = -1l;
    public static String DEFAULT_TITLE = "default_title";
    public static int DEFAULT_COLOR = 0xFF0000FF;

    private long rowId;
    private String title;
    private int color;

    public Category() {
        this.rowId = DEFAULT_ROWID;
        this.title = DEFAULT_TITLE;
        this.color = DEFAULT_COLOR;
    }

    public Category setRowId(long rowId) {
        this.rowId = rowId;
        return this;
    }

    public Category setTitle(String title) {
        this.title = title;
        return this;
    }

    public Category setColor(int color) {
        this.color = color;
        return this;
    }

    public long getRowId() { return rowId; }

    public String getTitle() {
        return title;
    }

    public int getColor() {
        return color;
    }
}
