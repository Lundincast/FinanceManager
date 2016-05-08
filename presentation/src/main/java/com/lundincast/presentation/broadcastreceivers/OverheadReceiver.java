package com.lundincast.presentation.broadcastreceivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.lundincast.presentation.services.OverheadService;

/**
 * {@link android.content.BroadcastReceiver} implementation to trigger OverheadService
 */
public class OverheadReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        Intent service = new Intent(context, OverheadService.class);
        context.startService(service);
    }
}
