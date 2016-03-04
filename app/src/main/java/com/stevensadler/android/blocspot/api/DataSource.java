package com.stevensadler.android.blocspot.api;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.util.Log;

import com.stevensadler.android.blocspot.api.model.Category;
import com.stevensadler.android.blocspot.api.model.PointOfInterest;
import com.stevensadler.android.blocspot.api.model.database.DatabaseOpenHelper;
import com.stevensadler.android.blocspot.api.model.database.table.CategoryTable;
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
    private CategoryTable mCategoryTable;

    private List<PointOfInterest> mPointsOfInterest;
    private List<Category> mCategories;

    public DataSource(Context context) {
        mPointOfInterestTable = new PointOfInterestTable();
        mCategoryTable = new CategoryTable();
        mDatabaseOpenHelper = new DatabaseOpenHelper(context,
                mPointOfInterestTable, mCategoryTable);

        //context.deleteDatabase("blocspot_db");
        mPointsOfInterest = readPointOfInterestTableToModel();
        mCategories = readCategoryTableToModel();

        /*
         * This section should be uncommented and re-run after an app uninstall
         * TODO get this working the right way
         */

//        createTestData(context);
//
//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                SQLiteDatabase writableDatabase = mDatabaseOpenHelper.getWritableDatabase();
//
//                for (PointOfInterest pointOfInterest : mPointsOfInterest) {
//                    long poiRowId = insertPointOfInterest(pointOfInterest, writableDatabase);
//                    pointOfInterest.setRowId(poiRowId);
//                }
//                for (Category category : mCategories) {
//                    insertCategory(category, writableDatabase);
//                }
//                writableDatabase.close();
//                Log.d(TAG, "constructor thread inner run complete");
//            }
//        }).start();
    }

    public List<PointOfInterest> getPointsOfInterest() {
        return mPointsOfInterest;
    }
    public List<Category> getCategories() {
        return mCategories;
    }

    /*
     * Point of Interest functions
     */

    public long insertPointOfInterest(PointOfInterest pointOfInterest, SQLiteDatabase writableDatabase) {
        PointOfInterestTable.Builder builder = new PointOfInterestTable.Builder()
                .setTitle(pointOfInterest.getTitle())
                .setGUID(pointOfInterest.getGuid())
                .setLatitude(pointOfInterest.getLatitude())
                .setLongitude(pointOfInterest.getLongitude());

        long rowId = builder.insert(writableDatabase);
        return rowId;
    }

    public static PointOfInterest pointOfInterestFromCursor(Cursor cursor) {
        return new PointOfInterest()
                .setRowId(PointOfInterestTable.getRowId(cursor))
                .setGuid(PointOfInterestTable.getGUID(cursor))
                .setTitle(PointOfInterestTable.getTitle(cursor))
                .setLatitude(PointOfInterestTable.getLatitude(cursor))
                .setLongitude(PointOfInterestTable.getLongitude(cursor));
    }

    private List<PointOfInterest> readPointOfInterestTableToModel() {
        Log.v(TAG, "readPointOfInterestTableToModel");

        List<PointOfInterest> pointsOfInterest = new ArrayList<PointOfInterest>();
        SQLiteDatabase readableDatabase = mDatabaseOpenHelper.getReadableDatabase();
        Cursor itemCursor = mPointOfInterestTable.fetchAll(readableDatabase);

        if (itemCursor != null && itemCursor.moveToFirst()) {
            do {
                PointOfInterest newPOI = pointOfInterestFromCursor(itemCursor);
                pointsOfInterest.add(newPOI);
                Log.v(TAG, newPOI.getTitle());
            } while (itemCursor.moveToNext());
        }
        readableDatabase.close();
        return pointsOfInterest;
    }

    /*
     * Category functions
     */
    public void createAndInsertCategory(Category category) {
        SQLiteDatabase writableDatabase = mDatabaseOpenHelper.getWritableDatabase();
        insertCategory(category, writableDatabase);
        mCategories.add(category);
    }

    public long insertCategory(Category category, SQLiteDatabase writableDatabase) {
        CategoryTable.Builder builder = new CategoryTable.Builder()
                .setTitle(category.getTitle())
                .setColor(category.getColor());

        long rowId;
        if (writableDatabase == null) {
            SQLiteDatabase wdb = mDatabaseOpenHelper.getWritableDatabase();
            rowId = builder.insert(wdb);
            wdb.close();

        } else {
            rowId = builder.insert(writableDatabase);
        }
        return rowId;
    }

    public static Category categoryFromCursor(Cursor cursor) {
        return new Category()
                .setRowId(CategoryTable.getRowId(cursor))
                .setTitle(CategoryTable.getTitle(cursor))
                .setColor(CategoryTable.getColor(cursor));
    }

    private List<Category> readCategoryTableToModel() {
        Log.v(TAG, "readCategoryTableToModel");

        List<Category> categories = new ArrayList<Category>();
        SQLiteDatabase readableDatabase = mDatabaseOpenHelper.getReadableDatabase();
        Cursor itemCursor = mCategoryTable.fetchAll(readableDatabase);

        if (itemCursor != null && itemCursor.moveToFirst()) {
            do {
                Category newCategory = categoryFromCursor(itemCursor);
                categories.add(newCategory);
                Log.v(TAG, newCategory.getTitle());
            } while (itemCursor.moveToNext());
        }
        readableDatabase.close();
        return categories;
    }

    /*
     *
     */

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

        mCategories.add(new Category()
                .setTitle("Restaurant")
                .setColor(Color.YELLOW));


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
