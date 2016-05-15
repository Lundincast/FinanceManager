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
import com.lundincast.presentation.dagger.components.AccountComponent;
import com.lundincast.presentation.model.AccountModel;
import com.lundincast.presentation.presenter.AccountListPresenter;
import com.lundincast.presentation.view.AccountListView;
import com.lundincast.presentation.view.adapter.AccountAdapter;
import com.lundincast.presentation.view.adapter.CategoriesLayoutManager;
import com.lundincast.presentation.view.utilities.SimpleDividerItemDecoration;

import java.util.ArrayList;
import java.util.Collection;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * A {@link Fragment} subclass for listing accounts
 */
public class AccountListFragment extends BaseFragment implements AccountListView {

    /**
     * Interface for listening account list events.
     */
    public interface AccountListListener {
        void onAccountClicked(final AccountModel accountModel);
    }

    @Inject AccountListPresenter accountListPresenter;

    @Bind(R.id.ll_loading) LinearLayout ll_loading;
    @Bind(R.id.rv_categories) RecyclerView rv_accounts;

    private AccountAdapter accountAdapter;
    private CategoriesLayoutManager categoriesLayoutManager;

    private AccountListListener accountListListener;

    public AccountListFragment() {
        super();
    }

    // TODO refactor to new method onAttach(Context context)
    @Override public void onAttach(Activity activity) {
        super.onAttach(activity);
        if (activity instanceof AccountListListener) {
            this.accountListListener = (AccountListListener) activity;
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View fragmentView = inflater.inflate(R.layout.fragment_category_list, container, false);
        ButterKnife.bind(this, fragmentView);
        setupUi();

        return fragmentView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        this.initialize();
        this.loadAccountList();
        this.hideLoading();
    }

    @Override public void onResume() {
        super.onResume();
        this.accountListPresenter.resume();
    }

    @Override public void onPause() {
        super.onPause();
        this.accountListPresenter.pause();
    }

    @Override public void onDestroy() {
        super.onDestroy();
        this.accountListPresenter.destroy();
    }

    @Override public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    @Override
    public void showLoading() {
        this.ll_loading.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideLoading() {
        this.ll_loading.setVisibility(View.GONE);
    }

    /**
     * Show an error message
     *
     * @param message A string representing an error.
     */
    public void showError(String message) {
        this.showToastMessage(message);
    }



    private void initialize() {
        this.getComponent(AccountComponent.class).inject(this);
        this.accountListPresenter.setView(this);
    }

    private void setupUi() {
        this.categoriesLayoutManager = new CategoriesLayoutManager(getActivity());
        this.rv_accounts.setLayoutManager(this.categoriesLayoutManager);
        this.rv_accounts.addItemDecoration(new SimpleDividerItemDecoration(getActivity()));

        this.accountAdapter = new AccountAdapter(getActivity(), new ArrayList<AccountModel>());
        this.accountAdapter.setOnItemClickListener(onItemClickListener);
        this.rv_accounts.setAdapter(this.accountAdapter);
    }

    /**
     * Render a list of accounts in the UI.
     *
     * @param accountModelCollection The collection of {@link AccountModel} that will be shown.
     */
    @Override
    public void renderAccountList(Collection<AccountModel> accountModelCollection) {
        if (accountModelCollection != null) {
            this.accountAdapter.setAccountCollection(accountModelCollection);
        }
    }

    /**
     * View a {@link AccountModel} details.
     *
     * @param accountModel The account that will be shown.
     */
    @Override
    public void viewAccount(AccountModel accountModel) {
        if (this.accountListListener != null) {
            this.accountListListener.onAccountClicked(accountModel);
        }
    }

    /**
     * Loads all accounts.
     */
    private void loadAccountList() {
        this.accountListPresenter.initialize();
    }

    private AccountAdapter.OnItemClickListener onItemClickListener =
            new AccountAdapter.OnItemClickListener() {
                @Override
                public void onAccountItemClicked(AccountModel accountModel) {
                    if (AccountListFragment.this.accountListPresenter != null && accountModel != null) {
                        AccountListFragment.this.accountListPresenter.onAccountClicked(accountModel);
                    }
                }
            };
}
