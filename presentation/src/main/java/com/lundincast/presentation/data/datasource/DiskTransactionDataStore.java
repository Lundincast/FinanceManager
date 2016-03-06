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

        // Create transaction just for testing
        realm.beginTransaction();
        TransactionModel transaction = realm.createObject(TransactionModel.class);
        transaction.setTransactionId(2);
        transaction.setComment("test");
        transaction.setDate(new Date());
        transaction.setPrice(12);
        transaction.setCategory(null);
        realm.commitTransaction();

        // retrieve all transactions
        RealmResults<TransactionModel> result = realm.where(TransactionModel.class)
                .findAll();

        // convert to a Collection<TransactionModel> obeject
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
}
