package com.lundincast.presentation;

import android.app.Application;

import com.lundincast.presentation.dagger.components.ApplicationComponent;
import com.lundincast.presentation.dagger.components.DaggerApplicationComponent;
import com.lundincast.presentation.dagger.modules.ApplicationModule;

import io.realm.Realm;
import io.realm.RealmConfiguration;

/**
 * Android Main Application
 */
public class AndroidApplication extends Application {

    private ApplicationComponent applicationComponent;

    @Override public void onCreate() {
        super.onCreate();
        this.initializeInjector();
        this.setRealmDefaultConfig();
    }

    private void initializeInjector() {
        this.applicationComponent = DaggerApplicationComponent.builder()
                .applicationModule(new ApplicationModule(this))
                .build();
    }

    private void setRealmDefaultConfig() {
        RealmConfiguration config = new RealmConfiguration.Builder(this)
                .name("financemanager.realm")
                .schemaVersion(1)
                .build();
        Realm.setDefaultConfiguration(config);
    }

    public ApplicationComponent getApplicationComponent() {
        return this.applicationComponent;
    }
}
