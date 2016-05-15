package com.lundincast.presentation.data;

import com.lundincast.presentation.dagger.PerActivity;
import com.lundincast.presentation.data.datasource.AccountDataStore;
import com.lundincast.presentation.model.AccountModel;

import java.io.IOException;
import java.util.List;

import javax.inject.Inject;

/**
 * {@link AccountRepository} implementation for retrieving account data.
 */
@PerActivity
public class AccountRepositoryImpl implements AccountRepository {

    private final AccountDataStore accountDataStore;

    /**
     * Constructs an {@link AccountRepository}.
     *
     * @param accountDataStore A factory to construct different data source implementations.
     */
    @Inject
    public AccountRepositoryImpl(AccountDataStore accountDataStore) {
        this.accountDataStore = accountDataStore;
    }

    @Override
    public List<AccountModel> accounts() {
        List<AccountModel> accountList = null;
        try {
            accountList = accountDataStore.accountList();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return accountList;
    }

    @Override
    public void saveAccount(AccountModel accountModel) {
        this.accountDataStore.saveAccount(accountModel);
    }

    @Override
    public void deleteAccount(long accountId) {
        this.accountDataStore.deleteAccount(accountId);
    }
}
