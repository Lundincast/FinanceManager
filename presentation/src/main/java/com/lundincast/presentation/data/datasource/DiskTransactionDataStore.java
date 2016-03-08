package com.lundincast.presentation.data.datasource;

import com.lundincast.data.entity.TransactionEntity;
import com.lundincast.presentation.model.TransactionModel;

import java.io.IOException;
import java.util.ArrayList;
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
        RealmAsyncTask transaction = realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                TransactionModel transactionModel1 = realm.createObject(TransactionModel.class);
                transactionModel1.setTransactionId(realm.where(TransactionModel.class).max("transactionId").intValue() + 1);
                transactionModel1.setPrice(transactionModel.getPrice());
                transactionModel1.setCategory(transactionModel.getCategory());
                transactionModel1.setDate(transactionModel.getDate());
                transactionModel1.setComment(transactionModel.getComment());
            }
        }, null);
    }
}
