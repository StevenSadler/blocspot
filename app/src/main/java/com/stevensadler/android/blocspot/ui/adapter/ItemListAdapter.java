package com.stevensadler.android.blocspot.ui.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.stevensadler.android.blocspot.R;
import com.stevensadler.android.blocspot.api.model.PointOfInterest;

import java.lang.ref.WeakReference;
import java.util.List;

/**
 * Created by Steven on 2/24/2016.
 */
public class ItemListAdapter extends RecyclerView.Adapter<ItemListAdapter.ItemHolder> {

    public static interface Delegate {
        public void onItemClicked(ItemListAdapter itemListAdapter, PointOfInterest pointOfInterest);
    }

    private static String TAG = ItemListAdapter.class.getSimpleName();

    private List<PointOfInterest> mPointsOfInterest;
    private WeakReference<Delegate> delegate;

    public ItemListAdapter(List<PointOfInterest> pointsOfInterest) {
        mPointsOfInterest = pointsOfInterest;
    }

    public ItemHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater
                .inflate(R.layout.poi_item, parent, false);
        return new ItemHolder(view);
    }

    @Override
    public void onBindViewHolder(ItemHolder holder, int position) {
        holder.update(mPointsOfInterest.get(position));
    }

    @Override
    public int getItemCount() {
        return mPointsOfInterest.size();
    }

    public Delegate getDelegate() {
        if (delegate == null) {
            return null;
        }
        return delegate.get();
    }

    public void setDelegate(Delegate delegate) {
        this.delegate = new WeakReference<Delegate>(delegate);
    }

    /*
     * ItemHolder class
     */
    class ItemHolder extends RecyclerView.ViewHolder {

        public TextView poiTitleTextView;

        private PointOfInterest pointOfInterest;

        public ItemHolder(View itemView) {
            super(itemView);
            poiTitleTextView = (TextView) itemView.findViewById(R.id.tv_poi_item_title);
        }

        void update(PointOfInterest poi) {
            pointOfInterest = poi;
            poiTitleTextView.setText(pointOfInterest.getTitle());
        }
    }
}
