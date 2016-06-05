package com.lundincast.presentation.dagger.modules;

import android.content.SharedPreferences;

import com.lundincast.presentation.data.AccountRepository;
import com.lundincast.presentation.data.AccountRepositoryImpl;
import com.lundincast.presentation.data.datasource.AccountDataStore;
import com.lundincast.presentation.data.datasource.DiskAccountDataStore;
import com.lundincast.presentation.presenter.AccountListPresenter;
import com.lundincast.presentation.presenter.CreateAccountPresenter;

import dagger.Module;
import dagger.Provides;
import io.realm.Realm;

/**
 * Dagger module that provides account related collaborators.
 */
@Module
public class AccountModule {

    public AccountModule() {}

    @Provides
    AccountListPresenter provideAccountListPresenter(AccountRepository accountRepository) {
        return new AccountListPresenter(accountRepository);
    }

    @Provides
    CreateAccountPresenter provideCreateAccountPresenter(SharedPreferences sharedPreferences,
                                                         AccountRepository accountRepository) {
        return new CreateAccountPresenter(sharedPreferences, accountRepository);
    }

    @Provides
    AccountDataStore provideAccountDataStore() {
        return new DiskAccountDataStore(Realm.getDefaultInstance());
    }

    @Provides
    AccountRepository provideAccountRepository(AccountRepositoryImpl accountDataRepository) {
        return accountDataRepository;
    }
}
