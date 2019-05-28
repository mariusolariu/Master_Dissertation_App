package com.example.myapplication.model_firebase;

import android.support.annotation.NonNull;

import com.example.myapplication.model.AppointmentsListener;
import com.example.myapplication.model.ManagerInfo;
import com.example.myapplication.model.UserInfo;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

//logic for interacting with the firebase database
public class ConnectionFirebase {
    private DatabaseReference databaseReference;
    private AppointmentsListener listener;
    private String userId;

    static final String PROGRESS_NODE = "progressAppts";
    static final String UPCOMING_NODE = "upcomingAppts";
    static final String FEEDBACK_NODE = "feedbackAppts";
    static final String PAST_NODE = "pastAppts";
    static final String USER_NODE = "userInfo";
    static final String MANAGER_NODE = "managerInfo";

    public ConnectionFirebase(AppointmentsListener listener, String userId) {
        databaseReference = FirebaseDatabase.getInstance().getReference();
        this.listener = listener;
        this.userId = userId;

        updateAppointmentsDb();
    }

    void addListenersForCategories() {
        databaseReference.child(userId).child(PROGRESS_NODE).addValueEventListener(new ValueEventListener() {
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

        databaseReference.child(userId).child(UPCOMING_NODE).addValueEventListener(new ValueEventListener() {
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

        databaseReference.child(userId).child(FEEDBACK_NODE).addValueEventListener(new ValueEventListener() {
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

        databaseReference.child(userId).child(PAST_NODE).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    Iterable<DataSnapshot> iter = dataSnapshot.getChildren();

                    Iterator<?> iterator = iter.iterator();

                    List<Appointment> appointments = new ArrayList<>();

                    while (iterator.hasNext()) {
                        DataSnapshot data = (DataSnapshot) iterator.next();
                        Appointment appointment = data.getValue(Appointment.class);
                        appointments.add(appointment);
                    }

                    listener.onPastApptsChanged(appointments);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        databaseReference.child(userId).child(USER_NODE).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                UserInfo userInfo = dataSnapshot.getValue(UserInfo.class);

                listener.onUserInfoChanged(userInfo);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        databaseReference.child(MANAGER_NODE).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                ManagerInfo managerInfo = dataSnapshot.getValue(ManagerInfo.class);

                listener.onManagerInfoChanged(managerInfo);
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
        databaseReference.child(userId).addListenerForSingleValueEvent(new UpdateApptsListener(this, userId));

    }

    public void addAppointment(Appointment appt) {
        //TODO: maybe decide if it needs to be put in Progress appts or Upcoming appts based on when is scheduled to take place
        databaseReference.child(userId).child(UPCOMING_NODE).push().setValue(appt);
    }


}
