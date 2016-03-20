package com.lundincast.presentation.dagger.modules;

import com.lundincast.presentation.data.CategoryRepository;
import com.lundincast.presentation.data.CategoryRepositoryImpl;
import com.lundincast.presentation.data.datasource.CategoryDataStore;
import com.lundincast.presentation.data.datasource.DiskCategoryDataStore;
import com.lundincast.presentation.presenter.CategoryListPresenter;
import com.lundincast.presentation.presenter.CreateCategoryPresenter;

import dagger.Module;
import dagger.Provides;
import io.realm.Realm;

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

    @Provides
    CreateCategoryPresenter provideCreateCategoryPresenter(CategoryRepository categoryRepository) {
        return new CreateCategoryPresenter(categoryRepository);
    }

    @Provides
    CategoryDataStore provideCategoryDataStore() {
        return new DiskCategoryDataStore(Realm.getDefaultInstance());
    }

    @Provides
    CategoryRepository provideCategoryRepository(CategoryRepositoryImpl categoryDataRepository) {
        return categoryDataRepository;
    }
}
