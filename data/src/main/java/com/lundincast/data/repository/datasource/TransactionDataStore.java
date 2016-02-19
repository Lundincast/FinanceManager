package com.lundincast.data.repository.datasource;

import com.lundincast.data.entity.TransactionEntity;

import java.io.IOException;
import java.util.List;

import rx.Observable;

/**
 * Interface that represents a data store from where transaction data is retrieved.
 */
public interface TransactionDataStore {

    /**
     * Get an {@link rx.Observable} which will emit a List of {@link TransactionEntity}.
     */
    Observable<List<TransactionEntity>> transactionEntityList() throws IOException;

    /**
     * Get an {@link rx.Observable} which will emit a {@link TransactionEntity} by its id.
     *
     * @param transactionId The id to retrieve transaction data.
     */
    Observable<TransactionEntity> transactionEntityDetails(final int transactionId);
}

