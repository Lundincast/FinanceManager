package com.lundincast.presentation.view.fragment;

import android.app.Fragment;
import android.content.SharedPreferences;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.LayerDrawable;
import android.os.Bundle;
import android.support.annotation.ColorInt;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.lundincast.presentation.R;
import com.lundincast.presentation.dagger.components.AccountComponent;
import com.lundincast.presentation.presenter.CreateAccountPresenter;
import com.lundincast.presentation.view.CreateOrUpdateView;
import com.lundincast.presentation.view.activity.CreateOrUpdateAccountActivity;

import java.util.Locale;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * A {@link Fragment} subclass for creating new account
 */
public class CreateOrUpdateAccountFragment extends BaseFragment implements CreateOrUpdateView {

    @Bind(R.id.iv_category_icon) ImageView iv_account_icon;
    @Bind(R.id.et_category_name) EditText et_account_name;
    @Bind(R.id.tv_account_balance_label) TextView tv_account_balance_label;
    @Bind(R.id.et_account_balance_value) EditText et_account_balance_value;
    @Bind(R.id.tv_account_balance_currency) TextView tv_account_balance_currency;
    // View in activity can't be accessed from fragment
    ImageView iv_delete;
    ImageView iv_done;

    @Inject CreateAccountPresenter createAccountPresenter;
    @Inject SharedPreferences sharedPreferences;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_create_or_update_account, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initialize();
        ButterKnife.bind(this, getView());

        setDoneBehaviour();
        setAccountCircleListener();


        this.createAccountPresenter.initialize(((CreateOrUpdateAccountActivity) getActivity()).accountId);
    }

    private void initialize() {
        this.getComponent(AccountComponent.class).inject(this);
        this.createAccountPresenter.setView(this);
    }

    private void setDoneBehaviour() {
        // get iv_done view from activity and set onClick listener
        iv_done = (ImageView) getActivity().findViewById(R.id.iv_done);
        iv_done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (et_account_name.getText().toString().equals("")) {
                    CreateOrUpdateAccountFragment.this.showToastMessage("Account name cannot be empty!");
                } else {
                    double balance;
                    if (et_account_balance_value.getText().toString().equals("")) {
                        balance = 0;
                    } else {
                        balance = Double.valueOf(et_account_balance_value.getText().toString());
                    }
                    CreateOrUpdateAccountFragment.this.createAccountPresenter
                            .saveAccount(((CreateOrUpdateAccountActivity) getActivity()).accountId,
                                         et_account_name.getText().toString(),
                                         ((CreateOrUpdateAccountActivity) getActivity()).color,
                                         balance);
                    getActivity().finish();
                }
            }
        });
    }

    private void setAccountCircleListener() {
        // Set onclicklistener on category circle ImageView
        iv_account_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((CreateOrUpdateAccountActivity) CreateOrUpdateAccountFragment.this.getActivity()).displayColorChooserDialog();
            }
        });
    }

    @Override
    public void setToolbarTitle(@StringRes int title) {
        TextView tv_title = (TextView) getActivity().findViewById(R.id.tv_title);
        if (tv_title != null) {
            tv_title.setText(title);
        }
    }

    @Override
    public void setName(String name) {
        et_account_name.setText(name);
    }

    @Override
    public void setColor(int color) {
        LayerDrawable bgDrawable = (LayerDrawable) iv_account_icon.getBackground();
        GradientDrawable shape = (GradientDrawable) bgDrawable.findDrawableByLayerId(R.id.circle_id);
        shape.setColor(color);
    }

    @Override
    public void setBalanceLabel(@StringRes int stringResource) {
        tv_account_balance_label.setText(stringResource);
    }

    @Override
    public void setBalance(double balance) {
        et_account_balance_value.setText((String.format(Locale.getDefault(), "%.2f", balance)));
    }

    @Override
    public void setCurrency(String currency) {
        tv_account_balance_currency.setText(currency);
    }

    @Override
    public void showDeleteIcon() {
        // show delete icon in parent activity toolbar
        CreateOrUpdateAccountActivity activity = (CreateOrUpdateAccountActivity) getActivity();
        iv_delete = (ImageView) activity.findViewById(R.id.iv_delete);
        if (iv_delete != null) {
            iv_delete.setVisibility(View.VISIBLE);
            iv_delete.setClickable(true);
            iv_delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    CreateOrUpdateAccountFragment.this.createAccountPresenter.setDeleteDialogBuilder();
                }
            });
        }
    }

    public void showDialog(MaterialDialog.Builder builder) {
        builder.show();
    }

    @Override
    public void closeActivity() {
        getActivity().finish();
    }


    // TODO implement displayDeleteConfirmationDialog when Transaction model is updated with account field

    /**
     * Called from CreateOrUpdateAccountActivity after a color is selected
     *
     * @param selectedColor The @ColorInt selected from dialog
     */
    public void onColorSelected(@ColorInt int selectedColor) {
        LayerDrawable bgDrawable = (LayerDrawable) iv_account_icon.getBackground();
        GradientDrawable shape = (GradientDrawable) bgDrawable.findDrawableByLayerId(R.id.circle_id);
        shape.setColor(selectedColor);
    }


}
