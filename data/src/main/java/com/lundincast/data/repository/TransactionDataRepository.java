package com.lundincast.data.repository;

import com.lundincast.domain.Transaction;
import com.lundincast.domain.repository.TransactionRepository;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import rx.Observable;

/**
 * {@link TransactionRepository} for retrieving transaction data.
 */
@Singleton
public class TransactionDataRepository implements TransactionRepository {

    @Inject
    public TransactionDataRepository() {
        super();
    }

    @Override
    public Observable<List<Transaction>> transactions() {
        return null;
    }

    @Override
    public Observable<Transaction> transaction(int transactionId) {
        return null;
    }
}
