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

        mPointsOfInterest = new ArrayList<PointOfInterest>();
        createTestData(context);

        new Thread(new Runnable() {
            @Override
            public void run() {
                SQLiteDatabase writableDatabase = mDatabaseOpenHelper.getWritableDatabase();

               for (PointOfInterest pointOfInterest : mPointsOfInterest) {
//                   new PointOfInterestTable.Builder()
//                           .setTitle(pointOfInterest.getTitle())
//                           .insert(writableDatabase);

                   insertPointOfInterest(pointOfInterest);
               }
                Log.d(TAG, "constructor thread inner run complete");
            }
        }).start();
    }

    public void insertPointOfInterest(PointOfInterest pointOfInterest) {
        SQLiteDatabase writableDatabase = mDatabaseOpenHelper.getWritableDatabase();
        new PointOfInterestTable.Builder()
                .setTitle(pointOfInterest.getTitle())
                .setGUID(pointOfInterest.getGuid())
                .setLatitude(pointOfInterest.getLatitude())
                .setLongitude(pointOfInterest.getLongitude())
                .insert(writableDatabase);
    }

    public List<PointOfInterest> getPointsOfInterest() {
        return mPointsOfInterest;
    }

    public static PointOfInterest pointOfInterestFromCursor(Cursor cursor) {
        return new PointOfInterest()
                .setGuid(PointOfInterestTable.getGUID(cursor))
                .setTitle(PointOfInterestTable.getTitle(cursor))
                .setLatitude(PointOfInterestTable.getLatitude(cursor))
                .setLongitude(PointOfInterestTable.getLongitude(cursor));
    }

    public void createTestData(Context context) {
        context.deleteDatabase("blocspot_db");

//        for (int i = 0; i < 10; i++) {
//            mPointsOfInterest.add(new PointOfInterest(String.valueOf(i),
//                    "a Point of Interest #" + i));
//        }
        mPointsOfInterest.add(new PointOfInterest());
        mPointsOfInterest.add(new PointOfInterest());
        mPointsOfInterest.add(new PointOfInterest()
                .setGuid("guid1")
                .setTitle("title 1")
                .setLatitude(38f)
                .setLongitude(-122f));
        mPointsOfInterest.add(new PointOfInterest()
                .setGuid("guid2")
                .setTitle("title 2")
                .setLatitude(38f)
                .setLongitude(-121f));
        mPointsOfInterest.add(new PointOfInterest()
                .setGuid("guid3")
                .setTitle("title 3")
                .setLatitude(38f)
                .setLongitude(-120f));
//        mPointsOfInterest.add(new PointOfInterest("guid1", "title1", 38f, -122f));
//        mPointsOfInterest.add(new PointOfInterest("guid2", "title2", 38f, -121f));
//        mPointsOfInterest.add(new PointOfInterest("guid3", "title3", 38f, -120f));

        Log.d(TAG, "createTestData end");
    }

    public void testDatabase() {
        Log.d(TAG, "testDatabase");

        Cursor itemCursor = mPointOfInterestTable.fetchAll(mDatabaseOpenHelper.getReadableDatabase());

        itemCursor.moveToFirst();
        do {
            PointOfInterest newPOI = pointOfInterestFromCursor(itemCursor);
            Log.d(TAG, "testDatabase " + newPOI.getTitle());
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
