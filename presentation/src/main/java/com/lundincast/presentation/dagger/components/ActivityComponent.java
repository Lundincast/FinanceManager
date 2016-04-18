package com.lundincast.presentation.dagger.components;

import android.app.Activity;

import com.lundincast.presentation.dagger.PerActivity;
import com.lundincast.presentation.dagger.modules.ActivityModule;

import dagger.Component;

/**
 * A base component upon which fragment's components may depend.
 * Activity-level components should extend this component.
 *
 * Subtypes of ActivityComponent should be decorated with annotation:
 * {@link com.lundincast.presentation.dagger.PerActivity}
 */
@PerActivity
@Component(dependencies = ApplicationComponent.class, modules = ActivityModule.class)
public interface ActivityComponent {

    //Exposed to sub-graphs.
    Activity activity();
}
