package com.stevensadler.android.blocspot;

import android.database.Cursor;
import android.test.ApplicationTestCase;
import android.test.RenamingDelegatingContext;
import android.util.Log;

import com.stevensadler.android.blocspot.api.DataSource;
import com.stevensadler.android.blocspot.api.model.Category;
import com.stevensadler.android.blocspot.api.model.PointOfInterest;
import com.stevensadler.android.blocspot.api.model.database.table.CategoryTable;
import com.stevensadler.android.blocspot.api.model.database.table.PointOfInterestTable;
import com.stevensadler.android.blocspot.ui.BlocspotApplication;

import java.util.List;

/**
 * <a href="http://d.android.com/tools/testing/testing_android.html">Testing Fundamentals</a>
 */
public class ApplicationTest extends ApplicationTestCase<BlocspotApplication> {

    private static String TAG = ApplicationTest.class.getSimpleName();

    private static String TEST_GUID = "test_guid";
    private static String TEST_TITLE = "test_title";
    private static float TEST_LATITUDE = 37f;
    private static float TEST_LONGITUDE = -121f;
    private static String TEST_CATEGORY_TITLE = "test_category_title";
    private static int TEST_CATEGORY_COLOR = 0xFFFFFF;

    private BlocspotApplication application;
    private DataSource dataSource;

    private PointOfInterest emptyPOI;
    private PointOfInterest testPOI;
    private PointOfInterest emptyPOIPulled;
    private PointOfInterest testPOIPulled;
    private long emptyPOIRowId;
    private long testPOIRowId;
    private List<PointOfInterest> poiList;
    private Cursor emptyPOICursor;
    private Cursor testPOICursor;

    private Category emptyCategory;
    private Category testCategory;
    private Category emptyCategoryPulled;
    private Category testCategoryPulled;
    private long emptyCategoryRowId;
    private long testCategoryRowId;
    private List<Category> categoryList;
    private Cursor emptyCategoryCursor;
    private Cursor testCategoryCursor;

    public ApplicationTest() {
        super(BlocspotApplication.class);
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        setContext(new RenamingDelegatingContext(getContext(), "test_"));
        createApplication();

        application = getApplication();
        application.onCreate();
        dataSource = application.getDataSource();

        /*
         * Point of Interest model and table
         */
        emptyPOI = new PointOfInterest();
        testPOI = new PointOfInterest()
                .setGuid(TEST_GUID)
                .setTitle(TEST_TITLE)
                .setLatitude(TEST_LATITUDE)
                .setLongitude(TEST_LONGITUDE);

        emptyPOIRowId = dataSource.insertPointOfInterest(emptyPOI, null);
        testPOIRowId = dataSource.insertPointOfInterest(testPOI, null);

        emptyPOICursor = dataSource.getCursorOfInsertedPOIWithRowId(emptyPOIRowId);
        testPOICursor = dataSource.getCursorOfInsertedPOIWithRowId(testPOIRowId);
        emptyPOICursor.moveToFirst();
        testPOICursor.moveToFirst();

        poiList = dataSource.readPointOfInterestTableToModel();
        emptyPOIPulled = poiList.get(0);
        testPOIPulled = poiList.get(1);

        /*
         * Category model and table
         */
        emptyCategory = new Category();
        testCategory = new Category()
                .setTitle(TEST_CATEGORY_TITLE)
                .setColor(TEST_CATEGORY_COLOR);

        emptyCategoryRowId = dataSource.insertCategory(emptyCategory, null);
        testCategoryRowId = dataSource.insertCategory(testCategory, null);

        emptyCategoryCursor = dataSource.getCursorOfInsertedCategoryWithRowId(emptyCategoryRowId);
        testCategoryCursor = dataSource.getCursorOfInsertedCategoryWithRowId(testCategoryRowId);
        emptyCategoryCursor.moveToFirst();
        testCategoryCursor.moveToFirst();

        categoryList = dataSource.readCategoryTableToModel();
        emptyCategoryPulled = categoryList.get(0);
        testCategoryPulled = categoryList.get(1);

        Log.v(TAG, "categoryList.size() = " + categoryList.size() + "    emptyCategoryRowId = " + emptyCategoryRowId + "    testCategoryRowId = " + testCategoryRowId);
    }

