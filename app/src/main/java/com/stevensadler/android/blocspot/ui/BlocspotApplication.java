package com.stevensadler.android.blocspot.ui;

import android.app.Application;
import android.util.Log;

import com.stevensadler.android.blocspot.api.DataSource;

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

    private static BlocspotApplication sharedInstance;
    private DataSource dataSource;

    @Override
    public void onCreate() {
        super.onCreate();
        sharedInstance = this;
        dataSource = new DataSource(this);

        Log.d(TAG, "onCreate end");
    }

    public DataSource getDataSource() {
        return dataSource;
    }

}
