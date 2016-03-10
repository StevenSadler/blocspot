package com.stevensadler.android.blocspot.ui.fragment;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.graphics.ColorUtils;
import android.util.Log;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.stevensadler.android.blocspot.api.model.Category;
import com.stevensadler.android.blocspot.api.model.PointOfInterest;
import com.stevensadler.android.blocspot.ui.BlocspotApplication;

import java.util.List;
import java.util.Observable;
import java.util.Observer;

/**
 * Created by Steven on 2/23/2016.
 */
public class BlocspotMapFragment extends SupportMapFragment implements
        Observer {

    private static String TAG = BlocspotMapFragment.class.getSimpleName();

    private static int DEFAULT_COLOR = 0xff0000;
    private static int DEFAULT_ALPHA = 0x20;

    private static List<PointOfInterest> mPointsOfInterest;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPointsOfInterest = BlocspotApplication.getSharedDataSource().getPointsOfInterest();
        initMap();

    }

    /*
     * Observer
     */
    @Override
    public void update(Observable observable, Object data) {
        Log.v(TAG, "update");
        mPointsOfInterest = BlocspotApplication.getSharedDataSource().getPointsOfInterest();
        initMap();
    }

    /*
     * private helper functions
     */
    private void initMap() {
        getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap googleMap) {
                googleMap.clear();
                for (PointOfInterest pointOfInterest : mPointsOfInterest) {
                    LatLng latLng = new LatLng(pointOfInterest.getLatitude(), pointOfInterest.getLongitude());
                    Category category = BlocspotApplication.getSharedDataSource()
                            .getCategoryWithRowId(pointOfInterest.getCategoryId());

                    int baseColor = (category == null) ? DEFAULT_COLOR : category.getColor();
                    int color = ColorUtils.setAlphaComponent(baseColor, DEFAULT_ALPHA);
                    float[] hsl = new float[3];
                    ColorUtils.colorToHSL(color, hsl);
                    float hue = hsl[0];

                    googleMap.addMarker(new MarkerOptions()
                            .title(pointOfInterest.getTitle())
                            .position(latLng)
                            .icon(BitmapDescriptorFactory.defaultMarker(hue)));

                    googleMap.addCircle(new CircleOptions()
                            .center(latLng)
                            .radius(pointOfInterest.getRadius())
                            .fillColor(color)
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
