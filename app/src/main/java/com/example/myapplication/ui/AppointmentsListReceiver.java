package com.example.myapplication.ui;

import com.example.myapplication.model.Appointment;

import java.util.List;

interface AppointmentsListReceiver {

    void onAppoinmentsListChanged(List<Appointment> list);
}
