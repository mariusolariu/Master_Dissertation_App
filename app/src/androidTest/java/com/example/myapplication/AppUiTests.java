package com.example.myapplication;

import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.TimePicker;

import androidx.test.espresso.UiController;
import androidx.test.espresso.ViewAction;
import androidx.test.espresso.action.GeneralLocation;
import androidx.test.espresso.action.GeneralSwipeAction;
import androidx.test.espresso.action.Press;
import androidx.test.espresso.action.Swipe;
import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;
import androidx.test.rule.ActivityTestRule;

import com.example.myapplication.ui.MainActivity;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.clearText;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.longClick;
import static androidx.test.espresso.action.ViewActions.scrollTo;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.doesNotExist;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

@RunWith(AndroidJUnit4.class)
@LargeTest
/**
 * The tests should be started with an empty Database
 */
public class AppUiTests {
    static final String M_CODE_TEXT = "testMCode";
    private final String LOCATION_TEXT = "34 Test Road, TestTown";
    private final int startH = 10;
    private final int startM = 30;
    private final int endH = 12;
    private final int endM = 30;
    private final int apptY = 2040;
    private final int apptM = 10;
    private final int apptD = 15;

    static final String UPCOMING_TEXT = "UPCOMING";

    @Rule
    /* can be used to start activities, services, intents  */
    public ActivityTestRule<MainActivity> mainActivityRule =
            new ActivityTestRule<>(MainActivity.class);


    @Test
    public void addAppointment() {
        onView(withId(R.id.newAppointmentFB)).perform(click());
        onView(withId(R.id.mcodeET)).perform(typeText("newAppt"));
        onView(withId(R.id.locationET)).perform(typeText(LOCATION_TEXT));
        onView(withId(R.id.locationET)).perform(closeSoftKeyboard());
        onView(withId(R.id.start_time_p_id)).perform(setTime(startH, startM));
        onView(withId(R.id.end_time_p_id)).perform(setTime(endH, endM));

        //NOTE: The problem might be caused because the DatePickerFragment is added dynamically
        //Fragment dialogFragment = new DatePickerFragment();
        // newApptRule.getActivity().getFragmentManager().beginTransaction().add(R.id.containerNewApptId, dialogFragment).commit();
        //onView(withId(R.id.pickDateET)).perform(scrollTo(),click()).perform(click());
        //onView(withId(R.id.datePickerID)).perform(setDate(1994, 11, 10));

        onView(withId(R.id.pickDateET)).perform(scrollTo(), setDate(apptY, apptM, apptD));


        onView(withText(R.string.save_btn_text)).perform(scrollTo(), click());

        onView(withText(UPCOMING_TEXT)).perform(click());

        //check to see if the appointment was created
        onView(withText("newAppt")).check(matches(isDisplayed()));
    }

    //@Ignore
    @Test
    /*
     * Before running this make just the user is singed out!!
     */
    public void testSingIn() throws InterruptedException {
        onView(withId(R.id.emailET)).perform(typeText("ymca.paisley.39@gmail.com"));
        onView(withId(R.id.passwordET)).perform(typeText("parttime"));
        onView(withId(R.id.loginB)).perform(click());

        //check the user is signed in by looking that one of the appoinments tabs is present
        Thread.sleep(2500);

        //NOTE: doesn't work
//        CountingIdlingResource singInResource = mainActivityRule.getActivity().getSingInResource();
//        IdlingRegistry.getInstance().register(singInResource);
//        registerIdlingResources(singInResource);
//        singInResource.isIdleNow();
        onView(withText(UPCOMING_TEXT)).check(matches(isDisplayed()));
    }

    @Test
//    @Ignore
    /**
     * Before running this test the user has to be singed in!
     */
    public void singOut() {
        onView(withId(R.id.nav_drawer)).
                perform(swipeRightLong());

        onView(withText("Sign out")).perform(click());

        onView(withId(R.id.loginB)).check(matches(isDisplayed()));

    }

    public static ViewAction swipeRightLong() {
        return new GeneralSwipeAction(Swipe.SLOW, GeneralLocation.CENTER_LEFT, GeneralLocation.CENTER_RIGHT, Press.THUMB);
    }

    //@Ignore
    /*
     * Add new appointment and then move it to the PAST appt by providing a reason for not completing it
     */
    @Test
    public void cancelAppointment() {
        addTestAppointment();

        inputCancelReason(M_CODE_TEXT);

        //check that appt is no longer present in UPCOMING section
        onView(withText(M_CODE_TEXT)).check(doesNotExist());
    }


    //    @Ignore
    /* Check that once the feedback has been provided for an appointment in UPCOMING the appointment is moved to PAST section
     */
    @Test
    public void provideFeedbackAppt() {
        addTestAppointment();

        onView(withText(M_CODE_TEXT)).perform(click());
        onView(withId(R.id.q1ET)).perform(typeText("a"));
        onView(withId(R.id.q2ET)).perform(typeText("a"));
        onView(withId(R.id.q3ET)).perform(scrollTo(), typeText("a"));
        onView(withId(R.id.q4ET)).perform(scrollTo(), typeText("a"));

        onView(withId(R.id.q5ET)).
                perform(scrollTo(), typeText("a")).
                perform(closeSoftKeyboard());

        onView(withText(R.string.save_btn_text)).perform(scrollTo(), click());

        //check that the appoinment is no longer in UPCOMING section
        onView(withText(UPCOMING_TEXT)).perform(click());
        onView(withText(M_CODE_TEXT)).check(doesNotExist());
    }

