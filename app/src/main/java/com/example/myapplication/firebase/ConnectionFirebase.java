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
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

//logic for interacting with the firebase database
public class ConnectionFirebase {
    private static final int PAST_APPTS_RETURNED_SIZE = 15;
    private final DatabaseReference databaseReference;
    private final AppointmentsListener listener;
    private final String userId;

    static final String PROGRESS_NODE = "progressAppts";
    static final String USERS_NODE = "users";
    static final String UPCOMING_NODE = "upcomingAppts";
    static final String FEEDBACK_NODE = "feedbackAppts";
    private static final String NOT_FINISHED_NODE = "notFinishedAppts";
    private static final String PAST_NODE = "pastAppts";
    private static final String USER_INFO_NODE = "userInfo";
    private static final String MANAGER_NODE = "managerInfo";
    private static final String FDBK_QUESTIONS_NODE = "fdbkQuestions";
    private static final String PARTICIPANT_FEEDBACK_NODE = "participantNode";
    private static final String FEEDBACK_COMPLETED_APPTS_NODE = "feedback_completed_appts";

    public ConnectionFirebase(AppointmentsListener listener, String userId) {
        databaseReference = FirebaseDatabase.getInstance().getReference();
        this.listener = listener;
        this.userId = userId;

        updateAppointmentsDb();
    }

