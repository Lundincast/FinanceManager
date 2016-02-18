package com.lundincast.presentation.presenter;

import com.lundincast.domain.Transaction;
import com.lundincast.domain.interactor.DefaultSubscriber;
import com.lundincast.domain.interactor.UseCase;
import com.lundincast.presentation.dagger.PerActivity;
import com.lundincast.presentation.mapper.TransactionModelDataMapper;
import com.lundincast.presentation.model.TransactionModel;
import com.lundincast.presentation.view.TransactionListView;
import com.lundincast.presentation.view.fragment.TransactionListFragment;

import java.util.Collection;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

/**
 * {@link Presenter} that controls communication between views and models of the presentation
 * layer.
 */
@PerActivity
public class TransactionListPresenter implements Presenter {

    private TransactionListView viewListView;

    private final UseCase getTransactionListUseCase;
    private final TransactionModelDataMapper transactionModelDataMapper;

    @Inject
    public TransactionListPresenter(@Named("transactionList") UseCase getTransactionListUseCase,
                                    TransactionModelDataMapper transactionModelDataMapper) {
        this.getTransactionListUseCase = getTransactionListUseCase;
        this.transactionModelDataMapper = transactionModelDataMapper;
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

    }

    @Override
    public void pause() {

    }

    @Override
    public void destroy() {

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

    private void showTransactionsCollectionInView(Collection<Transaction> transactionsCollection) {
        final Collection<TransactionModel> transactionModelCollection =
                this.transactionModelDataMapper.transform(transactionsCollection);
        this.viewListView.renderTransactionList(transactionModelCollection);
    }

    private void getTransactionList() {
        this.getTransactionListUseCase.execute(new TransactionListSubscriber());
    }


    private final class TransactionListSubscriber extends DefaultSubscriber<List<Transaction>> {

        @Override
        public void onCompleted() {
            TransactionListPresenter.this.hideViewLoading();
        }

        @Override
        public void onError(Throwable e) {
            TransactionListPresenter.this.hideViewLoading();
        }

        @Override
        public void onNext(List<Transaction> transactions) {
            TransactionListPresenter.this.showTransactionsCollectionInView(transactions);
        }
    }
}
