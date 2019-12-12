package com.sibot.mentorapp.model;

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
