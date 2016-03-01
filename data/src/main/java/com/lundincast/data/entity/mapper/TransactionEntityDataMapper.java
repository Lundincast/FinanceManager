package com.lundincast.data.entity.mapper;

import com.lundincast.data.entity.TransactionEntity;
import com.lundincast.domain.Transaction;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * Mapper class used to transform {@link TransactionEntity} (in the data layer) to {@link Transaction} in the
 * domain layer.
 */
@Singleton
public class TransactionEntityDataMapper {

    @Inject
    public TransactionEntityDataMapper() {
    }

    /**
     * Transform a {@link TransactionEntity} into an {@link Transaction}.
     *
     * @param transactionEntity Object to be transformed.
     * @return {@link Transaction} if valid {@link TransactionEntity} otherwise null.
     */
    public Transaction transform(TransactionEntity transactionEntity) {
        Transaction transaction = null;
        if (transactionEntity != null) {
            transaction = new Transaction(transactionEntity.getId());
            transaction.setPrice(transactionEntity.getPrice());
            transaction.setCategory(null);
            transaction.setDate(transactionEntity.getDate());
            transaction.setComment(transactionEntity.getComment());
        }

        return transaction;
    }

    /**
     * Transform a List of {@link TransactionEntity} into a Collection of {@link Transaction}.
     *
     * @param transactionEntityCollection Object Collection to be transformed.
     * @return {@link Transaction} if valid {@link TransactionEntity} otherwise null.
     */
    public List<Transaction> transform(Collection<TransactionEntity> transactionEntityCollection) {
        List<Transaction> transactionList = new ArrayList<>();
        Transaction transaction;
        for (TransactionEntity transactionEntity : transactionEntityCollection) {
            transaction = transform(transactionEntity);
            if (transaction != null) {
                transactionList.add(transaction);
            }
        }

        return transactionList;
    }
}
