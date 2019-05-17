package com.example.myapplication.model;

import com.example.myapplication.model_firebase.Appointment;

import java.util.List;

public interface AppointmentsListener {
    void onAppointmentsListChanged(List<Appointment> appointments);
}
