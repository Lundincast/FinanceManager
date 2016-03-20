package com.lundincast.presentation.dagger.components;

import com.lundincast.presentation.dagger.PerActivity;
import com.lundincast.presentation.dagger.modules.ActivityModule;
import com.lundincast.presentation.dagger.modules.CategoryModule;
import com.lundincast.presentation.presenter.CategoryListPresenter;
import com.lundincast.presentation.view.fragment.CategoryListFragment;
import com.lundincast.presentation.view.fragment.CreateOrUpdateCategoryFragment;

import dagger.Component;

/**
 * A scope {@link com.lundincast.presentation.dagger.PerActivity} component.
 * Injects transaction specific Fragments.
 */
@PerActivity
@Component(modules = CategoryModule.class)
public interface CategoryComponent {

    void inject(CategoryListFragment categoryListFragment);
    void inject(CreateOrUpdateCategoryFragment createCategoryFragment);
    void inject(CategoryListPresenter categoryListPresenter);
}
