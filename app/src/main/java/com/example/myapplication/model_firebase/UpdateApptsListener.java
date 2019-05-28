package com.example.myapplication.model_firebase;

import android.support.annotation.NonNull;
import android.util.Log;

import com.example.myapplication.ui.MainActivity;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import static com.example.myapplication.model_firebase.ConnectionFirebase.FEEDBACK_NODE;
import static com.example.myapplication.model_firebase.ConnectionFirebase.PROGRESS_NODE;
import static com.example.myapplication.model_firebase.ConnectionFirebase.UPCOMING_NODE;


/**
 * Listener used to move the appointments to the right category (e.g. from upcoming to progress) every time the app is opened
 */
public class UpdateApptsListener implements ValueEventListener {
    private SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.UK);
    private DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
    private ConnectionFirebase owner;
    private String userID;

    UpdateApptsListener(ConnectionFirebase owner, String userId) {
        this.owner = owner;
        this.userID = userId;
    }

    @Override
    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
        Iterable<DataSnapshot> upcomingAppts = dataSnapshot.child(userID).child(UPCOMING_NODE).getChildren();
        updateUpcomingAppts(upcomingAppts);

        Iterable<DataSnapshot> progressSnapshots = dataSnapshot.child(userID).child(PROGRESS_NODE).getChildren();
        updateProgressAppts(progressSnapshots);

    }


    @Override
    public void onCancelled(@NonNull DatabaseError databaseError) {

    }

    /**
     * Depending on the current time (when the app was opened) move upcoming appointments in db to progress or feedback section
     *
     *
     */
    private void updateUpcomingAppts(Iterable<DataSnapshot> dataSnapshots) {
        long currentTime = System.currentTimeMillis();

        for (DataSnapshot upcomingApptSnapshot : dataSnapshots) {
            Appointment currentAppt = upcomingApptSnapshot.getValue(Appointment.class);
            String apptName = upcomingApptSnapshot.getKey();

            String stTime = (String) upcomingApptSnapshot.child("start_time").getValue();
            String endTime = (String) upcomingApptSnapshot.child("end_time").getValue();
            String date = (String) upcomingApptSnapshot.child("date").getValue();

            long startTimeMillis = getTimeMillis(date, stTime);
            long endTimeMillis = getTimeMillis(date, endTime);

            if ((startTimeMillis <= currentTime) &&
                    (currentTime <= endTimeMillis)) { //the current appoinment is due at the current time interval
                moveAppointment(UPCOMING_NODE, PROGRESS_NODE, currentAppt, apptName);

            } else if (endTimeMillis < currentTime) { //the current appointment should have been done by the current time
                moveAppointment(UPCOMING_NODE, FEEDBACK_NODE, currentAppt, apptName);
            }

        }


    }

    /**
     * move appointments that appear in progress section to feedback if they we're supposed to be done at the current time
     *
     * @param progressSnapshots - collection of the current appointments in progress section
     */
    private void updateProgressAppts(Iterable<DataSnapshot> progressSnapshots) {
        long currentTimeMillis = System.currentTimeMillis();

        for (DataSnapshot progressSnapshot : progressSnapshots) {
            Appointment currentAppt = progressSnapshot.getValue(Appointment.class);
            String apptName = progressSnapshot.getKey();

            long endTimeMillis = getTimeMillis(currentAppt.getDate(), currentAppt.getEnd_time());

            if (endTimeMillis < currentTimeMillis) {
                moveAppointment(PROGRESS_NODE, FEEDBACK_NODE, currentAppt, apptName);
            }

        }

        //once the progress and upcoming appts are moved to the correct category at listeners to those categories for any modification
        owner.addListenersForCategories();
    }

    private void moveAppointment(String sourceCategory, String destinationCategory, Appointment a, String apptName) {
        //delete
        databaseReference.child(userID).child(sourceCategory).child(apptName).setValue(null);

        //add
        //FIXME: introduces an extra field in under the appointment entry in DB, namely appt name
        databaseReference.child(userID).child(destinationCategory).push().setValue(a);
    }

    private long getTimeMillis(String date, String time) {
        Date d1;
        long result = 0;

        try {
            d1 = dateFormat.parse(date + " " + time);
            result = d1.getTime();
        } catch (ParseException e) {
            Log.e(MainActivity.YMCA_TAG, "Failed to convert date and time into Date object");
            e.printStackTrace();
        }

        return result;
    }

}
