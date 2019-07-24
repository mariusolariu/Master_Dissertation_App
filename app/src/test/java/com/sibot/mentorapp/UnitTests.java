package com.sibot.mentorapp;

import android.content.Intent;

import com.sibot.mentorapp.ui.MainActivity;
import com.sibot.mentorapp.ui.SignUpActivity;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.shadows.ShadowActivity;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.robolectric.Shadows.shadowOf;

@RunWith(RobolectricTestRunner.class)
public class UnitTests {
    private MainActivity activity;

    @Before
    public void setUp() throws Exception {
        activity = Robolectric.buildActivity(MainActivity.class)
                .create()
                .start()
                .resume()
                .get();
    }

    @Test
    public void testSignInActivLaunched() {
        activity.findViewById(R.id.newAppointmentFB).performClick();
        ShadowActivity mainActivShadow = shadowOf(activity);

//        Intent startInt = new Intent(activity, SignUpActivity.class);
//        Intent resultInt = new Intent();
//        resultInt.putExtra("email", "ymca.paisley.39@gmail.com");
//        resultInt.putExtra("email", "parttime");
//
//        mainActivShadow.receiveResult(startInt, -1, resultInt);


        Intent startedIntent = mainActivShadow.getNextStartedActivity();

        String actual = startedIntent.getComponent().getClassName();
        String expected = SignUpActivity.class.getName();
        assertThat(actual, equalTo(expected));
    }

    @Test
    public void secondTest() {

    }
}
