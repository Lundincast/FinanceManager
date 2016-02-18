package com.lundincast.presentation.view.adapter;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;

/**
 * Layout manager to position transaction items inside a {@link android.support.v7.widget.RecyclerView}.
 */
public class TransactionsLayoutManager extends LinearLayoutManager {

    /**
     * Creates a vertical LinearLayoutManager
     *
     * @param context Current context, will be used to access resources.
     */
    public TransactionsLayoutManager(Context context) {
        super(context);
    }
}
