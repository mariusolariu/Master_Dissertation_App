package com.example.myapplication.model_firebase;

import android.support.annotation.NonNull;
import android.util.Log;

import com.example.myapplication.model.AppointmentsListener;
import com.example.myapplication.ui.MainActivity;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

//logic for interacting with the firebase database
public class ConnectionFirebase {
    private DatabaseReference databaseReference;
    private AppointmentsListener listener;

    public static final String PROGRESS_NODE = "progressAppts";
    public static final String UPCOMING_NODE = "upcomingAppts";
    public static final String FEEDBACK_NODE = "feedbackAppts";
    public static final String PAST_NODE = "pastAppts";

    public ConnectionFirebase(AppointmentsListener listener) {
        databaseReference = FirebaseDatabase.getInstance().getReference();
        this.listener = listener;

        updateAppointmentsDb();
    }

    void addListenersForCategories() {
        databaseReference.child(PROGRESS_NODE).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    Iterable<DataSnapshot> iter = dataSnapshot.getChildren();

                    Iterator<?> iterator = iter.iterator();

                    List<Appointment> appointments = new ArrayList<>();

                    while (iterator.hasNext()) {
                        Appointment appointment = ((DataSnapshot) iterator.next()).getValue(Appointment.class);
                        appointments.add(appointment);
                    }

                    listener.onProgressApptsChanged(appointments);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        databaseReference.child(UPCOMING_NODE).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    Iterable<DataSnapshot> iter = dataSnapshot.getChildren();

                    Iterator<?> iterator = iter.iterator();

                    List<Appointment> appointments = new ArrayList<>();

                    while (iterator.hasNext()) {
                        Appointment appointment = ((DataSnapshot) iterator.next()).getValue(Appointment.class);
                        appointments.add(appointment);
                    }

                    listener.onUpcomingApptsChanged(appointments);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        databaseReference.child(FEEDBACK_NODE).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    Iterable<DataSnapshot> iter = dataSnapshot.getChildren();

                    Iterator<?> iterator = iter.iterator();

                    List<Appointment> appointments = new ArrayList<>();

                    while (iterator.hasNext()) {
                        Appointment appointment = ((DataSnapshot) iterator.next()).getValue(Appointment.class);
                        appointments.add(appointment);
                    }

                    listener.onFeedbackApptsChanged(appointments);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        databaseReference.child(PAST_NODE).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    Iterable<DataSnapshot> iter = dataSnapshot.getChildren();

                    Iterator<?> iterator = iter.iterator();

                    List<Appointment> appointments = new ArrayList<>();

                    while (iterator.hasNext()) {
                        DataSnapshot data = (DataSnapshot) iterator.next();
                        Appointment appointment = data.getValue(Appointment.class);
                        appointment.appointment_name = data.getKey();
                        appointments.add(appointment);
                    }

                    listener.onPastApptsChanged(appointments);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    /**
     * move the appointments to the correct category
     * -> move from upcoming to progress appointments based on date and time
     * -> move from progress to feedback appointments which are done
     */
    private void updateAppointmentsDb() {
        databaseReference.addListenerForSingleValueEvent(new UpdateApptsListener(this));

    }


}