    void addListenersForCategories() {


        databaseReference.child(USERS_NODE).child(userId).addChildEventListener(new ChildEventListener() {
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
                        case FEEDBACK_NODE:
                            listener.onUpcomingApptsChanged(new ArrayList<Appointment>());
                            break;
                        default:
                            Log.i(MainActivity.APP_TAG, "Not a valid case. Check ConnectionFirebase class");
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


        //NOTE improvement: maybe replace the node listeners with ChildEventListener
        // ValueEventListener returns the whole list of data
        // Result: I tried but when a deletion/insertion occurs it takes long until the ui is notified by the model
        databaseReference.child(USERS_NODE).child(userId).child(PROGRESS_NODE).addValueEventListener(new ValueEventListener() {
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
                } else {
                    //gets to this part when the data at the requested path doesn't exist (e.g. all the upcoming apps are finished or you switch from an account with many entries in this categories to one which has none)
                    Log.i(MainActivity.APP_TAG, "Data at requested path doesn't exits: " + PROGRESS_NODE);
                    //required in order to clear the data in ui for the case when switching from one account to another
                    listener.onProgressApptsChanged(new ArrayList<Appointment>());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        databaseReference.child(USERS_NODE).child(userId).child(UPCOMING_NODE).addValueEventListener(new ValueEventListener() {
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
                } else {
                    //gets to this part when the data at the requested path doesn't exist (e.g. all the upcoming apps are finished or you switch from an account with many entries in this categories to one which has none)
                    Log.i(MainActivity.APP_TAG, "Data at requested path doesn't exits: " + UPCOMING_NODE);
                    //required in order to clear the data in ui for the case when switching from one account to another
                    listener.onUpcomingApptsChanged(new ArrayList<Appointment>());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e(MainActivity.APP_TAG, "Failed at the server");
            }
        });

        databaseReference.child(USERS_NODE).child(userId).child(FEEDBACK_NODE).addValueEventListener(new ValueEventListener() {
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
                } else {
                    //gets to this part when the data at the requested path doesn't exist (e.g. all the upcoming apps are finished or you switch from an account with many entries in this categories to one which has none)
                    Log.i(MainActivity.APP_TAG, "Data at requested path doesn't exits: " + FEEDBACK_NODE);
                    //required in order to clear the data in ui for the case when switching from one account to another
                    listener.onFeedbackApptsChanged(new ArrayList<Appointment>());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        //display only most recent apps
        databaseReference.child(USERS_NODE).child(userId).child(PAST_NODE).orderByKey().limitToLast(PAST_APPTS_RETURNED_SIZE).addValueEventListener(new ValueEventListener() {
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
                } else {
                    //gets to this part when the data at the requested path doesn't exist (e.g. all the upcoming apps are finished or you switch from an account with many entries in this categories to one which has none)
                    Log.i(MainActivity.APP_TAG, "Data at requested path doesn't exits: " + PAST_NODE);
//                    //required in order to clear the data in ui for the case when switching from one account to another
                    listener.onPastApptsChanged(new ArrayList<Appointment>());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        databaseReference.child(USERS_NODE).child(userId).child(USER_INFO_NODE).addValueEventListener(new ValueEventListener() {
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

        databaseReference.child(FDBK_QUESTIONS_NODE)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        ArrayList<String> fdbkQuestions = new ArrayList<>();
                        Iterable<DataSnapshot> children = dataSnapshot.getChildren();

                        for (DataSnapshot child : children) {
                            fdbkQuestions.add((String) child.getValue());
                        }

                        listener.onFdbkQuestionsRetrieved(fdbkQuestions);
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
        databaseReference.child(USERS_NODE).child(userId).addListenerForSingleValueEvent(new UpdateApptsListener(this, userId));


    }

    public void addAppointment(Appointment appt) {
        //push() generates a key that takes into account the timestamp, so the list elements can be sorted chronologically
        databaseReference.child(USERS_NODE).child(userId).child(UPCOMING_NODE).push().setValue(appt);
    }


    /* Called when an appointment needs to be edited; first I delete the old one and create a new one
     */
    public void removeAppointment(Appointment x, AppointmentState appointmentState) {
        //TOBE MODIFIED
        String key = x.getApptKey();

        switch (appointmentState) {
            case PROGRESS:
                databaseReference.child(USERS_NODE).child(userId).child(PROGRESS_NODE).child(key).setValue(null);
                break;

            case UPCOMING:
                databaseReference.child(USERS_NODE).child(userId).child(UPCOMING_NODE).child(key).setValue(null);
                break;

        }
    }

    public void createNotFinishedAppt(Appointment selectedAppt, AppointmentState appointmentState, String reason) {
        String sourceAppointmentType = appointmentState.equals(AppointmentState.PROGRESS) ? PROGRESS_NODE : UPCOMING_NODE;

        FirebaseHelper.moveAppointment(databaseReference, userId, sourceAppointmentType, PAST_NODE, selectedAppt);

        HashMap<String, Object> apptData = toMap(selectedAppt);
        apptData.put("reason", reason);

        //added to be easier on server side, although is quite rendundant (store the data twice)
        databaseReference.child(USERS_NODE).child(userId).child(NOT_FINISHED_NODE).child(selectedAppt.getApptKey()).updateChildren(apptData);
    }

    private HashMap<String, Object> toMap(Appointment appt) {
        HashMap<String, Object> result = new HashMap<>();

        result.put("apptKey", appt.getApptKey());
        result.put("date", appt.getDate());
        result.put("end_time", appt.getEnd_time());
        result.put("start_time", appt.getStart_time());
        result.put("location", appt.getLocation());
        result.put("m_code", appt.getM_code());

        return result;
    }

    public void apptFeedbackProvided(Appointment selectedAppt, AppointmentState appointmentState, String[] answers) {
        String sourceAppointmentType = toAppointmentNode(appointmentState);
        String apptKey = selectedAppt.getApptKey();

        FirebaseHelper.moveAppointment(databaseReference, userId, sourceAppointmentType, PAST_NODE, selectedAppt);

        for (int i = 0; i < answers.length; i++) {
            databaseReference.child(USERS_NODE)
                    .child(userId)
                    .child(FEEDBACK_COMPLETED_APPTS_NODE)
                    .child(apptKey)
                    .child("q" + (i + 1) + "_answer")
                    .setValue(answers[i]);
        }

    }

    public void participantFdbkProvided(Appointment selectedAppt, String participantMessage) {
        String apptKey = selectedAppt.getApptKey();
        databaseReference.child(USERS_NODE)
                .child(userId)
                .child(PARTICIPANT_FEEDBACK_NODE)
                .child(apptKey)
                .setValue(participantMessage);
    }

    private String toAppointmentNode(AppointmentState appointmentState) {
        String appointmentNode = "";

        switch (appointmentState) {
            case PROGRESS:
                appointmentNode = PROGRESS_NODE;
                break;
            case UPCOMING:
                appointmentNode = UPCOMING_NODE;
                break;
            case FEEDBACK:
                appointmentNode = FEEDBACK_NODE;
                break;
            default:
                Log.i(MainActivity.APP_TAG, "Invalid appointment state:  " + appointmentState.toString());
        }

        return appointmentNode;
    }

    public void saveNewUserName(String textValue) {
        databaseReference.child(USERS_NODE + "/" + userId)
                .child(USER_INFO_NODE)
                .child("user_name")
                .setValue(textValue);

    }
}
