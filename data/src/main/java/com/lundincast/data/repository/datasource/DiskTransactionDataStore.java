package com.lundincast.data.repository.datasource;

import com.lundincast.data.entity.TransactionEntity;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.inject.Inject;

import io.realm.Realm;
import io.realm.RealmList;
import io.realm.RealmResults;
import rx.Observable;

/**
 * {@link TransacDataStore} implementation based on database on disk.
 */
public class DiskTransactionDataStore implements TransacDataStore {

    @Inject
    public DiskTransactionDataStore() {}

    @Override
    public Observable transactionEntityList() throws IOException {

//        List<TransactionEntity> transactionList = new ArrayList<TransactionEntity>();
//
//        // Create transaction just for testing
//        realm.beginTransaction();
//        TransactionEntity transaction = realm.createObject(TransactionEntity.class);
//        transaction.setId(1);
//        transaction.setComment("test");
//        transaction.setDate(new Date());
//        transaction.setPrice(12);
//        transaction.setCategory(null);
//        realm.commitTransaction();
//
//        RealmList<TransactionEntity> transactionEntityRealmList = new RealmList<TransactionEntity>();
//
//        // Now retrieve this transaction
//        RealmResults<TransactionEntity> result =
//                realm.where(TransactionEntity.class)
//                    .findAll();
//
//        // build a List<TransactionEntity> out of query result
//        for (TransactionEntity transactionEntity : result) {
//            transactionList.add(transactionEntity);
//        }
//
//
//
//        return realm.where(TransactionEntity.class)
//                .findAllAsync()
//                .asObservable();
        return null;
    }

    @Override
    public Observable<TransactionEntity> transactionEntityDetails(int transactionId) {
        return null;
    }
}
