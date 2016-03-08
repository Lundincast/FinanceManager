package com.lundincast.presentation.view.adapter;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;

/**
 * Layout manager to position category items inside a {@link android.support.v7.widget.RecyclerView}.
 */
public class CategoriesLayoutManager extends LinearLayoutManager {

    /**
     * Creates a vertical LinearLayoutManager
     *
     * @param context Current context, will be used to access resources.
     */
    public CategoriesLayoutManager(Context context) {
        super(context);
    }
}
