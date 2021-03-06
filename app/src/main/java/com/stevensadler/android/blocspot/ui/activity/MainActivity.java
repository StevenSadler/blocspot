package com.stevensadler.android.blocspot.ui.activity;

import android.app.FragmentManager;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.location.Location;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.google.android.gms.location.Geofence;
import com.stevensadler.android.blocspot.R;
import com.stevensadler.android.blocspot.api.DataSource;
import com.stevensadler.android.blocspot.api.model.Category;
import com.stevensadler.android.blocspot.api.model.PointOfInterest;
import com.stevensadler.android.blocspot.api.yelp.YelpQueryIntentService;
import com.stevensadler.android.blocspot.api.yelp.YelpQueryReceiver;
import com.stevensadler.android.blocspot.geofence.GeofenceStore;
import com.stevensadler.android.blocspot.ui.BlocspotApplication;
import com.stevensadler.android.blocspot.ui.adapter.ViewPagerAdapter;
import com.stevensadler.android.blocspot.ui.fragment.BlocspotMapFragment;
import com.stevensadler.android.blocspot.ui.fragment.ChooseCategoryDialogFragment;
import com.stevensadler.android.blocspot.ui.fragment.IPointOfInterestInput;
import com.stevensadler.android.blocspot.ui.fragment.IYelpPointOfInterestInput;
import com.stevensadler.android.blocspot.ui.fragment.SaveCategoryDialogFragment;
import com.stevensadler.android.blocspot.ui.fragment.YelpDetailDialogFragment;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements
        ChooseCategoryDialogFragment.Listener,
        YelpDetailDialogFragment.Listener,
        IPointOfInterestInput,
        IYelpPointOfInterestInput,
        SaveCategoryDialogFragment.SaveCategoryDialogListener {

    private static String TAG = MainActivity.class.getSimpleName();

    private Toolbar mToolbar;
    private TabLayout mTabLayout;
    private ViewPager mViewPager;
    private ViewPagerAdapter mViewPagerAdapter;

    private ArrayList<Geofence> mGeofences;
    private GeofenceStore mGeofenceStore;
    private List<PointOfInterest> mPointsOfInterest;

    private Intent mServiceIntent;
    private YelpQueryReceiver mYelpQueryReceiver;


    @Override
    protected void onResume() {
        super.onResume();
        Log.v(TAG, "onResume");
        LocalBroadcastManager.getInstance(this).registerReceiver(mYelpQueryReceiver,
                new IntentFilter(YelpQueryIntentService.BROADCAST_FILTER));
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.v(TAG, "onPause");
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mYelpQueryReceiver);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.v(TAG, "onCreate");
        setContentView(R.layout.activity_main);

        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mViewPager = (ViewPager) findViewById(R.id.viewpager);
        mViewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());
        mViewPager.setAdapter(mViewPagerAdapter);

        mTabLayout = (TabLayout) findViewById(R.id.tabs);
        mTabLayout.setupWithViewPager(mViewPager);

        mPointsOfInterest = BlocspotApplication.getSharedDataSource().getPointsOfInterest();
        mGeofences = new ArrayList<Geofence>();
        for (int i = 0; i < mPointsOfInterest.size(); i++) {
            PointOfInterest poi = mPointsOfInterest.get(i);
            if (!poi.getVisited()) {
                mGeofences.add(new Geofence.Builder()
                        .setRequestId("" + poi.getRowId())
                        .setCircularRegion(poi.getLatitude(), poi.getLongitude(), poi.getRadius())
                        .setExpirationDuration(Geofence.NEVER_EXPIRE)
                        .setTransitionTypes(Geofence.GEOFENCE_TRANSITION_ENTER |
                                Geofence.GEOFENCE_TRANSITION_EXIT)
                        .build());
            }

        }
        mGeofenceStore = new GeofenceStore(this, mGeofences);

        mYelpQueryReceiver = new YelpQueryReceiver();
        mServiceIntent = new Intent(this, YelpQueryIntentService.class);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        Log.v(TAG, "onCreateOptionsMenu");
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);

        final MenuItem searchMenu = menu.findItem(R.id.action_search);

        // Get the SearchView and set the searchable configuration
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        final SearchView searchView = (SearchView) searchMenu.getActionView();

        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        searchView.setIconifiedByDefault(true);

        return super.onCreateOptionsMenu(menu);
    }

    public void onNewIntent(Intent intent) {
        Log.v(TAG, "onNewIntent");
        setIntent(intent);
        handleIntent(intent);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void handleIntent(Intent intent) {

        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            Log.v(TAG, "handleIntent true block");
            String query = intent.getStringExtra(SearchManager.QUERY);
            Location location = BlocspotApplication.getSharedDataSource().getLastLocation();
            doSearch(query, location.getLatitude(), location.getLongitude());

        } else {
            Log.v(TAG, "handleIntent false block");
        }
    }

    private void doSearch(String termString, double latitude, double longitude) {
        // get a Cursor, prepare the ListAdapter
        // and set it
        Log.v(TAG, "doSearch " + termString + " " + latitude + "," + longitude);

        mServiceIntent.putExtra("term", termString);
        mServiceIntent.putExtra("latitude", latitude);
        mServiceIntent.putExtra("longitude", longitude);
        startService(mServiceIntent);
    }

    public void onMenuSearchClick(MenuItem menuItem) {
        Log.d(TAG, "onMenuSearchClick");
        //onSearchRequested();
    }

    public void onMenuAddCategoryClick(MenuItem menuItem) {
        Log.d(TAG, "onMenuAddCategoryClick");

        SaveCategoryDialogFragment dialogFragment = new SaveCategoryDialogFragment();
        dialogFragment.show(getSupportFragmentManager(), "Save Category Dialog Fragment");
    }

    public void onMenuFilterCategoryClick(MenuItem menuItem) {
        Log.d(TAG, "onMenuFilterCategoryClick");
        int mode = BlocspotApplication.getSharedDataSource().FILTER_BY_CATEGORY;
        BlocspotApplication.getSharedDataSource().setChooseCategoryMode(mode);

        ChooseCategoryDialogFragment dialogFragment = new ChooseCategoryDialogFragment();
        dialogFragment.show(getSupportFragmentManager(), "filter category");
    }

    public void onMenuRemoveFilterCategoryClick(MenuItem menuItem) {
        Log.d(TAG, "onMenuRemoveFilterCategoryClick");
        BlocspotApplication.getSharedDataSource().setCategoryFilter(null);
    }


    /*
     * IPointOfInterestInput
     */
    @Override
    public void onSelectPointOfInterest(PointOfInterest pointOfInterest) {
        int mode = BlocspotApplication.getSharedDataSource().ASSIGN_CATEGORY;
        BlocspotApplication.getSharedDataSource().setSelectedPOI(pointOfInterest);
        BlocspotApplication.getSharedDataSource().setChooseCategoryMode(mode);

        ChooseCategoryDialogFragment dialogFragment = new ChooseCategoryDialogFragment();
        dialogFragment.show(getSupportFragmentManager(), "choose category");
    }
    @Override
    public void onVisitedPointOfInterest(PointOfInterest poi) {
        BlocspotApplication.getSharedDataSource().toggleVisited(poi);

        // this section needs lots of testing
        if (poi.getVisited()) {
            // remove the Geofence
            mGeofenceStore.removeSingleGeofence(""+poi.getRowId());
        } else {
            // add a Geofence
            Geofence geofence = new Geofence.Builder()
                    .setRequestId("" + poi.getRowId())
                    .setCircularRegion(poi.getLatitude(), poi.getLongitude(), poi.getRadius())
                    .setExpirationDuration(Geofence.NEVER_EXPIRE)
                    .setTransitionTypes(Geofence.GEOFENCE_TRANSITION_ENTER |
                            Geofence.GEOFENCE_TRANSITION_EXIT)
                    .build();

            mGeofenceStore.addSingleGeofence(geofence, ""+poi.getRowId());
        }
    }
    @Override
    public void onDeletePointOfInterest(PointOfInterest pointOfInterest) {
        BlocspotApplication.getSharedDataSource().deletePointOfInterest(pointOfInterest);
    }
    @Override
    public void onMapFindPointOfInterest(PointOfInterest pointOfInterest) {
        mViewPager.setCurrentItem(1, true);
        ViewPagerAdapter adapter = (ViewPagerAdapter) mViewPager.getAdapter();
        BlocspotMapFragment mapFragment = (BlocspotMapFragment) adapter.getItem(1);
        mapFragment.moveCameraToPointOfInterest(pointOfInterest);
    }

    /*
     * IYelpPointOfInterestInput
     */
    @Override
    public void onSelectYelpPointOfInterest(PointOfInterest pointOfInterest) {
        // pop a dialog fragment to show the yelp POI detail view
        // it needs a save button and dismiss button?
        // it needs a layout xml
        int mode = BlocspotApplication.getSharedDataSource().ASSIGN_YELP_CATEGORY;
        BlocspotApplication.getSharedDataSource().setSelectedPOI(pointOfInterest);
        BlocspotApplication.getSharedDataSource().setChooseCategoryMode(mode);

        YelpDetailDialogFragment dialogFragment = new YelpDetailDialogFragment();
        dialogFragment.show(getSupportFragmentManager(), "yelp detail");
    }

    /*
     * YelpDetailDialogFragment.Listener
     */
    @Override
    public void onSaveYelpDetailDialog(String note) {
        // insert the poi in the table
        // add poi to model list
        BlocspotApplication.getSharedDataSource().saveYelpPointOfInterest(note);
        BlocspotApplication.getSharedDataSource().clearYelpPointsOfInterest();
    }
    @Override
    public void onExitYelpDetailDialog() {
        // close the dialog, maybe pop backstack in dialog click listener
        FragmentManager fm = getFragmentManager();
        fm.popBackStack();

        BlocspotApplication.getSharedDataSource().clearYelpPointsOfInterest();
    }
    @Override
    public void onChooseYelpDetailDialog() {
        // close the dialog, maybe pop backstack in dialog click listener
        ChooseCategoryDialogFragment dialogFragment = new ChooseCategoryDialogFragment();
        dialogFragment.show(getSupportFragmentManager(), "choose category");
    }

    /*
     * ChooseCategoryDialogFragment.Listener
     */
    @Override
    public void onFinishChooseCategoryDialog(Category category) {
        int mode = BlocspotApplication.getSharedDataSource().getChooseCategoryMode();
        PointOfInterest pointOfInterest = BlocspotApplication.getSharedDataSource().getSelectedPOI();
        switch (mode) {
            case DataSource.ASSIGN_YELP_CATEGORY:
                Log.v(TAG, "onFinishChooseCategoryDialog    set yelp poi category");
                BlocspotApplication.getSharedDataSource().saveYelpPointOfInterestWithCategory(category);
                break;
            case DataSource.ASSIGN_CATEGORY:
                Log.v(TAG, "onFinishChooseCategoryDialog    set poi category");
                BlocspotApplication.getSharedDataSource().setPOICategory(category, pointOfInterest);
                break;
            case DataSource.FILTER_BY_CATEGORY:
                Log.v(TAG, "onFinishChooseCategoryDialog    set filter");
                BlocspotApplication.getSharedDataSource().setCategoryFilter(category);
                break;
            default:
                Log.e(TAG, "onFinishChooseCategoryDialog    mode invalid");
                break;
        }
        // clean up the mode once the dialog is finished
        // since only this listener needs it
        BlocspotApplication.getSharedDataSource().setChooseCategoryMode(DataSource.NO_MODE);
    }

    /*
     * SaveCategoryDialogFragment.SaveCategoryDialogListener
     */
    @Override
    public void onFinishSaveCategoryDialog(String inputText, int color) {
        Log.v(TAG, "onFinishSaveCategoryDialog  do something");
        // need to create a new category
        // save it to the model list
        // save it to the db table
        Category category = new Category()
                .setTitle(inputText)
                .setColor(color);

        BlocspotApplication.getSharedDataSource().createAndInsertCategory(category);
    }
}
