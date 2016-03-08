package com.lundincast.presentation.dagger.components;

import com.lundincast.presentation.dagger.PerActivity;
import com.lundincast.presentation.dagger.modules.ActivityModule;
import com.lundincast.presentation.dagger.modules.TransactionModule;
import com.lundincast.presentation.view.activity.CreateTransactionActivity;
import com.lundincast.presentation.view.fragment.CreateTransactionFragment;
import com.lundincast.presentation.view.fragment.TransactionListFragment;

import dagger.Component;

/**
 * A scope {@link com.lundincast.presentation.dagger.PerActivity} component.
 * Injects transaction specific Fragments.
 */
@PerActivity
@Component(dependencies = ApplicationComponent.class, modules = {ActivityModule.class, TransactionModule.class})
public interface TransactionComponent {

    void inject(TransactionListFragment transactionListFragment);
    void inject(CreateTransactionFragment createTransactionFragment);
}
