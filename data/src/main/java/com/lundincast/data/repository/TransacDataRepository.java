package com.lundincast.data.repository;

import com.lundincast.data.entity.TransactionEntity;
import com.lundincast.data.entity.mapper.TransactionEntityDataMapper;
import com.lundincast.data.repository.datasource.TransacDataStore;
import com.lundincast.domain.Transaction;
import com.lundincast.domain.repository.TransactionRepositoryDomain;

import java.io.IOException;
import java.util.Collection;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import rx.Observable;

/**
 * {@link TransactionRepositoryDomain} for retrieving transaction data.
 */
@Singleton
public class TransacDataRepository implements TransactionRepositoryDomain {

    private final TransacDataStore transactionDataStore;
    private final TransactionEntityDataMapper transactionEntityDataMapper;

    /**
     * Constructs a {@link TransactionRepositoryDomain}.
     *
     * @param transactionDataStore A factory to construct different data source implementations.
     */
    @Inject
    public TransacDataRepository(TransacDataStore transactionDataStore,
                                 TransactionEntityDataMapper transactionEntityDataMapper) {
        this.transactionDataStore = transactionDataStore;
        this.transactionEntityDataMapper = transactionEntityDataMapper;
    }

    @Override
    public Observable<List<Transaction>> transactions() {
        Observable<List<Transaction>> transactionList = null;
        try {
            transactionList = transactionDataStore.transactionEntityList()
                    .map(transactionEntities -> this.transactionEntityDataMapper.transform((Collection<TransactionEntity>) transactionEntities));
        } catch (IOException e) {
            e.printStackTrace();
        }

        return transactionList;
    }

    @Override
    public Observable<Transaction> transaction(int transactionId) {
        return null;
    }
}
