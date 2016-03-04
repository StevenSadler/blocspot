package com.stevensadler.android.blocspot.api.model.database.table;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;

/**
 * Created by Steven on 3/2/2016.
 */
public class CategoryTable extends Table {

    public static class Builder implements Table.Builder {

        ContentValues values = new ContentValues();

        public Builder setTitle(String title) {
            values.put(COLUMN_TITLE, title);
            return this;
        }

        public Builder setColor(int color) {
            values.put(COLUMN_COLOR, color);
            return this;
        }

        @Override
        public long insert(SQLiteDatabase writeableDB) {
            return writeableDB.insert(NAME, null, values);
        }
    }

    public static String getTitle(Cursor cursor) {
        return getString(cursor, COLUMN_TITLE);
    }
    public static int getColor(Cursor cursor) {
        return getInt(cursor, COLUMN_COLOR);
    }

    private static final String NAME = "categories";

    private static final String COLUMN_TITLE = "title";
    private static final String COLUMN_COLOR = "color";

    @Override
    public String getName() {
        return CategoryTable.NAME;
    }

    @Override
    public String getCreateStatement() {
        return "CREATE TABLE " + getName() + " ("
                + COLUMN_ID + " INTEGER PRIMARY KEY,"
                + COLUMN_TITLE + " TEXT,"
                + COLUMN_COLOR + " INTEGER)";
    }

    public Cursor fetchAll(SQLiteDatabase readonlyDatabase) {
        try {
            return readonlyDatabase.query(true, getName(), null, null,
                    null,
                    null, null, null, null);
        } catch (SQLiteException e) {
            // if the DB does not have a table named getName(),
            // then it will throw a SQLiteException
            return null;
        }
    }

}
