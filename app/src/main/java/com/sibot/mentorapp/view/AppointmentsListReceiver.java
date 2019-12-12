package com.sibot.mentorapp.view;

import com.sibot.mentorapp.model.Appointment;

import java.util.List;

interface AppointmentsListReceiver {

    void onAppoinmentsListChanged(List<Appointment> list);
}
