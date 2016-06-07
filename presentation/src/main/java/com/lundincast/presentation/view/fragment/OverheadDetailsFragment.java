package com.lundincast.presentation.view.fragment;

import android.app.AlertDialog;
import android.app.Fragment;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.LayerDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.NumberPicker;
import android.widget.TextView;

import com.lundincast.presentation.R;
import com.lundincast.presentation.model.CategoryModel;
import com.lundincast.presentation.view.activity.CreateOverheadActivity;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * A {@link Fragment} subclass for showing overhead details
 */
public class OverheadDetailsFragment  extends BaseFragment {

    @Bind(R.id.iv_category_icon) ImageView iv_category_icon;
    @Bind(R.id.et_category_name) TextView tv_category_name;
    @Bind(R.id.tv_on_day) TextView tv_on_day;
    @Bind(R.id.et_overhead_comment) EditText et_overhead_comment;

    private NumberPicker np;
    private short defaultDayValue;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View fragmentView = inflater.inflate(R.layout.fragment_overhead_details, container, false);
        ButterKnife.bind(this, fragmentView);

        return fragmentView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        // Set category as in activity's presenter
        CategoryModel category = ((CreateOverheadActivity) getActivity()).getCategory();
        int color = category.getColor();
        // set circle drawable color
        LayerDrawable bgDrawable = (LayerDrawable) iv_category_icon.getBackground();
        final GradientDrawable shape = (GradientDrawable) bgDrawable.findDrawableByLayerId(R.id.circle_id);
        shape.setColor(color);
        // Set category name
        tv_category_name.setText(category.getName());

        // Set day
        defaultDayValue = ((CreateOverheadActivity) getActivity()).getDayOfMonth();
        if (defaultDayValue == 0) {
            defaultDayValue = 1;
        }
        this.setOnDayTvText(defaultDayValue);

        // Set comment as in activity's presenter
        et_overhead_comment.setText(((CreateOverheadActivity) getActivity()).getComment());
        // set a listener on comment EditText to save it in parent's activity mComment variable
        et_overhead_comment.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // Do nothing here
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                ((CreateOverheadActivity) getActivity()).onCommentSet(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {
                // Do nothing here
            }
        });

    }

    private void setOnDayTvText(short dayOfMonth) {
        String suffix;
        if (dayOfMonth == 1 || dayOfMonth == 21) {
            suffix = "st";
        } else if (dayOfMonth == 2 || dayOfMonth == 22) {
            suffix = "nd";
        } else if (dayOfMonth == 3 || dayOfMonth == 23) {
            suffix = "rd";
        } else {
            suffix = "th";
        }
        tv_on_day.setText(" On " + String.valueOf(dayOfMonth) + suffix + " of the month");
    }


    @OnClick(R.id.et_category_name)
    void onCategoryClicked() {
        ((CreateOverheadActivity) getActivity()).onCategoryClickedInDetails();
    }

    @OnClick(R.id.tv_on_day)
    void onDayTvClicked() {
        // set up number picker
        LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View v = inflater.inflate(R.layout.number_picker, null);
        np = (NumberPicker) v.findViewById(R.id.numberPicker);
        np.setMaxValue(28);
        np.setMinValue(1);
        np.setValue(defaultDayValue);
        np.setWrapSelectorWheel(true);
        // set up dialog
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setView(v);
        builder.setPositiveButton("Set", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                updateDayCurrentValue((short) np.getValue());
                OverheadDetailsFragment.this.setOnDayTvText((short) np.getValue());
                dialog.dismiss();
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.create().show();
    }

    private void updateDayCurrentValue(short dayValue) {
        defaultDayValue = dayValue;
        ((CreateOverheadActivity) getActivity()).setDayOfMonth( dayValue);
    }

}
