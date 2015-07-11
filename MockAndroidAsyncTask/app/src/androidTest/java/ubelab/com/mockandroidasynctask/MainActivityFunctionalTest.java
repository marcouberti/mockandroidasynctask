package ubelab.com.mockandroidasynctask;

import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.*;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.*;

import java.net.URL;

@RunWith(AndroidJUnit4.class)
public class MainActivityFunctionalTest {

    MainActivity activity;

    @Rule
    public ActivityTestRule<MainActivity> mActivityRule = new ActivityTestRule<>(MainActivity.class);

    @Before
    public void setUp()  {
        activity = mActivityRule.getActivity();
    }

    @Test
    public void testServerReturnSuccess() throws InterruptedException {
        //Create mock task factory
        //and then mock the factory returned async task
        activity.factory = activity.new TaskFactory(){
            @Override
            public MainActivity.RegisterTask getTask() {
                return activity.new RegisterTask() {
                    @Override
                    protected Integer doInBackground(URL... params) {
                        //Mock the server call result here
                        return Result.SUCCESS;
                    }
                };
            }
        };

        //Fill the form
        onView(withId(R.id.username)).perform(typeText("marcouberti@example.com"), closeSoftKeyboard());
        closeSoftKeyboard();
        onView(withId(R.id.password)).perform(typeText("password123!"), closeSoftKeyboard());
        closeSoftKeyboard();
        onView(withId(R.id.verifypassword)).perform(typeText("password123!"), closeSoftKeyboard());

        //sleep to be sure the keyboard is close
        Thread.sleep(500);

        //Click the button
        onView(withId(R.id.createbutton)).perform(click());


        //Check the result
        onView(withId(R.id.statusText)).check(matches(withText("REGISTRATION SUCCESSFUL")));
    }

    @Test
    public void testServerReturnFail() throws InterruptedException {
        //Create mock task factory
        //and then mock the factory returned async task
        activity.factory = activity.new TaskFactory(){
            @Override
            public MainActivity.RegisterTask getTask() {
                return activity.new RegisterTask() {
                    @Override
                    protected Integer doInBackground(URL... params) {
                        //Mock the server call result here
                        return Result.FAILURE;
                    }
                };
            }
        };

        //Fill the form
        onView(withId(R.id.username)).perform(typeText("marcouberti@example.com"), closeSoftKeyboard());
        closeSoftKeyboard();
        onView(withId(R.id.password)).perform(typeText("password123!"), closeSoftKeyboard());
        closeSoftKeyboard();
        onView(withId(R.id.verifypassword)).perform(typeText("wrong"), closeSoftKeyboard());

        //sleep to be sure the keyboard is close
        Thread.sleep(500);

        //Click the button
        onView(withId(R.id.createbutton)).perform(click());

        //Check the result
        onView(withId(R.id.statusText)).check(matches(withText("REGISTRATION FAILED")));
    }
}
