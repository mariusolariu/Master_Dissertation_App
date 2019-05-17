package com.example.myapplication.ui;

import com.example.myapplication.model_firebase.Appointment;

import java.util.List;

public interface AppointmentsListReceiver {

    void onAppoinmentsListChanged(List<Appointment> list);
}
