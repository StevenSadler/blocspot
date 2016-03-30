package com.stevensadler.android.blocspot.api.yelp;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.stevensadler.android.blocspot.ui.BlocspotApplication;

/**
 * Created by Steven on 3/19/2016.
 */
public class YelpQueryReceiver extends BroadcastReceiver {

    private static String TAG = YelpQueryReceiver.class.getSimpleName();

    @Override
    public void onReceive(Context context, Intent intent) {
        String queryResult = intent.getStringExtra("queryResult");
        Log.v(TAG, "onReceive " + queryResult);
        BlocspotApplication.getSharedDataSource().setYelpPointsOfInterest(queryResult);
    }
}
