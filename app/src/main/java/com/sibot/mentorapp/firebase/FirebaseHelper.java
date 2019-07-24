package com.sibot.mentorapp.firebase;

import com.google.firebase.database.DatabaseReference;
import com.sibot.mentorapp.model.Appointment;

class FirebaseHelper {
    static void moveAppointment(DatabaseReference databaseReference, String userID, String sourceCategory, String destinationCategory, Appointment a) {
        String apptName = a.getApptKey();
        //delete
        databaseReference.child(ConnectionFirebase.USERS_NODE).child(userID).child(sourceCategory).child(apptName).setValue(null);

        //add
        databaseReference.child(ConnectionFirebase.USERS_NODE).child(userID).child(destinationCategory).child(apptName).setValue(a);
    }


}
