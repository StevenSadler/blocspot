package com.stevensadler.android.blocspot.ui.adapter;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.stevensadler.android.blocspot.R;
import com.stevensadler.android.blocspot.api.model.Category;
import com.stevensadler.android.blocspot.api.model.PointOfInterest;

import java.lang.ref.WeakReference;
import java.util.List;

/**
 * Created by Steven on 2/24/2016.
 */
public class ItemListAdapter extends RecyclerView.Adapter<ItemListAdapter.ItemHolder> {

    public static interface Delegate {
        public void onItemClicked(PointOfInterest pointOfInterest);
        public void onItemLongClicked(PointOfInterest pointOfInterest);
    }

    private static String TAG = ItemListAdapter.class.getSimpleName();

    private List<PointOfInterest> mPointsOfInterest;
    private List<Category> mCategories;
    private WeakReference<Delegate> delegate;

    public ItemListAdapter(List<PointOfInterest> pointsOfInterest, List<Category> categories) {
        mPointsOfInterest = pointsOfInterest;
        mCategories = categories;
    }

    public ItemHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater
                .inflate(R.layout.poi_item, parent, false);
        return new ItemHolder(view);
    }

    @Override
    public void onBindViewHolder(ItemHolder holder, int position) {
        PointOfInterest pointOfInterest = mPointsOfInterest.get(position);
        Category category = null;
        for (Category cat : mCategories) {
            if (cat.getRowId() == pointOfInterest.getCategoryId()) {
                category = cat;
            }
        }
//        Cursor cursor = BlocspotApplication.getSharedDataSource().getCursorOfInsertedCategoryWithRowId(pointOfInterest.getCategoryId());
//        Category category = BlocspotApplication.getSharedDataSource().getCategoryWithRowId(pointOfInterest.getCategoryId());
        holder.update(pointOfInterest, category);
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
    class ItemHolder extends RecyclerView.ViewHolder implements
            View.OnClickListener,
            View.OnLongClickListener {

        public View poiCategorySwatch;
        public TextView poiTitleTextView;

        private PointOfInterest pointOfInterest;
        private Category category;

        public ItemHolder(View itemView) {
            super(itemView);
            poiTitleTextView = (TextView) itemView.findViewById(R.id.tv_poi_item_title);
            poiTitleTextView.setOnClickListener(this);
            poiTitleTextView.setOnLongClickListener(this);

            poiCategorySwatch = (View) itemView.findViewById(R.id.v_poi_item_category_swatch);

        }

        void update(PointOfInterest pointOfInterest, Category category) {
            this.pointOfInterest = pointOfInterest;
            this.category = category;
            poiTitleTextView.setText(pointOfInterest.getTitle());
            if (category != null) {
                poiCategorySwatch.setBackgroundColor(category.getColor());
            }
        }

        @Override
        public void onClick(View view) {
            if (getDelegate() != null) {
                getDelegate().onItemClicked(pointOfInterest);
            }
        }

        @Override
        public boolean onLongClick(View view) {
            Log.v(TAG, "onLongClick");
            if (getDelegate() != null) {
                getDelegate().onItemLongClicked(pointOfInterest);
                return true;
            }
            return false;
        }
    }
}
