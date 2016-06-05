package com.lundincast.presentation.dagger.components;

import com.lundincast.presentation.dagger.PerActivity;
import com.lundincast.presentation.dagger.modules.AccountModule;
import com.lundincast.presentation.presenter.CreateTransactionPresenter;
import com.lundincast.presentation.view.activity.AccountListActivity;
import com.lundincast.presentation.view.fragment.AccountListFragment;
import com.lundincast.presentation.view.fragment.CreateOrUpdateAccountFragment;

import dagger.Component;

/**
 * A scope {@link com.lundincast.presentation.dagger.PerActivity} component.
 * Injects account specific Fragments.
 */
@PerActivity
@Component(dependencies = ApplicationComponent.class, modules = AccountModule.class)
public interface AccountComponent {

    void inject(AccountListFragment accountListFragment);
    void inject(CreateOrUpdateAccountFragment createOrUpdateAccountFragment);
    void inject(CreateTransactionPresenter createTransactionPresenter);
}
