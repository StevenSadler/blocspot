package com.stevensadler.android.blocspot.api.model.database.table;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.stevensadler.android.blocspot.api.model.Category;

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

        public Builder setCategoryId(long categoryId) {
            values.put(COLUMN_CATEGORY_ID, categoryId);
            return this;
        }

        public Builder setTitle(String title) {
            values.put(COLUMN_TITLE, title);
            return this;
        }

        public Builder setNote(String note) {
            values.put(COLUMN_NOTE, note);
            return this;
        }

        public Builder setVisited(Boolean visited) {
            values.put(COLUMN_VISITED, (visited) ? 1 : 0);
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
        public long insert(SQLiteDatabase writableDatabase) {
            return writableDatabase.insert(NAME, null, values);
        }
    }

    public static String getGUID(Cursor cursor) {
        return getString(cursor, COLUMN_GUID);
    }
    public static long getCategoryId(Cursor cursor) {
        return getLong(cursor, COLUMN_CATEGORY_ID);
    }
    public static String getTitle(Cursor cursor) {
        return getString(cursor, COLUMN_TITLE);
    }
    public static String getNote(Cursor cursor) {
        return getString(cursor, COLUMN_NOTE);
    }
    public static Boolean getVisited(Cursor cursor) { return getBoolean(cursor, COLUMN_VISITED); }
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
    private static final String COLUMN_CATEGORY_ID = "category_id";
    private static final String COLUMN_TITLE = "title";
    private static final String COLUMN_NOTE = "note";
    private static final String COLUMN_VISITED = "visited";
    private static final String COLUMN_LATITUDE = "latitude";
    private static final String COLUMN_LONGITUDE = "longitude";
    private static final String COLUMN_RADIUS = "geofence_radius";

    @Override
    public String getName() {
        return PointOfInterestTable.NAME;
    }

    @Override
    public String getCreateStatement() {
        return "CREATE TABLE IF NOT EXISTS " + getName() + " ("
                + COLUMN_ID + " INTEGER PRIMARY KEY,"
                + COLUMN_GUID + " TEXT,"
                + COLUMN_TITLE + " TEXT,"
                + COLUMN_NOTE + " TEXT,"
                + COLUMN_VISITED + " INTEGER,"
                + COLUMN_LATITUDE + " REAL,"
                + COLUMN_LONGITUDE + " REAL,"
                + COLUMN_RADIUS + " REAL,"
                + COLUMN_CATEGORY_ID + " INTEGER)";
    }

    @Override
    public void onUpgrade(SQLiteDatabase writableDatabase, int oldversion, int newversion) {
        // this only matters on production apps
        //
        // on oldversion 1, upgrade would never hit that case, but it might be
        // a visual record of development of the original getCreateStatement
        //
        // on all oldversions after 1, we need to use something like
        // "ALTER TABLE " + getName() +
        // "  ADD COLUMN column_name column_definition;"
        //
        switch (oldversion) {
            case 1:
                writableDatabase.execSQL("CREATE TABLE " + getName() + " ("
                        + COLUMN_ID + " INTEGER PRIMARY KEY,"
                        + COLUMN_GUID + " TEXT,"
                        + COLUMN_TITLE + " TEXT,"
                        + COLUMN_LATITUDE + " REAL,"
                        + COLUMN_LONGITUDE + " REAL,"
                        + COLUMN_RADIUS + " REAL)");
            case 2:
                writableDatabase.execSQL("ALTER TABLE " + getName()
                        + "ADD COLUMN " + COLUMN_CATEGORY_ID + " INTEGER");
            default:
                break;
        }
    }

    public Cursor fetchAll(SQLiteDatabase readonlyDatabase) {
        return readonlyDatabase.query(true, getName(), null, null,
                null,
                null, null, null, null);
    }

    public int updateCategoryId(SQLiteDatabase writableDatabase, Category category, long rowId) {
        ContentValues values = new ContentValues();
        values.put(COLUMN_CATEGORY_ID, category.getRowId());
        return writableDatabase.update(NAME, values, COLUMN_ID + " = " + rowId, null);
    }

    public int updateVisited(SQLiteDatabase writableDatabase, Boolean visited, long rowId) {
        ContentValues values = new ContentValues();
        values.put(COLUMN_VISITED, (visited) ? 1 : 0);
        return writableDatabase.update(NAME, values, COLUMN_ID + " = " + rowId, null);
    }

    public int deleteRow(SQLiteDatabase writableDatabase, long rowId) {
        return writableDatabase.delete(NAME, COLUMN_ID + " = " + rowId, null);
    }
}
