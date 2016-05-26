package com.lundincast.presentation.presenter;

import android.support.annotation.NonNull;

import com.lundincast.presentation.data.TransactionRepository;
import com.lundincast.presentation.model.AccountModel;
import com.lundincast.presentation.model.CategoryModel;
import com.lundincast.presentation.model.TransactionModel;
import com.lundincast.presentation.view.TransactionDetailsView;
import com.lundincast.presentation.view.activity.CreateTransactionActivity;

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
    private String mTransactionType;
    private double mPrice = 0;
    private CategoryModel mCategory = null;
    private Date mDate = new Date();
    private String mComment = "";
    private AccountModel mFromAccount = null;
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
            this.mTransactionType = transactionModel.getTransactionType();
            this.mPrice = transactionModel.getPrice();
            this.mCategory = transactionModel.getCategory();
            this.mDate = transactionModel.getDate();
            this.mComment = transactionModel.getComment();
            this.mFromAccount = transactionModel.getFromAccount();
            this.mPending = transactionModel.isPending();
            this.mDueToOrBy = transactionModel.getDueToOrBy();
            this.mDueName = transactionModel.getDueName();
        } else {
            AccountModel account = realm.where(AccountModel.class).findFirst();
            if (account != null) {
                this.mFromAccount = account;
            }
        }
        showTransactionPriceInView();
    }

    private void showTransactionPriceInView() {
        this.viewDetailsView.renderTransactionPrice(String.format("%.2f", mPrice));
    }

    public void saveTransaction() {
        TransactionModel transaction = new TransactionModel(mTransactionId);
        transaction.setTransactionType(mTransactionType);
        transaction.setPrice(mPrice);
        if (mTransactionType.equals(CreateTransactionActivity.TRANSACTION_TYPE_EXPENSE)) {
            transaction.setCategory(realm.copyFromRealm(mCategory));
        } else {
            transaction.setCategory(null);
        }
        transaction.setDate(mDate);
        transaction.setComment(mComment);
        transaction.setFromAccount(mFromAccount);
        transaction.setPending(mPending);
        transaction.setDueToOrBy(mDueToOrBy);
        transaction.setDueName(mDueName);
        this.transactionRepository.saveTransaction(transaction);
    }

    public void deleteTransaction(int transactionId) {
        this.transactionRepository.deleteTransaction(transactionId);
    }

    public String getmTransactionType() {
        return mTransactionType;
    }

    public void setmTransactionType(String mTransactionType) {
        this.mTransactionType = mTransactionType;
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

    public AccountModel getmFromAccount() {
        return mFromAccount;
    }

    public void setmFromAccount(String accountName) {
        this.mFromAccount = realm.where(AccountModel.class).equalTo("name", accountName).findFirst();
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
}
