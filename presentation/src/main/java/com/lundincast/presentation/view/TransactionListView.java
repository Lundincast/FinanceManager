package com.lundincast.presentation.view;

import android.widget.ListAdapter;

import com.lundincast.presentation.model.AccountModel;
import com.lundincast.presentation.model.CategoryModel;
import com.lundincast.presentation.model.TransactionModel;

import java.util.Collection;
import java.util.List;

import io.realm.RealmObject;

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
     * Display a message when empty list
     *
     */
    void showEmptyListMessage();

    /**
     * Hide the empty list message
     */
    void hideEmptyListMessage();

    /**
     * View a {@link TransactionModel} details.
     *
     * @param transactionModel The transaction that will be shown.
     */
    void viewTransaction(TransactionModel transactionModel);

    /**
     * Render a dialog to filter a transaction list by category
     *
     * @param list The list that provides the entries
     */
    void showFilterTransactionDialog(List<CategoryModel> list);
}
