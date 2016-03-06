package com.lundincast.presentation.mapper;

import com.lundincast.domain.Transaction;
import com.lundincast.presentation.model.TransactionModel;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

import javax.inject.Inject;

/**
 * Mapper class used to transform {@link Transaction} (in the domain layer) to {@link TransactionModel} in the
 * presentation layer.
 */
public class TransactionModelDataMapper {

    @Inject
    public TransactionModelDataMapper() {}

    /**
     * Transform a {@link Transaction} into an {@link TransactionModel}.
     *
     * @param transaction Object to be transformed.
     * @return {@link TransactionModel}.
     */
    public TransactionModel transform(Transaction transaction) {

        if (transaction == null) {
            throw new IllegalArgumentException("Cannot transform a null value");
        }
        TransactionModel transactionModel = new TransactionModel(transaction.getTransactionId());
        transactionModel.setPrice(transaction.getPrice());
        transactionModel.setCategory(null);
        transactionModel.setDate(transaction.getDate());
        transactionModel.setComment(transaction.getComment());

        return transactionModel;
    }

    /**
     * Transform a Collection of {@link Transaction} into a Collection of {@link TransactionModel}.
     *
     * @param transactionCollection Objects to be transformed.
     * @return List of {@link TransactionModel}.
     */
    public Collection<TransactionModel> transform(Collection<Transaction> transactionCollection) {

        Collection<TransactionModel> transactionModelCollection;

        if (transactionCollection != null && !transactionCollection.isEmpty()) {
            transactionModelCollection = new ArrayList<>();
            for (Transaction transaction : transactionCollection) {
                transactionModelCollection.add(transform(transaction));
            }
        } else {
            transactionModelCollection = Collections.emptyList();
        }

        return transactionModelCollection;
    }
}
