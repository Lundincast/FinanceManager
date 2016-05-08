package com.lundincast.presentation.services;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import com.lundincast.presentation.broadcastreceivers.OverheadReceiver;
import com.lundincast.presentation.data.TransactionRepository;
import com.lundincast.presentation.data.TransactionRepositoryImpl;
import com.lundincast.presentation.data.datasource.DiskTransactionDataStore;
import com.lundincast.presentation.model.OverheadModel;
import com.lundincast.presentation.model.TransactionModel;

import java.util.Calendar;
import java.util.Date;

import io.realm.Realm;
import io.realm.RealmResults;

/**
 * {@link android.app.Service} implementation to handle overhead creation on alarm and
 * new alarm setting
 */
public class OverheadService extends Service {

    TransactionRepository transactionRepository;

    private Realm realm;

    public OverheadService() {
        realm = Realm.getDefaultInstance();
        transactionRepository = new TransactionRepositoryImpl(new DiskTransactionDataStore(realm));
    }

    @Override
    public void onStart(Intent intent, int startId) {
        super.onStart(intent, startId);
        // get today's day of month to query overhead of this same day
        Calendar cal = Calendar.getInstance();
        cal.setTime(new Date());
        int dayOfMonth = cal.get(Calendar.DAY_OF_MONTH);
        // query realm for overhead to be created on this day
        RealmResults<OverheadModel> overheadsList =
                realm.where(OverheadModel.class).equalTo("dayOfMonth", dayOfMonth).findAll();
        // Create new transaction for each result
        for (OverheadModel overhead : overheadsList) {
            TransactionModel transaction = new TransactionModel(-1);
            transaction.setPrice(overhead.getPrice());
            transaction.setCategory(realm.copyFromRealm(overhead.getCategory()));
            transaction.setDate(cal.getTime());
            transaction.setComment(overhead.getComment());
            transaction.setPending(false);
            transaction.setDueToOrBy(0);
            transaction.setDueName(null);
            transactionRepository.saveTransaction(transaction);
        }
        // Set alarm for next month
        this.setUpNewAlarm();
        // Kill service
        stopSelf();
    }

    private void setUpNewAlarm() {
        // get todays date
        Calendar cal = Calendar.getInstance();
        // get current month
        int currentMonth = cal.get(Calendar.MONTH);
        // move month ahead
        currentMonth++;
        // check if has not exceeded threshold of december
        if(currentMonth > Calendar.DECEMBER){
            // alright, reset month to jan and forward year by 1 e.g fro 2013 to 2014
            currentMonth = Calendar.JANUARY;
            // Move year ahead as well
            cal.set(Calendar.YEAR, cal.get(Calendar.YEAR)+1);
        }
        // reset calendar to next month
        cal.set(Calendar.MONTH, currentMonth);

        // cal is set, now create new alarm for next month
        // Set intent to be broadcast for reminder
        Intent intent = new Intent(getApplicationContext(), OverheadReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(getApplicationContext(), cal.get(Calendar.DAY_OF_MONTH), intent, PendingIntent.FLAG_UPDATE_CURRENT);
        // Set AlarmManager to trigger broadcast
        AlarmManager alarmManager = (AlarmManager) getApplicationContext().getSystemService(Context.ALARM_SERVICE);
        alarmManager.set(AlarmManager.RTC, cal.getTimeInMillis(), pendingIntent);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
