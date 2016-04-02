package com.stevensadler.android.blocspot.ui.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.stevensadler.android.blocspot.ui.fragment.BlocspotMapFragment;
import com.stevensadler.android.blocspot.ui.fragment.ItemListFragment;

/**
 * Created by Steven on 2/23/2016.
 */
public class ViewPagerAdapter extends FragmentPagerAdapter {

    private static String TAG = ViewPagerAdapter.class.getSimpleName();
    public static int NUM_ITEMS = 2;

    public ViewPagerAdapter(FragmentManager manager) {
        super(manager);
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return ItemListFragment.newInstance(0, "ListPage");
            case 1:
                return BlocspotMapFragment.newInstance(1, "MapPage");
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return NUM_ITEMS;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        switch (position) {
            case 0:
                return "List";
            case 1:
                return "Map";
            default:
                return null;
        }
    }
}
