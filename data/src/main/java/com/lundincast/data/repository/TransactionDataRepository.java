package com.lundincast.data.repository;

import com.lundincast.data.entity.TransactionEntity;
import com.lundincast.data.entity.mapper.TransactionEntityDataMapper;
import com.lundincast.data.repository.datasource.TransactionDataStore;
import com.lundincast.domain.Transaction;
import com.lundincast.domain.repository.TransactionRepository;

import java.io.IOException;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import io.realm.RealmResults;
import io.realm.rx.RxObservableFactory;
import rx.Observable;

/**
 * {@link TransactionRepository} for retrieving transaction data.
 */
@Singleton
public class TransactionDataRepository implements TransactionRepository {

    private final TransactionDataStore transactionDataStore;
    private final TransactionEntityDataMapper transactionEntityDataMapper;

    /**
     * Constructs a {@link TransactionRepository}.
     *
     * @param transactionDataStore A factory to construct different data source implementations.
     */
    @Inject
    public TransactionDataRepository(TransactionDataStore transactionDataStore,
                                     TransactionEntityDataMapper transactionEntityDataMapper) {
        this.transactionDataStore = transactionDataStore;
        this.transactionEntityDataMapper = transactionEntityDataMapper;
    }

    @Override
    public Observable<List<Transaction>> transactions() {
        Observable<List<Transaction>> transactionList = null;
        try {
            transactionList = transactionDataStore.transactionEntityList()
                    .map(transactionEntities -> this.transactionEntityDataMapper.transform(transactionEntities));
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
