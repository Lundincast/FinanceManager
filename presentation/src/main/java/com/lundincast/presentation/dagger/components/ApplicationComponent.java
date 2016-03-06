package com.lundincast.presentation.dagger.components;

import android.content.Context;
import android.content.SharedPreferences;

import com.lundincast.domain.executor.PostExecutionThread;
import com.lundincast.domain.executor.ThreadExecutor;
import com.lundincast.domain.repository.TransactionRepositoryDomain;
import com.lundincast.presentation.dagger.modules.ApplicationModule;
import com.lundincast.presentation.data.TransactionRepository;
import com.lundincast.presentation.data.datasource.TransactionDataStore;
import com.lundincast.presentation.view.activity.BaseActivity;
import com.lundincast.presentation.view.fragment.SettingsFragment;
import com.lundincast.presentation.view.fragment.TransactionListFragment;

import javax.inject.Singleton;

import dagger.Component;

/**
 * A component whose lifetime is the life of the application.
 */
@Singleton
@Component(modules = ApplicationModule.class)
public interface ApplicationComponent {

    void inject(BaseActivity baseActivity);
    void inject(SettingsFragment settingsFragment);

    //Exposed to sub-graphs.
    Context context();
    ThreadExecutor threadExecutor();
    PostExecutionThread postExecutionThread();
    SharedPreferences sharedPreferences();
    TransactionDataStore transactionDataStore();
    TransactionRepositoryDomain transactionRepositoryDomain();
    TransactionRepository transactionRepository();
}
