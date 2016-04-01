package com.stevensadler.android.blocspot.ui.adapter;


import java.util.Observable;

/**
 * Created by Steven on 3/30/2016.
 */
public class FragmentObserver extends Observable {
    @Override
    public void notifyObservers() {
        setChanged();
        super.notifyObservers();
    }
}
