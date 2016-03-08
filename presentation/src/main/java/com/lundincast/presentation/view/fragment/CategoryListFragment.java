package com.lundincast.presentation.view.fragment;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.lundincast.presentation.R;
import com.lundincast.presentation.dagger.components.CategoryComponent;
import com.lundincast.presentation.model.CategoryModel;
import com.lundincast.presentation.presenter.CategoryListPresenter;
import com.lundincast.presentation.presenter.Presenter;
import com.lundincast.presentation.view.CategoryListView;
import com.lundincast.presentation.view.adapter.CategoriesAdapter;
import com.lundincast.presentation.view.adapter.CategoriesLayoutManager;
import com.lundincast.presentation.view.utilities.SimpleDividerItemDecoration;

import java.util.ArrayList;
import java.util.Collection;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * {@link Presenter} that controls communication between views and models of the presentation
 * layer.
 */
public class CategoryListFragment extends BaseFragment implements CategoryListView {

    /**
     * Interface for listening category list events.
     */
    public interface CategoryListListener {
        void onCategoryClicked(final CategoryModel categoryModel);
    }

    @Inject
    CategoryListPresenter categoryListPresenter;

    @Bind(R.id.ll_loading) LinearLayout ll_loading;
    @Bind(R.id.rv_categories) RecyclerView rv_categories;

    private CategoriesAdapter categoriesAdapter;
    private CategoriesLayoutManager categoriesLayoutManager;

    private CategoryListListener categoryListListener;

    public CategoryListFragment() {
        super();
    }

    @Override public void onAttach(Activity activity) {
        super.onAttach(activity);
        if (activity instanceof CategoryListListener) {
            this.categoryListListener = (CategoryListListener) activity;
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View fragmentView = inflater.inflate(R.layout.fragment_category_list, container, false);
        ButterKnife.bind(this, fragmentView);
        setupUi();

        return fragmentView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        this.initialize();
        this.loadCategoryList();
    }

    @Override public void onResume() {
        super.onResume();
        this.categoryListPresenter.resume();
    }

    @Override public void onPause() {
        super.onPause();
        this.categoryListPresenter.pause();
    }

    @Override public void onDestroy() {
        super.onDestroy();
        this.categoryListPresenter.destroy();
    }

    @Override public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    @Override
    public void showLoading() {
        this.ll_loading.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideLoading() {
        this.ll_loading.setVisibility(View.GONE);
    }

    /**
     * Show an error message
     *
     * @param message A string representing an error.
     */
    public void showError(String message) {
        this.showToastMessage(message);
    }

    private void initialize() {
        this.getComponent(CategoryComponent.class).inject(this);
        this.categoryListPresenter.setView(this);
    }

    private void setupUi() {
        this.categoriesLayoutManager = new CategoriesLayoutManager(getActivity());
        this.rv_categories.setLayoutManager(this.categoriesLayoutManager);
        this.rv_categories.addItemDecoration(new SimpleDividerItemDecoration(getActivity()));

        this.categoriesAdapter = new CategoriesAdapter(getActivity(), new ArrayList<CategoryModel>());
        this.categoriesAdapter.setOnItemClickListener(onItemClickListener);
        this.rv_categories.setAdapter(this.categoriesAdapter);
    }

    /**
     * Render a category list in the UI.
     *
     * @param categoryModelCollection The collection of {@link CategoryModel} that will be shown.
     */
    @Override
    public void renderCategoryList(Collection<CategoryModel> categoryModelCollection) {
        if (categoryModelCollection != null) {
            this.categoriesAdapter.setCategoryCollection(categoryModelCollection);
        }
    }

    /**
     * View a {@link CategoryModel} details.
     *
     * @param categoryModel The category that will be shown.
     */
    @Override
    public void viewCategory(CategoryModel categoryModel) {
        if (this.categoryListListener != null) {
            this.categoryListListener.onCategoryClicked(categoryModel);
        }
    }

    /**
     * Loads all categories.
     */
    private void loadCategoryList() {
        this.categoryListPresenter.initialize();
    }

    private CategoriesAdapter.OnItemClickListener onItemClickListener =
            new CategoriesAdapter.OnItemClickListener() {
                @Override
                public void onCategoryItemClicked(CategoryModel categoryModel) {
                    if (CategoryListFragment.this.categoryListPresenter != null && categoryModel != null) {
                        CategoryListFragment.this.categoryListPresenter.onCategoryClicked(categoryModel);
                    }
                }
            };
}
