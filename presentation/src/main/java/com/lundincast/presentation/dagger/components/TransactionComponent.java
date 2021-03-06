package com.lundincast.presentation.dagger.components;

import com.lundincast.presentation.dagger.PerActivity;
import com.lundincast.presentation.dagger.modules.AccountModule;
import com.lundincast.presentation.dagger.modules.ActivityModule;
import com.lundincast.presentation.dagger.modules.TransactionModule;
import com.lundincast.presentation.presenter.TransactionListPresenter;
import com.lundincast.presentation.services.OverheadService;
import com.lundincast.presentation.view.activity.CreateTransactionActivity;
import com.lundincast.presentation.view.fragment.OverviewFragment;
import com.lundincast.presentation.view.fragment.TransactionListFragment;

import dagger.Component;

/**
 * A scope {@link com.lundincast.presentation.dagger.PerActivity} component.
 * Injects transaction specific Fragments.
 */
@PerActivity
@Component(dependencies = ApplicationComponent.class, modules = {TransactionModule.class, AccountModule.class})
public interface TransactionComponent {

    void inject(TransactionListFragment transactionListFragment);
    void inject(OverviewFragment overviewFragment);
    void inject(TransactionListPresenter transactionListPresenter);
    void inject(CreateTransactionActivity createTransactionActivity);
    void inject(OverheadService overheadService);
}