    /*
     * BlocspotApplication
     */
    public void testApplicationHasDataSource() {
        assertNotNull(dataSource);
    }

    /*
     * PointOfInterest model
     */
    public void testPointOfInterestHasDefaultGuid() {
        assertNotNull(PointOfInterest.DEFAULT_GUID);
    }
    public void testPointOfInterestHasDefaultTitle() {
        assertNotNull(PointOfInterest.DEFAULT_TITLE);
    }

    public void testCreateEmptyPOI() {
        assertNotNull(emptyPOI);
    }
    public void testCreateEmptyPOIHasDefaultGuid() {
        assertEquals(PointOfInterest.DEFAULT_GUID, emptyPOI.getGuid());
    }
    public void testCreateEmptyPOIHasDefaultTitle() {
        assertEquals(PointOfInterest.DEFAULT_TITLE, emptyPOI.getTitle());
    }
    public void testCreateEmptyPOIHasDefaultLatitude() {
        assertEquals(PointOfInterest.DEFAULT_LATITUDE, emptyPOI.getLatitude());
    }
    public void testCreateEmptyPOIHasDefaultLongitude() {
        assertEquals(PointOfInterest.DEFAULT_LONGITUDE, emptyPOI.getLongitude());
    }

    public void testCreatePOI() {
        assertNotNull(testPOI);
    }
    public void testCreatePOIHasTestGuid() {
        assertEquals(TEST_GUID, testPOI.getGuid());
    }
    public void testCreatePOIHasTestTitle() {
        assertEquals(TEST_TITLE, testPOI.getTitle());
    }
    public void testCreatePOIHasTestLatitude() {
        assertEquals(TEST_LATITUDE, testPOI.getLatitude());
    }
    public void testCreatePOIHasTestLongitude() {
        assertEquals(TEST_LONGITUDE, testPOI.getLongitude());
    }

    /*
     * PointOfInterestTable
     * test a round trip insert poi to table
     * pull poi from table
     * verify that the original poi values and pulled poi values are the same
     */
    public void testInsertEmptyPOI() {
        assertFalse(emptyPOIRowId == -1);
    }
    public void testInsertEmptyPOIRowId() {
        assertEquals(emptyPOIRowId, emptyPOI.getRowId());
    }
    public void testInsertEmptyPOICursorMatches() {
        assertEquals(emptyPOIRowId, PointOfInterestTable.getRowId(emptyPOICursor));
        assertEquals(PointOfInterest.DEFAULT_TITLE, PointOfInterestTable.getTitle(emptyPOICursor));
        assertEquals(PointOfInterest.DEFAULT_LATITUDE, PointOfInterestTable.getLatitude(emptyPOICursor));
        assertEquals(PointOfInterest.DEFAULT_LONGITUDE, PointOfInterestTable.getLongitude(emptyPOICursor));
    }
    public void testInsertEmptyPOIPulledMatches() {
        assertEquals(emptyPOIRowId, emptyPOIPulled.getRowId());
        assertEquals(PointOfInterest.DEFAULT_TITLE, emptyPOIPulled.getTitle());
        assertEquals(PointOfInterest.DEFAULT_LATITUDE, emptyPOIPulled.getLatitude());
        assertEquals(PointOfInterest.DEFAULT_LONGITUDE, emptyPOIPulled.getLongitude());
    }

