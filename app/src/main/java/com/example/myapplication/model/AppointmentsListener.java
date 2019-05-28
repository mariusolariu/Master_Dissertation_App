package com.example.myapplication.model;

import com.example.myapplication.model_firebase.Appointment;

import java.util.List;

public interface AppointmentsListener {
    void onProgressApptsChanged(List<Appointment> appointments);

    void onUpcomingApptsChanged(List<Appointment> appointments);

    void onFeedbackApptsChanged(List<Appointment> appointments);

    void onPastApptsChanged(List<Appointment> appointments);

    void onUserInfoChanged(UserInfo userInfo);

    void onManagerInfoChanged(ManagerInfo managerInfo);
}
