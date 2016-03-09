package com.lundincast.presentation.view.fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.lundincast.presentation.R;
import com.lundincast.presentation.dagger.components.TransactionComponent;
import com.lundincast.presentation.model.TransactionModel;
import com.lundincast.presentation.presenter.CreateTransactionPresenter;
import com.lundincast.presentation.view.activity.BaseActivity;
import com.lundincast.presentation.view.activity.CreateTransactionActivity;
import com.melnykov.fab.FloatingActionButton;

import java.util.Date;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * A {@link Fragment} subclass for creating new transaction
 */
public class CreateTransactionFragment extends BaseFragment {

    @Bind(R.id.tv_transaction_price) TextView tv_transaction_price;
    @Bind(R.id.fl_transaction_details_container) FrameLayout fl_transaction_details_container;
    @Bind(R.id.fab) FloatingActionButton fab;

    @Inject CreateTransactionPresenter createTransactionPresenter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View fragmentView = inflater.inflate(R.layout.fragment_create_transaction, container, false);
        ButterKnife.bind(this, fragmentView);
        setUpUi();

        return fragmentView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        this.initialize();
    }

    private void setUpUi() {

        // Initialize price textView
        tv_transaction_price.setText("0,00 €");

        // Load numeric keyboard fragment in placeholder
        ((BaseActivity) getActivity()).addFragment(
                R.id.fl_transaction_details_container, new NumericKeyboardFragment());
    }

    private void initialize() {
        this.getComponent(TransactionComponent.class).inject(this);
    }

    @OnClick(R.id.fab)
    void onFabClicked() {
        TransactionModel transactionModel = new TransactionModel();
        transactionModel.setPrice(Double.valueOf(((CreateTransactionActivity) getActivity()).price));
        transactionModel.setDate(new Date());
        transactionModel.setCategory(null);
        transactionModel.setComment("Test aller!!!");
        this.createTransactionPresenter.saveTransaction(transactionModel);
        getActivity().finish();
    }


}
