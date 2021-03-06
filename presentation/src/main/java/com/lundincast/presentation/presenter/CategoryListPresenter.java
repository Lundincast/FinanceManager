package com.lundincast.presentation.presenter;

import com.lundincast.presentation.dagger.PerActivity;
import com.lundincast.presentation.dagger.components.CategoryComponent;
import com.lundincast.presentation.data.CategoryRepository;
import com.lundincast.presentation.model.CategoryModel;
import com.lundincast.presentation.view.fragment.CategoryListFragment;

import java.util.List;

import javax.inject.Inject;

import io.realm.Realm;
import io.realm.RealmChangeListener;
import io.realm.RealmResults;

/**
 * {@link Presenter} that controls communication between views and models of the presentation
 * layer.
 */
@PerActivity
public class CategoryListPresenter implements Presenter {

    private Realm realm;
    private RealmResults<CategoryModel> categoryList;
    private RealmChangeListener categoryListResultListener = new RealmChangeListener() {
        @Override
        public void onChange() {
            CategoryListPresenter.this.showCategoryCollectionInView(categoryList);
        }
    };

    private final CategoryRepository categoryRepository;

    private CategoryListFragment viewListView;

    @Inject
    public CategoryListPresenter(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
        this.realm = Realm.getDefaultInstance();
    }

    /**
     * Setter method to reference corresponding view (TransactionListFragment in this case)
     *
     * @param view
     */
    public void setView(CategoryListFragment view) {
        this.viewListView = view;
    }

    @Override
    public void resume() {
        categoryList.addChangeListener(categoryListResultListener);
    }

    @Override
    public void pause() {

    }

    @Override
    public void destroy() {
        categoryList.removeChangeListener(categoryListResultListener);
    }

    /**
     * Initializes the presenter by injecting dependencies and retrieving the category list.
     */
    public void initialize() {
        this.loadCategoryList();
    }

    /**
     * Loads categories
     */
    private void loadCategoryList() {
        this.showViewLoading();
        this.getCategoryList();
    }

    public void onCategoryClicked(CategoryModel categoryModel) {
        this.viewListView.viewCategory(categoryModel);
    }

    private void showViewLoading() {
        this.viewListView.showLoading();
    }

    private void hideViewLoading() {
        this.viewListView.hideLoading();
    }

    private void showCategoryCollectionInView(List<CategoryModel> categoryList) {
        this.hideViewLoading();
        this.viewListView.renderCategoryList(categoryList);
    }

    private void getCategoryList() {
        categoryList = realm.where(CategoryModel.class).findAll();
        showCategoryCollectionInView(categoryList);
    }
}
