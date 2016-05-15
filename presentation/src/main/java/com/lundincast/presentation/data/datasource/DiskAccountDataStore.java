package com.lundincast.presentation.data.datasource;

import com.lundincast.presentation.model.AccountModel;

import java.io.IOException;
import java.util.List;

import javax.inject.Inject;

import io.realm.Realm;
import io.realm.RealmResults;

/**
 * {@link AccountDataStore} implementation based on database on disk.
 */
public class DiskAccountDataStore implements AccountDataStore {

    private Realm realm;

    @Inject
    public DiskAccountDataStore(Realm realm) {
        this.realm = realm;
    }

    @Override
    public List<AccountModel> accountList() throws IOException {
        return realm.where(AccountModel.class).findAll();
    }

    @Override
    public void saveAccount(final AccountModel accountModel) {
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                AccountModel account = new AccountModel();
                if (accountModel.getId() == -1) {
                    if (realm.where(AccountModel.class).findFirst() == null) {
                        account.setId(1);
                    } else {
                        account.setId(realm.where(AccountModel.class).max("id").intValue() + 1);
                    }
                } else {
                    account.setId(accountModel.getId());
                }
                account.setName(accountModel.getName());
                account.setColor(accountModel.getColor());
                // TODO set balance
                realm.copyToRealmOrUpdate(account);
            }
        }, null);
    }

    @Override
    public void deleteAccount(final long accountId) {
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                AccountModel account = realm.where(AccountModel.class).equalTo("id", accountId).findFirst();
                // TODO delete all transactions under this account
                account.removeFromRealm();
            }
        }, null);
    }
}
