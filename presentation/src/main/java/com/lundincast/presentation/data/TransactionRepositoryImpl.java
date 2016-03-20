package com.lundincast.presentation.data;

import com.lundincast.domain.Transaction;
import com.lundincast.domain.repository.TransactionRepositoryDomain;
import com.lundincast.presentation.dagger.PerActivity;
import com.lundincast.presentation.data.datasource.TransactionDataStore;
import com.lundincast.presentation.model.TransactionModel;

import java.io.IOException;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import rx.Observable;

/**
 * {@link TransactionRepositoryDomain} for retrieving transaction data.
 */
@PerActivity
public class TransactionRepositoryImpl implements TransactionRepository {

    private final TransactionDataStore transactionDataStore;

    /**
     * Constructs a {@link TransactionRepositoryDomain}.
     *
     * @param transactionDataStore A factory to construct different data source implementations.
     */
    @Inject
    public TransactionRepositoryImpl(TransactionDataStore transactionDataStore) {
        this.transactionDataStore = transactionDataStore;
    }

    @Override
    public List<TransactionModel> transactions() {
        List<TransactionModel> transactionList = null;
        try {
            transactionList = transactionDataStore.transactionEntityList();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return transactionList;
    }

    @Override
    public Observable<Transaction> transaction(int transactionId) {
        return null;
    }

    @Override
    public void saveTransaction(TransactionModel transactionModel) {
        this.transactionDataStore.saveTransaction(transactionModel);
    }
}
