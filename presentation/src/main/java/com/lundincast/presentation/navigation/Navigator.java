package com.lundincast.presentation.navigation;

import android.content.Context;
import android.content.Intent;

import com.lundincast.presentation.view.activity.CategoryListActivity;
import com.lundincast.presentation.view.activity.CreateTransactionActivity;
import com.lundincast.presentation.view.activity.SettingsActivity;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * Class used to navigate through the application.
 */
@Singleton
public class Navigator {

    @Inject
    public Navigator() {}

    /**
     * Goes to Settings screen
     *
     * @param context A Context needed to open the destiny activity.
     */
    public void navigateToSettings(Context context) {
        if (context != null) {
            Intent intentToLaunch = SettingsActivity.getCallingIntent(context);
            context.startActivity(intentToLaunch);
        }
    }

    /**
     * Goes to create transaction screen
     *
     * @param context A Context needed to open the destiny activity.
     */
    public void navigateToCreateTransaction(Context context) {
        if (context != null) {
            Intent intentToLaunch = CreateTransactionActivity.getCallingIntent(context);
            context.startActivity(intentToLaunch);
        }
    }

    /**
     * Goes to list categories screen
     *
     * @param context A Context needed to open the destiny activity.
     */
    public void navigateToListCategories(Context context) {
        if (context != null) {
            Intent intentToLaunch = CategoryListActivity.getCallingIntent(context);
            context.startActivity(intentToLaunch);
        }
    }
}
