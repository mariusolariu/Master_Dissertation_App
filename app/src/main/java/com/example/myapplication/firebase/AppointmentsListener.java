package com.example.myapplication.firebase;

import com.example.myapplication.model.Appointment;
import com.example.myapplication.model.ManagerInfo;
import com.example.myapplication.model.UserInfo;

import java.util.List;

public interface AppointmentsListener {
    void onProgressApptsChanged(List<Appointment> appointments);

    void onUpcomingApptsChanged(List<Appointment> appointments);

    void onFeedbackApptsChanged(List<Appointment> appointments);

    void onPastApptsChanged(List<Appointment> appointments);

    void onUserInfoChanged(UserInfo userInfo);

    void onManagerInfoChanged(ManagerInfo managerInfo);

    void onFdbkQuestionsRetrieved(List<String> fdbkQuestions);
}
