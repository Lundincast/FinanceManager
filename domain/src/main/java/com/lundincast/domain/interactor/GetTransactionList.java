package com.lundincast.domain.interactor;

import com.lundincast.domain.Transaction;
import com.lundincast.domain.executor.PostExecutionThread;
import com.lundincast.domain.executor.ThreadExecutor;
import com.lundincast.domain.repository.TransactionRepository;

import javax.inject.Inject;

import rx.Observable;

/**
 * This class is an implementation of {@link UseCase} that represents a use case for
 * retrieving a collection of all {@link Transaction}.
 */
public class GetTransactionList extends UseCase {

    private final TransactionRepository transactionRepository;

    @Inject
    public GetTransactionList(ThreadExecutor threadExecutor,
                              PostExecutionThread postExecutionThread,
                              TransactionRepository transactionRepository) {
        super(threadExecutor, postExecutionThread);
        this.transactionRepository = transactionRepository;
    }

    @Override
    protected Observable buildUseCaseObservable() {
        return this.transactionRepository.transactions();
    }
}
