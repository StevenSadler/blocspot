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

    private static final String NAME = "points_of_interest";

    private static final String COLUMN_GUID = "guid";
    private static final String COLUMN_TITLE = "title";

    @Override
    public String getName() {
        return PointOfInterestTable.NAME;
    }

    @Override
    public String getCreateStatement() {
        return "CREATE TABLE " + getName() + " ("
                + COLUMN_ID + " INTEGER PRIMARY KEY,"
                + COLUMN_GUID + " TEXT,"
                + COLUMN_TITLE + " TEXT)";
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
