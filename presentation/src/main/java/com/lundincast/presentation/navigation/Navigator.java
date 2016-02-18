package com.lundincast.presentation.navigation;

import android.content.Context;
import android.content.Intent;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * Class used to navigate through the application.
 */
@Singleton
public class Navigator {

    @Inject
    public void Navigator() {}

    /**
     * Goes to Settings screen
     *
     * @param context A Context needed to open the destiny activity.
     */
    public void navigateToSettings(Context context) {
        if (context != null) {
//            Intent intentToLaunch = SettingsActivity.getCallingIntent(context);
//            context.startActivity(intentToLaunch);
        }
    }
}
