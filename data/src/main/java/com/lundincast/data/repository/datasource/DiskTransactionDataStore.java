package com.lundincast.data.repository.datasource;

import com.lundincast.data.entity.TransactionEntity;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.Future;

import javax.inject.Inject;

import io.realm.Realm;
import io.realm.RealmResults;
import rx.Observable;

/**
 * {@link TransactionDataStore} implementation based on database on disk.
 */
public class DiskTransactionDataStore implements TransactionDataStore {

    private Realm realm;

    @Inject
    public DiskTransactionDataStore(Realm realm) {
        this.realm = realm;
    }

    @Override
    public Observable<List<TransactionEntity>> transactionEntityList() throws IOException {

        List<TransactionEntity> transactionList = new ArrayList<TransactionEntity>();

        // Create transaction just for testing
        realm.beginTransaction();
        TransactionEntity transaction = realm.createObject(TransactionEntity.class);
        transaction.setId(1);
        transaction.setComment("test");
        transaction.setDate(new Date());
        transaction.setPrice(12);
        transaction.setCategory(null);
        realm.commitTransaction();

        // Now retrieve this transaction
        RealmResults<TransactionEntity> result = realm.where(TransactionEntity.class)
                                                                  .findAll();

        // build a List<TransactionEntity> out of query result
        for (TransactionEntity transactionEntity : result) {
            transactionList.add(transactionEntity);
        }

        return Observable.from((Iterable) transactionList);
    }

    @Override
    public Observable<TransactionEntity> transactionEntityDetails(int transactionId) {
        return null;
    }
}
