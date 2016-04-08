package com.lundincast.presentation.view.activity;

import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.lundincast.presentation.R;
import com.lundincast.presentation.dagger.HasComponent;
import com.lundincast.presentation.dagger.components.DaggerTransactionComponent;
import com.lundincast.presentation.dagger.components.TransactionComponent;
import com.lundincast.presentation.dagger.modules.TransactionModule;
import com.lundincast.presentation.model.CategoryModel;
import com.lundincast.presentation.model.TransactionModel;
import com.lundincast.presentation.presenter.CreateTransactionPresenter;
import com.lundincast.presentation.view.TransactionDetailsView;
import com.lundincast.presentation.view.fragment.CategoryListForNewTransactionFragment;
import com.lundincast.presentation.view.fragment.NumericKeyboardFragment;
import com.lundincast.presentation.view.fragment.TransactionDetailsFragment;
import com.melnykov.fab.FloatingActionButton;

import java.util.Date;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.realm.Realm;

/**
 * Activity that allows user to create a new transaction
 */
public class CreateTransactionActivity extends BaseActivity implements HasComponent<TransactionComponent>,
                                                                        TransactionDetailsView {

    private static final String INTENT_EXTRA_PARAM_TRANSACTION_ID = "com.lundincast.INTENT_PARAM_TRANSACTION_ID";
    private static final String INSTANCE_STATE_PARAM_TRANSACTION_ID = "com.lundincast.STATE_PARAM_TRANSACTION_ID";

    public static Intent getCallingIntent(Context context, int transactionId) {
        Intent callingIntent = new Intent(context, CreateTransactionActivity.class);
        callingIntent.putExtra(INTENT_EXTRA_PARAM_TRANSACTION_ID, transactionId);

        return callingIntent;
    }

    @Bind(R.id.toolbar) Toolbar toolbar;
    @Bind(R.id.iv_back) ImageView iv_back;
    @Bind(R.id.iv_delete) ImageView iv_delete;
    @Bind(R.id.tv_transaction_price) TextView tv_transaction_price;
    @Bind(R.id.fab) FloatingActionButton fab;

    @Inject SharedPreferences sharedPreferences;
    @Inject CreateTransactionPresenter createTransactionPresenter;

    public int transactionId = -1;
    private TransactionComponent transactionComponent;

    private enum FlowStep {Price, Category, Details, Final}
    private FlowStep step;
    private FlowStep previousStep = null;

    private String currPref;
    public String mPrice = "0";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_transaction);
        ButterKnife.bind(this);

        setUpToolbar();

        initializeInjector();
        // Initialize currency setting from shared preferences
        currPref = this.sharedPreferences.getString("pref_key_currency", "1");

        initializeActivity(savedInstanceState);
    }

    @Override protected void onSaveInstanceState(Bundle outState) {
        if (outState != null) {
            outState.putLong(INSTANCE_STATE_PARAM_TRANSACTION_ID, this.transactionId);
        }
        super.onSaveInstanceState(outState);
    }

    private void setUpToolbar() {
        toolbar.showOverflowMenu();
        setSupportActionBar(toolbar);
        iv_delete.setVisibility(View.VISIBLE);
    }

    /**
     * Initializes this activity.
     */
    private void initializeActivity(Bundle savedInstanceState) {
        this.createTransactionPresenter.setView(this);
        // Load transaction Id from intent or savedInstanceState if available.
        if (savedInstanceState == null) {
            this.transactionId = getIntent().getIntExtra(INTENT_EXTRA_PARAM_TRANSACTION_ID, -1);
        } else {
            this.transactionId = savedInstanceState.getInt(INTENT_EXTRA_PARAM_TRANSACTION_ID);
        }
        this.createTransactionPresenter.initialize(this.transactionId);

        // Load fragment depending if we are creating or updating a transaction and maintain step tracker
        if (transactionId == -1) {
            addFragment(R.id.fl_transaction_details_container, new NumericKeyboardFragment(), "NumericKeyboardFragment");
            step = FlowStep.Price;
        } else {
            addFragment(R.id.fl_transaction_details_container, new TransactionDetailsFragment(), "TransactionDetailsFragment");
            step = FlowStep.Final;
        }
    }

    private void initializeInjector() {
        this.transactionComponent = DaggerTransactionComponent.builder()
                .applicationComponent(getApplicationComponent())
                .transactionModule(new TransactionModule())
                .build();
        this.transactionComponent.inject(this);
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
    public Context getContext() {
        return null;
    }

    @OnClick(R.id.iv_back)
    void onBackClicked() {
        finish();
    }

    @OnClick(R.id.iv_delete)
    void onDeleteIconClicked() {
        // Display dialog to ask user confirmation to delete category
        new MaterialDialog.Builder(this)
                .title(R.string.delete_transaction_title_question)
                .content(R.string.delete_transaction_complete_question)
                .positiveText(R.string.delete)
                .negativeText(R.string.cancel)
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        CreateTransactionActivity.this.createTransactionPresenter.deleteTransaction(
                                                            CreateTransactionActivity.this.transactionId);
                        dialog.dismiss();
                        CreateTransactionActivity.this.finish();
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
                this.createTransactionPresenter.setmPrice(Double.valueOf(mPrice));
                if (previousStep == FlowStep.Category || previousStep == null) {
                    getFragmentManager()
                            .beginTransaction()
                            .replace(R.id.fl_transaction_details_container, new CategoryListForNewTransactionFragment(), "CategoryListForNewTransactionFragment")
                            .commit();
                    step = FlowStep.Category;
                } else {
                    getFragmentManager()
                            .beginTransaction()
                            .replace(R.id.fl_transaction_details_container, new TransactionDetailsFragment(),"TransactionDetailsFragment")
                            .commit();
                    step = FlowStep.Final;

                }
                break;
            case Final:
                this.createTransactionPresenter.saveTransaction();
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
        String formattedPrice = null;
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
            String formattedPrice = null;
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
        return this.createTransactionPresenter.getmCategory();
    }

    public void onCategorySet(CategoryModel categoryModel) {
        this.createTransactionPresenter.setmCategory(categoryModel);
        getFragmentManager()
                .beginTransaction()
                .replace(R.id.fl_transaction_details_container, new TransactionDetailsFragment(),"TransactionDetailsFragment")
                .commit();
        step = FlowStep.Final;
    }

    public void onCategoryClickedinDetails() {
        getFragmentManager()
                .beginTransaction()
                .replace(R.id.fl_transaction_details_container, new CategoryListForNewTransactionFragment(), "CategoryListForNewTransactionFragment")
                .commit();
        step = FlowStep.Category;
    }

    public Date getDate() {
        return this.createTransactionPresenter.getmDate();
    }

    public void onDateSet(Date date) {
        this.createTransactionPresenter.setmDate(date);
    }

    public String getComment() {
        return this.createTransactionPresenter.getmComment();
    }

    public void onCommentSet(String comment) {
        this.createTransactionPresenter.setmComment(comment);
    }

    public boolean isPending() {
        return this.createTransactionPresenter.ismPending();
    }

    public void setPending(boolean isPending) {
        this.createTransactionPresenter.setmPending(isPending);
    }

    public int getDueToOrBy() {
        return this.createTransactionPresenter.getmDueToOrBy();
    }

    public void setDueToOrBy(int dueToOrBy) {
        this.createTransactionPresenter.setmDueToOrBy(dueToOrBy);
    }

    public String getDueName() {
        return this.createTransactionPresenter.getmDueName();
    }

    public void setDueName(String dueName) {
        this.createTransactionPresenter.setmDueName(dueName);
    }

    @Override
    public TransactionComponent getComponent() {
        return transactionComponent;
    }
}
