package com.lundincast.presentation.data.datasource;

import com.lundincast.presentation.model.CategoryModel;
import com.lundincast.presentation.model.OverheadModel;

import javax.inject.Inject;

import io.realm.Realm;

/**
 * {@link OverheadsDataStore} implementation based on database on disk.
 */
public class DiskOverheadsDataStore implements OverheadsDataStore {

    private Realm realm;

    @Inject
    public DiskOverheadsDataStore(Realm realm) {
        this.realm = realm;
    }

    @Override
    public void saveOverhead(final OverheadModel overheadModel) {
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                OverheadModel overhead = new OverheadModel();
                if (overheadModel.getOverheadId() == -1) {
                    if (realm.where(OverheadModel.class).findFirst() == null) {
                        overhead.setOverheadId(1);
                    } else {
                        overhead.setOverheadId(realm.where(OverheadModel.class).max("overheadId").intValue() + 1);
                    }
                } else {
                    overhead.setOverheadId(overheadModel.getOverheadId());
                }
                overhead.setPrice(overheadModel.getPrice());
                CategoryModel category = realm.where(CategoryModel.class).equalTo("id", overheadModel.getCategory().getId()).findFirst();
                overhead.setCategory(category);
                overhead.setFromAccount(overheadModel.getFromAccount());
                overhead.setDayOfMonth(overheadModel.getDayOfMonth());
                overhead.setComment(overheadModel.getComment());
                realm.copyToRealmOrUpdate(overhead);
            }
        }, null);
    }

    @Override
    public void deleteOverhead(final int overheadId) {
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                realm.where(OverheadModel.class).equalTo("overheadId", overheadId)
                                                .findFirst()
                                                .removeFromRealm();
            }
        }, null);
    }
}
