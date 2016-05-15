package com.lundincast.presentation.view;

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
    void setToolbarTitle(int title);

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
