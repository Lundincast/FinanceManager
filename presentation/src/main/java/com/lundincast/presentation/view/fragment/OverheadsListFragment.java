package com.lundincast.presentation.view.fragment;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.lundincast.presentation.R;
import com.lundincast.presentation.dagger.components.OverheadsComponent;
import com.lundincast.presentation.model.OverheadModel;
import com.lundincast.presentation.presenter.OverheadsListPresenter;
import com.lundincast.presentation.view.OverheadsListView;
import com.lundincast.presentation.view.adapter.OverheadsAdapter;
import com.lundincast.presentation.view.adapter.TransactionsLayoutManager;
import com.lundincast.presentation.view.utilities.SimpleDividerItemDecoration;

import java.util.ArrayList;
import java.util.Collection;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * A {@link Fragment} subclass for listing overheads
 */
public class OverheadsListFragment extends BaseFragment implements OverheadsListView {

    /**
     * Interface for listening overheads list events.
     */
    public interface OverheadsListListener {
        void onOverheadsClicked(final OverheadModel overheadModel);
    }

    @Inject
    OverheadsListPresenter overheadsListPresenter;

    @Bind(R.id.ll_loading) LinearLayout ll_loading;
    @Bind(R.id.ll_empty_list) LinearLayout ll_empty_list;
    @Bind(R.id.rv_overheads) RecyclerView rv_overheads;

    private OverheadsAdapter overheadsAdapter;
    private TransactionsLayoutManager transactionsLayoutManager;

    private OverheadsListListener overheadsListListener;


    public OverheadsListFragment() {
        super();
    }

    // TODO refactor to new method onAttach(Context context)
    @Override public void onAttach(Activity activity) {
        super.onAttach(activity);
        if (activity instanceof OverheadsListListener) {
            this.overheadsListListener = (OverheadsListListener) activity;
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View fragmentView = inflater.inflate(R.layout.fragment_overheads_list, container, false);
        ButterKnife.bind(this, fragmentView);
        setupUi();

        return fragmentView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        this.initialize();
        this.loadOverheadsList();
    }

    @Override public void onResume() {
        super.onResume();
        this.overheadsListPresenter.resume();
    }

    @Override public void onPause() {
        super.onPause();
        this.overheadsListPresenter.pause();

    }

    @Override public void onDestroy() {
        super.onDestroy();
        this.overheadsListPresenter.destroy();
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

    @Override
    public void showEmptyListMessage() {
        this.ll_empty_list.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideEmptyListMessage() {
        this.ll_empty_list.setVisibility(View.GONE);
    }

    private void initialize() {
        this.getComponent(OverheadsComponent.class).inject(this);
        this.overheadsListPresenter.setView(this);
    }

    private void setupUi() {
        this.transactionsLayoutManager = new TransactionsLayoutManager(getActivity());
        this.rv_overheads.setLayoutManager(this.transactionsLayoutManager);
        this.rv_overheads.addItemDecoration(new SimpleDividerItemDecoration(getActivity()));

        this.overheadsAdapter = new OverheadsAdapter(getActivity(), new ArrayList<OverheadModel>());
        this.overheadsAdapter.setOnItemClickListener(onItemClickListener);
        this.rv_overheads.setAdapter(overheadsAdapter);
    }

    /**
     * Render a list of overheads in the UI.
     *
     * @param overheadModelCollection The collection of {@link OverheadModel} that will be shown.
     */
    @Override
    public void renderOverheadsList(Collection<OverheadModel> overheadModelCollection) {
        if (overheadModelCollection != null) {
            this.overheadsAdapter.setOverheadsCollection(overheadModelCollection);
        }
    }

    /**
     * View a {@link OverheadModel} details.
     *
     * @param overheadModel The overheads that will be shown.
     */
    @Override
    public void viewOverheads(OverheadModel overheadModel) {
        if (this.overheadsListListener != null) {
            this.overheadsListListener.onOverheadsClicked(overheadModel);
        }
    }

    /**
     * Loads overheads
     */
    private void loadOverheadsList() {
        this.overheadsListPresenter.initialize();
    }

    private OverheadsAdapter.OnItemClickListener onItemClickListener =
            new OverheadsAdapter.OnItemClickListener() {
                @Override
                public void onOverheadsItemClicked(OverheadModel overheadModel) {
                    if (OverheadsListFragment.this.overheadsListPresenter != null && overheadModel != null) {
                        OverheadsListFragment.this.overheadsListPresenter.onOverheadsClicked(overheadModel);
                    }
                }
            };
}
