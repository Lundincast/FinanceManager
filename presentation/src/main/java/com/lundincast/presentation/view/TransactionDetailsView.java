package com.lundincast.presentation.view;

import com.lundincast.presentation.model.TransactionModel;

/**
 * Interface representing a View in a model view Presenter (MVP) pattern.
 * In this case it is used as a view representing a {@link TransactionModel} details.
 */
public interface TransactionDetailsView extends LoadDataView {

    /**
     * Render a transaction in the UI
     */
//    void renderTransaction(TransactionModel transaction);

    void renderTransactionPrice(String price);
}
