package com.lundincast.presentation.broadcastreceivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.lundincast.presentation.services.NotificationService;

/**
 * {@link android.content.BroadcastReceiver} implementation to trigger NotificationService
 *
 */
public class NotificationAlarmReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {

        Log.d("Lundin Cast", "onReceive triggered in Broadcast Receiver");
        Intent service = new Intent(context, NotificationService.class);
        context.startService(service);
    }
}
