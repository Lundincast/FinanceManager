package com.lundincast.presentation.data;

import com.lundincast.domain.Transaction;
import com.lundincast.presentation.model.TransactionModel;

import java.util.List;

import rx.Observable;

/**
 * Public interface that represents a Repository for getting {@link Transaction} related data.
 */
public interface TransactionRepository {

    /**
     * Get an {@link rx.Observable} which will emit a list of {@link Transaction}.
     */
    List<TransactionModel> transactions();

    /**
     * Get an {@link rx.Observable} which will emit a {@link Transaction}.
     *
     * @param transactionId The transaction id used to retrieve user data.
     */
    Observable<Transaction> transaction(final int transactionId);
}