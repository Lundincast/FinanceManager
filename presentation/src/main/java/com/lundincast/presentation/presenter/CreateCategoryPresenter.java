package com.lundincast.presentation.presenter;

import com.lundincast.presentation.data.CategoryRepository;
import com.lundincast.presentation.model.CategoryModel;

import javax.inject.Inject;

/**
 * {@link Presenter} that controls communication between views and models of the presentation
 * layer.
 */
public class CreateCategoryPresenter implements Presenter {

    private final CategoryRepository categoryRepository;

    @Inject
    public CreateCategoryPresenter(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    @Override
    public void resume() {

    }

    @Override
    public void pause() {

    }

    @Override
    public void destroy() {

    }

    public void saveCategory(Long categoryId, String categoryName, String categoryColor) {
        if (!categoryName.equals("")) {
            CategoryModel categoryModel = new CategoryModel();
            categoryModel.setId(categoryId);
            categoryModel.setName(categoryName);
            categoryModel.setColor(categoryColor);
            this.categoryRepository.saveCategory(categoryModel);
        }
    }

    public void deleteCategory(Long categoryId) {
        this.categoryRepository.deleteCategory(categoryId);
    }
}
