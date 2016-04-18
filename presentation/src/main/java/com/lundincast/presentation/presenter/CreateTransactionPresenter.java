package com.lundincast.presentation.presenter;

import android.support.annotation.NonNull;

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

    private int mTransactionId = -1;
    private double mPrice = 0;
    private CategoryModel mCategory = null;
    private Date mDate = new Date();
    private String mComment = "";
    private boolean mPending;
    private int mDueToOrBy;
    private String mDueName;

    @Inject
    public CreateTransactionPresenter(TransactionRepository transactionRepository) {
        this.transactionRepository = transactionRepository;
        this.realm = Realm.getDefaultInstance();
    }

    public void setView(@NonNull TransactionDetailsView view) {
        this.viewDetailsView = view;
    }

    public void setMPrice(double price) {
        this.mPrice = price;
    }

    public CategoryModel getMCategory() {
        return this.mCategory;
    }

    public void setmCategory(CategoryModel categoryModel) {
        this.mCategory = categoryModel;
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

    public boolean ismPending() {
        return mPending;
    }

    public void setmPending(boolean mPending) {
        this.mPending = mPending;
    }

    public int getmDueToOrBy() {
        return mDueToOrBy;
    }

    public void setmDueToOrBy(int mDueToOrBy) {
        this.mDueToOrBy = mDueToOrBy;
    }

    public String getmDueName() {
        return mDueName;
    }

    public void setmDueName(String mDueName) {
        this.mDueName = mDueName;
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
            this.mTransactionId = transactionId;
            TransactionModel transactionModel =
                    realm.where(TransactionModel.class).equalTo("transactionId", transactionId).findFirst();
            this.mPrice = transactionModel.getPrice();
            this.mCategory = transactionModel.getCategory();
            this.mDate = transactionModel.getDate();
            this.mComment = transactionModel.getComment();
            this.mPending = transactionModel.isPending();
            this.mDueToOrBy = transactionModel.getDueToOrBy();
            this.mDueName = transactionModel.getDueName();
        }
        showTransactionPriceInView();
    }

    private void showTransactionPriceInView() {
        this.viewDetailsView.renderTransactionPrice(String.format("%.2f", mPrice));
    }

    public void saveTransaction() {
        TransactionModel transaction = new TransactionModel(mTransactionId);
        transaction.setPrice(mPrice);
        transaction.setCategory(realm.copyFromRealm(mCategory));
        transaction.setDate(mDate);
        transaction.setComment(mComment);
        transaction.setPending(mPending);
        transaction.setDueToOrBy(mDueToOrBy);
        transaction.setDueName(mDueName);
        this.transactionRepository.saveTransaction(transaction);
    }

    public void deleteTransaction(int transactionId) {
        this.transactionRepository.deleteTransaction(transactionId);
    }
}
