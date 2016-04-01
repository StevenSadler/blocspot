package com.stevensadler.android.blocspot.ui.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.util.Log;

import com.stevensadler.android.blocspot.ui.fragment.BlocspotMapFragment;
import com.stevensadler.android.blocspot.ui.fragment.ItemListFragment;

import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

/**
 * Created by Steven on 2/23/2016.
 */
public class ViewPagerAdapter extends FragmentPagerAdapter implements
        Observer {

    public static interface Listener {
        public void updateListener();
    }

    private String TAG = ViewPagerAdapter.class.getSimpleName()+" sjs";

    private List<Fragment> mFragmentList = new ArrayList<>();
    private List<String> mFragmentTitleList = new ArrayList<>();
//    private Observable mObservers = new FragmentObserver();

    public ViewPagerAdapter(FragmentManager manager) {
        super(manager);
        Log.v(TAG, "constructed");
    }

    @Override
    public Fragment getItem(int position) {
        Log.v(TAG, "getItem " + position);
        Fragment fragment = null;
        switch(position) {
            case 0:
                fragment = new ItemListFragment();
                break;
            case 1:
                fragment = new BlocspotMapFragment();
                break;
        }
        return fragment;


//        mObservers.deleteObservers();
//        Fragment fragment = mFragmentList.get(position);
//        if (fragment instanceof Observer) {
//            mObservers.addObserver((Observer) fragment);
//        }
//        return fragment;
    }

    @Override
    public int getCount() {
        return mFragmentList.size();
    }

    public void addFragment(Fragment fragment, String title) {
        mFragmentList.add(fragment);
        mFragmentTitleList.add(title);
    }

//    public void updateFragments() {
//        Log.v(TAG, "updateFragments");
//        mObservers.notifyObservers();
//    }

    @Override
    public CharSequence getPageTitle(int position) {
        return mFragmentTitleList.get(position);
    }

    /*
     * Observer
     * ViewPagerAdapter is observing the DataSource
     * the fragments within this ViewPagerAdapter are observing the ViewPagerAdapter
     */
    @Override
    public void update(Observable observable, Object data) {
        Log.v(TAG, "DataSource observer update");

        Listener listener = (Listener) mFragmentList.get(0);
        listener.updateListener();

        listener = (Listener) mFragmentList.get(1);
        listener.updateListener();
    }
}
