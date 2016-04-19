package com.lundincast.presentation.data.datasource;

import com.lundincast.presentation.model.OverheadModel;

/**
 * Interface that represents a data store from where overheads data is retrieved.
 */
public interface OverheadsDataStore {

    /**
     * Save a {@link OverheadModel} in database
     */
    void saveOverhead(final OverheadModel overheadModel);

    /**
     * Delete a {@link OverheadModel} in database
     */
    void deleteOverhead(final int overheadId);
}
