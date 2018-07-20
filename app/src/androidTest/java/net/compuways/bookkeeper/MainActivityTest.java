package net.compuways.bookkeeper;

import android.support.test.rule.ActivityTestRule;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import java.util.ArrayList;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;

import static org.junit.Assert.*;

/**
 * Created by zuleli on 2018-03-14.
 */
public class MainActivityTest {
    @Rule
    public  ActivityTestRule<MainActivity> mainActivityActivityTestRule=new ActivityTestRule<MainActivity>(MainActivity.class);
    private MainActivity mainActivity=null;

    @Before
    public void setUp() throws Exception {

        mainActivity=mainActivityActivityTestRule.getActivity();
    }

    @After
    public void tearDown() throws Exception {
        mainActivity=null;
    }

    @Test
    public void initiates(){

    }

}