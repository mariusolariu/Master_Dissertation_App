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

import com.example.myapplication.R;
import com.example.myapplication.model.MainActivityModel;
import com.example.myapplication.model_firebase.Appointment;
import com.example.myapplication.util.AppointmentState;
import com.example.myapplication.util.InternetCheckTask;


import java.util.ArrayList;
import java.util.List;


//TODO Write expresso tests:
//      * to add an appointment,
//      * remove one,
//      * change the appointment date and see if they appear in the right section
//      * provide feedback for an appointment and see if they are are moved to past appoinments

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, InternetCheckTask.InternetVisiter {
    private static final String MAIN_TAG = "Marius";

    //ui
    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle drawerToggle;
    private NavigationView navigationView;
    private ViewPager viewPager;
    private FloatingActionButton addButton;

    private MainActivityModel maModel;
    private List<Fragment> fragments;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.nav_drawer);

        //stop the execution until the internet is on
        InternetCheckTask internetCheckTask = new InternetCheckTask(this);
        internetCheckTask.execute();

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

        maModel = ViewModelProviders.of(this).get(MainActivityModel.class);
        //FIXME: to be modified later with the id of the logged user
        maModel.init(1);

        maModel.getAppointments().observe(this, new Observer<List<Appointment>>() {
            @Override
            public void onChanged(@Nullable List<Appointment> appointments) {
                //send the new list to the active fragment

                int fragmentIndex = viewPager.getCurrentItem();
                Fragment fragment = fragments.get(fragmentIndex);

                ((AppointmentsListReceiver)fragment).onAppoinmentsListReceived(appointments);
            }
        });

        addButton = findViewById(R.id.newAppointmentFB);

        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Launch Dialog, get data
                Appointment a = new Appointment();

                maModel.addAppointment(a);
            }
        });



        //FIXME modify it later and make sure that it complies with ViewModel a rchitecture
        //FIXME check internet is on
//        databaseReference = FirebaseDatabase.getInstance().getReference();
//
//
//        DatabaseReference appointments = databaseReference.child("appointments").child("app1");
//
//        appointments.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//
//
//                if (dataSnapshot.exists()) {
//                    Appointment a1 = dataSnapshot.getValue(Appointment.class);
//                    Log.d(MAIN_TAG, a1.getM_code());
//                    Log.d(MAIN_TAG, a1.getLocation());
//                    Log.d(MAIN_TAG, a1.getDate());
//                    Log.d(MAIN_TAG, a1.getTime());
//
//                }
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//
//            }
//        });


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

        fragments.add(new PastFragment());

        sectionsPageAdapter.addFragment(fragments.get(0), "Progress");
        sectionsPageAdapter.addFragment(fragments.get(1), "Upcoming");
        sectionsPageAdapter.addFragment(fragments.get(2), "Feedback");
        sectionsPageAdapter.addFragment(fragments.get(3), "Past");

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
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public void internetResult(Boolean internetOn) {
        if (internetOn){
            Toast.makeText(this, "Internet is on", Toast.LENGTH_SHORT).show();
        }else {
            Toast.makeText(this, "Internet is off", Toast.LENGTH_SHORT).show();
        }
    }
}
