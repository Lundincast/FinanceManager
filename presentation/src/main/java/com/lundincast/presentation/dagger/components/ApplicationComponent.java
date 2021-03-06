package com.lundincast.presentation.dagger.components;

import android.content.Context;
import android.content.SharedPreferences;

import com.lundincast.domain.executor.PostExecutionThread;
import com.lundincast.domain.executor.ThreadExecutor;
import com.lundincast.domain.repository.TransactionRepositoryDomain;
import com.lundincast.presentation.dagger.modules.ApplicationModule;
import com.lundincast.presentation.view.activity.AccountListActivity;
import com.lundincast.presentation.view.activity.CategoryListActivity;
import com.lundincast.presentation.view.activity.MainActivity;
import com.lundincast.presentation.view.activity.OverheadsListActivity;
import com.lundincast.presentation.view.fragment.SettingsFragment;

import javax.inject.Singleton;

import dagger.Component;

/**
 * A component whose lifetime is the life of the application.
 */
@Singleton
@Component(modules = ApplicationModule.class)
public interface ApplicationComponent {

    void inject(MainActivity mainActivity);
    void inject(CategoryListActivity categoryListActivity);
    void inject(OverheadsListActivity overheadsListActivity);
    void inject(AccountListActivity accountListActivity);
    void inject(SettingsFragment settingsFragment);


    //Exposed to sub-graphs.
    Context context();
    SharedPreferences sharedPreferences();
    ThreadExecutor threadExecutor();
    PostExecutionThread postExecutionThread();
    TransactionRepositoryDomain transactionRepositoryDomain();
}
