package com.lundincast.presentation.data.datasource;

import com.lundincast.presentation.model.CategoryModel;

import java.io.IOException;
import java.util.List;

/**
 * Interface that represents a data store from where category data is retrieved.
 */
public interface CategoryDataStore {

    /**
     * Get a {@link List} of {@link CategoryModel}.
     */
    List<CategoryModel> categoryList() throws IOException;

    /**
     * Save a {@link CategoryModel} in database
     */
    void saveCategory(final CategoryModel categoryModel);

    /**
     * Delete a {@link CategoryModel} by id in database
     */
    void deleteCategory(final long categoryId);
}
