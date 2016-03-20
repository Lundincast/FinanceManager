package com.lundincast.presentation.navigation;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.ActivityOptionsCompat;
import android.view.View;

import com.lundincast.presentation.view.activity.CategoryListActivity;
import com.lundincast.presentation.view.activity.CreateOrUpdateCategoryActivity;
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
    public void navigateToCreateTransaction(Context context, int transactionId) {
        if (context != null) {
            Intent intentToLaunch = CreateTransactionActivity.getCallingIntent(context, transactionId);
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

    /**
     * Goes to create category screen
     *
     * @param context A Context needed to open the destiny activity.
     */
    public void navigateToCreateOrUpdateCategory(Context context, long categoryId) {
        if (context != null) {
            Intent intentToLaunch = CreateOrUpdateCategoryActivity.getCallingIntent(context, categoryId);
            context.startActivity(intentToLaunch);
        }
    }
}
