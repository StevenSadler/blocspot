package com.stevensadler.android.blocspot.ui.fragment;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.TextView;

import com.stevensadler.android.blocspot.R;
import com.stevensadler.android.blocspot.api.model.Category;
import com.stevensadler.android.blocspot.ui.BlocspotApplication;

import java.util.List;

/**
 * Created by Steven on 3/6/2016.
 */
public class ChooseCategoryDialogFragment extends DialogFragment implements
        View.OnClickListener {

    public interface Listener {
        public void onFinishChooseCategoryDialog(Category category);
    }

    private static String TAG = ChooseCategoryDialogFragment.class.getSimpleName();

//    public static String SET_OPTION = "choose_category_set_option";
//    public static int SET_POI_CATEGORY = 1;
//    public static int SET_FILTER = 2;

    private int mSetOption;

    public ChooseCategoryDialogFragment() {
        // empty constructor required for Dialog Fragment
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        //mSetOption = savedInstanceState.getInt(SET_OPTION);
        final List<Category> categories = BlocspotApplication.getSharedDataSource().getCategories();

        ListAdapter adapter = new ArrayAdapter<Category>(
                getContext(), R.layout.category_item, categories) {

            ViewHolder holder;

            class ViewHolder {
                TextView title;
            }

            public View getView(int position, View view, ViewGroup parent) {
                final LayoutInflater inflater = getActivity().getLayoutInflater();
                if (view == null) {
                    view = inflater.inflate(R.layout.category_item, null);
                    holder = new ViewHolder();
                    holder.title = (TextView) view.findViewById(R.id.tv_category_item);
                    view.setTag(holder);
                } else {
                    holder = (ViewHolder) view.getTag();
                }
                holder.title.setText(categories.get(position).getTitle());
                holder.title.setBackgroundColor(categories.get(position).getColor());

                return view;
            }
        };

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(R.string.action_choose_category)
                .setAdapter(adapter,
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                //
                                // tell somebody that category[which] was clicked
                                Log.v(TAG, "click on " + which + " " + categories.get(which).getTitle());

//                                if (mSetOption == SET_POI_CATEGORY) {
//
//                                } else if (mSetOption == SET_FILTER) {
//
//                                } else {
//
//                                }
//                                switch (mSetOption) {
//                                    case SET_POI_CATEGORY:
//                                        break;
//                                    case SET_FILTER:
//                                        break;
//                                }

                                Listener activity = (Listener) getActivity();
                                activity.onFinishChooseCategoryDialog(categories.get(which));

                                ChooseCategoryDialogFragment.this.dismiss();

                            }
                        });
        return builder.create();
    }

    @Override
    public void onClick(View v) {

    }
}
