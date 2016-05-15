package com.lundincast.presentation.view.fragment;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceFragment;

import com.lundincast.presentation.R;
import com.lundincast.presentation.broadcastreceivers.NotificationAlarmReceiver;
import com.lundincast.presentation.navigation.Navigator;
import com.lundincast.presentation.view.activity.SettingsActivity;
import com.wdullaer.materialdatetimepicker.time.RadialPickerLayout;
import com.wdullaer.materialdatetimepicker.time.TimePickerDialog;

import java.util.Calendar;

import javax.inject.Inject;

/**
 * {@link android.preference.PreferenceFragment} implementation to manage global settings for the app
 */
public class SettingsFragment extends PreferenceFragment implements TimePickerDialog.OnTimeSetListener {

    @Inject Navigator navigator;
    @Inject SharedPreferences sharedPreferences;

    Preference timeOfDayPref;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Initialize dependencies
        ((SettingsActivity) getActivity()).getApplicationComponent().inject(this);

        // Load the preferences from an XML resource
        addPreferencesFromResource(R.xml.preferences);

        // set currency preference
        ListPreference currencyPref = (ListPreference) findPreference("pref_key_currency");
        String currencyValue = sharedPreferences.getString("pref_key_currency", "1");
        if (currencyValue.equals("2")) {
            currencyPref.setSummary("USD - Dollar");
        } else if (currencyValue.equals("3")) {
            currencyPref.setSummary("GBP - British Pound");
        } else {
            currencyPref.setSummary("EUR - Euro");
        }

        // set currency dialog listener to get chosen value and display it
        currencyPref.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {
                ListPreference pref = (ListPreference) preference;
                CharSequence[] values = pref.getEntries();
                pref.setSummary(values[Integer.parseInt((String) newValue) - 1]);
                return true;
            }
        });

        // Set onClick listener on Categories button to launch CategoryListActivity
        Preference categoryPref = findPreference("pref_key_categories");
        categoryPref.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                SettingsFragment.this.navigator.navigateToListCategories(getActivity());
                return false;
            }
        });

        // Set onClick listener on Overheads button to launch OverheadsListActivity
        Preference overheadsPref = findPreference("pref_key_recurring_expenses");
        overheadsPref.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                SettingsFragment.this.navigator.navigateToListOverheads(getActivity());
                return false;
            }
        });

        // Set onClick listener on Accounts button to launch AccountListActivity
        Preference accountPref = findPreference("pref_key_accounts");
        accountPref.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                SettingsFragment.this.navigator.navigateToListAccounts(getActivity());
                return false;
            }
        });

        // Set onClick listener on Daily reminder CheckBox to set up notification if checked
        CheckBoxPreference dailyReminder = (CheckBoxPreference) findPreference("pref_key_daily_reminder");
        dailyReminder.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {
                if ((boolean) newValue) {
                    setUpAlarmForNotification();
                }
                return true;
            }
        });

        // Set time of day
        timeOfDayPref = findPreference("pref_key_time_day");
        // Retrieve time of day from sharedPref and set summary
        final int hour = sharedPreferences.getInt("hour_of_day_alarm", 23);
        final int minute = sharedPreferences.getInt("minute_of_day_alarm", 00);
        if (minute <= 9) {
            timeOfDayPref.setSummary(hour + ":0" + minute);
        } else {
            timeOfDayPref.setSummary(hour + ":" + minute);
        }
        // Set onClick listener on "Time of day" button to launch TimePicker
        timeOfDayPref.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                TimePickerDialog tpd = TimePickerDialog.newInstance(
                        SettingsFragment.this, hour, minute, 00, true
                );
                tpd.show(getFragmentManager(), "Timepickerdialog");
                return true;
            }
        });

        // Dynamically load app version and display it
        Preference appVersionPref = findPreference("pref_key_about");
        PackageInfo packageInfo = null;
        try {
            packageInfo = getActivity().getPackageManager().getPackageInfo(getActivity().getPackageName(), 0);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        String versionName = packageInfo.versionName;
        if(versionName != null) {
            appVersionPref.setSummary(String.valueOf("App version " + versionName));
        } else {
            appVersionPref.setSummary("");
        }

    }

    @Override
    public void onTimeSet(RadialPickerLayout view, int hourOfDay, int minute, int second) {
        // set variables in SharedPreference with chosen values
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt("hour_of_day_alarm", hourOfDay);
        editor.putInt("minute_of_day_alarm", minute);
        editor.commit();
        // update Time of day summary with new time
        if (minute <= 9) {
            timeOfDayPref.setSummary(hourOfDay + ":0" + minute);
        } else {
            timeOfDayPref.setSummary(hourOfDay + ":" + minute);
        }
        // Since timeOfDayPref is disabled if Daily Reminder is not checked, it is indeed checked
        // if we could change timeOfDay so update alarm for notification.
        setUpAlarmForNotification();
    }

    private void setUpAlarmForNotification() {
        // Set intent to be broadcast for reminder
        Intent intent = new Intent(getActivity(), NotificationAlarmReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(getActivity(), 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        // Set calendar to time defined in preferences
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(System.currentTimeMillis());
        cal.set(Calendar.HOUR_OF_DAY, sharedPreferences.getInt("hour_of_day_alarm", 23));
        cal.set(Calendar.MINUTE, sharedPreferences.getInt("minute_of_day_alarm", 00));
        // Compare it with current time. If it is lower, set DAY + 1
        Calendar cal2 = Calendar.getInstance();
        cal2.setTimeInMillis(System.currentTimeMillis());
        if (cal.getTimeInMillis() < cal2.getTimeInMillis()) {
            cal.set(Calendar.DAY_OF_MONTH, cal.get(Calendar.DAY_OF_MONTH) + 1);
        }
        // Set AlarmManager to trigger broadcast
        AlarmManager alarmManager = (AlarmManager) getActivity().getSystemService(Context.ALARM_SERVICE);
        alarmManager.setInexactRepeating(AlarmManager.RTC, cal.getTimeInMillis(), AlarmManager.INTERVAL_DAY, pendingIntent);

    }
}
