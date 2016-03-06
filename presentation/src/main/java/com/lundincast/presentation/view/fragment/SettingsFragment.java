package com.lundincast.presentation.view.fragment;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.TimePickerDialog;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.text.format.DateFormat;
import android.widget.TimePicker;

import com.lundincast.presentation.R;
import com.lundincast.presentation.view.activity.SettingsActivity;

import javax.inject.Inject;

import butterknife.Bind;

/**
 * Created by lundincast on 2/03/16.
 */
public class SettingsFragment extends PreferenceFragment {

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
        String test = sharedPreferences.getString("pref_key_currency", "1");
        if (test.equals("2")) {
            currencyPref.setSummary("Dollar");
        } else {
            currencyPref.setSummary("Euro");
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

        timeOfDayPref = (Preference) findPreference("pref_key_time_day");
        // Retrieve time of day from sharedPref and set summary
        int hour = sharedPreferences.getInt("hour_of_day_alarm", 23);
        int minute = sharedPreferences.getInt("minute_of_day_alarm", 00);
        timeOfDayPref.setSummary(hour + "." + minute);
        // Set onClick listener on "Time of day" button to launch TimePicker
        timeOfDayPref.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                DialogFragment timePickerFragment = new TimePickerFragment();
                timePickerFragment.show(getFragmentManager(), "timePicker");
                return false;
            }
        });
        timeOfDayPref.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {
                int hour = sharedPreferences.getInt("hour_of_day_alarm", 23);
                int minute = sharedPreferences.getInt("minute_of_day_alarm", 00);
                timeOfDayPref.setSummary(hour + "." + minute);
                return false;
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
            appVersionPref.setSummary("App version 1.0.0");
        }

    }

    @SuppressLint("ValidFragment")
    public class TimePickerFragment extends DialogFragment
            implements TimePickerDialog.OnTimeSetListener {

        SharedPreferences sharedPref;

        public TimePickerFragment() {
            // Required empty public constructor
        }

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            super.onCreateDialog(savedInstanceState);

            // check in SharedPref if hour is set and apply. If not, set 23.00
            sharedPref = PreferenceManager.getDefaultSharedPreferences(getActivity());
            int hour = sharedPref.getInt("hour_of_day_alarm", 23);
            int minute = sharedPref.getInt("minute_of_day_alarm", 00);

            // Create a new instance of TimePickerDialog and return it
            return new TimePickerDialog(getActivity(), this, hour, minute,
                    DateFormat.is24HourFormat(getActivity()));
        }


        @Override
        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {

            // set variables in SharedPreference with chosen values
            SharedPreferences.Editor editor = sharedPref.edit();
            editor.putInt("hour_of_day_alarm", hourOfDay);
            editor.putInt("minute_of_day_alarm", minute);
            editor.commit();
            // update Time of day summary with new time
            timeOfDayPref.setSummary(hourOfDay + "." + minute);
        }
    }
}
