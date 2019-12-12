package com.sibot.mentorapp.view;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.shadows.ShadowActivity;

import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;
import static org.robolectric.Shadows.shadowOf;

@RunWith(RobolectricTestRunner.class)
public class NewAppointmentActivityTest {
    private NewAppointmentActivity activity;

    @Before
    public void setUp() {
        activity = Robolectric.buildActivity(NewAppointmentActivity.class)
                .create()
                .start()
                .resume()
                .get();
    }

    @Test
    public void testDateIsFormattedCorrectly() {
        ShadowActivity shadowActivity = shadowOf(activity);
        String expected = "03/05/2050";
        String result = activity.formatDate(3, 5, 2050);

        assertThat(expected, equalTo(result));
    }
}