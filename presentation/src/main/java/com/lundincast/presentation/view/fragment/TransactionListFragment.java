package com.lundincast.presentation.view.fragment;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.lundincast.presentation.R;
import com.lundincast.presentation.dagger.components.TransactionComponent;
import com.lundincast.presentation.model.TransactionModel;
import com.lundincast.presentation.presenter.TransactionListPresenter;
import com.lundincast.presentation.view.TransactionListView;
import com.lundincast.presentation.view.adapter.TransactionsAdapter;
import com.lundincast.presentation.view.adapter.TransactionsLayoutManager;
import com.lundincast.presentation.view.utilities.SimpleDividerItemDecoration;

import java.util.ArrayList;
import java.util.Collection;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * A {@link Fragment} subclass for "List" tab in Main Activity
 */
public class TransactionListFragment extends BaseFragment implements TransactionListView {

    /**
     * Interface for listening transaction list events.
     */
    public interface TransactionListListener {
        void onTransactionClicked(final TransactionModel transactionModel);
    }

    @Inject TransactionListPresenter transactionListPresenter;

    @Bind(R.id.ll_loading) LinearLayout ll_loading;
    @Bind(R.id.rv_transactions) RecyclerView rv_transactions;

    private TransactionsAdapter transactionsAdapter;
    private TransactionsLayoutManager transactionsLayoutManager;

    private TransactionListListener transactionListListener;

    public TransactionListFragment() {
        super();
    }

    // TODO refactor to new method onAttach(Context context)
    @Override public void onAttach(Activity activity) {
        super.onAttach(activity);
        if (activity instanceof TransactionListListener) {
            this.transactionListListener = (TransactionListListener) activity;
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View fragmentView = inflater.inflate(R.layout.fragment_transaction_list, container, false);
        ButterKnife.bind(this, fragmentView);
        setupUi();

        return fragmentView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        this.initialize();
        this.loadTransactionList();
    }

    @Override public void onResume() {
        super.onResume();
        this.transactionListPresenter.resume();
    }

    @Override public void onPause() {
        super.onPause();
        this.transactionListPresenter.pause();
    }

    @Override public void onDestroy() {
        super.onDestroy();
        this.transactionListPresenter.destroy();
    }

    @Override public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    /**
     * Show a view with a progress bar indicating a loading process.
     */
    @Override
    public void showLoading() {
        this.ll_loading.setVisibility(View.VISIBLE);
    }

    /**
     * Hide a loading view.
     */
    @Override
    public void hideLoading() {
        this.ll_loading.setVisibility(View.GONE);
    }

    private void initialize() {
        this.getComponent(TransactionComponent.class).inject(this);
        this.transactionListPresenter.setView(this);
    }

    private void setupUi() {
        this.transactionsLayoutManager = new TransactionsLayoutManager(getActivity());
        this.rv_transactions.setLayoutManager(this.transactionsLayoutManager);
        this.rv_transactions.addItemDecoration(new SimpleDividerItemDecoration(getActivity()));

        this.transactionsAdapter = new TransactionsAdapter(getActivity(), new ArrayList<TransactionModel>());
        this.transactionsAdapter.setOnItemClickListener(onItemClickListener);
        this.rv_transactions.setAdapter(transactionsAdapter);
    }

    /**
     * Render a list of transactions in the UI.
     *
     * @param transactionModelCollection The collection of {@link TransactionModel} that will be shown.
     */
    @Override
    public void renderTransactionList(Collection<TransactionModel> transactionModelCollection) {
        if (transactionModelCollection != null) {
            this.transactionsAdapter.setTransactionsCollection(transactionModelCollection);
        }
    }

    /**
     * View a {@link TransactionModel} details.
     *
     * @param transactionModel The transaction that will be shown.
     */
    @Override
    public void viewTransaction(TransactionModel transactionModel) {
        if (this.transactionListListener != null) {
            this.transactionListListener.onTransactionClicked(transactionModel);
        }
    }

    /**
     * Loads transactions.
     */
    private void loadTransactionList() {
        this.transactionListPresenter.initialize();
    }

    private TransactionsAdapter.OnItemClickListener onItemClickListener =
            new TransactionsAdapter.OnItemClickListener() {
                @Override
                public void onTransactionItemClicked(TransactionModel transactionModel) {
                    if (TransactionListFragment.this.transactionListPresenter != null && transactionModel != null) {
                        TransactionListFragment.this.transactionListPresenter.onTransactionClicked(transactionModel);
                    }
                }
            };
}
