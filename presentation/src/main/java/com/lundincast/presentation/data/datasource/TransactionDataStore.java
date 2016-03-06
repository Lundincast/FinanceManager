package com.lundincast.presentation.data.datasource;

import com.lundincast.data.entity.TransactionEntity;
import com.lundincast.presentation.model.TransactionModel;

import java.io.IOException;
import java.util.List;

import io.realm.RealmResults;
import rx.Observable;

/**
 * Interface that represents a data store from where transaction data is retrieved.
 */
public interface TransactionDataStore {

    /**
     * Get an {@link rx.Observable} which will emit a List of {@link TransactionEntity}.
     */
    List<TransactionModel> transactionEntityList() throws IOException;

    /**
     * Get an {@link rx.Observable} which will emit a {@link TransactionEntity} by its id.
     *
     * @param transactionId The id to retrieve transaction data.
     */
    Observable<TransactionEntity> transactionEntityDetails(final int transactionId);
}