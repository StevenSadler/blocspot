package com.stevensadler.android.blocspot.api.model.database.table;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

/**
 * Created by Steven on 2/17/2016.
 */
public abstract class Table {

    public static interface Builder {
        public long insert(SQLiteDatabase writeableDB);
    }

    protected static final String COLUMN_ID = "id";

    public abstract String getName();
    public abstract String getCreateStatement();

    abstract public void onUpgrade(SQLiteDatabase writableDatabase, int oldversion, int newversion);

    public Cursor fetchRow(SQLiteDatabase readonlyDatabase, long rowId) {
        return readonlyDatabase.query(true, getName(), null, COLUMN_ID + " = ?",
                new String[] {String.valueOf(rowId)}, null, null, null, null);
    }

    public static long getRowId(Cursor cursor) {
        return getLong(cursor, COLUMN_ID);
    }

    protected static String getString(Cursor cursor, String column) {
        int columnIndex = cursor.getColumnIndex(column);
        if (columnIndex == -1) {
            return "";
        }
        return cursor.getString(columnIndex);
    }

    protected static long getLong(Cursor cursor, String column) {
        int columnIndex = cursor.getColumnIndex(column);
        if (columnIndex == -1) {
            return -1l;
        }
        return cursor.getLong(columnIndex);
    }

    protected static float getFloat(Cursor cursor, String column) {
        int columnIndex = cursor.getColumnIndex(column);
        if (columnIndex == -1) {
            return Float.NaN;
        }
        return cursor.getFloat(columnIndex);
    }

    protected static int getInt(Cursor cursor, String column) {
        int columnIndex = cursor.getColumnIndex(column);
        if (columnIndex == -1) {
            return -1;
        }
        return cursor.getInt(columnIndex);
    }

    protected static boolean getBoolean(Cursor cursor, String column) {
        return getLong(cursor, column) == 1l;
    }
}
