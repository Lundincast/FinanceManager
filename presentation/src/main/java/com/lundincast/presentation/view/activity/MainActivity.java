package com.lundincast.presentation.view.activity;

import android.app.AlarmManager;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v13.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;

import com.lundincast.presentation.R;
import com.lundincast.presentation.broadcastreceivers.NotificationAlarmReceiver;
import com.lundincast.presentation.dagger.HasComponent;
import com.lundincast.presentation.dagger.components.DaggerTransactionComponent;
import com.lundincast.presentation.dagger.components.TransactionComponent;
import com.lundincast.presentation.dagger.modules.TransactionModule;
import com.lundincast.presentation.model.TransactionModel;
import com.lundincast.presentation.navigation.Navigator;
import com.lundincast.presentation.view.fragment.OverviewFragment;
import com.lundincast.presentation.view.fragment.TransactionListFragment;

import java.util.Calendar;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Main application screen. This is the app entry point.
 */
public class MainActivity extends BaseActivity implements HasComponent<TransactionComponent>,
                                                        TransactionListFragment.TransactionListListener {

    public static Intent getCallingIntent(Context context) {
        return new Intent(context, MainActivity.class);
    }

    @Bind(R.id.toolbar) Toolbar toolbar;
    @Bind(R.id.iv_filter_list_icon) ImageView iv_filter_list_icon;
    @Bind(R.id.tab_layout) TabLayout tabLayout;
    @Bind(R.id.viewpager) ViewPager viewPager;

    @Inject Navigator navigator;

    private TransactionComponent transactionComponent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        setUpToolbar();
        setUpTabLayout();
        setUpViewPager();

        initializeInjector();
    }

    private void setUpToolbar() {
        toolbar.showOverflowMenu();
        setSupportActionBar(toolbar);
    }

    private void setUpTabLayout() {
        tabLayout.addTab(tabLayout.newTab().setText("List"));
        tabLayout.addTab(tabLayout.newTab().setText("Overview"));
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

    private void setUpViewPager() {
        final PagerAdapter adapter = new PagerAdapter(getFragmentManager(), tabLayout.getTabCount());
        viewPager.setAdapter(adapter);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
    }

    private void initializeInjector() {
        this.getApplicationComponent().inject(this);
        this.transactionComponent = DaggerTransactionComponent.builder()
                .applicationComponent(getApplicationComponent())
                .transactionModule(new TransactionModule())
                .build();
    }

    @Override
    public TransactionComponent getComponent() {
        return transactionComponent;
    }

    @Override
    public void onTransactionClicked(TransactionModel transactionModel) {
        this.navigator.navigateToCreateTransaction(this, transactionModel.getTransactionId());
    }

    /**
     *
     * Launch create transaction activity on fab clicked
     */
    public void onFabClicked() {
        this.navigator.navigateToCreateTransaction(this, -1);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            this.navigator.navigateToSettings(this);
        }

        return super.onOptionsItemSelected(item);
    }


    /**
     * Custom {@link FragmentPagerAdapter} implementation to feed ViewPager
     */
    public class PagerAdapter extends FragmentPagerAdapter {
        int mNumOfTabs;

        public PagerAdapter(FragmentManager fm, int NumOfTabs) {
            super(fm);
            this.mNumOfTabs = NumOfTabs;
        }

        @Override
        public Fragment getItem(int position) {

            switch (position) {
                case 0:
                    return new TransactionListFragment();
                default:
                    return new OverviewFragment();
            }
        }

        @Override
        public int getCount() {
            return mNumOfTabs;
        }

    }
}
