package com.stevensadler.android.blocspot;

import android.test.ApplicationTestCase;
import android.test.RenamingDelegatingContext;

import com.stevensadler.android.blocspot.api.DataSource;
import com.stevensadler.android.blocspot.api.model.PointOfInterest;
import com.stevensadler.android.blocspot.ui.BlocspotApplication;

/**
 * <a href="http://d.android.com/tools/testing/testing_android.html">Testing Fundamentals</a>
 */
public class ApplicationTest extends ApplicationTestCase<BlocspotApplication> {

    private static String TEST_GUID = "test_guid";
    private static String TEST_TITLE = "test_title";

    private PointOfInterest emptyPOI;
    private PointOfInterest testPOI;
    private DataSource dataSource;

    public ApplicationTest() {
        super(BlocspotApplication.class);
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        setContext(new RenamingDelegatingContext(getContext(), "test_"));
        createApplication();

        emptyPOI = new PointOfInterest();
        testPOI = new PointOfInterest(TEST_GUID, TEST_TITLE);
    }

    /*
     * BlocspotApplication
     */
    public void testApplicationHasDataSource() {
        BlocspotApplication application = getApplication();
        application.onCreate();
        dataSource = application.getDataSource();
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

    public void testCreatePOI() {
        assertNotNull(testPOI);
    }
    public void testCreatePOIHasTestGuid() {
        assertEquals(TEST_GUID, testPOI.getGuid());
    }
    public void testCreatePOIHasTestTitle() {
        assertEquals(TEST_TITLE, testPOI.getTitle());
    }

    /*
     * PointOfInterestTable
     */
//    public void testTableInsertEmptyPOI() {
//        dataSource.insertPointOfInterest(emptyPOI);
//        Cursor itemCursor = dataSource.getCursorOfInsertedPOIWithGuid(emptyPOI.getGuid());
//        PointOfInterest pulledPOI = DataSource.pointOfInterestFromCursor(itemCursor);
//        assertEquals(emptyPOI.getGuid(), pulledPOI.getGuid());
//        assertEquals(emptyPOI.getTitle(), pulledPOI.getTitle());
//    }
//    public void testTableInsertPOI() {
//        dataSource.insertPointOfInterest(testPOI);
//        Cursor itemCursor = dataSource.getCursorOfInsertedPOI(testPOI);
//        PointOfInterest pulledPOI = DataSource.pointOfInterestFromCursor(itemCursor);
//        assertEquals(testPOI.getGuid(), pulledPOI.getGuid());
//        assertEquals(testPOI.getTitle(), pulledPOI.getTitle());
//    }
}