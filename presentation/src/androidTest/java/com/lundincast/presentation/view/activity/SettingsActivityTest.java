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
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.intent.Intents.intended;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static android.support.test.espresso.intent.matcher.IntentMatchers.toPackage;
import static android.support.test.espresso.matcher.ViewMatchers.assertThat;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.core.AllOf.allOf;
import static org.hamcrest.core.IsNot.not;

/**
 * Test class for SettingsActivity to check UI is properly displayed
 */
@RunWith(AndroidJUnit4.class)
@LargeTest
public class SettingsActivityTest {

    @Rule
    public ActivityTestRule mActivityRule = new IntentsTestRule<>(SettingsActivity.class);

    @Before
    public void setUp() throws Exception {}

    @After
    public void tearDown() throws Exception {}

    @Test
    public void containsSettingsFragment() {
        Fragment settingsFragment =
                mActivityRule.getActivity().getFragmentManager().findFragmentByTag("SettingsFragment");
        assertThat(settingsFragment, is(notNullValue()));
    }

    @Test
    public void settingsHappyCaseViews() {
        onView(withId(R.id.iv_back)).check(matches(isDisplayed()));
        onView(withId(R.id.tv_title)).check(matches(isDisplayed()));
        onView(withId(R.id.fl_settings)).check(matches(isDisplayed()));
        onView(withId(R.id.iv_delete)).check(matches(not(isDisplayed())));
    }

    @Test
    public void clickOnCategoriesFiresCorrectIntent() {
        onView(withText("Categories")).perform(click());
        intended(allOf(
                toPackage("com.lundincast.presentation"),
                hasComponent(CategoryListActivity.class.getName())));
    }

    @Test
    public void clickOnOverheadsFiresCorrectIntent() {
        onView(withText("Overheads")).perform(click());
        intended(allOf(
                toPackage("com.lundincast.presentation"),
                hasComponent(OverheadsListActivity.class.getName())));
    }
}
