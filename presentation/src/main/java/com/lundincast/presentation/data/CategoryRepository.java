package com.lundincast.presentation.data;

import com.lundincast.presentation.model.CategoryModel;

import java.util.List;

/**
 * Public interface that represents a Repository for getting {@link CategoryModel} related data.
 */
public interface CategoryRepository {

    /**
     * Get a {@link List} of {@link CategoryModel}.
     */
    List<CategoryModel> categories();

    /**
     * Save a {@link CategoryModel} in database
     */
    void saveCategory(final CategoryModel categoryModel);

    /**
     * Delete a {@link CategoryModel} by id in database
     */
    void deleteCategory(final long categoryId);
}
