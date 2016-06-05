package com.lundincast.presentation.view.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.ColorInt;
import android.support.annotation.NonNull;
import android.support.v7.widget.Toolbar;
import android.widget.ImageView;
import android.widget.TextView;

import com.afollestad.materialdialogs.color.ColorChooserDialog;
import com.lundincast.presentation.R;
import com.lundincast.presentation.dagger.HasComponent;
import com.lundincast.presentation.dagger.components.AccountComponent;
import com.lundincast.presentation.dagger.components.DaggerAccountComponent;
import com.lundincast.presentation.dagger.modules.AccountModule;
import com.lundincast.presentation.view.fragment.CreateOrUpdateAccountFragment;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Activity that allows user to create a new account
 */
public class CreateOrUpdateAccountActivity extends BaseActivity implements HasComponent<AccountComponent>,
                                                                    ColorChooserDialog.ColorCallback {

    public static final String INTENT_EXTRA_PARAM_ACCOUNT_ID = "com.lundincast.INTENT_PARAM_ACCOUNT_ID";
    private static final String INSTANCE_STATE_PARAM_ACCOUNT_ID = "com.lundincast.STATE_PARAM_ACCOUNT_ID";

    public static Intent getCallingIntent(Context context, long AccountId) {
        Intent callingIntent = new Intent(context, CreateOrUpdateAccountActivity.class);
        callingIntent.putExtra(INTENT_EXTRA_PARAM_ACCOUNT_ID, AccountId);

        return callingIntent;
    }

    @Bind(R.id.toolbar) Toolbar toolbar;
    @Bind(R.id.iv_back) ImageView iv_back;
    @Bind(R.id.tv_title) TextView tv_title;
    @Bind(R.id.iv_delete) ImageView iv_delete;

    public long accountId = -1;
    public int color;
    private AccountComponent accountComponent;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_category);
        ButterKnife.bind(this);

        initializeActivity(savedInstanceState);
        setUpToolbar();
        initializeInjector();
        loadFragment();
    }

    @Override protected void onSaveInstanceState(Bundle outState) {
        if (outState != null) {
            outState.putLong(INSTANCE_STATE_PARAM_ACCOUNT_ID, this.accountId);
        }
        super.onSaveInstanceState(outState);
    }

    /**
     * Initializes this activity.
     */
    private void initializeActivity(Bundle savedInstanceState) {
        if (savedInstanceState == null) {
            this.accountId = getIntent().getLongExtra(INTENT_EXTRA_PARAM_ACCOUNT_ID, -1);
        } else {
            this.accountId = savedInstanceState.getLong(INSTANCE_STATE_PARAM_ACCOUNT_ID);
        }
    }

    private void setUpToolbar() {
        toolbar.showOverflowMenu();
        setSupportActionBar(toolbar);
        if (accountId == -1) {
            tv_title.setText(R.string.new_account);
        } else {
            tv_title.setText(R.string.edit_account);
        }
    }

    private void initializeInjector() {
        this.accountComponent = DaggerAccountComponent.builder()
                .applicationComponent(getApplicationComponent())
                .accountModule(new AccountModule())
                .build();
    }

    private void loadFragment() {
        this.addFragment(R.id.createActivityFragment, new CreateOrUpdateAccountFragment(), "createActivityFragment");
    }

    @OnClick(R.id.iv_back)
    void onBackClicked() {
        finish();
    }

    @Override
    public void onColorSelection(@NonNull ColorChooserDialog dialog, @ColorInt int selectedColor) {
        this.color = selectedColor;
        // Set color in displayed CreateOrUpdateCategoryFragment
        CreateOrUpdateAccountFragment fragment = ((CreateOrUpdateAccountFragment) getFragmentManager().findFragmentById(R.id.createActivityFragment));
        fragment.onColorSelected(selectedColor);
    }

    public void displayColorChooserDialog() {
        new ColorChooserDialog.Builder(this, R.string.color_palette)
                .titleSub(R.string.colors)
                .doneButton(R.string.done)
                .cancelButton(R.string.cancel)
                .show();

    }

    @Override
    public AccountComponent getComponent() {
        return accountComponent;
    }
}
