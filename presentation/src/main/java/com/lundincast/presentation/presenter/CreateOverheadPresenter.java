package com.lundincast.presentation.presenter;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;

import com.lundincast.presentation.broadcastreceivers.OverheadReceiver;
import com.lundincast.presentation.data.OverheadsRepository;
import com.lundincast.presentation.model.CategoryModel;
import com.lundincast.presentation.model.OverheadModel;
import com.lundincast.presentation.view.TransactionDetailsView;
import com.lundincast.presentation.view.activity.CreateOverheadActivity;

import java.util.Calendar;

import javax.inject.Inject;

import io.realm.Realm;
import io.realm.RealmResults;

/**
 * {@link Presenter} that controls communication between views and models of the presentation
 * layer.
 */
public class CreateOverheadPresenter implements Presenter {

    private TransactionDetailsView viewDetailsView;

    private final OverheadsRepository overheadsRepository;
    private final Realm realm;

    private int mOverheadId = -1;
    private double mPrice = 0;
    private CategoryModel mCategory = null;
    private short mDayOfMonth = 1;
    private String mComment;

    @Inject
    public CreateOverheadPresenter(OverheadsRepository overheadsRepository) {
        this.overheadsRepository = overheadsRepository;
        this.realm = Realm.getDefaultInstance();
    }

    public void setView(@NonNull TransactionDetailsView view) {
        this.viewDetailsView = view;
    }

    public void setMPrice(double price) {
        this.mPrice = price;
    }

    public CategoryModel getMCategory() {
        return this.mCategory;
    }

    public void setmCategory(CategoryModel categoryModel) {
        this.mCategory = categoryModel;
    }

    public short getmDayOfMonth() {
        return mDayOfMonth;
    }

    public void setmDayOfMonth(short mDayOfMonth) {
        this.mDayOfMonth = mDayOfMonth;
    }

    public String getmComment() {
        return this.mComment;
    }

    public void setmComment(String comment) {
        this.mComment = comment;
    }

    @Override
    public void resume() {

    }

    @Override
    public void pause() {

    }

    @Override
    public void destroy() {

    }

    public void initialize(int overheadId) {
        if (overheadId != -1) {
            this.mOverheadId = overheadId;
            OverheadModel overheadModel =
                    realm.where(OverheadModel.class).equalTo("overheadId", overheadId).findFirst();
            this.mPrice = overheadModel.getPrice();
            this.mCategory = overheadModel.getCategory();
            this.mDayOfMonth = overheadModel.getDayOfMonth();
            this.mComment = overheadModel.getComment();
        }
        showTransactionPriceInView();
    }

    private void showTransactionPriceInView() {
        this.viewDetailsView.renderTransactionPrice(String.format("%.2f", mPrice));
    }

    public void saveOverhead() {
        OverheadModel overhead = new OverheadModel(mOverheadId);
        overhead.setPrice(mPrice);
        overhead.setCategory(realm.copyFromRealm(mCategory));
        overhead.setDayOfMonth(mDayOfMonth);
        overhead.setComment(mComment);
        this.overheadsRepository.saveOverhead(overhead);
        this.setUpAlarmForOverheadCreation(overhead);
    }

    public void deleteOverhead(int overheadId) {
        // check if there are other overheads scheduled on this day of month
        RealmResults<OverheadModel> result = realm.where(OverheadModel.class).equalTo("dayOfMonth", mDayOfMonth).findAll();
        // if current overhead was the only one, cancel the associated PendingIntent
        if (result.size() < 2) {
            this.cancelAlarm();
        }
        this.overheadsRepository.deleteOverhead(overheadId);
    }

    private void setUpAlarmForOverheadCreation(OverheadModel overhead) {
        // Set intent to be broadcast
        Context context = (CreateOverheadActivity) viewDetailsView;
        Intent intent = new Intent(context, OverheadReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, overhead.getDayOfMonth(), intent, PendingIntent.FLAG_UPDATE_CURRENT);
        // Set calendar to overhead creation date
        Calendar cal = Calendar.getInstance();
        if (cal.get(Calendar.DAY_OF_MONTH) > overhead.getDayOfMonth()) {
            cal.add(Calendar.MONTH, 1);
        }
        cal.set(Calendar.DAY_OF_MONTH, overhead.getDayOfMonth());
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        // Set AlarmManager to trigger broadcast
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        alarmManager.set(AlarmManager.RTC, cal.getTimeInMillis(), pendingIntent);
    }

    private void cancelAlarm() {
        // Set intent identical to original to retrieve existing PendingIntent
        Context context = (CreateOverheadActivity) viewDetailsView;
        Intent intent = new Intent(context, OverheadReceiver.class);
        // retrieve PendingIntent
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, mDayOfMonth, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        // cancel alarm associated with this PendingIntent
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        alarmManager.cancel(pendingIntent);
        // cancel pendingIntent
        pendingIntent.cancel();
    }
}
