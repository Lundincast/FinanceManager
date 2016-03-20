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
import com.lundincast.presentation.model.CategoryModel;
import com.lundincast.presentation.navigation.Navigator;
import com.lundincast.presentation.view.fragment.CategoryListFragment;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Activity to display the list of categories.
 */
public class CategoryListActivity extends BaseActivity implements HasComponent<CategoryComponent>,
                                                                CategoryListFragment.CategoryListListener {

    public static Intent getCallingIntent(Context context) {
        return new Intent(context, CategoryListActivity.class);
    }

    @Bind(R.id.toolbar) Toolbar toolbar;
    @Bind(R.id.iv_back) ImageView iv_back;
    @Bind(R.id.iv_add_icon) ImageView iv_add_icon;

    @Inject Navigator navigator;

    private CategoryComponent categoryComponent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_category);
        ButterKnife.bind(this);

        setUpToolbar();

        initializeInjector();
    }

    private void setUpToolbar() {
        toolbar.showOverflowMenu();
        setSupportActionBar(toolbar);
    }

    private void initializeInjector() {
        this.getApplicationComponent().inject(this);
        this.categoryComponent = DaggerCategoryComponent.builder()
                .categoryModule(new CategoryModule())
                .build();
    }

    @OnClick(R.id.iv_back)
    void onBackClicked() {
        finish();
    }

    @OnClick(R.id.iv_add_icon)
    void onAddIconClicked() {
        this.navigator.navigateToCreateOrUpdateCategory(this, -1);
    }

    @Override
    public CategoryComponent getComponent() {
        return categoryComponent;
    }

    @Override
    public void onCategoryClicked(CategoryModel categoryModel) {
        this.navigator.navigateToCreateOrUpdateCategory(this, categoryModel.getId());
    }
}
