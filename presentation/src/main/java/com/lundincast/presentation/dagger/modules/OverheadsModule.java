package com.lundincast.presentation.dagger.modules;

import com.lundincast.presentation.dagger.PerActivity;
import com.lundincast.presentation.data.OverheadsRepository;
import com.lundincast.presentation.data.OverheadsRepositoryImpl;
import com.lundincast.presentation.data.datasource.DiskOverheadsDataStore;
import com.lundincast.presentation.data.datasource.OverheadsDataStore;

import dagger.Module;
import dagger.Provides;
import io.realm.Realm;

/**
 * Dagger module that provides overheads related collaborators.
 */
@Module
public class OverheadsModule {

    private int overheadId = -1;

    public OverheadsModule() {}

    public OverheadsModule(int overheadId) {
        this.overheadId = overheadId;
    }

    @Provides @PerActivity
    OverheadsDataStore provideOverheadsDataStore() {
        return new DiskOverheadsDataStore(Realm.getDefaultInstance());
    }

    @Provides @PerActivity
    OverheadsRepository provideOverheadsRepository(OverheadsDataStore overheadsDataStore) {
        return new OverheadsRepositoryImpl(overheadsDataStore);
    }
}
