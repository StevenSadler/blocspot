package com.stevensadler.android.blocspot.api;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.stevensadler.android.blocspot.api.model.PointOfInterest;
import com.stevensadler.android.blocspot.api.model.database.DatabaseOpenHelper;
import com.stevensadler.android.blocspot.api.model.database.table.PointOfInterestTable;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Steven on 2/17/2016.
 */
public class DataSource {

    private static String TAG = DataSource.class.getSimpleName();

    private DatabaseOpenHelper mDatabaseOpenHelper;
    private PointOfInterestTable mPointOfInterestTable;

    private List<PointOfInterest> mPointsOfInterest;

    public DataSource(Context context) {
        mPointOfInterestTable = new PointOfInterestTable();
        mDatabaseOpenHelper = new DatabaseOpenHelper(context,
                mPointOfInterestTable);

        mPointsOfInterest = readTableToModel();
    }

    public long insertPointOfInterest(PointOfInterest pointOfInterest) {
        SQLiteDatabase writableDatabase = mDatabaseOpenHelper.getWritableDatabase();
        PointOfInterestTable.Builder builder = new PointOfInterestTable.Builder()
                .setTitle(pointOfInterest.getTitle())
                .setGUID(pointOfInterest.getGuid())
                .setLatitude(pointOfInterest.getLatitude())
                .setLongitude(pointOfInterest.getLongitude());

        long rowId = builder.insert(writableDatabase);
        return rowId;
    }

    public List<PointOfInterest> getPointsOfInterest() {
        return mPointsOfInterest;
    }

    public static PointOfInterest pointOfInterestFromCursor(Cursor cursor) {
        return new PointOfInterest()
                .setRowId(PointOfInterestTable.getRowId(cursor))
                .setGuid(PointOfInterestTable.getGUID(cursor))
                .setTitle(PointOfInterestTable.getTitle(cursor))
                .setLatitude(PointOfInterestTable.getLatitude(cursor))
                .setLongitude(PointOfInterestTable.getLongitude(cursor));
    }

    private List<PointOfInterest> readTableToModel() {
        Log.v(TAG, "readTableToModel");

        List<PointOfInterest> pointsOfInterest = new ArrayList<PointOfInterest>();
        Cursor itemCursor = mPointOfInterestTable.fetchAll(mDatabaseOpenHelper.getReadableDatabase());

        if (itemCursor.moveToFirst()) {
            do {
                PointOfInterest newPOI = pointOfInterestFromCursor(itemCursor);
                pointsOfInterest.add(newPOI);
            } while (itemCursor.moveToNext());
        }
        return pointsOfInterest;
    }

    public void createTestData(Context context) {
        context.deleteDatabase("blocspot_db");

//        mPointsOfInterest.add(new PointOfInterest());
//        mPointsOfInterest.add(new PointOfInterest());
//        mPointsOfInterest.add(new PointOfInterest()
//                .setGuid("guid1")
//                .setTitle("title 1")
//                .setLatitude(38f)
//                .setLongitude(-122f));
//        mPointsOfInterest.add(new PointOfInterest()
//                .setGuid("guid2")
//                .setTitle("title 2")
//                .setLatitude(37f)
//                .setLongitude(-122f));
//        mPointsOfInterest.add(new PointOfInterest()
//                .setGuid("guid3")
//                .setTitle("title 3")
//                .setLatitude(36f)
//                .setLongitude(-122f));

//        mPointsOfInterest.add(new PointOfInterest()
//                .setTitle("Picante Berkeley")
//                .setLatitude(37.8781f)
//                .setLongitude(-122.3010f));

        mPointsOfInterest.add(new PointOfInterest()
                .setTitle("Albany Hill")
                .setLatitude(37.892f)
                .setLongitude(-122.306f));

        Log.v(TAG, "createTestData end");
    }

    public void testDatabase() {
        Log.v(TAG, "testDatabase");

        Cursor itemCursor = mPointOfInterestTable.fetchAll(mDatabaseOpenHelper.getReadableDatabase());

        itemCursor.moveToFirst();
        do {
            PointOfInterest newPOI = pointOfInterestFromCursor(itemCursor);
            Log.v(TAG, "testDatabase " + newPOI.getTitle());
        } while (itemCursor.moveToNext());

    }

    /*
     * test function used by ApplicationTest
     */
    public Cursor getCursorOfInsertedPOIWithGuid(String guid) {
        // query the DB for the passed pointOfInterest
        Cursor itemCursor = mPointOfInterestTable.fetchWithGuid(
                mDatabaseOpenHelper.getReadableDatabase(), guid);
        return itemCursor;
    }
}
