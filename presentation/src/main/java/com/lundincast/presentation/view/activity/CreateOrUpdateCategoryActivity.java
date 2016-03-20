package com.lundincast.presentation.view.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.widget.ImageView;

import com.lundincast.presentation.R;
import com.lundincast.presentation.dagger.HasComponent;
import com.lundincast.presentation.dagger.components.CategoryComponent;
import com.lundincast.presentation.dagger.components.DaggerCategoryComponent;
import com.lundincast.presentation.dagger.modules.CategoryModule;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Activity that allows user to create a new category
 */
public class CreateOrUpdateCategoryActivity extends BaseActivity implements HasComponent<CategoryComponent> {

    private static final String INTENT_EXTRA_PARAM_CATEGORY_ID = "com.lundincast.INTENT_PARAM_CATEGORY_ID";
    private static final String INSTANCE_STATE_PARAM_CATEGORY_ID = "com.lundincast.STATE_PARAM_CATEGORY_ID";

    public static Intent getCallingIntent(Context context, long categoryId) {
        Intent callingIntent = new Intent(context, CreateOrUpdateCategoryActivity.class);
        callingIntent.putExtra(INTENT_EXTRA_PARAM_CATEGORY_ID, categoryId);

        return callingIntent;
    }

    @Bind(R.id.toolbar) Toolbar toolbar;
    @Bind(R.id.iv_back) ImageView iv_back;
    @Bind(R.id.iv_delete) ImageView iv_delete;

    public long categoryId = -1;
    private CategoryComponent categoryComponent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_category);
        ButterKnife.bind(this);

        setUpToolbar();
        initializeActivity(savedInstanceState);
        initializeInjector();
    }

    @Override protected void onSaveInstanceState(Bundle outState) {
        if (outState != null) {
            outState.putLong(INSTANCE_STATE_PARAM_CATEGORY_ID, this.categoryId);
        }
        super.onSaveInstanceState(outState);
    }

    private void setUpToolbar() {
        toolbar.showOverflowMenu();
        setSupportActionBar(toolbar);
    }

    /**
     * Initializes this activity.
     */
    private void initializeActivity(Bundle savedInstanceState) {
        if (savedInstanceState == null) {
            this.categoryId = getIntent().getLongExtra(INTENT_EXTRA_PARAM_CATEGORY_ID, -1);
        } else {
            this.categoryId = savedInstanceState.getLong(INSTANCE_STATE_PARAM_CATEGORY_ID);
        }
    }

    private void initializeInjector() {
        this.categoryComponent = DaggerCategoryComponent.builder()
                .categoryModule(new CategoryModule())
                .build();
    }

    @OnClick(R.id.iv_back)
    void onBackClicked() {
        finish();
    }

    @Override
    public CategoryComponent getComponent() {
        return categoryComponent;
    }
}
