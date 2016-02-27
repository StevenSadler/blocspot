package com.stevensadler.android.blocspot.ui.fragment;

import android.os.Bundle;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.stevensadler.android.blocspot.api.model.PointOfInterest;
import com.stevensadler.android.blocspot.ui.BlocspotApplication;

import java.util.List;

/**
 * Created by Steven on 2/23/2016.
 */
public class BlocspotMapFragment extends SupportMapFragment {

    private static String TAG = BlocspotMapFragment.class.getSimpleName();

    private static List<PointOfInterest> mPointsOfInterest;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPointsOfInterest = BlocspotApplication.getSharedDataSource().getPointsOfInterest();

        getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap googleMap) {
                for (PointOfInterest pointOfInterest : mPointsOfInterest) {
                    googleMap.addMarker(new MarkerOptions()
                            .title(pointOfInterest.getTitle())
                            .position(new LatLng(pointOfInterest.getLatitude(),
                                    pointOfInterest.getLongitude())));
                }
            }
        });
    }

}
