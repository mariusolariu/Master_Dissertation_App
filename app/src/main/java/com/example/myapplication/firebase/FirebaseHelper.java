package com.example.myapplication.firebase;

import com.example.myapplication.model.Appointment;
import com.google.firebase.database.DatabaseReference;

class FirebaseHelper {
    static void moveAppointment(DatabaseReference databaseReference, String userID, String sourceCategory, String destinationCategory, Appointment a) {
        String apptName = a.getApptKey();
        //delete
        databaseReference.child(ConnectionFirebase.USERS_NODE).child(userID).child(sourceCategory).child(apptName).setValue(null);

        //add
        databaseReference.child(ConnectionFirebase.USERS_NODE).child(userID).child(destinationCategory).child(apptName).setValue(a);
    }


}
