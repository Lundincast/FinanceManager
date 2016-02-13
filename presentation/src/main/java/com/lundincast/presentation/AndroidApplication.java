package com.lundincast.presentation;

import android.app.Application;

import com.lundincast.presentation.dagger.components.ApplicationComponent;
import com.lundincast.presentation.dagger.components.DaggerApplicationComponent;
import com.lundincast.presentation.dagger.modules.ApplicationModule;

/**
 * Android Main Application
 */
public class AndroidApplication extends Application {

    private ApplicationComponent applicationComponent;

    @Override public void onCreate() {
        super.onCreate();
        this.initializeInjector();
    }

    private void initializeInjector() {
        this.applicationComponent = DaggerApplicationComponent.builder()
                .applicationModule(new ApplicationModule(this))
                .build();
    }

    public ApplicationComponent getApplicationComponent() {
        return this.applicationComponent;
    }
}
