package com.stevensadler.android.blocspot.ui.fragment;

import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.stevensadler.android.blocspot.R;
import com.stevensadler.android.blocspot.ui.UIUtils;

/**
 * Created by Steven on 3/3/2016.
 */
public class SaveCategoryDialogFragment extends DialogFragment implements
        View.OnClickListener,
        TextView.OnEditorActionListener {

    public interface SaveCategoryDialogListener {
        void onFinishSaveCategoryDialog(String inputText, int color);
    }

    private EditText mEditText;
    private Button mClearButton;
    private Button mRandomColorButton;
    private int mColor;

    public SaveCategoryDialogFragment() {
        // empty constructor required for Dialog Fragment
    }

    @Override
    public View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle savedInstanceState) {
        View view = layoutInflater.inflate(R.layout.save_category_fragment, viewGroup);
        mEditText = (EditText) view.findViewById(R.id.et_input_category);
        mClearButton = (Button) view.findViewById(R.id.b_clear);
        mRandomColorButton = (Button) view.findViewById(R.id.b_random_color);

        //mColor = UIUtils.generateRandomColor(Color.WHITE);
        mColor = UIUtils.generateRandomBrightColor();
        mRandomColorButton.setBackgroundColor(mColor);

        mClearButton.setOnClickListener(this);
        mRandomColorButton.setOnClickListener(this);

        // show soft keyboard automatically
        mEditText.requestFocus();
        getDialog().getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
        mEditText.setOnEditorActionListener(this);

        return view;
    }

    @Override
    public boolean onEditorAction(TextView tectView, int actionId, KeyEvent keyEvent) {
        if (EditorInfo.IME_ACTION_DONE == actionId) {
            SaveCategoryDialogListener activity = (SaveCategoryDialogListener) getActivity();
            activity.onFinishSaveCategoryDialog(mEditText.getText().toString(), mColor);
            this.dismiss();
            return true;
        }
        return false;
    }

    @Override
    public void onClick(View view) {
        if (view == mClearButton) {
            mEditText.setText("");
        } else if (view == mRandomColorButton) {
            //mColor = UIUtils.generateRandomColor(Color.WHITE);
            mColor = UIUtils.generateRandomBrightColor();
            mRandomColorButton.setBackgroundColor(mColor);
        }
    }
}
