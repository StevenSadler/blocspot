package com.stevensadler.android.blocspot.ui.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.stevensadler.android.blocspot.R;
import com.stevensadler.android.blocspot.api.model.PointOfInterest;
import com.stevensadler.android.blocspot.ui.BlocspotApplication;
import com.stevensadler.android.blocspot.ui.adapter.ItemListAdapter;

import java.util.List;

/**
 * Created by Steven on 2/24/2016.
 */
public class ItemListFragment extends Fragment implements
        ItemListAdapter.Delegate {

    private static String TAG = ItemListFragment.class.getSimpleName();

    private static List<PointOfInterest> mPointsOfInterest;

    private RecyclerView mRecyclerView;
    private ItemListAdapter mItemListAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mPointsOfInterest = BlocspotApplication.getSharedDataSource().getPointsOfInterest();

        mItemListAdapter = new ItemListAdapter(mPointsOfInterest);
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
    public void onItemClicked(ItemListAdapter itemListAdapter, PointOfInterest pointOfInterest) {

    }
}
