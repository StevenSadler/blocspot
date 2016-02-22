package com.stevensadler.android.blocspot.api.model;

/**
 * Created by Steven on 2/18/2016.
 */
public abstract class Model {

    private final long rowId;

    public Model(long rowId) {
        this.rowId = rowId;
    }

    public long getRowId() {
        return rowId;
    }
}
