package com.sumeet.persistancelist.ui;

import androidx.test.espresso.Espresso;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.rule.ActivityTestRule;

import com.sumeet.persistancelist.ui.memelist.MemeListActivity;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.RootMatchers.withDecorView;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.not;

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class MemeListActivityTests {

    @Rule
    public ActivityTestRule<MemeListActivity> mActivityRule = new ActivityTestRule<>(MemeListActivity.class);

    @Test
    public void onBackPress_showToast() {

        //when : on back Press
        Espresso.pressBack();

        //Then : showToast
        onView(withText("Press back again to exit")).inRoot(withDecorView(not((mActivityRule.getActivity().getWindow().getDecorView()))))
                .check(matches(isDisplayed()));
    }


}
