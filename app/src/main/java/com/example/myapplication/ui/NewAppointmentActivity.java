package com.example.myapplication.ui;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;

import com.droidnet.DroidListener;
import com.droidnet.DroidNet;
import com.example.myapplication.R;
import com.example.myapplication.model.Appointment;

import java.util.Calendar;

import static com.example.myapplication.util.EditTextHelper.isET_empty;

/**
 * Allows the user to input all the details needed for creating a new appointment and the passes them back to the MainActivity which in turn passes them to the model
 */
public class NewAppointmentActivity extends AppCompatActivity implements DroidListener {
    //used as tag to put the newly created appt in the intent
    public static final String NEW_APPOINTMENT_TAG = "new_appointment";

    private EditText dateET;
    private EditText mcodeET;
    private EditText locationET;
    private TimePicker startTimePicker;
    private TimePicker endTimePicker;
    private Button saveBtn;

    private DatePickerFragment pickerDialog;

    private boolean activityNotSetUp = true;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.new_appt_layout);

        DroidNet.init(this);
        DroidNet.getInstance().addInternetConnectivityListener(this);
    }

    private void setUp() {
        saveBtn = findViewById(R.id.saveNewApptsB);
        mcodeET = findViewById(R.id.mcodeET);
        locationET = findViewById(R.id.locationET);
        startTimePicker = findViewById(R.id.start_time_p_id);
        startTimePicker.setIs24HourView(true);

        endTimePicker = findViewById(R.id.end_time_p_id);
        endTimePicker.setIs24HourView(true);

        dateET = findViewById(R.id.pickDateET);
        Calendar calendar = Calendar.getInstance();

        //calendar.get(MONTH) -> returns int in [0,11]
        String currentDateText = formatDate(calendar.get(Calendar.DAY_OF_MONTH), calendar.get(Calendar.MONTH) + 1, calendar.get(Calendar.YEAR));
        dateET.setText(currentDateText);
        dateET.setInputType(InputType.TYPE_NULL);

        pickerDialog = new DatePickerFragment();

        Intent passedIntent = getIntent();

        Appointment appointment;

        //it means that the activity was opened to edit an existing appt
        Bundle extras = passedIntent.getExtras();
        if (extras != null) {
            appointment = extras.getParcelable(MainActivity.EDIT_APPOINTMENT);
            fillInFields(appointment);
        }

        addListeners();

        activityNotSetUp = false;
    }

    private void fillInFields(Appointment appointment) {
        mcodeET.setText(appointment.getM_code());
        locationET.setText(appointment.getLocation());

        int[] startTime = getHourAndMinute(appointment.getStart_time());
        startTimePicker.setCurrentHour(startTime[0]);
        startTimePicker.setCurrentMinute(startTime[1]);

        int[] endTime = getHourAndMinute(appointment.getEnd_time());
        endTimePicker.setCurrentHour(endTime[0]);
        endTimePicker.setCurrentMinute(endTime[1]);

        dateET.setText(appointment.getDate());
    }

    private int[] getHourAndMinute(String textTime) {
        int[] result = new int[2];
        String hourString = textTime.substring(0, 2);

        result[0] = Integer.valueOf(hourString);

        String minuteString = textTime.substring(3, 5);
        result[1] = Integer.valueOf(minuteString);

        return result;
    }

    private void addListeners() {

        dateET.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pickerDialog.show(getFragmentManager(), "datepicker");
            }
        });

        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String menteeCode = mcodeET.getText().toString();

                if (isET_empty(mcodeET, menteeCode, "Please provide mentee code")) {
                    return;
                }

                String location = locationET.getText().toString();

                if (isET_empty(locationET, location, "Please provide location")) {
                    return;
                }

                String start_time = getTimeFormated(startTimePicker);

                String end_time = getTimeFormated(endTimePicker);

                String date = dateET.getText().toString();
                String feedbackProvided = "no";

                Appointment newAppt = new Appointment("", location, menteeCode, date, start_time, end_time);

                Intent intent = new Intent();
                intent.putExtra(NEW_APPOINTMENT_TAG, newAppt);
                setResult(RESULT_OK, intent);
                finish();
            }

            private String getTimeFormated(TimePicker timePicker) {
                String time;
                Integer currentHour;
                Integer currentMinute;

                if (Build.VERSION.SDK_INT < 23) {
                    currentHour = timePicker.getCurrentHour();
                    currentMinute = timePicker.getCurrentMinute();

                } else {
                    currentHour = timePicker.getHour();
                    currentMinute = timePicker.getMinute();
                }

                String hour = (currentHour >= 0 && currentHour <= 9) ? "0" + currentHour : String.valueOf(currentHour);
                String minute = (currentMinute >= 0 && currentMinute <= 9) ? "0" + currentMinute : String.valueOf(currentMinute);

                time = hour + ":" + minute;

                return time;
            }
        });
    }


    public void setDate(int day, int month, int year) {
        String dateText = formatDate(day, month, year);

        dateET.setText(dateText);
    }

    public String formatDate(int day, int month, int year) {
        StringBuilder date = new StringBuilder();

        String theDay = (day >= 1 && day <= 9) ? "0" + day : String.valueOf(day);
        date.append(theDay);
        date.append("/");

        String theMonth = (month >= 1 && month <= 9) ? "0" + month : String.valueOf(month);
        date.append(theMonth);

        date.append("/");
        date.append(year);

        return date.toString();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        setResult(RESULT_CANCELED);
        finish();
    }

    @Override
    public void onInternetConnectivityChanged(boolean isConnected) {

        if (isConnected) {
//            Toast.makeText(this, "Internet is on!", Toast.LENGTH_SHORT).show();

            if (activityNotSetUp) {
                setUp();
            }
        } else {
            Toast.makeText(this, "Turn on the Internet!", Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            setResult(RESULT_CANCELED);
            finish();
        }

        return true;
    }
}
