package com.stevensadler.android.blocspot.api.model.database.table;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

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
        return "CREATE TABLE IF NOT EXISTS " + getName() + " ("
                + COLUMN_ID + " INTEGER PRIMARY KEY,"
                + COLUMN_TITLE + " TEXT,"
                + COLUMN_COLOR + " INTEGER)";
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
                        + COLUMN_TITLE + " TEXT)");
            case 2:
                writableDatabase.execSQL("ALTER TABLE " + getName()
                        + "ADD COLUMN " + COLUMN_COLOR + " INTEGER");
            default:
                break;
        }
    }

    public Cursor fetchAll(SQLiteDatabase readonlyDatabase) {
        return readonlyDatabase.query(true, getName(), null, null,
                null,
                null, null, null, null);
//        try {
//            return readonlyDatabase.query(true, getName(), null, null,
//                    null,
//                    null, null, null, null);
//        } catch (SQLiteException e) {
//            // if the DB does not have a table named getName(),
//            // then it will throw a SQLiteException
//            return null;
//        }
    }
}
