package com.lundincast.presentation.data.datasource;

import com.lundincast.presentation.data.datasource.CategoryDataStore;
import com.lundincast.presentation.model.CategoryModel;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import io.realm.Realm;
import io.realm.RealmQuery;
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
                CategoryModel category = new CategoryModel();
                if (categoryModel.getId() == -1) {
                    category.setId(realm.where(CategoryModel.class).max("id").intValue() + 1);
                } else {
                    category.setId(categoryModel.getId());
                }
                category.setName(categoryModel.getName());
                category.setColor(categoryModel.getColor());
                realm.copyToRealmOrUpdate(category);
            }
        }, null);
    }

    @Override
    public void deleteCategory(final long categoryId) {
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                CategoryModel category = realm.where(CategoryModel.class).equalTo("id", categoryId).findFirst();
                category.removeFromRealm();
            }
        }, null);
    }
}
