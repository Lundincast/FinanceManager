package com.lundincast.presentation.presenter;

import com.lundincast.presentation.dagger.PerActivity;
import com.lundincast.presentation.model.CategoryModel;
import com.lundincast.presentation.model.OverheadModel;
import com.lundincast.presentation.view.OverheadsListView;
import com.lundincast.presentation.view.fragment.OverheadsListFragment;

import java.util.List;

import javax.inject.Inject;

import io.realm.Realm;
import io.realm.RealmChangeListener;
import io.realm.RealmResults;
import io.realm.Sort;

/**
 * {@link Presenter} that controls communication between views and models of the presentation
 * layer.
 */
@PerActivity
public class OverheadsListPresenter implements Presenter {

    private final Realm realm;
    private RealmResults<OverheadModel> overheadsList;
    private RealmChangeListener overheadsListResultListener = new RealmChangeListener() {
        @Override
        public void onChange() {
            OverheadsListPresenter.this.showOverheadsCollectionInView(overheadsList);
        }
    };

    private OverheadsListView viewListView;

    @Inject
    public OverheadsListPresenter() {
        this.realm = Realm.getDefaultInstance();
    }

    /**
     * Setter method to reference corresponding view (TransactionListFragment in this case)
     *
     * @param view
     */
    public void setView(OverheadsListFragment view) {
        this.viewListView = view;
    }

    @Override
    public void resume() {
        overheadsList.addChangeListener(overheadsListResultListener);
    }

    @Override
    public void pause() {

    }

    @Override
    public void destroy() {
        overheadsList.removeChangeListener(overheadsListResultListener);
    }

    /**
     * Initializes the presenter by retrieving the overheads list.
     */
    public void initialize() {
        this.loadOverheadsList();
    }

    /**
     * Loads transactions.
     */
    private void loadOverheadsList() {
        this.showViewLoading();
        this.getOverheadsList();
    }

    public void onOverheadsClicked(OverheadModel overheadModel) {
        this.viewListView.viewOverheads(overheadModel);
    }

    private void showViewLoading() {
        this.viewListView.showLoading();
    }

    private void hideViewLoading() {
        this.viewListView.hideLoading();
    }

    private void showEmptyListMessage() { this.viewListView.showEmptyListMessage(); }

    private void hideEmptyListMessage() { this.viewListView.hideEmptyListMessage(); }

    private void showOverheadsCollectionInView(List<OverheadModel> overheadsList) {
        this.hideViewLoading();
        if (overheadsList == null || overheadsList.size() == 0) {
            this.showEmptyListMessage();
        } else {
            this.hideEmptyListMessage();
        }
        this.viewListView.renderOverheadsList(overheadsList);
    }

    private void getOverheadsList() {
        overheadsList = realm.where(OverheadModel.class).findAll();
        overheadsList.sort("dayOfMonth", Sort.ASCENDING);
        showOverheadsCollectionInView(overheadsList);
    }
}
