package com.lundincast.presentation.dagger.modules;

import com.lundincast.presentation.data.CategoryRepository;
import com.lundincast.presentation.presenter.CategoryListPresenter;

import dagger.Module;
import dagger.Provides;

/**
 * Dagger module that provides category related collaborators.
 */
@Module
public class CategoryModule {

    public CategoryModule() {}

    @Provides
    CategoryListPresenter provideCategoryListPresenter(CategoryRepository categoryRepository) {
        return new CategoryListPresenter(categoryRepository);
    }
}
