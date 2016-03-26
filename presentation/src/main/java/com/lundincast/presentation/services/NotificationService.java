package com.lundincast.presentation.services;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.lundincast.presentation.R;
import com.lundincast.presentation.model.TransactionModel;
import com.lundincast.presentation.view.activity.CreateTransactionActivity;
import com.lundincast.presentation.view.activity.MainActivity;

import java.util.Calendar;
import java.util.Date;

import io.realm.Realm;

/**
 * {@link android.app.Service} implementation to handle notifications
 */
public class NotificationService extends Service {

    public NotificationService() {
        Log.d("Lundin Cast", "NotificationService instantiated");
    }

    @Override
    public void onStart(Intent intent, int startId) {
        super.onStart(intent, startId);
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        boolean notificationEnabled = sharedPreferences.getBoolean("pref_key_daily_reminder", false);
        boolean adaptedEnabled = sharedPreferences.getBoolean("pref_key_adapted_reminder", true);

        if (notificationEnabled) {
            if (adaptedEnabled) {
                Realm realm = Realm.getDefaultInstance();
                Calendar cal = Calendar.getInstance();
                cal.setTime(new Date());
                TransactionModel existingTransaction = realm.where(TransactionModel.class)
                        .equalTo("day", cal.get(Calendar.DAY_OF_MONTH))
                        .equalTo("month", cal.get(Calendar.MONTH))
                        .equalTo("year", cal.get(Calendar.YEAR))
                        .findFirst();
                if (existingTransaction != null) {
                    setupNotification();
                }
            } else {
                setupNotification();
            }

        }
    }

    private void setupNotification() {
        PendingIntent pendingAddIntent = PendingIntent.getActivity(
                this, 0, new Intent(getApplicationContext(), CreateTransactionActivity.class), PendingIntent.FLAG_ONE_SHOT);
        PendingIntent pendingListIntent = PendingIntent.getActivity(
                this, 0, new Intent(getApplicationContext(), MainActivity.class), PendingIntent.FLAG_ONE_SHOT);

        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.drawable.ic_filter_list_white_24dp)
                .setContentTitle("Too good to be true !")
                .setContentText("Haven't you spent any money today ?")
                .addAction(R.drawable.ic_add_white_24dp, "Add expense", pendingAddIntent)
                .addAction(R.drawable.ic_filter_list_white_24dp, "View list", pendingListIntent);

        // Sets an ID for the notification
        int mNotificationId = 001;
        // Gets an instance of the NotificationManager service
        NotificationManager mNotifyMgr = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        // Builds the notification and issues it.
        mNotifyMgr.notify(mNotificationId, mBuilder.build());
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
