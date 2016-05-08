package com.lundincast.presentation.view;

import com.lundincast.presentation.model.CategoryModel;

/**
 * Utility interface to reuse CategoryListForNewTransactionFragment in transaction and overhead creation
 */
public interface CreateItemUtility {

    void onCategorySet(CategoryModel categoryModel);
}
