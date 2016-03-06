package com.lundincast.domain.repository;

import com.lundincast.domain.Transaction;

import java.util.List;

import rx.Observable;

/**
 * Public interface that represents a Repository for getting {@link Transaction} related data.
 */
public interface TransactionRepositoryDomain {

    /**
     * Get an {@link rx.Observable} which will emit a list of {@link Transaction}.
     */
    Observable<List<Transaction>> transactions();

    /**
     * Get an {@link rx.Observable} which will emit a {@link Transaction}.
     *
     * @param transactionId The transaction id used to retrieve user data.
     */
    Observable<Transaction> transaction(final int transactionId);
}
