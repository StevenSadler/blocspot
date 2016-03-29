package com.stevensadler.android.blocspot.ui.fragment;

import com.stevensadler.android.blocspot.api.model.PointOfInterest;

/**
 * Created by Steven on 3/8/2016.
 */
public interface IPointOfInterestInput {
    public void onSelectPointOfInterest(PointOfInterest pointOfInterest);
    public void onVisitedPointOfInterest(PointOfInterest pointOfInterest);
    public void onDeletePointOfInterest(PointOfInterest pointOfInterest);
    public void onMapFindPointOfInterest(PointOfInterest pointOfInterest);
    //public void onEditPointOfInterest(PointOfInterest pointOfInterest);
}
