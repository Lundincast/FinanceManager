package com.lundincast.presentation.view.fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.lundincast.presentation.R;
import com.lundincast.presentation.model.CategoryModel;
import com.lundincast.presentation.view.activity.CreateTransactionActivity;
import com.lundincast.presentation.view.adapter.CategoriesAdapter;
import com.lundincast.presentation.view.adapter.CategoriesLayoutManager;
import com.lundincast.presentation.view.utilities.SimpleDividerItemDecoration;

import java.util.ArrayList;
import java.util.Collection;

import butterknife.Bind;
import butterknife.ButterKnife;
import io.realm.Realm;
import io.realm.RealmResults;

/**
 * A {@link Fragment} subclass for choosing categories when creating new transaction
 */
public class CategoryListForNewTransactionFragment extends BaseFragment {

    /**
     * Interface for listening category list events.
     */
    public interface CategoryListListener {
        void onCategoryClicked(final CategoryModel categoryModel);
    }

    @Bind(R.id.rv_categories) RecyclerView rv_categories;

    private Realm realm;
    private RealmResults<CategoryModel> categoryList;

    private CategoriesAdapter categoriesAdapter;
    private CategoriesLayoutManager categoriesLayoutManager;

    private CategoryListListener categoryListListener;

    public CategoryListForNewTransactionFragment() {
        super();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View fragmentView = inflater.inflate(R.layout.fragment_create_transaction_category_list, container, false);
        ButterKnife.bind(this, fragmentView);
        setupUi();

        return fragmentView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onStart() {
        super.onStart();
        realm = Realm.getDefaultInstance();
        this.loadCategoryList();
    }

    @Override
    public void onStop() {
        super.onStop();
        realm.close();
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
    public void renderCategoryList(Collection<CategoryModel> categoryModelCollection) {
        if (categoryModelCollection != null) {
            this.categoriesAdapter.setCategoryCollection(categoryModelCollection);
        }
    }

    private void loadCategoryList() {
        categoryList = realm.where(CategoryModel.class).findAll();
        renderCategoryList(categoryList);
    }

    private void onCategorySelected(CategoryModel categoryModel) {
        ((CreateTransactionActivity) getActivity()).onCategorySet(categoryModel);
    }

    private CategoriesAdapter.OnItemClickListener onItemClickListener =
            new CategoriesAdapter.OnItemClickListener() {
                @Override
                public void onCategoryItemClicked(CategoryModel categoryModel) {
                    CategoryListForNewTransactionFragment.this.onCategorySelected(categoryModel);
                }
            };
}
