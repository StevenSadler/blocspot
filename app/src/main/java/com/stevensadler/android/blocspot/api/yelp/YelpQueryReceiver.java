package com.stevensadler.android.blocspot.api.yelp;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

/**
 * Created by Steven on 3/19/2016.
 */
public class YelpQueryReceiver extends BroadcastReceiver {

    private static String TAG = YelpQueryReceiver.class.getSimpleName();

    @Override
    public void onReceive(Context context, Intent intent) {
        Toast.makeText(context, "Broadcast intent detected" + intent.getAction(),
                Toast.LENGTH_LONG).show();
    }
}
