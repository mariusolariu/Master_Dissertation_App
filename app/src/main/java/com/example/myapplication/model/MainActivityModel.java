package com.example.myapplication.model;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;

import com.example.myapplication.model_firebase.Appointment;
import com.example.myapplication.model_firebase.ConnectionFirebase;

import java.util.List;

public class MainActivityModel extends ViewModel implements AppointmentsListener {
    private MutableLiveData<List<Appointment>> appointmnentsLD;
    private ConnectionFirebase firebaseDatabase;
    private int userId;

    public void init(int userId) {
        this.userId = userId;

        if (appointmnentsLD == null) {
            appointmnentsLD = new MutableLiveData<>();
            firebaseDatabase = new ConnectionFirebase(this);
        }
    }

    public LiveData<List<Appointment>> getAppointments() {
        return appointmnentsLD;
    }

    //handle insert probably through this as well


    @Override
    public void onAppointmentsListChanged(List<Appointment> appointmentsList) {
        //the appoinments list was changed
        appointmnentsLD.setValue(appointmentsList);
    }

    //interract with Firebase to add new appointment
    public void addAppointment(Appointment newA){

    }

    //interract with Firebase to remove appointment x
    public void removeAppointmnet(Appointment x){

    }

    @Override
    protected void onCleared() {
        super.onCleared();
        //clear any subscription of data
    }

    public List<Appointment> getProgressAppts() {
        //FIXME
        return null;
    }
}
