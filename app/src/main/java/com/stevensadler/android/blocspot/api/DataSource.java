package com.stevensadler.android.blocspot.api;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.location.Location;
import android.util.Log;

import com.stevensadler.android.blocspot.api.model.Category;
import com.stevensadler.android.blocspot.api.model.PointOfInterest;
import com.stevensadler.android.blocspot.api.model.database.DatabaseOpenHelper;
import com.stevensadler.android.blocspot.api.model.database.table.CategoryTable;
import com.stevensadler.android.blocspot.api.model.database.table.PointOfInterestTable;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.util.ArrayList;
import java.util.List;
import java.util.Observable;

/**
 * Created by Steven on 2/17/2016.
 */
public class DataSource extends Observable {

    private static String TAG = DataSource.class.getSimpleName();

    // ChooseCategory modes
    final public static int NO_MODE = 1;
    final public static int ASSIGN_CATEGORY = 2;
    final public static int FILTER_BY_CATEGORY = 3;

    private DatabaseOpenHelper mDatabaseOpenHelper;
    private PointOfInterestTable mPointOfInterestTable;
    private CategoryTable mCategoryTable;

    private List<PointOfInterest> mYelpPointsOfInterest;
    private List<PointOfInterest> mPointsOfInterest;
    private List<Category> mCategories;

    private PointOfInterest mSelectedPOI;
    private int mChooseCategoryMode = NO_MODE;
    private Category mCategoryFilter = null;
    private Location mLastLocation = null;

    public DataSource(Context context) {
        mYelpPointsOfInterest = new ArrayList<>();
        mPointOfInterestTable = new PointOfInterestTable();
        mCategoryTable = new CategoryTable();
        mDatabaseOpenHelper = new DatabaseOpenHelper(context,
                mPointOfInterestTable, mCategoryTable);

        //context.deleteDatabase("blocspot_db");
        mPointsOfInterest = readPointOfInterestTableToModel();
        mCategories = readCategoryTableToModel();

        // only use this while in dev
//        devChangePOICategoryId();
//        mPointsOfInterest = readPointOfInterestTableToModel();
//        Log.v(TAG, "constructor end after devChange");


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
        if (mCategoryFilter == null) {
            return mPointsOfInterest;
        } else {
            List<PointOfInterest> filteredPOIList = new ArrayList<>();
            long categoryId = mCategoryFilter.getRowId();
            for (PointOfInterest poi : mPointsOfInterest) {
                if (poi.getCategoryId() == categoryId) {
                    filteredPOIList.add(poi);
                }
            }
            return filteredPOIList;
        }
    }
    public List<Category> getCategories() {
        return mCategories;
    }

    /*
     * convert the jsonString yelp query result
     * to a poi list
     */
    public void setYelpPointsOfInterest(String jsonString) {
        List<PointOfInterest> tempList = new ArrayList<>();
        JSONParser parser = new JSONParser();
        try {
            Object obj = parser.parse(jsonString);
            JSONObject jsonObject = (JSONObject) obj;
            JSONArray businesses = (JSONArray) jsonObject.get("businesses");

            for (Object object : businesses) {
                JSONObject business = (JSONObject) object;
                String name = (String) business.get("name");
                JSONObject location = (JSONObject) business.get("location");
                JSONObject coordinate = (JSONObject) location.get("coordinate");
                double dLatitude = (double) coordinate.get("latitude");
                double dLongitude = (double) coordinate.get("longitude");


                float latitude = (float) dLatitude;
                float longitude = (float) dLongitude;

                Log.v(TAG, "business name : " + name + " " + latitude + " " + longitude);

                PointOfInterest poi = new PointOfInterest()
                        .setTitle(name)
                        .setLatitude(latitude)
                        .setLongitude(longitude);

                tempList.add(poi);
            }

        } catch (ParseException e) {
            e.printStackTrace();
        }
        mYelpPointsOfInterest = tempList;

        // can't notify views becuase they were created on a different thread
//        // notify observers
//        setChanged();
//        notifyObservers();
//        clearChanged();
    }
    public List<PointOfInterest> getYelpPointsOfInterest() {
        return mYelpPointsOfInterest;
    }

