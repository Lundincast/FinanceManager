package com.lundincast.presentation.view.activity;

import android.app.Fragment;
import android.content.Intent;
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
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.assertThat;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.notNullValue;

/**
 * Test class for CreateOverheadActivity to check UI is properly displayed
 */
@RunWith(AndroidJUnit4.class)
@LargeTest
public class CreateOverheadActivityTest {

    @Rule
    public ActivityTestRule mActivityRule = new ActivityTestRule<>(CreateOverheadActivity.class);

    @Before
    public void setUp() throws Exception {}

    @After
    public void tearDown() throws Exception {}

    public void containsNumericKeyboardFragment() {
        Fragment numericKeyboardFragment =
                mActivityRule.getActivity().getFragmentManager().findFragmentByTag("NumericKeyboardFragment");
        assertThat(numericKeyboardFragment, is(notNullValue()));
    }

    @Test
    public void activityLayoutHappyCaseViews() {
        onView(withId(R.id.iv_back)).check(matches(isDisplayed()));
        onView(withId(R.id.tv_title)).check(matches(isDisplayed()));
        onView(withId(R.id.iv_delete)).check(matches(not(isDisplayed())));
        onView(withId(R.id.tv_transaction_price)).check(matches(isDisplayed()));
    }

    @Test
    public void categoryFragmentDisplayedOnFabClicked() {
        onView(withId(R.id.fab)).perform(click());
        Fragment CategoryListForNewTransactionFragment =
                mActivityRule.getActivity().getFragmentManager().findFragmentByTag("CategoryListForNewTransactionFragment");
        assertThat(CategoryListForNewTransactionFragment, is(notNullValue()));
    }
}
