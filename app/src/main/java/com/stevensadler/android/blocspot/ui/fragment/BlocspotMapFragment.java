package com.stevensadler.android.blocspot.ui.fragment;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.graphics.ColorUtils;
import android.util.Log;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.stevensadler.android.blocspot.R;
import com.stevensadler.android.blocspot.api.model.Category;
import com.stevensadler.android.blocspot.api.model.PointOfInterest;
import com.stevensadler.android.blocspot.ui.BlocspotApplication;
import com.stevensadler.android.blocspot.ui.adapter.ViewPagerAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Steven on 2/23/2016.
 */
public class BlocspotMapFragment extends SupportMapFragment implements
        ViewPagerAdapter.Listener {

    private static String TAG = BlocspotMapFragment.class.getSimpleName()+" sjs";

    private static int DEFAULT_COLOR = 0xff0000;
    private static int DEFAULT_ALPHA = 0x20;

    private static List<PointOfInterest> mPointsOfInterest;
    private static List<PointOfInterest> mYelpPointsOfInterest;
    private static List<Marker> mMarkerList;
    private static HashMap<Marker, PointOfInterest> mYelpMarkerHashMap;

    private GoogleMap mGoogleMap;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.v(TAG, "onCreate");
        mMarkerList = new ArrayList<>();
        mYelpMarkerHashMap = new HashMap<>();
        mPointsOfInterest = BlocspotApplication.getSharedDataSource().getPointsOfInterest();
        mYelpPointsOfInterest = BlocspotApplication.getSharedDataSource().getYelpPointsOfInterest();
        initMap();

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        Log.v(TAG, "onDestroyView");
        Fragment fragment =  getFragmentManager()
                .findFragmentById(R.id.map);
        if (fragment != null) {
            getFragmentManager().beginTransaction().remove(fragment).commit();
        }
    }

    /*
     * ViewPagerAdapter.Listener
     */
    @Override
    public void updateListener() {
        Log.v(TAG, "updateListener");
        mPointsOfInterest = BlocspotApplication.getSharedDataSource().getPointsOfInterest();
        mYelpPointsOfInterest = BlocspotApplication.getSharedDataSource().getYelpPointsOfInterest();
        initMap();

//        if (mGoogleMap != null) {
//            setupMap();
//        } else {
//            Log.v(TAG, "update   why is mGoogleMap null");
//        }
    }

    /*
     * private helper functions
     */
    private void initMap() {
        getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap googleMap) {

                mGoogleMap = googleMap;

                setupMap();

//                // clean up the markers and lists from previous runs
//                googleMap.clear();
//                for (Marker marker : mMarkerList) {
//                    marker.remove();
//                }
//                mMarkerList.clear();
//                mYelpMarkerHashMap.clear();
//                for (Map.Entry<Marker, PointOfInterest> entry : mYelpMarkerHashMap.entrySet()) {
//                    // do something
//                }
//
//                // add real markers for saved POI's
//                for (PointOfInterest pointOfInterest : mPointsOfInterest) {
//                    Marker marker = showMarker(googleMap, pointOfInterest, false);
//
//                    if (mMarkerList.contains(marker)) {
//                        marker.remove();
//                    } else {
//                        mMarkerList.add(marker);
//                    }
//                }
//
//                // add yelp markers for unsaved yelp POI's
//                //final HashMap<Marker, PointOfInterest> yelpHashMap = new HashMap<Marker, PointOfInterest>();
//                for (PointOfInterest yelpPointOfInterest : mYelpPointsOfInterest) {
//                    Marker marker = showMarker(googleMap, yelpPointOfInterest, false);
//
//                    // remove the the yelp marker if it is a dupe of a real marker
//                    // otherwise add it to the hashmap
//                    if (mMarkerList.contains(marker)) {
//                        marker.remove();
//                    } else {
//                        mYelpMarkerHashMap.put(marker, yelpPointOfInterest);
//                    }
//                }
//
//                PointOfInterest selectedPOI = null;
//                if (mYelpPointsOfInterest.size() > 0) {
//                    selectedPOI = mYelpPointsOfInterest.get(0);
//                    googleMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
//                        @Override
//                        public boolean onMarkerClick(Marker clickedMarker) {
//                            for (Map.Entry<Marker, PointOfInterest> entry : mYelpMarkerHashMap.entrySet()) {
//                                if (clickedMarker.equals(entry.getKey())) {
//                                    Log.v(TAG, "onMarkerClick Yelp POI " + entry.getValue().getTitle());
//                                    IYelpPointOfInterestInput activity =
//                                            (IYelpPointOfInterestInput) getActivity();
//                                    activity.onSelectYelpPointOfInterest(entry.getValue());
//
//                                    return true;
//                                }
//                            }
//
//                            return false;
//                        }
//                    });
//                } else if (mPointsOfInterest.size() > 0) {
//                    selectedPOI = mPointsOfInterest.get(0);
//                }
//
//                if (selectedPOI != null) {
//                    LatLng cameraLatLng = new LatLng(selectedPOI.getLatitude(), selectedPOI.getLongitude());
//                    googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(cameraLatLng, 14));
//                }
            }
        });
    }

    private void setupMap() {
        // clean up the markers and lists from previous runs
        mGoogleMap.clear();
        for (Marker marker : mMarkerList) {
            marker.remove();
        }
        mMarkerList.clear();
        mYelpMarkerHashMap.clear();
        for (Map.Entry<Marker, PointOfInterest> entry : mYelpMarkerHashMap.entrySet()) {
            // do something
        }

        // add real markers for saved POI's
        for (PointOfInterest pointOfInterest : mPointsOfInterest) {
            Marker marker = showMarker(mGoogleMap, pointOfInterest, false);

            if (mMarkerList.contains(marker)) {
                marker.remove();
            } else {
                mMarkerList.add(marker);
            }
        }

        // add yelp markers for unsaved yelp POI's
        //final HashMap<Marker, PointOfInterest> yelpHashMap = new HashMap<Marker, PointOfInterest>();
        for (PointOfInterest yelpPointOfInterest : mYelpPointsOfInterest) {
            Marker marker = showMarker(mGoogleMap, yelpPointOfInterest, false);

            // remove the the yelp marker if it is a dupe of a real marker
            // otherwise add it to the hashmap
            if (mMarkerList.contains(marker)) {
                marker.remove();
            } else {
                mYelpMarkerHashMap.put(marker, yelpPointOfInterest);
            }
        }

        PointOfInterest selectedPOI = null;
        if (mYelpPointsOfInterest.size() > 0) {
            selectedPOI = mYelpPointsOfInterest.get(0);
            mGoogleMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
                @Override
                public boolean onMarkerClick(Marker clickedMarker) {
                    for (Map.Entry<Marker, PointOfInterest> entry : mYelpMarkerHashMap.entrySet()) {
                        if (clickedMarker.equals(entry.getKey())) {
                            Log.v(TAG, "onMarkerClick Yelp POI " + entry.getValue().getTitle());
                            IYelpPointOfInterestInput activity =
                                    (IYelpPointOfInterestInput) getActivity();
                            activity.onSelectYelpPointOfInterest(entry.getValue());

                            return true;
                        }
                    }

                    return false;
                }
            });
        } else if (mPointsOfInterest.size() > 0) {
            selectedPOI = mPointsOfInterest.get(0);
        }

        if (selectedPOI != null) {
            LatLng cameraLatLng = new LatLng(selectedPOI.getLatitude(), selectedPOI.getLongitude());
            mGoogleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(cameraLatLng, 14));
        }
    }

    private Marker showMarker(GoogleMap googleMap, PointOfInterest pointOfInterest, Boolean showCircle) {
        LatLng latLng = new LatLng(pointOfInterest.getLatitude(), pointOfInterest.getLongitude());
        Category category = BlocspotApplication.getSharedDataSource()
                .getCategoryWithRowId(pointOfInterest.getCategoryId());

        int baseColor = (category == null) ? DEFAULT_COLOR : category.getColor();
        int color = ColorUtils.setAlphaComponent(baseColor, DEFAULT_ALPHA);
        float[] hsl = new float[3];
        ColorUtils.colorToHSL(color, hsl);
        float hue = hsl[0];

        MarkerOptions markerOptions = new MarkerOptions()
                .title(pointOfInterest.getTitle())
                .position(latLng);

        if (pointOfInterest.getVisited()){
            markerOptions = markerOptions
                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_beenhere_black_24dp));
        } else {
            markerOptions = markerOptions
                    .icon(BitmapDescriptorFactory.defaultMarker(hue));
        }
        if (!pointOfInterest.getNote().equals(PointOfInterest.DEFAULT_NOTE)) {
            markerOptions = markerOptions.snippet(pointOfInterest.getNote());
        }
        Marker marker = googleMap.addMarker(markerOptions);
        
        if (showCircle) {
            googleMap.addCircle(new CircleOptions()
                    .center(latLng)
                    .radius(pointOfInterest.getRadius())
                    .fillColor(color)
                    .strokeColor(Color.TRANSPARENT)
                    .strokeWidth(2));
        }

        return marker;
    }

    public void moveCameraToPointOfInterest(PointOfInterest pointOfInterest) {
        GoogleMap map = getMap();
        if (map != null) {
            LatLng cameraLatLng = new LatLng(pointOfInterest.getLatitude(),
                    pointOfInterest.getLongitude());
            map.moveCamera(CameraUpdateFactory.newLatLngZoom(cameraLatLng, 14));
        }
    }

//    /*
//     * ItemListAdapter.Delegate
//     */
//    @Override
//    public void onItemClicked(PointOfInterest pointOfInterest) {
//        Log.v(TAG, "onItemClicked");
//    }
//
//    @Override
//    public void onItemLongClicked(PointOfInterest pointOfInterest) {
//        Log.v(TAG, "onItemLongClicked ");
//        IPointOfInterestInput activity = (IPointOfInterestInput) getActivity();
//        activity.onSelectPointOfInterest(pointOfInterest);
//    }
}
