package com.stevensadler.android.blocspot.ui.fragment;

import android.graphics.Color;
import android.os.Bundle;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CircleOptions;
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
                    LatLng latLng = new LatLng(pointOfInterest.getLatitude(),pointOfInterest.getLongitude());
                    googleMap.addMarker(new MarkerOptions()
                            .title(pointOfInterest.getTitle())
                            .position(latLng));
                    googleMap.addCircle(new CircleOptions()
                            .center(latLng)
                            .radius(pointOfInterest.getRadius())
                            .fillColor(0x20ff0000)
                            .strokeColor(Color.TRANSPARENT)
                            .strokeWidth(2));
                }
                if (mPointsOfInterest.size() > 0) {
                    LatLng cameraLatLng = new LatLng(mPointsOfInterest.get(0).getLatitude(),
                            mPointsOfInterest.get(0).getLongitude());

                    googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(cameraLatLng, 15));
                }
            }
        });
    }

}
