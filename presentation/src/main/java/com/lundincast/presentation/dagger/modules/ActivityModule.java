package com.lundincast.presentation.dagger.modules;

import android.app.Activity;

import com.lundincast.presentation.dagger.PerActivity;
import com.lundincast.presentation.presenter.Presenter;

import dagger.Module;
import dagger.Provides;

/**
 * A module to wrap the Activity state and expose it to the graph.
 */
@Module
public class ActivityModule {

    private final Activity activity;

    public ActivityModule(Activity activity) {
        this.activity = activity;
    }

    /**
     * Expose the activity to dependents in the graph.
     */
    @Provides @PerActivity Activity activity() {
        return this.activity;
    }

}
