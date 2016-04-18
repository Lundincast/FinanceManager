package com.lundincast.presentation.view.activity;

import android.app.AlarmManager;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.v13.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.lundincast.presentation.R;
import com.lundincast.presentation.broadcastreceivers.NotificationAlarmReceiver;
import com.lundincast.presentation.dagger.HasComponent;
import com.lundincast.presentation.dagger.components.DaggerTransactionComponent;
import com.lundincast.presentation.dagger.components.TransactionComponent;
import com.lundincast.presentation.dagger.modules.TransactionModule;
import com.lundincast.presentation.model.CategoryModel;
import com.lundincast.presentation.model.TransactionModel;
import com.lundincast.presentation.navigation.Navigator;
import com.lundincast.presentation.view.fragment.OverviewFragment;
import com.lundincast.presentation.view.fragment.TransactionListFragment;

import java.util.Calendar;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;
import io.realm.Realm;

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
    @Inject public SharedPreferences sharedPreferences;

    private TransactionComponent transactionComponent;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        initializeInjector();

        setUpToolbar();
        setUpTabLayout();
        setUpViewPager();

        checkFirstRun();

        // Retrieve intent and check for notificationId extra. If it exists, it means the activity
        // has been started from a notification, so dismiss it.
        Intent intent = getIntent();
        int notificationId = intent.getIntExtra("notificationId", -1);
        if (notificationId == 001) {
            NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            manager.cancel(notificationId);
        }
    }


    private void setUpToolbar() {
        toolbar.showOverflowMenu();
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
    }

    private void setUpTabLayout() {
        tabLayout.addTab(tabLayout.newTab().setText("List"));
        tabLayout.addTab(tabLayout.newTab().setText("Overview"));
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
                if (tab.getPosition() == 0) {
                    iv_filter_list_icon.setVisibility(View.VISIBLE);
                } else {
                    iv_filter_list_icon.setVisibility(View.GONE);
                }
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
        } else if (id == R.id.action_report_issue) {
            Uri issuesWebpage = Uri.parse("https://github.com/Lundincast/FinanceManager/issues");
            Intent intent = new Intent(Intent.ACTION_VIEW, issuesWebpage);
            startActivity(intent);
        }

        return super.onOptionsItemSelected(item);
    }

    private void checkFirstRun() {

        final String PREF_VERSION_CODE_KEY = "version_code";
        final int DOESNT_EXIST = -1;

        // Get current version code
        int currentVersionCode;
        try {
            currentVersionCode = getPackageManager().getPackageInfo(getPackageName(), 0).versionCode;
        } catch (android.content.pm.PackageManager.NameNotFoundException e) {
            // handle exception
            e.printStackTrace();
            return;
        }

        // Get saved version code
        int savedVersionCode = sharedPreferences.getInt(PREF_VERSION_CODE_KEY, DOESNT_EXIST);
        // Check for first run or upgrade
        //noinspection StatementWithEmptyBody
        if (currentVersionCode == savedVersionCode) {
            // This is just a normal run

        } else if (savedVersionCode == DOESNT_EXIST) {
            // This is a new install (or the user cleared the shared preferences)
            Realm realm = Realm.getDefaultInstance();
            realm.executeTransaction(new Realm.Transaction() {
                @Override
                public void execute(Realm realm) {
                    CategoryModel housing = realm.createObject(CategoryModel.class);
                    housing.setId(1);
                    housing.setName("Housing");
                    housing.setColor(-769226);
                    CategoryModel atWork = realm.createObject(CategoryModel.class);
                    atWork.setId(2);
                    atWork.setName("At work");
                    atWork.setColor(-5317);
                    CategoryModel atTheSupermarket = realm.createObject(CategoryModel.class);
                    atTheSupermarket.setId(3);
                    atTheSupermarket.setName("At the supermarket");
                    atTheSupermarket.setColor(-7617718);
                    CategoryModel transport = realm.createObject(CategoryModel.class);
                    transport.setId(4);
                    transport.setName("Transport");
                    transport.setColor(-4342339);
                    CategoryModel foodAndDrinks = realm.createObject(CategoryModel.class);
                    foodAndDrinks.setId(5);
                    foodAndDrinks.setName("Food & Drinks");
                    foodAndDrinks.setColor(-6543440);
                    CategoryModel tobacco = realm.createObject(CategoryModel.class);
                    tobacco.setId(6);
                    tobacco.setName("Tobacco");
                    tobacco.setColor(-7508381);
                    CategoryModel sport = realm.createObject(CategoryModel.class);
                    sport.setId(7);
                    sport.setName("Sport");
                    sport.setColor(-26624);
                    CategoryModel books = realm.createObject(CategoryModel.class);
                    books.setId(8);
                    books.setName("Books & Press");
                    books.setColor(-16537100);
                    CategoryModel phone = realm.createObject(CategoryModel.class);
                    phone.setId(9);
                    phone.setName("Phone");
                    phone.setColor(-1739917);
                    CategoryModel miscellaneous = realm.createObject(CategoryModel.class);
                    miscellaneous.setId(10);
                    miscellaneous.setName("Miscellaneous");
                    miscellaneous.setColor(-10453621);
                    CategoryModel entertainment = realm.createObject(CategoryModel.class);
                    entertainment.setId(11);
                    entertainment.setName("Entertainment");
                    entertainment.setColor(-10929);
                    CategoryModel travel = realm.createObject(CategoryModel.class);
                    travel.setId(12);
                    travel.setName("Travel");
                    travel.setColor(-11677471);
                    CategoryModel technology = realm.createObject(CategoryModel.class);
                    technology.setId(13);
                    technology.setName("Technology");
                    technology.setColor(-8497214);
                    CategoryModel education = realm.createObject(CategoryModel.class);
                    education.setId(14);
                    education.setName("Education");
                    education.setColor(-1499549);
                }
            });
        } else if (currentVersionCode > savedVersionCode) {
            // This is an upgrade
            // Display dialog to show changelog for new version
            new MaterialDialog.Builder(this)
                    .title(R.string.what_new)
                    .content(Html.fromHtml("Version 0.6.1<br />" +
                            "<br />" +
                            "<b>1. IMPROVEMENT:</b> General improvements in usability.<br />" +
                            "<b>2. FIX:</b> Notification set up with new app icon and proper behaviour."))
                    .positiveText(R.string.ok)
                    .onPositive(new MaterialDialog.SingleButtonCallback() {
                        @Override
                        public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                            dialog.dismiss();
                        }
                    }).show();
        }

        // Update the shared preferences with the current version code
        sharedPreferences.edit().putInt(PREF_VERSION_CODE_KEY, currentVersionCode).apply();
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
