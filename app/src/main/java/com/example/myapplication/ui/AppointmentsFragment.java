package com.example.myapplication.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.example.myapplication.R;
import com.example.myapplication.model.Appointment;
import com.example.myapplication.util.AppointmentState;

import java.util.ArrayList;
import java.util.List;

import static com.example.myapplication.ui.MainActivity.FEEDBACK_QUESTIONS_TAG;
import static com.example.myapplication.ui.MainActivity.PROVID_FDBK_CODE;

public class AppointmentsFragment extends Fragment implements AppointmentsListReceiver {

    private final List<Appointment> appointmentList;
    private ListView progressAppsLV;
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

        progressAppsLV = view.findViewById(R.id.apptsLV);
        progressAppsLV.setAdapter(appointmentsAdapter);

        //edit or provide reason for canceling event
        progressAppsLV.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            /*
             This returns a boolean to indicate whether you have consumed the event and it should not be carried further.
             That is, return true to indicate that you have handled the event and it should stop here; return false if you have not handled it and/or the event should continue to any other on-click listeners.
             */
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int position, long rowId) {
                boolean eventHandled = true;

                //allow editing only of fragments which are UPCOMIN or PROGRESS
                if (fragmentType.equals(AppointmentState.PROGRESS) || (fragmentType.equals(AppointmentState.UPCOMING))) {
                    Appointment selectedAppt = appointmentList.get(position);
                    MainActivity mainActivity = (MainActivity) getActivity();
                    mainActivity.setSelectedAppt(selectedAppt, fragmentType);

                    //makes the buttons in toolbar appear
                    mainActivity.invalidateOptionsMenu();
                }

                return eventHandled;
            }
        });

        //go to provide feedback for appt
        progressAppsLV.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {

                if (!fragmentType.equals(AppointmentState.PAST)) {
                    Appointment selectedAppt = appointmentList.get(position);
                    MainActivity mainActivity = (MainActivity) getActivity();
                    mainActivity.setSelectedAppt(selectedAppt, fragmentType);

                    Intent intent = new Intent(mainActivity, ProvideFdbkActivity.class);
                    Bundle bundle = new Bundle();
                    List<String> fdbkQuestions = mainActivity.getFdbkQuestions();
                    String[] questions = fdbkQuestions.toArray(new String[fdbkQuestions.size()]);

                    bundle.putStringArray(FEEDBACK_QUESTIONS_TAG, questions);
                    intent.putExtras(bundle);

                    //fragments can start activities too and they have their own onActivityResult()
                    mainActivity.startActivityForResult(intent, PROVID_FDBK_CODE);
                }
            }
        });


        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
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

        if ((upToDateAppointments != null) &&
                ((upToDateAppointments.size() != cachedApptsSize) ||
                        !upToDateAppointments.equals(appointmentList))) {

//            Log.i(MainActivity.YMCA_TAG, "appt list changed for " + fragmentType);

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
    public void onAppoinmentsListChanged(List<Appointment> list) {
        //notify the adapter that data has changed

        appointmentList.clear();
        appointmentList.addAll(list);
        appointmentsAdapter.notifyDataSetChanged();

    }
}
