package com.example.myapplication.model;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;

import com.example.myapplication.firebase.AppointmentsListener;
import com.example.myapplication.firebase.ConnectionFirebase;
import com.example.myapplication.util.AppointmentState;

import java.util.List;

public class MainActivityModel extends ViewModel implements AppointmentsListener {
    private MutableLiveData<List<Appointment>> progressAppts;
    private MutableLiveData<List<Appointment>> upcomingAppts;
    private MutableLiveData<List<Appointment>> feedbackAppts;
    private MutableLiveData<List<Appointment>> pastAppts;
    private MutableLiveData<UserInfo> userInfoLD;
    private MutableLiveData<ManagerInfo> managerInfoLD;


    private ConnectionFirebase connectionFirebase;


    public void init(String userId) {
        String userId1 = userId;

        progressAppts = new MutableLiveData<>();
        upcomingAppts = new MutableLiveData<>();
        feedbackAppts = new MutableLiveData<>();
        pastAppts = new MutableLiveData<>();
        userInfoLD = new MutableLiveData<>();
        managerInfoLD = new MutableLiveData<>();

        connectionFirebase = new ConnectionFirebase(this, userId);
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

    //handle insert probably through this as well


    //FIXME handle the db fetching of data and check it
    //then implement the logic for moving an appointment from one section to another

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

    //interract with Firebase to add new appointment
    public void addAppointment(Appointment newA) {
        connectionFirebase.addAppointment(newA);
    }

    //interract with Firebase to remove appointment
    //Should they be allowed to do it?
    public void removeAppointmnet(Appointment x, AppointmentState appointmentState) {
        connectionFirebase.removeAppointment(x, appointmentState);
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
        connectionFirebase.createNotFinishedAppt(selectedAppt, appointmentState, reason);
    }

    /**
     * Called when the user has provide for an appointment. The appointment has to be moved from initial category to PAST appts and the answers stored under the same key in "feedback" node
     */
    public void apptFeedbackProvided(final Appointment selectedAppt, final AppointmentState appointmentState, final String[] answers) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                connectionFirebase.apptFeedbackProvided(selectedAppt, appointmentState, answers);
            }
        }).start();

    }
}
