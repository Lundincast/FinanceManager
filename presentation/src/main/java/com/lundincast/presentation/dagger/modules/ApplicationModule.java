package com.lundincast.presentation.dagger.modules;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.lundincast.data.executor.JobExecutor;
import com.lundincast.data.repository.TransactionDataRepository;
import com.lundincast.data.repository.datasource.DiskTransactionDataStore;
import com.lundincast.data.repository.datasource.TransactionDataStore;
import com.lundincast.domain.executor.PostExecutionThread;
import com.lundincast.domain.executor.ThreadExecutor;
import com.lundincast.domain.repository.TransactionRepository;
import com.lundincast.presentation.AndroidApplication;
import com.lundincast.presentation.UIThread;
import com.lundincast.presentation.navigation.Navigator;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import io.realm.Realm;
import io.realm.RealmConfiguration;

/**
 * Dagger module that provides objects which will live during the application lifecycle.
 */
@Module
public class ApplicationModule {

    private final AndroidApplication application;

    public ApplicationModule(AndroidApplication application) {
        this.application = application;
    }

    @Provides @Singleton
    Context provideApplicationContext() {
        return this.application;
    }

    @Provides @Singleton
    Navigator provideNavigator() {
        return new Navigator();
    }

    @Provides @Singleton
    SharedPreferences provideSharedPreferences(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context);
    }

    @Provides @Singleton
    ThreadExecutor provideThreadExecutor(JobExecutor jobExecutor) {
        return jobExecutor;
    }

    @Provides @Singleton
    PostExecutionThread providePostExecutionThread(UIThread uiThread) {
        return uiThread;
    }

    @Provides @Singleton
    Realm provideRealm () {
        RealmConfiguration config = new RealmConfiguration.Builder(provideApplicationContext())
                .name("financemanager.realm")
                .schemaVersion(1)
                .build();
        return Realm.getInstance(config);
    }

    @Provides @Singleton
    TransactionRepository provideTransactionRepository(TransactionDataRepository transactionDataRepository) {
        return transactionDataRepository;
    }

    @Provides @Singleton
    TransactionDataStore provideTransactionDataStore(Realm realm) {
        return new DiskTransactionDataStore(realm);
    }
}
