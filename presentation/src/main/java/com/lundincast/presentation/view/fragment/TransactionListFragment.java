package com.lundincast.presentation.view.fragment;

import android.app.Activity;
import android.app.Fragment;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;

import com.afollestad.materialdialogs.MaterialDialog;
import com.lundincast.presentation.R;
import com.lundincast.presentation.dagger.components.TransactionComponent;
import com.lundincast.presentation.model.TransactionModel;
import com.lundincast.presentation.presenter.TransactionListPresenter;
import com.lundincast.presentation.view.TransactionListView;
import com.lundincast.presentation.view.activity.MainActivity;
import com.lundincast.presentation.view.adapter.TransactionsAdapter;
import com.lundincast.presentation.view.adapter.TransactionsLayoutManager;
import com.lundincast.presentation.view.utilities.SimpleDividerItemDecoration;
import com.melnykov.fab.FloatingActionButton;

import java.util.ArrayList;
import java.util.Collection;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

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
    @Bind(R.id.ll_empty_list) LinearLayout ll_empty_list;
    @Bind(R.id.rv_transactions) RecyclerView rv_transactions;
    @Bind(R.id.fab) FloatingActionButton fab;

    private TransactionsAdapter transactionsAdapter;
    private TransactionsLayoutManager transactionsLayoutManager;

    private TransactionListListener transactionListListener;
    private SharedPreferences.OnSharedPreferenceChangeListener preferencesListener;

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

        preferencesListener = new SharedPreferences.OnSharedPreferenceChangeListener() {
            @Override
            public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
                if (key.equals("pref_key_currency")) {
                    TransactionListFragment.this.transactionsAdapter.setCurrencyPref(getActivity());
                    TransactionListFragment.this.transactionsAdapter.notifyDataSetChanged();
                }
            }
        };
        ((MainActivity) getActivity()).sharedPreferences.registerOnSharedPreferenceChangeListener(preferencesListener);
    }

    @Override public void onResume() {
        super.onResume();
        this.transactionListPresenter.resume();

        this.fab.setVisibility(View.VISIBLE);
    }

    @Override public void onPause() {
        super.onPause();
        this.transactionListPresenter.pause();

    }

    @Override public void onDestroy() {
        super.onDestroy();
        this.transactionListPresenter.destroy();

        ((MainActivity) getActivity()).sharedPreferences.unregisterOnSharedPreferenceChangeListener(preferencesListener);
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

    @Override
    public void showEmptyListMessage() {
        this.ll_empty_list.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideEmptyListMessage() {
        this.ll_empty_list.setVisibility(View.GONE);
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

        // Attach fab to RecyclerView
        fab.attachToRecyclerView(this.rv_transactions);

        // Set listener on filter icon click event in Main Activity
        ImageView filterIcon = (ImageView) getActivity().findViewById(R.id.iv_filter_list_icon);
        if (filterIcon != null) {
            filterIcon.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (TransactionListFragment.this.transactionListPresenter != null) {
                        TransactionListFragment.this.transactionListPresenter.buildCategoryAdapterForFilterDialog();
                    }
                }
            });
        }
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

    public void showFilterTransactionDialog(ListAdapter adapter) {
        new MaterialDialog.Builder(getActivity())
                .title(R.string.filter_by_category)
                .titleColorRes(R.color.colorPrimary)
                .adapter(adapter, new MaterialDialog.ListCallback() {
                    @Override
                    public void onSelection(MaterialDialog dialog, View itemView, int which, CharSequence text) {
                        if (TransactionListFragment.this.transactionListPresenter != null) {
                            TransactionListFragment.this.transactionListPresenter.filterListByCategory(text);
                        }
                        dialog.dismiss();
                    }
                })
                .show();
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

    @OnClick(R.id.fab)
    void onFabClicked() {
        ((MainActivity) getActivity()).onFabClicked();
    }
}
