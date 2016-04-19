package com.lundincast.presentation.view;

import com.lundincast.presentation.model.OverheadModel;

import java.util.Collection;

/**
 * Interface representing a View in a model view Presenter (MVP) pattern.
 * In this case it is used as a view representing a list of {@link OverheadModel}.
 */
public interface OverheadsListView extends LoadDataView {

    /**
     * Render a list of overheads in the UI.
     *
     * @param overheadModelCollection The collection of {@link OverheadModel} that will be shown.
     */
    void renderOverheadsList(Collection<OverheadModel> overheadModelCollection);

    /**
     * Display a message when empty list
     *
     */
    void showEmptyListMessage();

    /**
     * Hide the empty list message
     */
    void hideEmptyListMessage();

    /**
     * View a {@link OverheadModel} details.
     *
     * @param overheadModel The overheads that will be shown.
     */
    void viewOverheads(OverheadModel overheadModel);
}
