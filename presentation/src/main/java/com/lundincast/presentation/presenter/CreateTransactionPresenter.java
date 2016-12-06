package com.lundincast.presentation.presenter;

import android.support.annotation.NonNull;

import com.lundincast.presentation.data.AccountRepository;
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
    private final AccountRepository accountRepository;
    private final Realm realm;

    private int mTransactionId = -1;
    private String mTransactionType;
    private double previousPrice = -1;
    private double mPrice = 0;
    private CategoryModel mCategory = null;
    private Date mDate = new Date();
    private String mComment = "";
    private AccountModel mPreviousFromAccount = null;
    private AccountModel mFromAccount = null;
    private AccountModel mPreviousToAccount = null;
    private AccountModel mToAccount = null;
    private boolean mPending;
    private int mDueToOrBy;
    private String mDueName;

    @Inject
    public CreateTransactionPresenter(TransactionRepository transactionRepository,
                                      AccountRepository accountRepository) {
        this.transactionRepository = transactionRepository;
        this.accountRepository = accountRepository;
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
            this.previousPrice = this.mPrice;
            this.mCategory = transactionModel.getCategory();
            this.mDate = transactionModel.getDate();
            this.mComment = transactionModel.getComment();
            this.mFromAccount = transactionModel.getFromAccount();
            this.mPreviousFromAccount = this.mFromAccount;
            this.mToAccount = transactionModel.getToAccount();
            this.mPreviousToAccount = this.mToAccount;
            this.mPending = transactionModel.isPending();
            this.mDueToOrBy = transactionModel.getDueToOrBy();
            this.mDueName = transactionModel.getDueName();
        } else {
            AccountModel account = realm.where(AccountModel.class).findFirst();
            if (account != null) {
                if (this.mTransactionType == CreateTransactionActivity.TRANSACTION_TYPE_EXPENSE) {
                    this.mFromAccount = account;
                } else if (this.mTransactionType == CreateTransactionActivity.TRANSACTION_TYPE_INCOME) {
                    this.mToAccount = account;
                }

            }
        }
        showTransactionPriceInView();
    }

    private void showTransactionPriceInView() {
        this.viewDetailsView.renderTransactionPrice(String.format("%.2f", mPrice));
    }

    public void saveTransaction() {
        String errorMessage = validateData();
        if (errorMessage != null) {
            this.viewDetailsView.showMessage(errorMessage);
        } else {
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
            transaction.setToAccount(mToAccount);
            transaction.setPending(mPending);
            transaction.setDueToOrBy(mDueToOrBy);
            transaction.setDueName(mDueName);
            this.transactionRepository.saveTransaction(transaction);

            // Update account balance with transaction value if necessary
            double delta;
            if (previousPrice == -1 || previousPrice == mPrice) {
                delta = mPrice;
            } else {
                delta = mPrice - previousPrice;
            }
            if (delta != 0) {
                if (transaction.getTransactionType().equals(CreateTransactionActivity.TRANSACTION_TYPE_EXPENSE)) {
                    if (mPreviousFromAccount == null || mFromAccount == mPreviousFromAccount) {
                        // In case of a new expense or if the fromAccount hasn't changed, only apply the delta
                        this.accountRepository.updateAccountBalance(mFromAccount.getId(), -delta);
                    } else {
                        // If fromAccount has been updated, add previous price to old fromAccount and subtract
                        // new price to new fromAccount
                        this.accountRepository.updateAccountBalance(mPreviousFromAccount.getId(), previousPrice);
                        this.accountRepository.updateAccountBalance(mFromAccount.getId(), -mPrice);
                    }
                } else if (transaction.getTransactionType().equals(CreateTransactionActivity.TRANSACTION_TYPE_INCOME)) {
                    if (mPreviousToAccount == null || mToAccount == mPreviousToAccount) {
                        // In case of a new income or if the toAccount hasn't changed, only apply the delta
                        this.accountRepository.updateAccountBalance(transaction.getToAccount().getId(), delta);
                    } else {
                        // If toAccount has been updated, substract previous price to old toAccount and add
                        // new price to new toAccount
                        this.accountRepository.updateAccountBalance(mPreviousToAccount.getId(), -previousPrice);
                        this.accountRepository.updateAccountBalance(mToAccount.getId(), mPrice);
                    }
                } else {
                    if (mPreviousFromAccount == null || (mFromAccount == mPreviousFromAccount && mToAccount == mPreviousToAccount)) {
                        // In case of a new transfer or if none of the accounts have changed, only apply the delta
                        this.accountRepository.updateAccountBalance(mFromAccount.getId(), -delta);
                        this.accountRepository.updateAccountBalance(mToAccount.getId(), delta);
                    } else {
                        if (mFromAccount != mPreviousFromAccount) {
                            this.accountRepository.updateAccountBalance(mFromAccount.getId(), delta);
                            this.accountRepository.updateAccountBalance(mPreviousFromAccount.getId(), -delta);
                        }
                        if (mToAccount != mPreviousToAccount) {
                            this.accountRepository.updateAccountBalance(mToAccount.getId(), -delta);
                            this.accountRepository.updateAccountBalance(mPreviousToAccount.getId(), delta);
                        }
                    }
                }
            } else {
                if (transaction.getTransactionType().equals(CreateTransactionActivity.TRANSACTION_TYPE_EXPENSE)) {
                    if (mFromAccount != mPreviousFromAccount) {
                        this.accountRepository.updateAccountBalance(mFromAccount.getId(), -delta);
                        this.accountRepository.updateAccountBalance(mPreviousFromAccount.getId(), delta);
                    }
                } else if (transaction.getTransactionType().equals(CreateTransactionActivity.TRANSACTION_TYPE_INCOME)) {
                    if (mToAccount != mPreviousToAccount) {
                        this.accountRepository.updateAccountBalance(mToAccount.getId(), delta);
                        this.accountRepository.updateAccountBalance(mPreviousToAccount.getId(), -delta);
                    }
                } else {
                    if (mFromAccount != mPreviousFromAccount) {
                        this.accountRepository.updateAccountBalance(mFromAccount.getId(), delta);
                        this.accountRepository.updateAccountBalance(mPreviousFromAccount.getId(), -delta);
                    }
                    if (mToAccount != mPreviousToAccount) {
                        this.accountRepository.updateAccountBalance(mToAccount.getId(), -delta);
                        this.accountRepository.updateAccountBalance(mPreviousToAccount.getId(), delta);
                    }
                }

            }
            this.viewDetailsView.closeActivity();
        }
    }

    public void deleteTransaction(int transactionId) {
        // remove transaction value from account balance
        TransactionModel transaction = realm.where(TransactionModel.class).equalTo("transactionId", transactionId).findFirst();
        if (transaction.getTransactionType().equals(CreateTransactionActivity.TRANSACTION_TYPE_EXPENSE)) {
            this.accountRepository.updateAccountBalance(transaction.getFromAccount().getId(), transaction.getPrice());
        } else if (transaction.getTransactionType().equals(CreateTransactionActivity.TRANSACTION_TYPE_INCOME)) {
            this.accountRepository.updateAccountBalance(transaction.getToAccount().getId(), -transaction.getPrice());
        } else {
            this.accountRepository.updateAccountBalance(transaction.getFromAccount().getId(), transaction.getPrice());
            this.accountRepository.updateAccountBalance(transaction.getToAccount().getId(), -transaction.getPrice());
        }
        this.transactionRepository.deleteTransaction(transactionId);
    }

    private String validateData() {
        String errorMessage = null;
        if (this.mTransactionType.equals(CreateTransactionActivity.TRANSACTION_TYPE_EXPENSE)) {
            if (this.mFromAccount == null) {
                errorMessage = "Please select an account";
            }
        } else if (this.mTransactionType.equals(CreateTransactionActivity.TRANSACTION_TYPE_INCOME)) {
            if (this.mToAccount == null) {
                errorMessage = "Please select an account";
            }
        } else if (this.mTransactionType.equals(CreateTransactionActivity.TRANSACTION_TYPE_TRANSFER)) {
            if (this.mFromAccount == null) {
                errorMessage = "Please select source account";
            }
            if (this.mToAccount == null) {
                errorMessage = "Please select target account";
            }
        }
        return errorMessage;
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

    public AccountModel getmToAccount() {
        return mToAccount;
    }

    public void setmToAccount(String accountName) {
        this.mToAccount = realm.where(AccountModel.class).equalTo("name", accountName).findFirst();
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
