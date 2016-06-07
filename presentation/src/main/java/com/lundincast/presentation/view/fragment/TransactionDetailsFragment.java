package com.lundincast.presentation.view.fragment;

import android.app.Fragment;
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

import com.afollestad.materialdialogs.MaterialDialog;
import com.lundincast.presentation.R;
import com.lundincast.presentation.model.AccountModel;
import com.lundincast.presentation.model.CategoryModel;
import com.lundincast.presentation.view.activity.CreateTransactionActivity;
import com.lundincast.presentation.utils.CustomDateFormatter;
import com.lundincast.presentation.view.adapter.AccountListDialogAdapter;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;
import com.wdullaer.materialdatetimepicker.time.RadialPickerLayout;
import com.wdullaer.materialdatetimepicker.time.TimePickerDialog;

import java.util.Calendar;
import java.util.Date;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.realm.Realm;
import io.realm.RealmResults;

/**
 * A {@link Fragment} subclass for showing transaction details
 */
public class TransactionDetailsFragment extends BaseFragment implements DatePickerDialog.OnDateSetListener,
                                                                        TimePickerDialog.OnTimeSetListener {

    @Bind(R.id.iv_category_icon) ImageView iv_category_icon;
    @Bind(R.id.et_category_name) TextView et_category_name;
    @Bind(R.id.et_transaction_date) TextView et_transaction_date;
    @Bind(R.id.et_transaction_time) TextView et_transaction_time;
    @Bind(R.id.et_account_name) EditText et_account_name;
    @Bind(R.id.et_transaction_comment) EditText et_transaction_comment;
    @Bind(R.id.cb_due) CheckBox cb_due;
    @Bind(R.id.sp_due_by_to) Spinner sp_due_by_to;
    @Bind(R.id.et_due_name) EditText et_due_name;

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
        CategoryModel category = ((CreateTransactionActivity) getActivity()).getCategory();
        int color = category.getColor();
        // set circle drawable color
        LayerDrawable bgDrawable = (LayerDrawable) iv_category_icon.getBackground();
        final GradientDrawable shape = (GradientDrawable) bgDrawable.findDrawableByLayerId(R.id.circle_id);
        shape.setColor(color);
        // Set category name
        et_category_name.setText(category.getName());

        // Set date and time as in activity's presenter
        Calendar cal = Calendar.getInstance();
        Date date = ((CreateTransactionActivity) getActivity()).getDate();
        cal.setTime(date);
        et_transaction_date.setText(CustomDateFormatter.getShortFormattedDate(cal));
        if (cal.get(Calendar.MINUTE) <= 9) {
            et_transaction_time.setText(cal.get(Calendar.HOUR_OF_DAY) + ":0" + cal.get(Calendar.MINUTE));
        } else {
            et_transaction_time.setText(cal.get(Calendar.HOUR_OF_DAY) + ":" + cal.get(Calendar.MINUTE));
        }

        // Set account as in activity's presenter
        AccountModel account = ((CreateTransactionActivity) getActivity()).getAccount();
        // Set account name
        if (account != null) {
            et_account_name.setText(account.getName());
        } else {
            et_account_name.setText(R.string.unassigned);
        }

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

    @OnClick(R.id.et_category_name)
    void onCategoryClicked() {
        ((CreateTransactionActivity) getActivity()).onCategoryClickedInDetails();
    }

    @OnClick(R.id.et_transaction_date)
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

    @OnClick(R.id.et_transaction_time)
    void onTimeClicked() {
        Calendar cal = Calendar.getInstance();
        Date date = ((CreateTransactionActivity) getActivity()).getDate();
        cal.setTime(date);
        TimePickerDialog tpd = TimePickerDialog.newInstance(
                this,
                cal.get(Calendar.HOUR_OF_DAY),
                cal.get(Calendar.MINUTE),
                true
        );
        tpd.show(getFragmentManager(), "TimepickerDialog");
    }

    @OnClick(R.id.et_account_name)
    void onAccountClicked() {
        Realm realm = Realm.getDefaultInstance();
        RealmResults<AccountModel> result = realm.where(AccountModel.class).findAll();

        new MaterialDialog.Builder(getActivity())
                .title(R.string.choose_account)
                .adapter(new AccountListDialogAdapter(getActivity(), result),
                        new MaterialDialog.ListCallback() {
                            @Override
                            public void onSelection(MaterialDialog dialog, View itemView, int which, CharSequence text) {
                                dialog.dismiss();
                                TransactionDetailsFragment.this.onAccountSet(itemView);
                            }
                        })
                .show();
    }

    @Override
    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(((CreateTransactionActivity) getActivity()).getDate());
        cal.set(Calendar.YEAR, year);
        cal.set(Calendar.MONTH, monthOfYear);
        cal.set(Calendar.DAY_OF_MONTH, dayOfMonth);
        // assign selected date to mDate variable in parent activity
        ((CreateTransactionActivity) getActivity()).onDateSet(cal.getTime());
        // format date and display
        et_transaction_date.setText(CustomDateFormatter.getShortFormattedDate(cal));
    }

    @Override
    public void onTimeSet(RadialPickerLayout view, int hourOfDay, int minute, int second) {
        Calendar cal = Calendar.getInstance();
        Date date = ((CreateTransactionActivity) getActivity()).getDate();
        cal.setTime(date);
        cal.set(Calendar.HOUR_OF_DAY, hourOfDay);
        cal.set(Calendar.MINUTE, minute);
        // assign updated time to mDate in parent activity. We can reuse onDateSet here.
        ((CreateTransactionActivity) getActivity()).onDateSet(cal.getTime());
        // Display time on screen
        if (cal.get(Calendar.MINUTE) <= 9) {
            et_transaction_time.setText(cal.get(Calendar.HOUR_OF_DAY) + ":0" + cal.get(Calendar.MINUTE));
        } else {
            et_transaction_time.setText(cal.get(Calendar.HOUR_OF_DAY) + ":" + cal.get(Calendar.MINUTE));
        }
    }

    private void onAccountSet(View itemView) {
        TextView accountNameTv = (TextView) itemView.findViewById(R.id.et_category_name);
        this.et_account_name.setText(accountNameTv.getText());
        ((CreateTransactionActivity) getActivity()).onAccountSet((String) accountNameTv.getText());
    }
}
