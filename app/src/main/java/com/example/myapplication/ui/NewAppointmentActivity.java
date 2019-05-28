package com.example.myapplication.ui;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;

import com.droidnet.DroidListener;
import com.droidnet.DroidNet;
import com.example.myapplication.R;
import com.example.myapplication.model_firebase.Appointment;

import java.util.Calendar;

import static com.example.myapplication.util.EditTextHelper.emptyET;

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

    private Calendar calendar;
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
        saveBtn = findViewById(R.id.saveBtn);
        mcodeET = findViewById(R.id.mcodeET);
        locationET = findViewById(R.id.locationET);
        startTimePicker = findViewById(R.id.start_time_p_id);
        startTimePicker.setIs24HourView(true);

        endTimePicker = findViewById(R.id.end_time_p_id);
        endTimePicker.setIs24HourView(true);

        dateET = findViewById(R.id.pickDateET);
        calendar = Calendar.getInstance();
        String currentDateText = formatDate(calendar.get(Calendar.DAY_OF_MONTH), calendar.get(Calendar.MONTH), calendar.get(Calendar.YEAR));
        dateET.setText(currentDateText);
        dateET.setInputType(InputType.TYPE_NULL);

        pickerDialog = new DatePickerFragment();

        addListeners();

        activityNotSetUp = false;
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

                if (emptyET(mcodeET, menteeCode, "Please provide mentee code")) {
                    return;
                }

                String location = locationET.getText().toString();

                if (emptyET(locationET, location, "Please provide location")) {
                    return;
                }

                String start_time = getTimeFormated(startTimePicker);

                String end_time = getTimeFormated(endTimePicker);

                String date = dateET.getText().toString();
                String feedbackProvided = "no";
                Appointment newAppt = new Appointment(location, menteeCode, date, start_time, end_time, feedbackProvided);

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

    private String formatDate(int day, int month, int year) {
        StringBuilder date = new StringBuilder();

        String theDay = (day >= 1 && day <= 9) ? "0" + String.valueOf(day) : String.valueOf(day);
        date.append(theDay);
        date.append("/");

        month++;
        String theMonth = (month >= 1 && month <= 9) ? "0" + String.valueOf(month) : String.valueOf(month);
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
            Toast.makeText(this, "Turn on the Interent!", Toast.LENGTH_SHORT).show();
        }

    }
}
