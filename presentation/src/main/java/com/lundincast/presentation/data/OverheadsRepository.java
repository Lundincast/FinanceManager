package com.lundincast.presentation.data;

import com.lundincast.presentation.model.OverheadModel;

/**
 * Public interface that represents a Repository for getting {@link OverheadModel} related data.
 */
public interface OverheadsRepository {

    /**
     * Save a {@link OverheadModel} in database
     */
    void saveOverhead(final OverheadModel overheadModel);

    /**
     * Delete a {@link OverheadModel} in database
     */
    void deleteOverhead(final int overheadId);
}
