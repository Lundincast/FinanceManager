package com.lundincast.presentation.view.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.lundincast.presentation.R;
import com.lundincast.presentation.dagger.HasComponent;
import com.lundincast.presentation.dagger.components.DaggerTransactionComponent;
import com.lundincast.presentation.dagger.components.TransactionComponent;
import com.lundincast.presentation.presenter.CreateTransactionPresenter;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Activity that allows user to create a new transaction
 */
public class CreateTransactionActivity extends BaseActivity implements HasComponent<TransactionComponent> {

    public static Intent getCallingIntent(Context context) {
        return new Intent(context, CreateTransactionActivity.class);
    }

    @Bind(R.id.toolbar) Toolbar toolbar;
    @Bind(R.id.iv_back) ImageView iv_back;
    @Bind(R.id.tv_transaction_price) TextView tv_transaction_price;

    @Inject public CreateTransactionPresenter createTransactionPresenter;

    private TransactionComponent transactionComponent;

    public String price = "0.00";
    private String currPref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_transaction);
        ButterKnife.bind(this);

        setUpToolbar();

        // Initialize currency setting from shared preferences
        currPref = this.sharedPreferences.getString("pref_key_currency", "1");

        initializeInjector();
    }

    private void setUpToolbar() {
        toolbar.showOverflowMenu();
        setSupportActionBar(toolbar);
    }

    private void initializeInjector() {
        this.transactionComponent = DaggerTransactionComponent.builder()
                .applicationComponent(getApplicationComponent())
                .activityModule(getActivityModule())
                .build();
    }

    @OnClick(R.id.iv_back)
    void onBackClicked() {
        finish();
    }

    public void priceTypingListener(View v) {
        TextView tv = (TextView) v;
        String inputValue = tv.getText().toString();
        if (price.equals("0")) {
            price = "";
        }
        if (!price.equals("")) {
            if (inputValue.equals(".")) {
                if (!price.contains(".")) {
                    price += inputValue;
                }
            } else {
                if (!price.contains(".")) {
                    price += inputValue;
                } else {
                    if (price.length() < 3) {
                        price += inputValue;
                    } else {
                        if (price.charAt(price.length() - 3) != '.') {
                            price += inputValue;
                        }
                    }
                }
            }
        } else {
            if (!inputValue.equals("0")) {
                if (inputValue.equals(".")) {
                    price = "0.";
                } else {
                    price = inputValue;
                }
            }
        }
        // Format price with 2 decimal and display it
        String formattedPrice = null;
        if (!price.contains(".")) {
            if (price.equals("")) {
                formattedPrice = "0.00";
            } else {
                formattedPrice = price + ".00";
            }
        } else {
            if (price.charAt(price.length() - 1) == '.') {
                formattedPrice = price + "00";
            } else if (price.charAt(price.length() - 2) == '.') {
                formattedPrice = price + "0";
            } else {
                formattedPrice = price;
            }
        }
        if (currPref.equals("2")) {
            formattedPrice += " $";
        } else {
            formattedPrice += " €";
        }
        tv_transaction_price.setText(formattedPrice);
    }

    public void priceBackListener(View v) {
        if (price.length() > 0) {
            price = price.substring(0, price.length() - 1);
            String formattedPrice = null;
            if (!price.contains(".")) {
                if (!price.equals("")) {
                    formattedPrice = price + ".00";
                } else {
                    formattedPrice = " 0.00";
                }
            } else {
                if (price.charAt(price.length() - 1) == '.') {
                    formattedPrice = price + "00";
                } else if (price.charAt(price.length() - 2) == '.') {
                    formattedPrice = price + "0";
                } else {
                    formattedPrice = price;
                }
            }
            if (currPref.equals("2")) {
                formattedPrice += " $";
            } else {
                formattedPrice += " €";
            }
            tv_transaction_price.setText(formattedPrice);
        }
    }

    @Override
    public TransactionComponent getComponent() {
        return transactionComponent;
    }
}
