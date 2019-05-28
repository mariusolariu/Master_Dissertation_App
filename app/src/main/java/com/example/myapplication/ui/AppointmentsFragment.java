package com.example.myapplication.ui;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.example.myapplication.R;
import com.example.myapplication.model.ProgressModel;
import com.example.myapplication.model_firebase.Appointment;
import com.example.myapplication.util.AppointmentState;

import java.util.ArrayList;
import java.util.List;

public class AppointmentsFragment extends Fragment implements AppointmentsListReceiver {
    private ProgressModel progressModel;

    private List<Appointment> appointmentList;
    private ListView progressAppsLV;
    //FIXME to be modified later with custom adapter
    private ArrayAdapter<Appointment> appointmentsAdapter;
    private AppointmentState fragmentType;

    public AppointmentsFragment() {
        appointmentList = new ArrayList<>();

    }

    /**
     * The system calls this when creating the fragment. Within your implementation, you should initialize essential components of the fragment that you want to retain when the fragment is paused or stopped, then resumed.
     */
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle passedBundle = getArguments();
        if (passedBundle != null) {
            fragmentType = AppointmentState.valueOf(passedBundle.getString("type"));
        }
        appointmentsAdapter = new AppointmentAdapter(getActivity(), R.layout.appointment_item, appointmentList);

    }

    @Nullable
    @Override
    /*
     * The system calls this when it's time for the fragment to draw its user interface for the first time. To draw a UI for your fragment, you must return a View from this method that is the root of your fragment's layout. You can return null if the fragment does not provide a UI.
     */
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
//    Log.i(MainActivity.YMCA_TAG, "onCreateView called " + fragmentType);
        View view = inflater.inflate(R.layout.fragment_layout, container, false);

        progressAppsLV = view.findViewById(R.id.progressLV);
        progressAppsLV.setAdapter(appointmentsAdapter);


        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    }

    //Every time the fragments is resumed the list of appointments is update with what's currently in DB
    @Override
    public void onResume() {
        super.onResume();
        List<Appointment> upToDateAppointments = ((MainActivity) getActivity()).getAppointments(fragmentType);
        int cachedApptsSize = appointmentList.size();

        if ((upToDateAppointments != null) && (upToDateAppointments.size() != cachedApptsSize)) {

            Log.i(MainActivity.YMCA_TAG, "appt list changed for " + fragmentType);

            //maybe run the next two instructions on a separate thread
            appointmentList.clear();
            appointmentList.addAll(upToDateAppointments);
            appointmentsAdapter.notifyDataSetChanged();
        }

//    Log.i(MainActivity.YMCA_TAG, "onResume called for " + fragmentType);
    }

    /**
     * The system calls this method as the first indication that the user is leaving the fragment (though it doesn't always mean the fragment is being destroyed). This is usually where you should commit any changes that should be persisted beyond the current user session (because the user might not come back)
     */
    @Override
    public void onPause() {
        super.onPause();
//    Log.i(MainActivity.YMCA_TAG, "onPause called for " + fragmentType);

    }

    @Override
    public void onStop() {
        super.onStop();
//    Log.i(MainActivity.YMCA_TAG, "onStop called for " + fragmentType);
    }

    @Override
    public void onAppoinmentsListChanged(List<com.example.myapplication.model_firebase.Appointment> list) {
        //notify the adapter that data has changed

        appointmentList.clear();
        appointmentList.addAll(list);
        appointmentsAdapter.notifyDataSetChanged();

    }
}
