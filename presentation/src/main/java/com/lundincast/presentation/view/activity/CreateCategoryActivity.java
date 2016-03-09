package com.lundincast.presentation.view.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.widget.ImageView;
import android.widget.Toast;

import com.lundincast.presentation.R;
import com.lundincast.presentation.dagger.HasComponent;
import com.lundincast.presentation.dagger.components.CategoryComponent;
import com.lundincast.presentation.dagger.components.DaggerCategoryComponent;
import com.lundincast.presentation.dagger.components.TransactionComponent;
import com.lundincast.presentation.presenter.CreateCategoryPresenter;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Activity that allows user to create a new category
 */
public class CreateCategoryActivity extends BaseActivity implements HasComponent<CategoryComponent> {

    public static Intent getCallingIntent(Context context) {
        return new Intent(context, CreateCategoryActivity.class);
    }

    @Bind(R.id.toolbar) Toolbar toolbar;
    @Bind(R.id.iv_back) ImageView iv_back;


    private CategoryComponent categoryComponent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_category);
        ButterKnife.bind(this);

        setUpToolbar();

        initializeInjector();
    }

    private void setUpToolbar() {
        toolbar.showOverflowMenu();
        setSupportActionBar(toolbar);
    }

    private void initializeInjector() {
        this.categoryComponent = DaggerCategoryComponent.builder()
                .applicationComponent(getApplicationComponent())
                .activityModule(getActivityModule())
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
