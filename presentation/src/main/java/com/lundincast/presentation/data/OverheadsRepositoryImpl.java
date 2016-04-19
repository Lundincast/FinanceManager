package com.lundincast.presentation.data;

import com.lundincast.presentation.dagger.PerActivity;
import com.lundincast.presentation.data.datasource.OverheadsDataStore;
import com.lundincast.presentation.model.OverheadModel;

import javax.inject.Inject;

/**
 * {@link OverheadsRepository} implementation for retrieving overheads data.
 */
@PerActivity
public class OverheadsRepositoryImpl implements OverheadsRepository {

    private final OverheadsDataStore overheadsDataStore;

    /**
     * Constructs a {@link OverheadsRepository}.
     *
     * @param overheadsDataStore A factory to construct different data source implementations.
     */
    @Inject
    public OverheadsRepositoryImpl(OverheadsDataStore overheadsDataStore) {
        this.overheadsDataStore = overheadsDataStore;
    }

    @Override
    public void saveOverhead(OverheadModel overheadModel) {
        this.overheadsDataStore.saveOverhead(overheadModel);
    }

    @Override
    public void deleteOverhead(int overheadId) {
        this.overheadsDataStore.deleteOverhead(overheadId);
    }
}
