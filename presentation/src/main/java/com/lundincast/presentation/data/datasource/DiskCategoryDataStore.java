package com.lundincast.presentation.data.datasource;

import com.lundincast.presentation.data.datasource.CategoryDataStore;
import com.lundincast.presentation.model.CategoryModel;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import io.realm.Realm;
import io.realm.RealmResults;

/**
 * {@link CategoryDataStore} implementation based on database on disk.
 */
public class DiskCategoryDataStore implements CategoryDataStore {

    private Realm realm;

    @Inject
    public DiskCategoryDataStore(Realm realm) {
        this.realm = realm;
    }

    @Override
    public List<CategoryModel> categoryList() throws IOException {

        // retrieve all categories
        RealmResults<CategoryModel> result = realm.where(CategoryModel.class).findAll();

        // convert to a List of CategoryModel
        List<CategoryModel> categoryModelList = new ArrayList<>();
        for (CategoryModel categoryModel : result) {
            categoryModelList.add(categoryModel);
        }

        return categoryModelList;
    }

    @Override
    public void saveCategory(final CategoryModel categoryModel) {
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                CategoryModel category = realm.createObject(CategoryModel.class);
                category.setId(realm.where(CategoryModel.class).max("Id").intValue() + 1);
                category.setName(categoryModel.getName());
                category.setColor(categoryModel.getColor());
            }
        }, null);

    }
}
