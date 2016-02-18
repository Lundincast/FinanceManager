package com.lundincast.presentation.view;

import com.lundincast.presentation.model.TransactionModel;

import java.util.Collection;

/**
 * Interface representing a View in a model view Presenter (MVP) pattern.
 * In this case it is used as a view representing a list of {@link TransactionModel}.
 */
public interface TransactionListView extends LoadDataView {

    /**
     * Render a list of transactions in the UI.
     *
     * @param transactionModelCollection The collection of {@link TransactionModel} that will be shown.
     */
    void renderTransactionList(Collection<TransactionModel> transactionModelCollection);

    /**
     * View a {@link TransactionModel} details.
     *
     * @param transactionModel The transaction that will be shown.
     */
    void viewTransaction(TransactionModel transactionModel);
}
