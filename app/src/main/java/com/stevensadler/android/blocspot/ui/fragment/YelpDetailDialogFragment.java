package com.stevensadler.android.blocspot.ui.fragment;

import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.stevensadler.android.blocspot.R;
import com.stevensadler.android.blocspot.api.model.PointOfInterest;
import com.stevensadler.android.blocspot.ui.BlocspotApplication;

/**
 * Created by Steven on 3/21/2016.
 */
public class YelpDetailDialogFragment extends DialogFragment implements
        View.OnClickListener {

    public interface Listener {
        public void onSaveYelpDetailDialog(String note);
        public void onExitYelpDetailDialog();
        public void onChooseYelpDetailDialog();
    }

    private static String TAG = YelpDetailDialogFragment.class.getSimpleName();

    private TextView mTitleTextView;
    private EditText mNoteEditText;
    private Button mSaveButton;
    private Button mExitButton;
    private Button mChooseCategoryButton;

    public YelpDetailDialogFragment() {
        // empty constructor required for Dialog Fragment
    }

    @Override
    public View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle savedInstanceState) {
        View view = layoutInflater.inflate(R.layout.yelp_poi_detail_fragment, viewGroup);
        mNoteEditText = (EditText) view.findViewById(R.id.et_poi_item_note);
        mSaveButton = (Button) view.findViewById(R.id.b_save);
        mExitButton = (Button) view.findViewById(R.id.b_exit);
        mChooseCategoryButton = (Button) view.findViewById(R.id.b_category);
        mTitleTextView = (TextView) view.findViewById(R.id.tv_poi_item_title);

        PointOfInterest poi = BlocspotApplication.getSharedDataSource().getSelectedPOI();
        mTitleTextView.setText(poi.getTitle());
        if (!PointOfInterest.DEFAULT_NOTE.equals(poi.getNote())) {
            mNoteEditText.setText(poi.getNote());
        }

        mSaveButton.setOnClickListener(this);
        mExitButton.setOnClickListener(this);
        mChooseCategoryButton.setOnClickListener(this);

        return view;
    }

    @Override
    public void onClick(View view) {
        if (view == mSaveButton) {
            Log.v(TAG, "onClick mSaveButton");
            Listener activity = (Listener) getActivity();
            activity.onSaveYelpDetailDialog(mNoteEditText.getText().toString());
            this.dismiss();
        } else if (view == mExitButton) {
            Log.v(TAG, "onClick mExitButton");
            Listener activity = (Listener) getActivity();
            activity.onExitYelpDetailDialog();
            this.dismiss();
        } else if (view == mChooseCategoryButton) {
            Log.v(TAG, "onClick mChooseCategoryButton");
            Listener activity = (Listener) getActivity();
            activity.onChooseYelpDetailDialog();
            this.dismiss();
        }
    }
}
