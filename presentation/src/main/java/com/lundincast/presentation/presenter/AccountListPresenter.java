package com.lundincast.presentation.presenter;

import com.lundincast.presentation.dagger.PerActivity;
import com.lundincast.presentation.data.AccountRepository;
import com.lundincast.presentation.model.AccountModel;
import com.lundincast.presentation.view.fragment.AccountListFragment;

import java.util.List;

import javax.inject.Inject;

import io.realm.Realm;
import io.realm.RealmChangeListener;
import io.realm.RealmResults;

/**
 * {@link Presenter} that controls communication between views and models of the presentation
 * layer.
 */
@PerActivity
public class AccountListPresenter implements Presenter {

    private Realm realm;
    private RealmResults<AccountModel> accountList;
    private RealmChangeListener accountListResultListener = new RealmChangeListener() {
        @Override
        public void onChange() {
            AccountListPresenter.this.showAccountCollectionInView(accountList);
        }
    };

    private final AccountRepository accountRepository;

    private AccountListFragment viewListView;

    @Inject
    public AccountListPresenter(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
        this.realm = Realm.getDefaultInstance();
    }

    /**
     * Setter method to reference corresponding view (AccountListFragment in this case)
     *
     * @param view
     */
    public void setView(AccountListFragment view) {
        this.viewListView = view;
    }

    @Override
    public void resume() {
        accountList.addChangeListener(accountListResultListener);
    }

    @Override
    public void pause() {

    }

    @Override
    public void destroy() {
        accountList.removeChangeListener(accountListResultListener);
    }

    /**
     * Initializes the presenter by injecting dependencies and retrieving the account list.
     */
    public void initialize() {
        this.loadAccountList();
    }

    /**
     * Loads accounts
     */
    private void loadAccountList() {
        this.showViewLoading();
        this.getAccountList();
    }

    public void onAccountClicked(AccountModel accountModel) {
        this.viewListView.viewAccount(accountModel);
    }

    private void showViewLoading() {
        this.viewListView.showLoading();
    }

    private void hideViewLoading() {
        this.viewListView.hideLoading();
    }

    private void showAccountCollectionInView(List<AccountModel> accountList) {
        this.hideViewLoading();
        this.viewListView.renderAccountList(accountList);
    }

    private void getAccountList() {
        accountList = realm.where(AccountModel.class).findAll();
        showAccountCollectionInView(accountList);
    }
}