    public void testInsertPOI() {
        assertFalse(testPOIRowId == -1);
    }
    public void testInsertPOIRowId() {
        assertEquals(testPOIRowId, testPOI.getRowId());
    }
    public void testInsertPOICursorMatches() {
        assertEquals(testPOIRowId, PointOfInterestTable.getRowId(testPOICursor));
        assertEquals(TEST_TITLE, PointOfInterestTable.getTitle(testPOICursor));
        assertEquals(TEST_LATITUDE, PointOfInterestTable.getLatitude(testPOICursor));
        assertEquals(TEST_LONGITUDE, PointOfInterestTable.getLongitude(testPOICursor));
    }
    public void testInsertPOIPulledMatches() {
        assertEquals(testPOIRowId, testPOIPulled.getRowId());
        assertEquals(TEST_TITLE, testPOIPulled.getTitle());
        assertEquals(TEST_LATITUDE, testPOIPulled.getLatitude());
        assertEquals(TEST_LONGITUDE, testPOIPulled.getLongitude());
    }

    /*
     * change category in PointOfInterest
     */
//    public void testChangePOICategoryId() {
//
//    }

    /*
     * Category model
     */
    public void testCategoryHasDefaultColor() {
        assertNotNull(Category.DEFAULT_COLOR);
    }
    public void testCategorytHasDefaultTitle() {
        assertNotNull(Category.DEFAULT_TITLE);
    }

    public void testCreateEmptyCategory() {
        assertNotNull(emptyCategory);
    }
    public void testCreateEmptyCategoryHasDefaultColor() {
        assertEquals(Category.DEFAULT_COLOR, emptyCategory.getColor());
    }
    public void testCreateEmptyCategoryHasDefaultTitle() {
        assertEquals(Category.DEFAULT_TITLE, emptyCategory.getTitle());
    }
    public void testCreateCategory() {
        assertNotNull(testCategory);
    }
    public void testCreateCategoryHasTestColor() {
        assertEquals(TEST_CATEGORY_COLOR, testCategory.getColor());
    }
    public void testCreateCategoryHasTestTitle() {
        assertEquals(TEST_CATEGORY_TITLE, testCategory.getTitle());
    }

    /*
     * CategoryTable
     * test a round trip insert category to table
     * pull category from table
     * verify that the original category values and pulled category values are the same
     */
    public void testInsertEmptyCategory() {
        assertFalse(emptyCategoryRowId == -1);
    }
    public void testInsertEmptyCategoryRowId() {
        assertEquals(emptyCategoryRowId, emptyCategory.getRowId());
    }
    public void testInsertEmptyCategoryCursorMatches() {
        assertEquals(emptyCategoryRowId, CategoryTable.getRowId(emptyCategoryCursor));
        assertEquals(Category.DEFAULT_COLOR, CategoryTable.getColor(emptyCategoryCursor));
        assertEquals(Category.DEFAULT_TITLE, CategoryTable.getTitle(emptyCategoryCursor));
    }
    public void testInsertEmptyCategoryPulledMatches() {
        assertEquals(emptyCategoryRowId, emptyCategoryPulled.getRowId());
        assertEquals(Category.DEFAULT_COLOR, CategoryTable.getColor(emptyCategoryCursor));
        assertEquals(Category.DEFAULT_TITLE, CategoryTable.getTitle(emptyCategoryCursor));
    }

    public void testInsertCategory() {
        assertFalse(testCategoryRowId == -1);
    }
    public void testInsertCategoryRowId() {
        assertEquals(testCategoryRowId, testCategory.getRowId());
    }
    public void testInsertCategoryCursorMatches() {
        assertEquals(testCategoryRowId, CategoryTable.getRowId(testCategoryCursor));
        assertEquals(TEST_CATEGORY_COLOR, CategoryTable.getColor(testCategoryCursor));
        assertEquals(TEST_CATEGORY_TITLE, CategoryTable.getTitle(testCategoryCursor));
    }
    public void testInsertCategoryPulledMatches() {
        assertEquals(testCategoryRowId, testCategoryPulled.getRowId());
        assertEquals(TEST_CATEGORY_COLOR, testCategoryPulled.getColor());
        assertEquals(TEST_CATEGORY_TITLE, testCategoryPulled.getTitle());
    }
}