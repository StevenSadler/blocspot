package com.stevensadler.android.blocspot.api.model.database.table;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

/**
 * Created by Steven on 2/17/2016.
 */
public class PointOfInterestTable extends Table {

    public static class Builder implements Table.Builder {

        ContentValues values = new ContentValues();

        public Builder setGUID(String guid) {
            values.put(COLUMN_GUID, guid);
            return this;
        }

        public Builder setTitle(String title) {
            values.put(COLUMN_TITLE, title);
            return this;
        }

        public Builder setLatitude(float latitude) {
            values.put(COLUMN_LATITUDE, latitude);
            return this;
        }

        public Builder setLongitude(float longitude) {
            values.put(COLUMN_LONGITUDE, longitude);
            return this;
        }

        public Builder setRadius(float radius) {
            values.put(COLUMN_RADIUS, radius);
            return this;
        }

        @Override
        public long insert(SQLiteDatabase writeableDB) {
            return writeableDB.insert(NAME, null, values);
        }
    }

    public static String getGUID(Cursor cursor) {
        return getString(cursor, COLUMN_GUID);
    }
    public static String getTitle(Cursor cursor) {
        return getString(cursor, COLUMN_TITLE);
    }
    public static float getLatitude(Cursor cursor) {
        return getFloat(cursor, COLUMN_LATITUDE);
    }
    public static float getLongitude(Cursor cursor) {
        return getFloat(cursor, COLUMN_LONGITUDE);
    }
    public static float getRadius(Cursor cursor) {
        return getFloat(cursor, COLUMN_RADIUS);
    }

    private static final String NAME = "points_of_interest";

    private static final String COLUMN_GUID = "guid";
    private static final String COLUMN_TITLE = "title";
    private static final String COLUMN_LATITUDE = "latitude";
    private static final String COLUMN_LONGITUDE = "longitude";
    private static final String COLUMN_RADIUS = "geofence_radius";

    @Override
    public String getName() {
        return PointOfInterestTable.NAME;
    }

    @Override
    public String getCreateStatement() {
        return "CREATE TABLE " + getName() + " ("
                + COLUMN_ID + " INTEGER PRIMARY KEY,"
                + COLUMN_GUID + " TEXT,"
                + COLUMN_TITLE + " TEXT,"
                + COLUMN_LATITUDE + " REAL,"
                + COLUMN_LONGITUDE + " REAL,"
                + COLUMN_RADIUS + " REAL)";
    }

    public Cursor fetchAll(SQLiteDatabase readonlyDatabase) {
        return readonlyDatabase.query(true, getName(), null, null,
                null,
                null, null, null, null);
    }

    public Cursor fetchWithGuid(SQLiteDatabase readonlyDatabase, String guid) {
        return readonlyDatabase.query(true,getName(), null, "? = ?",
                new String[] {COLUMN_GUID, guid},
                null, null, null, null);
    }
}
