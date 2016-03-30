package com.stevensadler.android.blocspot.ui;

import android.app.Application;
import android.util.Log;

import com.stevensadler.android.blocspot.R;
import com.stevensadler.android.blocspot.api.DataSource;
import com.stevensadler.android.blocspot.api.yelp.YelpAPI;

/**
 * Created by Steven on 2/17/2016.
 */
public class BlocspotApplication extends Application {

    public static String TAG = BlocspotApplication.class.getSimpleName();

    public static BlocspotApplication getSharedInstance() {
        return sharedInstance;
    }

    public static DataSource getSharedDataSource() {
        return BlocspotApplication.getSharedInstance().getDataSource();
    }
    public static YelpAPI getSharedYelpAPI() {
        return BlocspotApplication.getSharedInstance().getYelpAPI();
    }

    private static BlocspotApplication sharedInstance;
    private DataSource mDataSource;
    private YelpAPI mYelpAPI;

    @Override
    public void onCreate() {
        super.onCreate();
        sharedInstance = this;
        mDataSource = new DataSource(this);
        mYelpAPI = new YelpAPI(
                getApplicationContext().getString(R.string.consumer_key),
                getApplicationContext().getString(R.string.consumer_secret),
                getApplicationContext().getString(R.string.token),
                getApplicationContext().getString(R.string.token_secret)
        );

        Log.d(TAG, "onCreate end");
    }

    public DataSource getDataSource() {
        return mDataSource;
    }
    public YelpAPI getYelpAPI() {
        return mYelpAPI;
    }

}
