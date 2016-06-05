package com.lundincast.presentation.dagger.modules;

import com.lundincast.domain.interactor.GetTransactionList;
import com.lundincast.domain.interactor.UseCase;
import com.lundincast.presentation.dagger.PerActivity;
import com.lundincast.presentation.data.AccountRepository;
import com.lundincast.presentation.data.TransactionRepository;
import com.lundincast.presentation.data.TransactionRepositoryImpl;
import com.lundincast.presentation.data.datasource.DiskTransactionDataStore;
import com.lundincast.presentation.data.datasource.TransactionDataStore;
import com.lundincast.presentation.presenter.CreateTransactionPresenter;

import javax.inject.Named;

import dagger.Module;
import dagger.Provides;
import io.realm.Realm;

/**
 * Dagger module that provides transaction related collaborators.
 */
@Module
public class TransactionModule {

    private int transactionId = -1;

    public TransactionModule() {}

    public TransactionModule(int transactionId) {
        this.transactionId = transactionId;
    }

    @Provides @PerActivity @Named("transactionList")
    UseCase provideGetTransactionListUseCase(GetTransactionList getTransactionList) {
        return getTransactionList;
    }

    @Provides @PerActivity
    TransactionDataStore provideTransactionDataStore() {
        return new DiskTransactionDataStore(Realm.getDefaultInstance());
    }

    @Provides @PerActivity
    TransactionRepository provideTransactionRepository(TransactionDataStore transactionDataStore) {
        return new TransactionRepositoryImpl(transactionDataStore);
    }

    @Provides @PerActivity
    CreateTransactionPresenter provideCreateTransactionPresenter(
            TransactionRepository transactionRepository, AccountRepository accountRepository) {
        return new CreateTransactionPresenter(transactionRepository, accountRepository);
    }
}
