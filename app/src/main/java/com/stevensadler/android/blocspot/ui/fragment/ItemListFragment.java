package com.stevensadler.android.blocspot.ui.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.stevensadler.android.blocspot.R;
import com.stevensadler.android.blocspot.api.model.Category;
import com.stevensadler.android.blocspot.api.model.PointOfInterest;
import com.stevensadler.android.blocspot.ui.BlocspotApplication;
import com.stevensadler.android.blocspot.ui.adapter.ItemListAdapter;
import com.stevensadler.android.blocspot.ui.adapter.ViewPagerAdapter;

import java.util.List;

/**
 * Created by Steven on 2/24/2016.
 */
public class ItemListFragment extends Fragment implements
        ViewPagerAdapter.Listener,
        ItemListAdapter.Delegate {

    private static String TAG = ItemListFragment.class.getSimpleName()+" sjs";

    private static List<PointOfInterest> mPointsOfInterest;
    private static List<Category> mCategories;

    private RecyclerView mRecyclerView;
    private ItemListAdapter mItemListAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.v(TAG, "onCreate");

        mPointsOfInterest = BlocspotApplication.getSharedDataSource().getPointsOfInterest();
        mCategories = BlocspotApplication.getSharedDataSource().getCategories();

        mItemListAdapter = new ItemListAdapter(getResources(), mPointsOfInterest, mCategories);
        mItemListAdapter.setDelegate(this);
    }

    @Override
    public View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle savedInstanceState) {
        View view = layoutInflater.inflate(R.layout.poi_item_list_fragment, viewGroup, false);
        mRecyclerView = (RecyclerView) view.findViewById(R.id.rv_poi_item_list_fragment);

        LinearLayoutManager llm = new LinearLayoutManager(getActivity());
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(llm);
        mRecyclerView.setAdapter(mItemListAdapter);
        return view;
    }

    /*
     * ItemListAdapter.Delegate
     */
    @Override
    public void onVisitedClicked(PointOfInterest pointOfInterest) {
        Log.v(TAG, "onVisitedClicked");
        IPointOfInterestInput activity = (IPointOfInterestInput) getActivity();
        activity.onVisitedPointOfInterest(pointOfInterest);
        activity.onMapFindPointOfInterest(pointOfInterest);
    }

    @Override
    public void onDeleteClicked(PointOfInterest pointOfInterest) {
        Log.v(TAG, "onDeleteClicked");
        IPointOfInterestInput activity = (IPointOfInterestInput) getActivity();
        activity.onDeletePointOfInterest(pointOfInterest);
    }

    @Override
    public void onItemClicked(PointOfInterest pointOfInterest) {
        Log.v(TAG, "onItemClicked ");
        IPointOfInterestInput activity = (IPointOfInterestInput) getActivity();
        activity.onMapFindPointOfInterest(pointOfInterest);
    }

    @Override
    public void onItemLongClicked(PointOfInterest pointOfInterest) {
        Log.v(TAG, "onItemLongClicked ");
        IPointOfInterestInput activity = (IPointOfInterestInput) getActivity();
        activity.onSelectPointOfInterest(pointOfInterest);
    }

    /*
     * ViewPagerAdapter.Listener
     */
    @Override
    public void updateListener() {

        mPointsOfInterest = BlocspotApplication.getSharedDataSource().getPointsOfInterest();
        mCategories = BlocspotApplication.getSharedDataSource().getCategories();

        if (isAdded()) {
            Log.v(TAG, "update  ItemListFragment is added to its activity");
            mItemListAdapter = new ItemListAdapter(getResources(), mPointsOfInterest, mCategories);
            mItemListAdapter.setDelegate(this);
            mRecyclerView.setAdapter(mItemListAdapter);
        } else {
            Log.d(TAG, "update  ItemListFragment is NOT added to its activity");
        }
    }
}
