package com.lundincast.presentation.presenter;

import com.lundincast.presentation.data.CategoryRepository;
import com.lundincast.presentation.model.CategoryModel;

import javax.inject.Inject;

import io.realm.Realm;

/**
 * {@link Presenter} that controls communication between views and models of the presentation
 * layer.
 */
public class CreateCategoryPresenter implements Presenter {

    private Realm realm;

    private final CategoryRepository categoryRepository;

    @Inject
    public CreateCategoryPresenter(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
        this.realm = Realm.getDefaultInstance();
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

    public boolean isDuplicate(String categoryName) {
        return (realm.where(CategoryModel.class).equalTo("name", categoryName).findFirst() != null);
    }

    public void saveCategory(Long categoryId, String categoryName, int categoryColor) {
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
