package com.lundincast.presentation.view;

import android.support.annotation.StringRes;

import com.afollestad.materialdialogs.MaterialDialog;

/**
 * Interface representing a View in a model view Presenter (MVP) pattern.
 * In this case it is used as a view representing a detail view of an entity.
 */
public interface CreateOrUpdateView {

    /**
     * Display title
     *
     * @param title The title that will be displayed.
     */
    void setToolbarTitle(@StringRes int title);

    /**
     * Display name (of a Category or an Account for example)
     *
     * @param name The name that will be displayed.
     */
    void setName(String name);

    /**
     * Display color (of a Category or an Account for example)
     *
     * @param color The color that will be displayed.
     */
    void setColor(int color);

    /**
     * Display balance label text
     *
     * @param stringResource The string resource that will be displayed.
     */
    void setBalanceLabel(@StringRes int stringResource);

    /**
     * Display balance
     *
     * @param balance The balance that will be displayed.
     */
    void setBalance(double balance);

    /**
     * Display currency icon
     *
     * @param currency The currency icon that will be displayed.
     */
    void setCurrency(String currency);

    /**
     * Display delete icon
     *
     */
    void showDeleteIcon();

    /**
     * Display dialog
     *
     */
    void showDialog(MaterialDialog.Builder builder);

    /**
     * Finish current activity.
     */
    void closeActivity();
}
