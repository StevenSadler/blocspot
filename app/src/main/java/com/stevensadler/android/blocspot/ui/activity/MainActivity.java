package com.stevensadler.android.blocspot.ui.activity;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.google.android.gms.location.Geofence;
import com.stevensadler.android.blocspot.R;
import com.stevensadler.android.blocspot.api.model.PointOfInterest;
import com.stevensadler.android.blocspot.geofence.GeofenceStore;
import com.stevensadler.android.blocspot.ui.BlocspotApplication;
import com.stevensadler.android.blocspot.ui.adapter.ViewPagerAdapter;
import com.stevensadler.android.blocspot.ui.fragment.BlocspotMapFragment;
import com.stevensadler.android.blocspot.ui.fragment.ItemListFragment;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private Toolbar mToolbar;
    private TabLayout mTabLayout;
    private ViewPager mViewPager;

    private ArrayList<String> mStaleGeofenceIds;
    private ArrayList<Geofence> mGeofences;
    private GeofenceStore mGeofenceStore;
    private List<PointOfInterest> mPointsOfInterest;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mViewPager = (ViewPager) findViewById(R.id.viewpager);
        setupViewPager(mViewPager);

        mTabLayout = (TabLayout) findViewById(R.id.tabs);
        mTabLayout.setupWithViewPager(mViewPager);

//       FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//
//                BlocspotApplication.getSharedDataSource().testDatabase();
//            }
//        });

        mPointsOfInterest = BlocspotApplication.getSharedDataSource().getPointsOfInterest();
        mGeofences = new ArrayList<Geofence>();
        mStaleGeofenceIds = new ArrayList<String>();
        for (int i = 0; i < mPointsOfInterest.size(); i++) {
            PointOfInterest poi = mPointsOfInterest.get(i);
            mStaleGeofenceIds.add(""+poi.getRowId());
            mGeofences.add(new Geofence.Builder()
                            .setRequestId(""+poi.getRowId())
                            .setCircularRegion(poi.getLatitude(), poi.getLongitude(), poi.getRadius())
                            .setExpirationDuration(Geofence.NEVER_EXPIRE)
                            .setTransitionTypes(Geofence.GEOFENCE_TRANSITION_ENTER |
                                    Geofence.GEOFENCE_TRANSITION_EXIT)
                            .build());
        }
        mStaleGeofenceIds.add("default_guid");
        mGeofenceStore = new GeofenceStore(this, mGeofences, mStaleGeofenceIds);
    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new ItemListFragment(), "List");
        adapter.addFragment(new BlocspotMapFragment(), "Map");
        viewPager.setAdapter(adapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
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
}
