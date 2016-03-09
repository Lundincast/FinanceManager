package com.lundincast.presentation.dagger.components;

import com.lundincast.presentation.dagger.PerActivity;
import com.lundincast.presentation.dagger.modules.ActivityModule;
import com.lundincast.presentation.dagger.modules.CategoryModule;
import com.lundincast.presentation.view.activity.CreateCategoryActivity;
import com.lundincast.presentation.view.fragment.CategoryListFragment;
import com.lundincast.presentation.view.fragment.CreateCategoryFragment;

import dagger.Component;

/**
 * A scope {@link com.lundincast.presentation.dagger.PerActivity} component.
 * Injects transaction specific Fragments.
 */
@PerActivity
@Component(dependencies = ApplicationComponent.class, modules = {ActivityModule.class, CategoryModule.class})
public interface CategoryComponent {

    void inject(CategoryListFragment categoryListFragment);
    void inject(CreateCategoryFragment createCategoryFragment);
}
