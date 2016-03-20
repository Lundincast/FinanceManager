package com.lundincast.presentation.data;

import com.lundincast.presentation.dagger.PerActivity;
import com.lundincast.presentation.data.datasource.CategoryDataStore;
import com.lundincast.presentation.model.CategoryModel;

import java.io.IOException;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * {@link CategoryRepository} implementation for retrieving category data.
 */
@PerActivity
public class CategoryRepositoryImpl implements CategoryRepository {

    private final CategoryDataStore categoryDataStore;

    /**
     * Constructs a {@link CategoryRepository}.
     *
     * @param categoryDataStore A factory to construct different data source implementations.
     */
    @Inject
    public CategoryRepositoryImpl(CategoryDataStore categoryDataStore) {
        this.categoryDataStore = categoryDataStore;
    }

    @Override
    public List<CategoryModel> categories() {
        List<CategoryModel> categoryList = null;
        try {
            categoryList = categoryDataStore.categoryList();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return categoryList;
    }

    @Override
    public void saveCategory(CategoryModel categoryModel) {
        this.categoryDataStore.saveCategory(categoryModel);
    }

    @Override
    public void deleteCategory(long categoryId) {
        this.categoryDataStore.deleteCategory(categoryId);
    }
}
