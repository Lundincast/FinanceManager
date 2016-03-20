package com.lundincast.presentation.view.fragment;

import android.app.Fragment;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.LayerDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.lundincast.presentation.R;
import com.lundincast.presentation.model.CategoryModel;
import com.lundincast.presentation.view.activity.CreateTransactionActivity;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * A {@link Fragment} subclass for showing transaction details
 */
public class TransactionDetailsFragment extends BaseFragment implements DatePickerDialog.OnDateSetListener {

    @Bind(R.id.iv_category_icon) ImageView iv_category_icon;
    @Bind(R.id.tv_category_name) TextView tv_category_name;
    @Bind(R.id.tv_transaction_date) TextView tv_transaction_date;
    @Bind(R.id.et_transaction_comment) EditText et_transaction_comment;
    @Bind(R.id.cb_due) CheckBox cb_due;
    @Bind(R.id.sp_due_by_to) Spinner sp_due_by_to;
    @Bind(R.id.et_due_name) EditText et_due_name;

    SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View fragmentView = inflater.inflate(R.layout.fragment_transaction_details, container, false);
        ButterKnife.bind(this, fragmentView);

        return fragmentView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        // Set category as in activity's presenter
        String[] colorsArray = getActivity().getResources().getStringArray(R.array.colors_name);
        String[] colorValue = getActivity().getResources().getStringArray(R.array.colors_value);
        CategoryModel category = ((CreateTransactionActivity) getActivity()).getCategory();
        String colorName = category.getColor();
        String colorCode = null;
        int it = 0;
        for (String s: colorsArray) {
            if (s.equals(colorName)) {
                colorCode = colorValue[it];
                break;
            }
            it++;
        }
        // set circle drawable color
        LayerDrawable bgDrawable = (LayerDrawable) iv_category_icon.getBackground();
        final GradientDrawable shape = (GradientDrawable) bgDrawable.findDrawableByLayerId(R.id.circle_id);
        shape.setColor(Color.parseColor(colorCode));
        // Set category name
        tv_category_name.setText(category.getName());

        // Set date as in activity's presenter
        Calendar cal = Calendar.getInstance();
        Date date = ((CreateTransactionActivity) getActivity()).getDate();
        cal.setTime(date);
        tv_transaction_date.setText(sdf.format(cal.getTime()));

        // Set comment as in activity's presenter
        et_transaction_comment.setText(((CreateTransactionActivity) getActivity()).getComment());
        // set a listener on comment EditText to save it in parent's activity mComment variable
        et_transaction_comment.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // Do nothing here
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                ((CreateTransactionActivity) getActivity()).onCommentSet(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {
                // Do nothing here
            }
        });

        // Set pending info
        cb_due.setChecked(((CreateTransactionActivity) getActivity()).isPending());
        if (!cb_due.isChecked()) {
            sp_due_by_to.setEnabled(false);
            et_due_name.setEnabled(false);
        } else {
            sp_due_by_to.setSelection(((CreateTransactionActivity) getActivity()).getDueToOrBy());
            et_due_name.setText(((CreateTransactionActivity) getActivity()).getDueName());
        }
        // Set listeners to update pending info in presenter
        cb_due.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (!isChecked) {
                    ((CreateTransactionActivity) getActivity()).setPending(false);
                    sp_due_by_to.setEnabled(false);
                    et_due_name.setEnabled(false);
                } else {
                    ((CreateTransactionActivity) getActivity()).setPending(true);
                    sp_due_by_to.setEnabled(true);
                    et_due_name.setEnabled(true);
                }
            }
        });
        sp_due_by_to.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                ((CreateTransactionActivity) getActivity()).setDueToOrBy(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Do nothing here
            }
        });
        et_due_name.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // Do nothing here
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                ((CreateTransactionActivity) getActivity()).setDueName(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {
                // Do nothing here
            }
        });

    }

    @OnClick(R.id.tv_category_name)
    void onCategoryClicked() {
        ((CreateTransactionActivity) getActivity()).onCategoryClickedinDetails();
    }

    @OnClick(R.id.tv_transaction_date)
    void onDateClicked() {
        Calendar cal = Calendar.getInstance();
        Date date = ((CreateTransactionActivity) getActivity()).getDate();
        cal.setTime(date);
        DatePickerDialog dpd = DatePickerDialog.newInstance(
                this,
                cal.get(Calendar.YEAR),
                cal.get(Calendar.MONTH),
                cal.get(Calendar.DAY_OF_MONTH)
        );
        dpd.show(getFragmentManager(), "Datepickerdialog");
    }

    @Override
    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.YEAR, year);
        cal.set(Calendar.MONTH, monthOfYear);
        cal.set(Calendar.DAY_OF_MONTH, dayOfMonth);
        // assign selected date to mPrice variable in parent activity
        ((CreateTransactionActivity) getActivity()).onDateSet(cal.getTime());
        // format date and display
        tv_transaction_date.setText(sdf.format(cal.getTime()));
    }
}
