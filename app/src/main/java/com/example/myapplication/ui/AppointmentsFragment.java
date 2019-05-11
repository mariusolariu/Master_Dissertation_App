package com.example.myapplication.ui;

import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
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

public class AppointmentsFragment extends Fragment implements AppointmentsListReceiver{
  private ProgressModel progressModel;

  private List<Appointment> appointmentList;
  private ListView progressAppsLV;
  //FIXME to be modified later with custom adapter
  private ArrayAdapter<Appointment> appointmentsAdapter;
  private AppointmentState fragmentType;

  /**
   * The system calls this when creating the fragment. Within your implementation, you should initialize essential components of the fragment that you want to retain when the fragment is paused or stopped, then resumed.
   */
  @Override
  public void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    //whenever onCreate is called again ViewModelProviders.of returns again
    progressModel = ViewModelProviders.of(this).get(ProgressModel.class);

    //FIXME to be fetched from login information later
    progressModel.init(1);

    if (savedInstanceState != null) {
      fragmentType = AppointmentState.valueOf(savedInstanceState.getString("type"));
    }

//    progressModel.getProgressAppointments().observe(this, new Observer<List<Appointment>>() {
//      @Override
//      public void onChanged(@Nullable List<Appointment> appointments) {
//        //Change/Update the UI of this fragment
//      }
//    });
  }

  @Nullable
  @Override
  /*
   * The system calls this when it's time for the fragment to draw its user interface for the first time. To draw a UI for your fragment, you must return a View from this method that is the root of your fragment's layout. You can return null if the fragment does not provide a UI.
   */
  public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
      @Nullable Bundle savedInstanceState) {

    View view = inflater.inflate(R.layout.frag_progress, container, false);

    appointmentList = new ArrayList<>();
    progressAppsLV =  view.findViewById(R.id.progressLV);

    appointmentsAdapter = new AppointmentAdapter(getActivity(), R.layout.appointment_item, appointmentList);
    progressAppsLV.setAdapter(appointmentsAdapter);

    return view;
  }

  @Override
  public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);

  }

  /**
   * The system calls this method as the first indication that the user is leaving the fragment (though it doesn't always mean the fragment is being destroyed). This is usually where you should commit any changes that should be persisted beyond the current user session (because the user might not come back)
   */
  @Override
  public void onPause() {
    super.onPause();
  }

  @Override
  public void onAppoinmentsListReceived(List<com.example.myapplication.model_firebase.Appointment> list) {
    //notify the adapter that data has changed
    //depending on the fragmentType filter the data

    appointmentList.clear();
    appointmentList.addAll(list);
    appointmentsAdapter.notifyDataSetChanged();

  }
}
