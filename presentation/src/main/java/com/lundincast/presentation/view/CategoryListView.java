package com.lundincast.presentation.view;

import com.lundincast.presentation.model.CategoryModel;

import java.util.Collection;

/**
 * Interface representing a View in a model view Presenter (MVP) pattern.
 * In this case it is used as a view representing a list of {@link CategoryModel}.
 */
public interface CategoryListView extends LoadDataView {

    /**
     * Render a list of categories in the UI.
     *
     * @param categoryModelCollection The collection of {@link CategoryModel} that will be shown.
     */
    void renderCategoryList(Collection<CategoryModel> categoryModelCollection);

    /**
     * View a {@link CategoryModel} details.
     *
     * @param categoryModel The category that will be shown.
     */
    void viewCategory(CategoryModel categoryModel);
}
