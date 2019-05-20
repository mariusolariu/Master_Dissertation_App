package com.example.myapplication.ui;

import android.support.v4.app.Fragment;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.droidnet.DroidListener;
import com.droidnet.DroidNet;
import com.example.myapplication.R;
import com.example.myapplication.model.MainActivityModel;
import com.example.myapplication.model_firebase.Appointment;
import com.example.myapplication.util.AppointmentState;
import com.example.myapplication.util.InternetConnectivityHelper;


import java.util.ArrayList;
import java.util.List;


//TODO Write expresso tests:
//      * to add an appointment,
//      * remove one,
//      * change the appointment date and see if they appear in the right section
//      * provide feedback for an appointment and see if they are are moved to past appoinments

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, DroidListener {
    public static final String YMCA_TAG = "Ymca_Paisley";

    //ui
    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle drawerToggle;
    private NavigationView navigationView;
    private ViewPager viewPager;
    private FloatingActionButton addButton;

    private MainActivityModel maModel;
    private List<Fragment> fragments;

    private boolean appWasntSetUp = true;

    private static final int PROGRESS_FRAG_INDEX = 0;
    private static final int UPCOMING_FRAG_INDEX = 1;
    private static final int FEEDBACK_FRAG_INDEX = 2;
    private static final int PAST_FRAG_INDEX = 3;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.nav_drawer);

        //Library that helps to check the internet is always on when using the app
        DroidNet.init(this);
        DroidNet.getInstance().addInternetConnectivityListener(this);


    }

    public void setUpApp(){
        maModel = ViewModelProviders.of(this).get(MainActivityModel.class);
        //FIXME: to be modified later with the id of the logged user
        maModel.init(1);



        Toolbar toolbar = findViewById(R.id.toolbar);

        //FIXME: add check for the internet to be on
//        setupDrawer();

        drawerLayout = findViewById(R.id.nav_drawer);
        navigationView = findViewById(R.id.nv);
        drawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.Open, R.string.Close);
        drawerLayout.addDrawerListener(drawerToggle);
        drawerToggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);

        //set view pager
        viewPager = findViewById(R.id.viewPager);
        setupViewPager(viewPager);

        TabLayout tabLay = findViewById(R.id.tabLayout);
        tabLay.setupWithViewPager(viewPager);

        addModelChangeDataListeners();

        addButton = findViewById(R.id.newAppointmentFB);

        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Launch Dialog, get data
                Appointment a = new Appointment();

                maModel.addAppointment(a);
            }
        });

        appWasntSetUp = false;
    }

    private void addModelChangeDataListeners() {
        maModel.getProgressAppts().observe(this, new Observer<List<Appointment>>() {
            @Override
            public void onChanged(@Nullable List<Appointment> appointments) {
                //send the new list to the active fragment

                Fragment fragment = fragments.get(PROGRESS_FRAG_INDEX);

                if (fragment.isVisible()) {
                    ((AppointmentsListReceiver) fragment).onAppoinmentsListChanged(appointments);
                }

                //FIXME: filter the data and update the lists for each category, eventually move appointments from one category to another (this should be done by the model)
            }
        });

        maModel.getUpcomingAppts().observe(this, new Observer<List<Appointment>>() {
            @Override
            public void onChanged(@Nullable List<Appointment> appointments) {

                Fragment fragment = fragments.get(UPCOMING_FRAG_INDEX);

                if (fragment.isVisible()) {
                    ((AppointmentsListReceiver) fragment).onAppoinmentsListChanged(appointments);
                }

            }
        });

        maModel.getFeedbackAppts().observe(this, new Observer<List<Appointment>>() {
            @Override
            public void onChanged(@Nullable List<Appointment> appointments) {

                Fragment fragment = fragments.get(FEEDBACK_FRAG_INDEX);

                if (fragment.isVisible()) {
                    ((AppointmentsListReceiver) fragment).onAppoinmentsListChanged(appointments);
                }

            }
        });

        maModel.getPastAppts().observe(this, new Observer<List<Appointment>>() {
            @Override
            public void onChanged(@Nullable List<Appointment> appointments) {

                Fragment fragment = fragments.get(PAST_FRAG_INDEX);

                if (fragment.isVisible()) {
                    ((AppointmentsListReceiver) fragment).onAppoinmentsListChanged(appointments);
                }

            }
        });
    }

    @Override
    public void onAttachFragment(Fragment fragment) {
        super.onAttachFragment(fragment);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (drawerToggle.onOptionsItemSelected(item))
            return true;

        return super.onOptionsItemSelected(item);
    }

    private void setupViewPager(ViewPager viewPager) {
        SectionsPageAdapter sectionsPageAdapter = new SectionsPageAdapter(getSupportFragmentManager());

        fragments = new ArrayList<>();

        AppointmentsFragment progressFragment = new AppointmentsFragment();
        Bundle b = new Bundle();
        b.putString("type", AppointmentState.PROGRESS.toString());
        progressFragment.setArguments(b);
        fragments.add(progressFragment);

        AppointmentsFragment upcomingFragment = new AppointmentsFragment();
        Bundle b1 = new Bundle();
        b1.putString("type", AppointmentState.UPCOMING.toString());
        upcomingFragment.setArguments(b1);
        fragments.add(upcomingFragment);

        AppointmentsFragment feedbackFragment = new AppointmentsFragment();
        Bundle b2 = new Bundle();
        b2.putString("type", AppointmentState.FEEDBACK.toString());
        feedbackFragment.setArguments(b2);
        fragments.add(feedbackFragment);

        AppointmentsFragment pastFragment = new AppointmentsFragment();
        Bundle b3 = new Bundle();
        b3.putString("type", AppointmentState.PAST.toString());
        pastFragment.setArguments(b3);
        fragments.add(pastFragment);

        sectionsPageAdapter.addFragment(fragments.get(PROGRESS_FRAG_INDEX), "Progress");
        sectionsPageAdapter.addFragment(fragments.get(UPCOMING_FRAG_INDEX), "Upcoming");
        sectionsPageAdapter.addFragment(fragments.get(FEEDBACK_FRAG_INDEX), "Feedback");
        sectionsPageAdapter.addFragment(fragments.get(PAST_FRAG_INDEX), "Past");

        viewPager.setOffscreenPageLimit(0);

        viewPager.setAdapter(sectionsPageAdapter);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        int itemId = menuItem.getItemId();

        switch (itemId) {
            case R.id.sosButton:
                Toast.makeText(MainActivity.this, "Please help", Toast.LENGTH_SHORT).show();
                break;

            case R.id.resetPassB:
                Toast.makeText(MainActivity.this, "Reset Pass", Toast.LENGTH_SHORT).show();
                break;

            case R.id.signOutB:
                Toast.makeText(MainActivity.this, "Sign out", Toast.LENGTH_SHORT).show();
                break;

            default:
                return true;
        }

        return true;
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        DroidNet.getInstance().removeAllInternetConnectivityChangeListeners();
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public void onInternetConnectivityChanged(boolean isConnected) {
        InternetConnectivityHelper icHelper = InternetConnectivityHelper.getInstance(this);

        if (isConnected) {

            if (icHelper.isDialogShowing()){
//                Toast.makeText(this, "Dialog is showing", Toast.LENGTH_SHORT).show();
                icHelper.dismissDialog();
            }

            if (appWasntSetUp){
                setUpApp();
            }

        } else {

            icHelper.showDialog();
        }

    }

    public List<Appointment> getAppointments(AppointmentState fragmentType){
        List<Appointment> appointments;

        switch (fragmentType){
            case PROGRESS:
                appointments = maModel.getProgressApptsList();
                break;

            case UPCOMING:
                appointments = maModel.getUpcomingApptsList();
                break;

            case FEEDBACK:
                appointments = maModel.getFeedbackApptsList();
                break;

            case PAST:
                appointments = maModel.getPastApptsList();
                break;

            default:
                //returns an empty list
                appointments = new ArrayList<>();

        }

        return appointments;
    }
}
