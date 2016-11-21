package com.lundincast.presentation.view.activity;

import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.lundincast.presentation.R;
import com.lundincast.presentation.dagger.HasComponent;
import com.lundincast.presentation.dagger.components.DaggerOverheadsComponent;
import com.lundincast.presentation.dagger.components.OverheadsComponent;
import com.lundincast.presentation.dagger.modules.OverheadsModule;
import com.lundincast.presentation.model.CategoryModel;
import com.lundincast.presentation.presenter.CreateOverheadPresenter;
import com.lundincast.presentation.view.CreateItemUtility;
import com.lundincast.presentation.view.TransactionDetailsView;
import com.lundincast.presentation.view.fragment.CategoryListForNewTransactionFragment;
import com.lundincast.presentation.view.fragment.NumericKeyboardFragment;
import com.lundincast.presentation.view.fragment.OverheadDetailsFragment;


import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Activity that allows user to create a new overhead
 */
public class CreateOverheadActivity extends BaseActivity implements HasComponent<OverheadsComponent>,
                                                                    TransactionDetailsView,
                                                                    CreateItemUtility {

    private static final String INTENT_EXTRA_PARAM_OVERHEADS_ID = "com.lundincast.INTENT_PARAM_OVERHEADS_ID";
    private static final String INSTANCE_STATE_PARAM_OVERHEADS_ID = "com.lundincast.STATE_PARAM_OVERHEADS_ID";

    public static Intent getCallingIntent(Context context, int overheadsId) {
        Intent callingIntent = new Intent(context, CreateOverheadActivity.class);
        callingIntent.putExtra(INTENT_EXTRA_PARAM_OVERHEADS_ID, overheadsId);

        return callingIntent;
    }

    @Bind(R.id.toolbar) Toolbar toolbar;
    @Bind(R.id.iv_back) ImageView iv_back;
    @Bind(R.id.iv_delete) ImageView iv_delete;
    @Bind(R.id.tv_transaction_price) TextView tv_transaction_price;
    @Bind(R.id.fab)
    FloatingActionButton fab;

    @Inject SharedPreferences sharedPreferences;
    @Inject CreateOverheadPresenter createOverheadPresenter;

    public int overheadId = -1;
    private OverheadsComponent overheadsComponent;

    private enum FlowStep {Price, Category, Final}
    private FlowStep step;
    private FlowStep previousStep = null;

    private String currPref;
    public String mPrice = "0";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_transaction);
        ButterKnife.bind(this);

        initializeInjector();
        // Initialize currency setting from shared preferences
        currPref = this.sharedPreferences.getString("pref_key_currency", "1");

        initializeActivity(savedInstanceState);
        setUpToolbar();
    }

    @Override protected void onSaveInstanceState(Bundle outState) {
        if (outState != null) {
            outState.putLong(INSTANCE_STATE_PARAM_OVERHEADS_ID, this.overheadId);
        }
        super.onSaveInstanceState(outState);
    }

    private void setUpToolbar() {
        toolbar.showOverflowMenu();
        setSupportActionBar(toolbar);
        if (overheadId > -1) {
            iv_delete.setVisibility(View.VISIBLE);
        }
    }

    private void initializeInjector() {
        this.overheadsComponent = DaggerOverheadsComponent.builder()
                .applicationComponent(getApplicationComponent())
                .overheadsModule(new OverheadsModule())
                .build();
        this.overheadsComponent.inject(this);
    }

    /**
     * Initializes this activity.
     */
    private void initializeActivity(Bundle savedInstanceState) {
        this.createOverheadPresenter.setView(this);
        // Load overhead Id from intent or savedInstanceState if available.
        if (savedInstanceState == null) {
            this.overheadId = getIntent().getIntExtra(INTENT_EXTRA_PARAM_OVERHEADS_ID, -1);
        } else {
            this.overheadId = savedInstanceState.getInt(INTENT_EXTRA_PARAM_OVERHEADS_ID);
        }
        this.createOverheadPresenter.initialize(this.overheadId);

        // Load fragment depending if we are creating or updating an overhead and maintain step tracker
        if (overheadId == -1) {
            addFragment(R.id.fl_transaction_details_container, new NumericKeyboardFragment(), "NumericKeyboardFragment");
            step = FlowStep.Price;
        } else {
            addFragment(R.id.fl_transaction_details_container, new OverheadDetailsFragment(), "TransactionDetailsFragment");
            step = FlowStep.Final;
        }
    }

    @Override
    public void renderTransactionPrice(String price) {
        if (currPref.equals("1")) {
            tv_transaction_price.setText(price + " €");
        } else if (currPref.equals("2")) {
            tv_transaction_price.setText(price + " $");
        } else {
            tv_transaction_price.setText(price + " £");
        }
    }

    @Override
    public void showLoading() {

    }

    @Override
    public void hideLoading() {

    }

    @Override
    public void showMessage(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void closeActivity() {
        this.finish();
    }

    @Override
    public Context getContext() {
        return null;
    }

    @OnClick(R.id.iv_back)
    void onBackClicked() {
        finish();
    }

    @OnClick(R.id.iv_delete)
    void onDeleteIconClicked() {
        // Display dialog to ask user confirmation to delete overheads
        new MaterialDialog.Builder(this)
                .title(R.string.delete_overhead_title_question)
                .content(R.string.delete_overhead_complete_question)
                .positiveText(R.string.delete)
                .negativeText(R.string.cancel)
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        CreateOverheadActivity.this.createOverheadPresenter.deleteOverhead(
                                CreateOverheadActivity.this.overheadId);
                        dialog.dismiss();
                        CreateOverheadActivity.this.finish();
                    }
                })
                .onNegative(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        dialog.dismiss();
                    }
                })
                .show();
    }

    @OnClick(R.id.tv_transaction_price)
    void onPriceClicked() {
        FragmentManager fm = getFragmentManager();
        Fragment keyboardFragment = fm.findFragmentByTag("NumericKeyboardFragment");
        if (keyboardFragment == null) {
            previousStep = step;
            fm.beginTransaction()
                    .replace(R.id.fl_transaction_details_container, new NumericKeyboardFragment(), "NumericKeyboardFragment")
                    .commit();
            step = FlowStep.Price;
        }
    }

    @OnClick(R.id.fab)
    void onFabClicked() {
        switch (step) {
            case Price:
                if (mPrice.equals("")) {
                    mPrice = "0";
                }
                this.createOverheadPresenter.setMPrice(Double.valueOf(mPrice));
                if (previousStep == FlowStep.Category || previousStep == null) {
                    getFragmentManager()
                            .beginTransaction()
                            .replace(R.id.fl_transaction_details_container, new CategoryListForNewTransactionFragment(), "CategoryListForNewTransactionFragment")
                            .commit();
                    step = FlowStep.Category;
                } else {
                    getFragmentManager()
                            .beginTransaction()
                            .replace(R.id.fl_transaction_details_container, new OverheadDetailsFragment(),"OverheadDetailsFragment")
                            .commit();
                    step = FlowStep.Final;

                }
                break;
            case Final:
                this.createOverheadPresenter.saveOverhead();
                finish();
                break;
            default:
                break;
        }
    }

    public void priceTypingListener(View v) {
        TextView tv = (TextView) v;
        String inputValue = tv.getText().toString();
        if (mPrice.equals("0")) {
            mPrice = "";
        }
        if (!mPrice.equals("")) {
            if (inputValue.equals(".")) {
                if (!mPrice.contains(".")) {
                    mPrice += inputValue;
                }
            } else {
                if (!mPrice.contains(".")) {
                    mPrice += inputValue;
                } else {
                    if (mPrice.length() < 3) {
                        mPrice += inputValue;
                    } else {
                        if (mPrice.charAt(mPrice.length() - 3) != '.') {
                            mPrice += inputValue;
                        }
                    }
                }
            }
        } else {
            if (!inputValue.equals("0")) {
                if (inputValue.equals(".")) {
                    mPrice = "0.";
                } else {
                    mPrice = inputValue;
                }
            }
        }
        // Format mPrice with 2 decimal and display it
        String formattedPrice;
        if (!mPrice.contains(".")) {
            if (mPrice.equals("")) {
                formattedPrice = "0.00";
            } else {
                formattedPrice = mPrice + ".00";
            }
        } else {
            if (mPrice.charAt(mPrice.length() - 1) == '.') {
                formattedPrice = mPrice + "00";
            } else if (mPrice.charAt(mPrice.length() - 2) == '.') {
                formattedPrice = mPrice + "0";
            } else {
                formattedPrice = mPrice;
            }
        }
        this.renderTransactionPrice(formattedPrice);
    }

    public void priceBackListener(View v) {
        if (mPrice.length() > 0) {
            mPrice = mPrice.substring(0, mPrice.length() - 1);
            String formattedPrice;
            if (!mPrice.contains(".")) {
                if (!mPrice.equals("")) {
                    formattedPrice = mPrice + ".00";
                } else {
                    formattedPrice = " 0.00";
                }
            } else {
                if (mPrice.charAt(mPrice.length() - 1) == '.') {
                    formattedPrice = mPrice + "00";
                } else if (mPrice.charAt(mPrice.length() - 2) == '.') {
                    formattedPrice = mPrice + "0";
                } else {
                    formattedPrice = mPrice;
                }
            }
            this.renderTransactionPrice(formattedPrice);
        }
    }

    public CategoryModel getCategory() {
        return this.createOverheadPresenter.getMCategory();
    }

    public void onCategorySet(CategoryModel categoryModel) {
        this.createOverheadPresenter.setmCategory(categoryModel);
        getFragmentManager()
                .beginTransaction()
                .replace(R.id.fl_transaction_details_container, new OverheadDetailsFragment(),"OverheadDetailsFragment")
                .commit();
        step = FlowStep.Final;
    }

    public void onCategoryClickedInDetails() {
        getFragmentManager()
                .beginTransaction()
                .replace(R.id.fl_transaction_details_container, new CategoryListForNewTransactionFragment(), "CategoryListForNewTransactionFragment")
                .commit();
        step = FlowStep.Category;
    }

    public short getDayOfMonth() {
        return this.createOverheadPresenter.getmDayOfMonth();
    }

    public void setDayOfMonth(short dayOfMonth) {
        this.createOverheadPresenter.setmDayOfMonth(dayOfMonth);
    }

    public String getComment() {
        return this.createOverheadPresenter.getmComment();
    }

    public void onCommentSet(String comment) {
        this.createOverheadPresenter.setmComment(comment);
    }

    @Override
    public OverheadsComponent getComponent() {
        return overheadsComponent;
    }
}
