package com.lundincast.presentation.data;

import com.lundincast.domain.Transaction;
import com.lundincast.presentation.model.TransactionModel;

import java.util.List;

import rx.Observable;

/**
 * Public interface that represents a Repository for getting {@link TransactionModel} related data.
 */
public interface TransactionRepository {

    /**
     * Get a {@link List} of {@link TransactionModel}.
     */
    List<TransactionModel> transactions();

    /**
     * Get an {@link rx.Observable} which will emit a {@link Transaction}.
     *
     * @param transactionId The transaction id used to retrieve user data.
     */
    Observable<Transaction> transaction(final int transactionId);

    /**
     * Save a {@link TransactionModel} in database
     */
    void saveTransaction(final TransactionModel transactionModel);
}