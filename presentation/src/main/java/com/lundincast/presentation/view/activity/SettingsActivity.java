package com.lundincast.presentation.view.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.widget.ImageView;

import com.lundincast.presentation.R;
import com.lundincast.presentation.view.fragment.SettingsFragment;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Activity that shows application settings.
 */
public class SettingsActivity extends BaseActivity {

    public static Intent getCallingIntent(Context context) {
        return new Intent(context, SettingsActivity.class);
    }

    @Bind(R.id.toolbar) Toolbar toolbar;
    @Bind(R.id.iv_back) ImageView iv_back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        ButterKnife.bind(this);

        setSupportActionBar(toolbar);

        // Display SettingsFragment
        this.addFragment(R.id.fl_settings, new SettingsFragment());
    }

    @OnClick(R.id.iv_back)
    void onBackClicked() {
        finish();
    }
}
