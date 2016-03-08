package com.lundincast.presentation.dagger.modules;

import com.lundincast.domain.interactor.GetTransactionList;
import com.lundincast.domain.interactor.UseCase;
import com.lundincast.presentation.dagger.PerActivity;
import com.lundincast.presentation.data.TransactionRepository;
import com.lundincast.presentation.presenter.CreateTransactionPresenter;

import javax.inject.Named;

import dagger.Module;
import dagger.Provides;

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

    @Provides
    CreateTransactionPresenter provideCreateTransactionPresenter(TransactionRepository transactionRepository) {
        return new CreateTransactionPresenter(transactionRepository);
    }
}