    //    @Ignore
    @Test
    public void editTestAppt() {
        addTestAppointment();

        String newApptIdentifier = "newMCode";
        String newLocation = "New City";

        int startNH = 12;
        int startNM = 15;
        int endNH = 14;
        int endNM = 20;

        int newY = 2055;
        int newM = 11;
        int newD = 25;

        //modifiy all fields of appt
        onView(withText(M_CODE_TEXT))
                .perform(longClick());

        onView(withId(R.id.editApptB))
                .perform(click());

        onView(withId(R.id.mcodeET))
                .perform(clearText());

        onView(withId(R.id.mcodeET))
                .perform(typeText(newApptIdentifier));

        onView(withId(R.id.locationET)).
                perform(clearText());

        onView(withId(R.id.locationET)).
                perform(typeText(newLocation));


        onView(withId(R.id.start_time_p_id)).perform(setTime(startNH, startNM));
        onView(withId(R.id.end_time_p_id)).perform(setTime(endNH, endNM));

        onView(withId(R.id.pickDateET)).perform(setDate(newY, newM, newD));

        onView(withText(R.string.save_btn_text)).perform(scrollTo(), click());

        //NOTE: check that all fields have been modified
        onView(withText(newApptIdentifier)).
                check(matches(isDisplayed()));

        onView(withText(newLocation)).
                check(matches(isDisplayed()));

        String timeAndDate = startNH + ":" + startNM + "-" + endNH + ":" + endNM +
                " " + newD + "/" + newM + "/" + newY;
        onView(withText(timeAndDate)).check(matches(isDisplayed()));

        inputCancelReason(newApptIdentifier);
    }

    private void addTestAppointment() {
        onView(withId(R.id.newAppointmentFB)).perform(click());
        onView(withId(R.id.mcodeET)).perform(typeText(M_CODE_TEXT));
        onView(withId(R.id.locationET)).perform(typeText(LOCATION_TEXT));
        onView(withId(R.id.locationET)).perform(closeSoftKeyboard());
        onView(withId(R.id.start_time_p_id)).perform(setTime(startH, startM));
        onView(withId(R.id.end_time_p_id)).perform(setTime(endH, endM));

        //NOTE: The problem might be caused because the DatePickerFragment is added dynamically
        //Fragment dialogFragment = new DatePickerFragment();
        // newApptRule.getActivity().getFragmentManager().beginTransaction().add(R.id.containerNewApptId, dialogFragment).commit();
        //onView(withId(R.id.pickDateET)).perform(scrollTo(),click()).perform(click());
        //onView(withId(R.id.datePickerID)).perform(setDate(1994, 11, 10));

        onView(withId(R.id.pickDateET)).perform(scrollTo(), setDate(apptY, apptM, apptD));


        onView(withText(R.string.save_btn_text)).perform(scrollTo(), click());

        onView(withText(UPCOMING_TEXT)).perform(click());

        //check to see if the appointment was created
        onView(withText(M_CODE_TEXT)).check(matches(isDisplayed()));
    }

    /**
     * Provide a reason to cancel the "test" appointment
     */
    private void inputCancelReason(String apptIdentifier) {
        onView(withText(apptIdentifier)).perform(longClick());
        onView(withId(R.id.prvidReasoB)).perform(click());
        onView(withId(R.id.reasonNotCompletingApptET)).perform(typeText("It was raining outside"));
        onView(withText("OK")).perform(click());
    }


    private static Matcher<View> withAdaptedData(final Matcher<Object> dataMatcher) {
        return new TypeSafeMatcher<View>() {

            @Override
            public void describeTo(Description description) {
                description.appendText("with class name: ");
                dataMatcher.describeTo(description);
            }

            @Override
            public boolean matchesSafely(View view) {
                if (!(view instanceof AdapterView)) {
                    return false;
                }

                @SuppressWarnings("rawtypes")
                Adapter adapter = ((AdapterView) view).getAdapter();
                for (int i = 0; i < adapter.getCount(); i++) {
                    if (dataMatcher.matches(adapter.getItem(i))) {
                        return true;
                    }
                }
                return false;
            }
        };
    }

    private static ViewAction setTime(final int hour, final int minute) {
        return new ViewAction() {
            @Override
            public void perform(UiController uiController, View view) {
                TimePicker tp = (TimePicker) view;
                tp.setCurrentHour(hour);
                tp.setCurrentMinute(minute);
            }

            @Override
            public String getDescription() {
                return "Set the passed time into the TimePicker";
            }

            @Override
            public Matcher<View> getConstraints() {
                return ViewMatchers.isAssignableFrom(TimePicker.class);
            }
        };
    }

    private static ViewAction setDate(final int year, final int month, final int day) {
        return new ViewAction() {
            @Override
            public void perform(UiController uiController, View view) {
                EditText editText = (EditText) view;

                editText.setText(day + "/" + month + "/" + year);
//                DatePicker datePicker = (DatePicker) view;
//                datePicker.init(year, month, day, new DatePicker.OnDateChangedListener() {
//                    @Override
//                    public void onDateChanged(DatePicker datePicker, int i, int i1, int i2) {
//                        //wont change
//                    }
//                });
            }

            @Override
            public String getDescription() {
                return "Set the passed date into the DatePicker for appointment";
            }

            @Override
            public Matcher<View> getConstraints() {
                return ViewMatchers.isAssignableFrom(EditText.class);
            }
        };
    }


}
