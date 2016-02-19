package com.lundincast.data.repository;

import com.lundincast.data.repository.datasource.TransactionDataStore;
import com.lundincast.domain.Transaction;
import com.lundincast.domain.repository.TransactionRepository;

import java.io.IOException;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import rx.Observable;

/**
 * {@link TransactionRepository} for retrieving transaction data.
 */
@Singleton
public class TransactionDataRepository implements TransactionRepository {

    private final TransactionDataStore transactionDataStore;

    /**
     * Constructs a {@link TransactionRepository}.
     *
     * @param transactionDataStore A factory to construct different data source implementations.
     */
    @Inject
    public TransactionDataRepository(TransactionDataStore transactionDataStore) {
        this.transactionDataStore = transactionDataStore;
    }

    @Override
    public Observable<List<Transaction>> transactions() {
        try {
            transactionDataStore.transactionEntityList();
        } catch (IOException e) {
            e.printStackTrace();
        }


        return null;
    }

    @Override
    public Observable<Transaction> transaction(int transactionId) {
        return null;
    }
}
