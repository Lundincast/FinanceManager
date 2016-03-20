package com.lundincast.presentation.presenter;

import android.support.annotation.NonNull;
import android.view.View;

import com.lundincast.domain.Category;
import com.lundincast.presentation.data.TransactionRepository;
import com.lundincast.presentation.model.CategoryModel;
import com.lundincast.presentation.model.TransactionModel;
import com.lundincast.presentation.view.TransactionDetailsView;

import java.util.Date;

import javax.inject.Inject;

import io.realm.Realm;

/**
 * {@link Presenter} that controls communication between views and models of the presentation
 * layer.
 */
public class CreateTransactionPresenter implements Presenter {

    private TransactionDetailsView viewDetailsView;

    private final TransactionRepository transactionRepository;
    private final Realm realm;

    private int mTtransactionId = -1;
    private double mPrice = 0;
    private CategoryModel mCategory = null;
    private Date mDate = new Date();
    private String mComment = "";

    @Inject
    public CreateTransactionPresenter(TransactionRepository transactionRepository) {
        this.transactionRepository = transactionRepository;
        this.realm = Realm.getDefaultInstance();
    }

    public void setView(@NonNull TransactionDetailsView view) {
        this.viewDetailsView = view;
    }

    public void setmPrice(double price) {
        this.mPrice = price;
    }

    public CategoryModel getmCategory() {
        return this.mCategory;
    }

    public void setmCategory(CategoryModel categoryModel) {
        this.mCategory = categoryModel;
        int i = 0;
    }

    public Date getmDate() {
        return this.mDate;
    }

    public void setmDate(Date date) {
        this.mDate = date;
    }

    public String getmComment() {
        return this.mComment;
    }

    public void setmComment(String comment) {
        this.mComment = comment;
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

    public void initialize(int transactionId) {
        if (transactionId != -1) {
            this.mTtransactionId = transactionId;
            TransactionModel transactionModel =
                    realm.where(TransactionModel.class).equalTo("transactionId", transactionId).findFirst();
            this.mPrice = transactionModel.getPrice();
            this.mCategory = transactionModel.getCategory();
            this.mDate = transactionModel.getDate();
            this.mComment = transactionModel.getComment();
        }
        showTransactionPriceInView();
    }

    private void showTransactionPriceInView() {
        this.viewDetailsView.renderTransactionPrice(String.format("%.2f", mPrice));
    }

    public void saveTransaction() {
        TransactionModel transaction = new TransactionModel(mTtransactionId);
        transaction.setPrice(mPrice);
        transaction.setCategory(realm.copyFromRealm(mCategory));
        transaction.setDate(mDate);
        transaction.setComment(mComment);
        this.transactionRepository.saveTransaction(transaction);
    }
}
