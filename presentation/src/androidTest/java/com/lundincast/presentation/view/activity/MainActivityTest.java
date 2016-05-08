package com.lundincast.presentation.view.activity;

import android.support.test.espresso.intent.rule.IntentsTestRule;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.test.suitebuilder.annotation.LargeTest;

import com.lundincast.presentation.R;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.swipeLeft;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.intent.Intents.intended;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasExtra;
import static android.support.test.espresso.intent.matcher.IntentMatchers.toPackage;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.not;

/**
 * Test class in JUnit4 for MainActivity to check UI is properly displayed
 */
@RunWith(AndroidJUnit4.class)
@LargeTest
public class MainActivityTest {

    @Rule
    public ActivityTestRule mActivityRule = new IntentsTestRule<>(MainActivity.class);

    @Before
    public void setUp() throws Exception {}

    @After
    public void tearDown() throws Exception {}

    @Test
    public void listTransactionHappyCaseViews() {
        onView(withId(R.id.iv_app_logo)).check(matches(isDisplayed()));
        onView(withId(R.id.tv_app_name)).check(matches(isDisplayed()));
        onView(withId(R.id.iv_filter_list_icon)).check(matches(isDisplayed()));
        onView(withId(R.id.tab_layout)).check(matches(isDisplayed()));
        onView(withId(R.id.viewpager)).check(matches(isDisplayed()));
        onView(withId(R.id.rv_transactions)).check(matches(isDisplayed()));
        onView(withId(R.id.fab)).check(matches(isDisplayed()));
    }

    @Test
    public void overviewGraphCardsHappyCaseViews() {
        onView(withId(R.id.viewpager)).perform(swipeLeft());
        onView(withId(R.id.iv_filter_list_icon)).check(matches(not(isDisplayed())));
        onView(withId(R.id.cv_piechart_distribution)).check(matches(isDisplayed()));
        onView(withId(R.id.cv_barchart_category)).check(matches(isDisplayed()));
    }

    @Test
    public void clickOnFabFiresCorrectIntent() {
        onView(withId(R.id.fab)).perform(click());
        intended(allOf(
                toPackage("com.lundincast.presentation"),
                hasComponent(CreateTransactionActivity.class.getName()),
                hasExtra(CreateTransactionActivity.INTENT_EXTRA_PARAM_TRANSACTION_ID, -1)
        ));
    }
}