    public void setLastLocation(Location location) {
        mLastLocation = location;
    }
    public Location getLastLocation() {
        return mLastLocation;
    }
    public void setSelectedPOI(PointOfInterest pointOfInterest) {
        mSelectedPOI = pointOfInterest;
    }
    public PointOfInterest getSelectedPOI() {
        return mSelectedPOI;
    }
    public void setChooseCategoryMode(int mode) {
        mChooseCategoryMode = mode;
    }
    public int getChooseCategoryMode() {
        return mChooseCategoryMode;
    }
    public void setCategoryFilter(Category category) {
        mCategoryFilter = category;

        // notify observers
        setChanged();
        notifyObservers();
        clearChanged();
    }
    public Category getCategoryFilter() {
        return mCategoryFilter;
    }
    public void setPOICategory(Category category, PointOfInterest pointOfInterest) {
        // update the table
        SQLiteDatabase writableDatabase = mDatabaseOpenHelper.getWritableDatabase();
        long categoryId = category.getRowId();
        long poiRowId = pointOfInterest.getRowId();
        int updatedRowCount = mPointOfInterestTable.updateCategoryId(writableDatabase, category, poiRowId);
        Log.v(TAG, "setPOICategory   updatedRowCount = " + updatedRowCount);
        //writableDatabase.close();

        // verify the cursor has changed
        Cursor cursor = getCursorOfInsertedPOIWithRowId(poiRowId);
        cursor.moveToFirst();
        long catId = PointOfInterestTable.getCategoryId(cursor);
        Log.v(TAG, "setPOICategory   before = " + categoryId + "    after = " + catId);

        //mPointsOfInterest = readPointOfInterestTableToModel();
        //PointOfInterest newPOI = mPointsOfInterest.get

        // update the model list
        //Log.v(TAG, "setPOICategory   before = " + categoryId + "    after = " + catId);
        pointOfInterest.setCategoryId(categoryId);

        // notify observers
        setChanged();
        notifyObservers();
        clearChanged();
    }


    public void devChangePOICategoryId() {
        // change categoryId value in PointOfInterestTable cursor for a POI
        // update the POI model in the list
        //
        PointOfInterest pointOfInterest = mPointsOfInterest.get(0);
        Category category = mCategories.get(0);
        setPOICategory(category, pointOfInterest);
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

        long rowId;
        if (writableDatabase == null) {
            SQLiteDatabase wdb = mDatabaseOpenHelper.getWritableDatabase();
            rowId = builder.insert(wdb);
            wdb.close();

        } else {
            rowId = builder.insert(writableDatabase);
        }
        pointOfInterest.setRowId(rowId);
        return rowId;
    }

    public static PointOfInterest pointOfInterestFromCursor(Cursor cursor) {
        return new PointOfInterest()
                .setCategoryId(PointOfInterestTable.getCategoryId(cursor))
                .setRowId(PointOfInterestTable.getRowId(cursor))
                .setGuid(PointOfInterestTable.getGUID(cursor))
                .setTitle(PointOfInterestTable.getTitle(cursor))
                .setLatitude(PointOfInterestTable.getLatitude(cursor))
                .setLongitude(PointOfInterestTable.getLongitude(cursor));
    }

    public List<PointOfInterest> readPointOfInterestTableToModel() {
        Log.v(TAG, "readPointOfInterestTableToModel");

        List<PointOfInterest> pointsOfInterest = new ArrayList<PointOfInterest>();
        SQLiteDatabase readableDatabase = mDatabaseOpenHelper.getReadableDatabase();
        Cursor itemCursor = mPointOfInterestTable.fetchAll(readableDatabase);

        if (itemCursor != null && itemCursor.moveToFirst()) {
            do {
                PointOfInterest newPOI = pointOfInterestFromCursor(itemCursor);
                pointsOfInterest.add(newPOI);
                Log.v(TAG, "" + newPOI.getRowId() + " " + newPOI.getTitle() + " " + newPOI.getCategoryId());
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
        category.setRowId(rowId);
        return rowId;
    }

    public static Category categoryFromCursor(Cursor cursor) {
        return new Category()
                .setRowId(CategoryTable.getRowId(cursor))
                .setTitle(CategoryTable.getTitle(cursor))
                .setColor(CategoryTable.getColor(cursor));
    }

    public List<Category> readCategoryTableToModel() {
        Log.v(TAG, "readCategoryTableToModel");

        List<Category> categories = new ArrayList<Category>();
        SQLiteDatabase readableDatabase = mDatabaseOpenHelper.getReadableDatabase();
        Cursor itemCursor = mCategoryTable.fetchAll(readableDatabase);

        if (itemCursor != null && itemCursor.moveToFirst()) {
            do {
                Category newCategory = categoryFromCursor(itemCursor);
                categories.add(newCategory);
                Log.v(TAG, "" + newCategory.getRowId() + " " + newCategory.getTitle());
            } while (itemCursor.moveToNext());
        }
        readableDatabase.close();
        return categories;
    }

    public Category getCategoryWithRowId(long rowId) {
        for (Category category : mCategories) {
            if (category.getRowId() == rowId) {
                return category;
            }
        }
        return null;
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
    public Cursor getCursorOfInsertedPOIWithRowId(long rowId) {
        Cursor itemCursor = mPointOfInterestTable.fetchRow(
                mDatabaseOpenHelper.getReadableDatabase(), rowId);
        return itemCursor;
    }
    public Cursor getCursorOfInsertedCategoryWithRowId(long rowId) {
        Cursor itemCursor = mCategoryTable.fetchRow(
                mDatabaseOpenHelper.getReadableDatabase(), rowId);
        return itemCursor;
    }
}
