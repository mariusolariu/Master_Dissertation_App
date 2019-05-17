package com.example.myapplication.model;

import android.arch.lifecycle.ViewModel;

import com.example.myapplication.model_firebase.Appointment;
import com.example.myapplication.util.AppointmentState;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * In fact, if you need an Application context, you should extend AndroidViewModel which is simply a ViewModel that includes an Application reference.
 */
public class ProgressModel extends ViewModel {
    private List<Appointment> progressAppointments;
    private int userId;

    public void init(int userId) {
        this.userId = userId;
    }

    public List<Appointment> getProgressAppointments() {
        //get the data from the Db


        return progressAppointments;
    }
}
