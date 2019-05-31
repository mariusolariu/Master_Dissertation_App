package com.example.myapplication.firebase;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import com.example.myapplication.model.Appointment;
import com.example.myapplication.model.ManagerInfo;
import com.example.myapplication.model.UserInfo;
import com.example.myapplication.ui.MainActivity;
import com.example.myapplication.util.AppointmentState;
import com.google.firebase.database.ChildEventListener;
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
    private final DatabaseReference databaseReference;
    private final AppointmentsListener listener;
    private final String userId;

    static final String PROGRESS_NODE = "progressAppts";
    static final String UPCOMING_NODE = "upcomingAppts";
    static final String FEEDBACK_NODE = "feedbackAppts";
    private static final String NOT_FINISHED_NODE = "notFinishedAppts";
    private static final String PAST_NODE = "pastAppts";
    private static final String USER_NODE = "userInfo";
    private static final String MANAGER_NODE = "managerInfo";
    private static final String FEEDBACK_COMPLETED_APPTS_NODE = "feedback_completed_appts";

    public ConnectionFirebase(AppointmentsListener listener, String userId) {
        databaseReference = FirebaseDatabase.getInstance().getReference();
        this.listener = listener;
        this.userId = userId;

        updateAppointmentsDb();
    }

    void addListenersForCategories() {


        databaseReference.child(userId).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String previousElementKey) {

            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            //when an appointment type list gets deleted notify the UI to display an empty list under that type
            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    String key = dataSnapshot.getKey();

                    switch (key) {
                        case PROGRESS_NODE:
                            listener.onProgressApptsChanged(new ArrayList<Appointment>());
                            break;
                        case UPCOMING_NODE:
                            listener.onUpcomingApptsChanged(new ArrayList<Appointment>());
                            break;
                        default:
                            Log.i(MainActivity.YMCA_TAG, "Not a valid case. Check ConnectionFirebase class");
                    }
                }


            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String previousElementKey) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        //TODO improvement: maybe replace the node listeners with ChildEventListener
        // ValueEventListener returns the whole list of data
        databaseReference.child(userId).child(PROGRESS_NODE).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    Iterable<DataSnapshot> iter = dataSnapshot.getChildren();

                    Iterator<?> iterator = iter.iterator();

                    List<Appointment> appointments = new ArrayList<>();

                    while (iterator.hasNext()) {
                        DataSnapshot dS = (DataSnapshot) iterator.next();
                        Appointment appointment = dS.getValue(Appointment.class);
                        appointment.setApptKey(dS.getKey());
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
                        DataSnapshot dS = (DataSnapshot) iterator.next();
                        Appointment appointment = dS.getValue(Appointment.class);
                        appointment.setApptKey(dS.getKey());
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
                        DataSnapshot dS = (DataSnapshot) iterator.next();
                        Appointment appointment = dS.getValue(Appointment.class);
                        appointment.setApptKey(dS.getKey());
                        appointments.add(appointment);
                    }

                    listener.onFeedbackApptsChanged(appointments);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        //TODO: add a query which returns ten most recent past appts and then a listener to it
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
                        appointment.setApptKey(data.getKey());
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
        //push() generates a key that takes into account the timestamp, so the list elements can be sorted chronologically
        databaseReference.child(userId).child(UPCOMING_NODE).push().setValue(appt);
    }


    /* Called when an appointment needs to be edited; first I delete the old one and create a new one
     */
    public void removeAppointment(Appointment x, AppointmentState appointmentState) {
        //TOBE MODIFIED
        String key = x.getApptKey();

        switch (appointmentState) {
            case PROGRESS:
                databaseReference.child(userId).child(PROGRESS_NODE).child(key).setValue(null);
                break;

            case UPCOMING:
                databaseReference.child(userId).child(UPCOMING_NODE).child(key).setValue(null);
                break;

        }
    }

    public void createNotFinishedAppt(Appointment selectedAppt, AppointmentState appointmentState, String reason) {
        String sourceAppointmentType = appointmentState.equals(AppointmentState.PROGRESS) ? PROGRESS_NODE : UPCOMING_NODE;

        FirebaseHelper.moveAppointment(databaseReference, userId, sourceAppointmentType, PAST_NODE, selectedAppt);

        databaseReference.child(userId).child(NOT_FINISHED_NODE).child(selectedAppt.getApptKey()).setValue(reason);
    }

    public void apptFeedbackProvided(Appointment selectedAppt, AppointmentState appointmentState, String[] answers) {
        String sourceAppointmentType = "";
        String apptKey = selectedAppt.getApptKey();

        switch (appointmentState) {
            case PROGRESS:
                sourceAppointmentType = PROGRESS_NODE;
                break;
            case UPCOMING:
                sourceAppointmentType = UPCOMING_NODE;
                break;
            case FEEDBACK:
                sourceAppointmentType = FEEDBACK_NODE;
                break;
            default:
                Log.i(MainActivity.YMCA_TAG, "Invalid appointment state:  " + appointmentState.toString());
        }

        FirebaseHelper.moveAppointment(databaseReference, userId, sourceAppointmentType, PAST_NODE, selectedAppt);

        for (int i = 0; i < answers.length; i++) {
            databaseReference.child(userId).child(FEEDBACK_COMPLETED_APPTS_NODE).child(apptKey).child("q" + (i + 1) + "_answer").setValue(answers[i]);
        }

    }
}
