package com.lundincast.presentation.presenter;

import com.lundincast.presentation.data.TransactionRepository;
import com.lundincast.presentation.model.TransactionModel;

import javax.inject.Inject;

/**
 * {@link Presenter} that controls communication between views and models of the presentation
 * layer.
 */
public class CreateTransactionPresenter implements Presenter {

    private final TransactionRepository transactionRepository;

    @Inject
    public CreateTransactionPresenter(TransactionRepository transactionRepository) {
        this.transactionRepository = transactionRepository;
    }

    @Override
    public void resume() {

    }

    @Override
    public void pause() {

    }

    @Override
    public void destroy() {

    }

    public void saveTransaction(TransactionModel transactionModel) {
        this.transactionRepository.saveTransaction(transactionModel);
    }
}
