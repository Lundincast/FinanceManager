package com.lundincast.presentation.view.activity;

import android.app.Fragment;
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
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.assertThat;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;

/**
 * Test class in JUnit4 for AccountListActivity to check UI is properly displayed
 */
@RunWith(AndroidJUnit4.class)
@LargeTest
public class AccountListActivityTest {

    @Rule
    public ActivityTestRule mActivityRule = new IntentsTestRule<>(AccountListActivity.class);

    @Before
    public void setUp() throws Exception {}

    @After
    public void tearDown() throws Exception {}

    @Test
    public void containsAccountListFragment() {
        Fragment accountListFragment =
                mActivityRule.getActivity().getFragmentManager().findFragmentByTag("accountListFragment");
        assertThat(accountListFragment, is(notNullValue()));
    }

    @Test
    public void listCategoriesHappyCaseViews() {
        onView(withId(R.id.iv_back)).check(matches(isDisplayed()));
        onView(withId(R.id.tv_title)).check(matches(isDisplayed()));
        onView(withId(R.id.iv_add_icon)).check(matches(isDisplayed()));
        onView(withId(R.id.listCategoryFragment)).check(matches(isDisplayed()));
    }

    @Test
    public void activityTitleIsCorrect() {
        onView(withId(R.id.tv_title)).check(matches(withText("Accounts")));
    }
}
