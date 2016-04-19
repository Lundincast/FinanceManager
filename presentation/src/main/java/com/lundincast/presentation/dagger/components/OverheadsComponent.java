package com.lundincast.presentation.dagger.components;

import com.lundincast.presentation.dagger.PerActivity;
import com.lundincast.presentation.dagger.modules.OverheadsModule;
import com.lundincast.presentation.view.fragment.OverheadsListFragment;

import dagger.Component;

/**
 * A scope {@link com.lundincast.presentation.dagger.PerActivity} component.
 * Injects overheads specific Fragments.
 */
@PerActivity
@Component(dependencies = ApplicationComponent.class, modules = OverheadsModule.class)
public interface OverheadsComponent {

    void inject(OverheadsListFragment overheadsListFragment);
}
