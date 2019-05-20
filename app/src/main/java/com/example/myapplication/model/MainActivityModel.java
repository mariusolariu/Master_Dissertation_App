package com.example.myapplication.model;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;

import com.example.myapplication.model_firebase.Appointment;
import com.example.myapplication.model_firebase.ConnectionFirebase;

import java.util.List;

public class MainActivityModel extends ViewModel implements AppointmentsListener {
    private MutableLiveData<List<Appointment>> progressAppts;
    private MutableLiveData<List<Appointment>> upcomingAppts;
    private MutableLiveData<List<Appointment>> feedbackAppts;
    private MutableLiveData<List<Appointment>> pastAppts;

    private ConnectionFirebase firebaseDatabase;


    private int userId;

    public void init(int userId) {
        this.userId = userId;

        if (progressAppts == null) {
            progressAppts = new MutableLiveData<>();
            upcomingAppts = new MutableLiveData<>();
            feedbackAppts = new MutableLiveData<>();
            pastAppts = new MutableLiveData<>();

            firebaseDatabase = new ConnectionFirebase(this);
        }
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

    //interract with Firebase to add new appointment
    public void addAppointment(Appointment newA) {

    }

    //interract with Firebase to remove appointment
    //Should they be allowed to do it?
    public void removeAppointmnet(Appointment x) {

    }

    @Override
    protected void onCleared() {
        super.onCleared();
        //clear any subscription of data
    }

}
