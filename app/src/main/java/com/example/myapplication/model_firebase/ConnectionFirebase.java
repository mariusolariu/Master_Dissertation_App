package com.example.myapplication.model_firebase;

import android.provider.ContactsContract;
import android.support.annotation.NonNull;

import com.example.myapplication.model.AppointmentsListener;
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

    public ConnectionFirebase(AppointmentsListener listener) {
        databaseReference = FirebaseDatabase.getInstance().getReference();
        this.listener = listener;

        addListeners();
    }

    private void addListeners() {
        databaseReference.child("appointments").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()){
                    Iterable<DataSnapshot> iter = dataSnapshot.getChildren();

                    Iterator<?> iterator = iter.iterator();

                    List<Appointment> appointments = new ArrayList<>();

                    while (iterator.hasNext()){
                        Appointment appointment = ((DataSnapshot) iterator.next()).getValue(Appointment.class);
                        appointments.add(appointment);
                    }

                    listener.onAppointmentsListChanged(appointments);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }


}
