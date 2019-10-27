package com.sibot.mentorapp.model;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;

import com.sibot.mentorapp.firebase.AppointmentsListener;
import com.sibot.mentorapp.firebase.FirebaseConnection;
import com.sibot.mentorapp.util.AppointmentState;

import java.util.List;

public class ViewModelMA extends ViewModel implements AppointmentsListener {
    private MutableLiveData<List<Appointment>> progressAppts;
    private MutableLiveData<List<Appointment>> upcomingAppts;
    private MutableLiveData<List<Appointment>> feedbackAppts;
    private MutableLiveData<List<Appointment>> pastAppts;
    private MutableLiveData<UserInfo> userInfoLD;
    private MutableLiveData<ManagerInfo> managerInfoLD;
    private MutableLiveData<List<String>> fdbkQuestionsLD;


    private FirebaseConnection firebaseConnection;


    public void init(String userId) {
        String userId1 = userId;

        progressAppts = new MutableLiveData<>();
        upcomingAppts = new MutableLiveData<>();
        feedbackAppts = new MutableLiveData<>();
        pastAppts = new MutableLiveData<>();
        userInfoLD = new MutableLiveData<>();
        managerInfoLD = new MutableLiveData<>();
        fdbkQuestionsLD = new MutableLiveData<>();

        firebaseConnection = new FirebaseConnection(this, userId);
    }

    public MutableLiveData<ManagerInfo> getManagerInfoLD() {
        return managerInfoLD;
    }

    public MutableLiveData<UserInfo> getUserInfoLD() {
        return userInfoLD;
    }
    public LiveData<List<Appointment>> getProgressAppts() {
        return progressAppts;
    }

    public LiveData<List<Appointment>> getUpcomingAppts() {
        return upcomingAppts;
    }

    public LiveData<List<Appointment>> getFeedbackAppts() {
        return feedbackAppts;
    }

    public LiveData<List<Appointment>> getPastAppts() {
        return pastAppts;
    }

    public List<Appointment> getUpcomingApptsList() {
        return upcomingAppts.getValue();
    }

    public List<Appointment> getFeedbackApptsList() {
        return feedbackAppts.getValue();
    }

    public List<Appointment> getPastApptsList() {
        return pastAppts.getValue();
    }

    public List<Appointment> getProgressApptsList() {
        return progressAppts.getValue();
    }

    @Override
    public void onProgressApptsChanged(List<Appointment> progressAppts) {
        //the appoinments list was changed
        this.progressAppts.setValue(progressAppts);
    }

    @Override
    public void onUpcomingApptsChanged(List<Appointment> appointments) {
        this.upcomingAppts.setValue(appointments);
    }

    @Override
    public void onFeedbackApptsChanged(List<Appointment> appointments) {
        this.feedbackAppts.setValue(appointments);
    }

    @Override
    public void onPastApptsChanged(List<Appointment> appointments) {
        this.pastAppts.setValue(appointments);
    }

    @Override
    public void onUserInfoChanged(UserInfo userInfo) {
        this.userInfoLD.setValue(userInfo);
    }

    @Override
    public void onManagerInfoChanged(ManagerInfo managerInfo) {
        this.managerInfoLD.setValue(managerInfo);
    }

    public MutableLiveData<List<String>> getFdbkQuestionsLD() {
        return fdbkQuestionsLD;
    }

    @Override
    public void onFdbkQuestionsRetrieved(List<String> fdbkQuestions) {
        this.fdbkQuestionsLD.setValue(fdbkQuestions);
    }

    //interract with Firebase to add new appointment
    public void addAppointment(Appointment newA) {
        firebaseConnection.addAppointment(newA);
    }

    //interract with Firebase to remove appointment
    //Should they be allowed to do it?
    public void removeAppointmnet(Appointment x, AppointmentState appointmentState) {
        firebaseConnection.removeAppointment(x, appointmentState);
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        //clear any subscription of data
    }

    /**
     * move appointment to past appts and add a reason for not executing it
     */
    public void createNotFinishedAppt(Appointment selectedAppt, AppointmentState appointmentState, String reason) {
        firebaseConnection.createNotFinishedAppt(selectedAppt, appointmentState, reason);
    }

    /**
     * Called when the user has provide for an appointment. The appointment has to be moved from initial category to PAST appts and the answers stored under the same key in "feedback" node
     */
    public void apptFeedbackProvided(final Appointment selectedAppt, final AppointmentState appointmentState, final String[] answers) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                firebaseConnection.apptFeedbackProvided(selectedAppt, appointmentState, answers);
            }
        }).start();

    }

    public void participantFdbkProvided(final Appointment selectedAppt, final String participantMessage) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                firebaseConnection.participantFdbkProvided(selectedAppt, participantMessage);
            }
        }).start();
    }

    public void saveNewUserName(String textValue) {
        firebaseConnection.saveNewUserName(textValue);
    }
}
