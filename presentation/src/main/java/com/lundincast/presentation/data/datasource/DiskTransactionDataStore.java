package com.lundincast.presentation.data.datasource;

import com.lundincast.data.entity.TransactionEntity;
import com.lundincast.presentation.model.CategoryModel;
import com.lundincast.presentation.model.TransactionModel;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import io.realm.Realm;
import io.realm.RealmAsyncTask;
import io.realm.RealmResults;
import rx.Observable;

/**
 * {@link com.lundincast.presentation.data.datasource.TransactionDataStore} implementation based on database on disk.
 */
public class DiskTransactionDataStore implements TransactionDataStore {

    private Realm realm;

    @Inject
    public DiskTransactionDataStore(Realm realm) {
        this.realm = realm;
    }

    @Override
    public List<TransactionModel> transactionEntityList() throws IOException {

        // retrieve all transactions
        RealmResults<TransactionModel> result = realm.where(TransactionModel.class)
                .findAll();

        // convert to a Collection<TransactionModel> object
        List<TransactionModel> transactionModelCollection = new ArrayList<>();
        for (TransactionModel transactionModel : result) {
            transactionModelCollection.add(transactionModel);
        }

        return transactionModelCollection;
    }

    @Override
    public Observable<TransactionEntity> transactionEntityDetails(int transactionId) {
        return null;
    }

    @Override
    public void saveTransaction(final TransactionModel transactionModel) {
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                TransactionModel transaction = new TransactionModel();
                if (transactionModel.getTransactionId() == -1) {
                    if (realm.where(TransactionModel.class).findFirst() == null) {
                        transaction.setTransactionId(1);
                    } else {
                        transaction.setTransactionId(realm.where(TransactionModel.class).max("transactionId").intValue() + 1);
                    }
                } else {
                    transaction.setTransactionId(transactionModel.getTransactionId());
                }
                transaction.setPrice(transactionModel.getPrice());
                CategoryModel category = realm.where(CategoryModel.class).equalTo("id", transactionModel.getCategory().getId()).findFirst();
                transaction.setCategory(category);
                transaction.setDate(transactionModel.getDate());
                Calendar cal = Calendar.getInstance();
                cal.setTime(transactionModel.getDate());
                transaction.setDay(cal.get(Calendar.DAY_OF_MONTH));
                transaction.setMonth(cal.get(Calendar.MONTH));
                transaction.setYear(cal.get(Calendar.YEAR));
                transaction.setComment(transactionModel.getComment());
                transaction.setPending(transactionModel.isPending());
                transaction.setDueToOrBy(transactionModel.getDueToOrBy());
                transaction.setDueName(transactionModel.getDueName());
                realm.copyToRealmOrUpdate(transaction);
            }
        }, null);
    }

    @Override
    public void deleteTransaction(final int transactionId) {
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                realm.where(TransactionModel.class).equalTo("transactionId", transactionId).findFirst().removeFromRealm();
            }
        }, null);
    }
}
