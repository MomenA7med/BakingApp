package com.example.momen.baking_app;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.espresso.IdlingRegistry;
import android.support.test.espresso.IdlingResource;
import android.support.test.espresso.contrib.RecyclerViewActions;
import android.support.test.espresso.matcher.ViewMatchers;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.junit.Assert.*;

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class MainActivityTest {

    private static final String RECIPE_ITEM = "Brownies";

   @Rule
   public ActivityTestRule<MainActivity> activityTestRule = new ActivityTestRule<>(MainActivity.class);
   private IdlingResource mIdlingResource;

   @Before
    public void registerIdlingResource(){
       mIdlingResource = activityTestRule.getActivity().getIdlingResource();
       IdlingRegistry.getInstance().register(mIdlingResource);
   }

   @Test
    public void testMainActivity(){
       try {
           Thread.sleep(2000);
       } catch (InterruptedException e) {
           e.printStackTrace();
       }
       onView(ViewMatchers.withId(R.id.recipeRV)).perform(RecyclerViewActions.scrollToPosition(1));
       onView(withText(RECIPE_ITEM)).check(matches(isDisplayed()));
   }

   @After
   public void unRegisterIdlingResource(){
       if (mIdlingResource != null)
           IdlingRegistry.getInstance().unregister(mIdlingResource);
   }
}
