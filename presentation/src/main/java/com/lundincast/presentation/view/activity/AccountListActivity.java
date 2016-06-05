package com.lundincast.presentation.view.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.widget.ImageView;
import android.widget.TextView;

import com.lundincast.presentation.R;
import com.lundincast.presentation.dagger.HasComponent;
import com.lundincast.presentation.dagger.components.AccountComponent;
import com.lundincast.presentation.dagger.components.DaggerAccountComponent;
import com.lundincast.presentation.dagger.modules.AccountModule;
import com.lundincast.presentation.model.AccountModel;
import com.lundincast.presentation.navigation.Navigator;
import com.lundincast.presentation.view.fragment.AccountListFragment;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Activity to display the list of accounts.
 */
public class AccountListActivity extends BaseActivity implements HasComponent<AccountComponent>,
                                                                AccountListFragment.AccountListListener {

    public static Intent getCallingIntent(Context context) {
        return new Intent(context, AccountListActivity.class);
    }

    @Bind(R.id.toolbar) Toolbar toolbar;
    @Bind(R.id.iv_back) ImageView iv_back;
    @Bind(R.id.tv_title) TextView tv_title;
    @Bind(R.id.iv_add_icon) ImageView iv_add_icon;

    @Inject Navigator navigator;

    private AccountComponent accountComponent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_category);
        ButterKnife.bind(this);

        setUpToolbar();
        initializeInjector();
        loadFragment();
    }

    private void setUpToolbar() {
        toolbar.showOverflowMenu();
        setSupportActionBar(toolbar);
        // Set title
        tv_title.setText(R.string.accounts);
    }

    private void initializeInjector() {
        this.getApplicationComponent().inject(this);
        this.accountComponent = DaggerAccountComponent.builder()
                .applicationComponent(getApplicationComponent())
                .accountModule(new AccountModule())
                .build();
    }

    private void loadFragment() {
        this.addFragment(R.id.listCategoryFragment, new AccountListFragment(), "accountListFragment");
    }

    @OnClick(R.id.iv_back)
    void onBackClicked() {
        finish();
    }

    @OnClick(R.id.iv_add_icon)
    void onAddIconClicked() {
        this.navigator.navigateToCreateOrUpdateAccount(this, -1);
    }

    @Override
    public AccountComponent getComponent() {
        return accountComponent;
    }

    @Override
    public void onAccountClicked(AccountModel accountModel) {
        this.navigator.navigateToCreateOrUpdateAccount(this, accountModel.getId());
    }
}
