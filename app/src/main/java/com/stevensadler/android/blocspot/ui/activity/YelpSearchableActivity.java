package com.stevensadler.android.blocspot.ui.activity;

import android.app.ListActivity;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.ListView;

import com.stevensadler.android.blocspot.R;
import com.stevensadler.android.blocspot.api.yelp.YelpQueryIntentService;
import com.stevensadler.android.blocspot.ui.BlocspotApplication;

/**
 * Created by Steven on 3/13/2016.
 */
public class YelpSearchableActivity extends ListActivity {

    private static String TAG = YelpSearchableActivity.class.getSimpleName();

    private Intent mServiceIntent;
//    private YelpQueryReceiver mYelpQueryReceiver;

//    @Override
//    protected void onResume() {
//        super.onResume();
//        Log.v(TAG, "onResume");
//        registerReceiver(mYelpQueryReceiver, new IntentFilter(YelpQueryIntentService.BROADCAST_FILTER));
//    }
//
//    @Override
//    protected void onPause() {
//        super.onPause();
//        unregisterReceiver(mYelpQueryReceiver);
//    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.v(TAG, "onCreate");

//        mYelpQueryReceiver = new YelpQueryReceiver();
        mServiceIntent = new Intent(this, YelpQueryIntentService.class);

        //mServiceIntent = new Intent(this,)

        handleIntent(getIntent());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);

        // Get the SearchView and set the searchable configuration
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView = (SearchView) menu.findItem(R.id.action_search).getActionView();

        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        searchView.setIconifiedByDefault(false);

        return true;
    }

    public void onNewIntent(Intent intent) {
        setIntent(intent);
        handleIntent(intent);
    }

    public void onListItemClick(ListView listView, View view, int position, long id) {
        // call detail activity for clicked entry
    }

    private void handleIntent(Intent intent) {
        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            String query = intent.getStringExtra(SearchManager.QUERY);
            Location location = BlocspotApplication.getSharedDataSource().getLastLocation();
            doSearch(query, location.getLatitude(), location.getLongitude());
            //doSearch(query, "San Francisco, CA");
            //doSearch("dinner", "San Francisco, CA");

            //Log.v(TAG, "handleIntent should add the query parameters to something here " + intent.getDataString());
            //mServiceIntent.setData(Uri.parse(intent.getDataString()));
            //startService(mServiceIntent);

        }
    }

    private void doSearch(String termString, double latitude, double longitude) {
        // get a Cursor, prepare the ListAdapter
        // and set it
        Log.v(TAG, "doSearch " + termString + " " + latitude + "," + longitude);
        //startService
        //startService(mServiceIntent);

        mServiceIntent.putExtra("term", termString);
        mServiceIntent.putExtra("latitude", latitude);
        mServiceIntent.putExtra("longitude", longitude);
        startService(mServiceIntent);

        //String resultString = mYelpAPI.searchForBusinessesByLocation(queryString, locationString);

        Intent intent = new Intent(this, MainActivity.class);
        intent.setFlags(android.content.Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }
}
