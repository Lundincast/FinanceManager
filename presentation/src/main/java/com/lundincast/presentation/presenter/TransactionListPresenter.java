package com.lundincast.presentation.presenter;

import android.content.Context;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;

import com.lundincast.presentation.R;
import com.lundincast.presentation.dagger.PerActivity;
import com.lundincast.presentation.model.CategoryModel;
import com.lundincast.presentation.model.TransactionModel;
import com.lundincast.presentation.view.TransactionListView;
import com.lundincast.presentation.view.fragment.TransactionListFragment;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import io.realm.Realm;
import io.realm.RealmChangeListener;
import io.realm.RealmResults;
import io.realm.Sort;

/**
 * {@link Presenter} that controls communication between views and models of the presentation
 * layer.
 */
@PerActivity
public class TransactionListPresenter implements Presenter {

    private final Realm realm;
    private RealmResults<TransactionModel> transactionList;
    private RealmChangeListener transactionListResultListener = new RealmChangeListener() {
        @Override
        public void onChange() {
            TransactionListPresenter.this.showTransactionsCollectionInView(transactionList);
        }
    };

    private TransactionListView viewListView;

    // Necessary to support multi language
    String allWordString;

    @Inject
    public TransactionListPresenter() {
        this.realm = Realm.getDefaultInstance();
    }

    /**
     * Setter method to reference corresponding view (TransactionListFragment in this case)
     *
     * @param view
     */
    public void setView(TransactionListFragment view) {
        this.viewListView = view;
    }

    @Override
    public void resume() {
        transactionList.addChangeListener(transactionListResultListener);
    }

    @Override
    public void pause() {

    }

    @Override
    public void destroy() {
        transactionList.removeChangeListener(transactionListResultListener);
    }

    /**
     * Initializes the presenter by retrieving the transaction list.
     */
    public void initialize() {
        this.loadTransactionList();
    }

    /**
     * Loads transactions.
     */
    private void loadTransactionList() {
        this.showViewLoading();
        this.getTransactionList();
    }

    public void onTransactionClicked(TransactionModel transactionModel) {
        this.viewListView.viewTransaction(transactionModel);
    }

    private void showViewLoading() {
        this.viewListView.showLoading();
    }

    private void hideViewLoading() {
        this.viewListView.hideLoading();
    }

    private void showEmptyListMessage() { this.viewListView.showEmptyListMessage(); }

    private void hideEmptyListMessage() { this.viewListView.hideEmptyListMessage(); }

    private void showTransactionsCollectionInView(List<TransactionModel> transactionList) {
        this.hideViewLoading();
        if (transactionList == null || transactionList.size() == 0) {
            this.showEmptyListMessage();
        } else {
            this.hideEmptyListMessage();
        }
        this.viewListView.renderTransactionList(transactionList);
    }

    private void getTransactionList() {
        transactionList = realm.where(TransactionModel.class).findAll();
        transactionList.sort("date", Sort.DESCENDING);
        showTransactionsCollectionInView(transactionList);
    }

    public void filterListByCategory(CharSequence categoryName) {
        RealmResults<TransactionModel> filteredTransactionList;
        if (categoryName.toString().equals(allWordString)) {
            filteredTransactionList = realm.where(TransactionModel.class).findAll();
        } else {
            filteredTransactionList = realm.where(TransactionModel.class)
                            .equalTo("category.name", categoryName.toString())
                            .findAll();
        }
        if (filteredTransactionList != null) {
            filteredTransactionList.sort("date", Sort.DESCENDING);
        }
        showTransactionsCollectionInView(filteredTransactionList);
    }

    public void buildCategoryAdapterForFilterDialog() {
        ArrayList<String> categoryNameList= new ArrayList<>();
        RealmResults<CategoryModel> categoryModels = realm.where(CategoryModel.class).findAll();
        // Add manually the "all" option so that user can come back to all transactions
        allWordString = ((TransactionListFragment)viewListView).getResources().getString(R.string.all);
        categoryNameList.add(allWordString);
        for (CategoryModel categoryModel : categoryModels) {
            categoryNameList.add(categoryModel.getName());
        }

        this.viewListView.showFilterTransactionDialog(new ArrayAdapter<>(
                                                ((TransactionListFragment)viewListView).getActivity(),
                                                R.layout.dialog_list_entry,
                                                categoryNameList));
    }

}
