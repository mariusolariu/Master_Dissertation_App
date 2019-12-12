package com.sibot.mentorapp.model;

import com.google.firebase.database.DatabaseReference;

class FirebaseHelper {
    static void moveAppointment(DatabaseReference databaseReference, String userID, String sourceCategory, String destinationCategory, Appointment a) {
        String apptName = a.getApptKey();
        //delete
        databaseReference.child(FirebaseConnection.USERS_NODE).child(userID).child(sourceCategory).child(apptName).setValue(null);

        //add
        databaseReference.child(FirebaseConnection.USERS_NODE).child(userID).child(destinationCategory).child(apptName).setValue(a);
    }


}
