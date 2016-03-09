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

    public String categoryName = "test";
    public String categoryColor = "red";

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

    public void saveCategory() {
        if (categoryName.equals("")) {
            // TODO display error message
        } else {
            CategoryModel categoryModel = new CategoryModel();
            categoryModel.setName(this.categoryName);
            categoryModel.setColor(this.categoryColor);
            this.categoryRepository.saveCategory(categoryModel);
        }
    }
}
