package com.sibot.mentorapp.ui;

import android.Manifest;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.telephony.SmsManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.droidnet.DroidListener;
import com.droidnet.DroidNet;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.sibot.mentorapp.R;
import com.sibot.mentorapp.model.Appointment;
import com.sibot.mentorapp.model.ManagerInfo;
import com.sibot.mentorapp.model.UserInfo;
import com.sibot.mentorapp.model.ViewModelMA;
import com.sibot.mentorapp.util.AppointmentState;
import com.sibot.mentorapp.util.EditTextHelper;
import com.sibot.mentorapp.util.InternetDialogHelper;

import java.util.ArrayList;
import java.util.List;


//TODO Write expresso tests:
//      * to add an appointment,
//      * mark as non completed with reason,
//      * change the appointment date and see if they appear in the right section
//      * provide feedback for an appointment and see if they are are moved to past appoinments

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, DroidListener {
    public static final String APP_TAG = "Booking_App";
    final static String EDIT_APPOINTMENT = "EDIT_APPOINTMENT";
    final static String ANSWERS_ARRAY_CODE = "ANSWERS_ARRAY_CODE";
    final static String PARTICIPANT_FEEDBACK = "PARTICIPANT_FEEDBACK";


    final static String FEEDBACK_QUESTIONS_TAG = "FEEDBACK_QUESTIONS";

    private static final int NEW_APPT_CODE = 100;
    static final int REQ_SIGN_IN = 101;
    private static final int REQ_SMS_PERMISSION = 102;
    private static final int EDIT_APPT_CODE = 103;
    static final int PROVID_FDBK_CODE = 104;
    static final int PARTICIPANT_PROVID_FDBK_CODE = 105;

    //ui
    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle drawerToggle;
    private FloatingActionButton addApptBtn;
    private ProgressBar loginPB;
    private TextView userNameTV;
    private TextView userEmailTV;
    private View headerLayout;
    private AlertDialog reasonAlertDialog;
    private AlertDialog usernameAlertDialog;

    private ViewModelMA maModel;
    private List<Fragment> fragments;

    private boolean smsPermissionGranted;
    //updated whenever the user of the app is modified;
    private UserInfo userInfo;
    private ManagerInfo managerInfo;
    private List<String> fdbkQuestions;
    //this gets initialized when the user long clicks an ListView item in fragment
    private Appointment selectedAppt;
    private AppointmentState appointmentState;
    private boolean listenerWasntAttached = true;


    private static final int PROGRESS_FRAG_INDEX = 0;
    private static final int UPCOMING_FRAG_INDEX = 1;
    private static final int FEEDBACK_FRAG_INDEX = 2;
    private static final int PAST_FRAG_INDEX = 3;

    //Authentification
    private FirebaseAuth firebaseAuth;
    private FirebaseAuth.AuthStateListener authStateListener;

    //used for telling Espresso sign in test to wait for operation to finish
//    CountingIdlingResource singInResource = new CountingIdlingResource("SING_IN_RESOURCE");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.nav_drawer);

        //Library that helps to check the internet is always on when using the app
        DroidNet.init(this);
        DroidNet.getInstance().addInternetConnectivityListener(this);


        FirebaseApp.initializeApp(this);
        firebaseAuth = FirebaseAuth.getInstance();

        authStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser currentUser = firebaseAuth.getCurrentUser();

                if ((currentUser != null) /*&& (currentUser.isEmailVerified())*/) {                    //user is signed in
//                    Toast.makeText(MainActivity.this, "Successful sign in!", Toast.LENGTH_SHORT).show();
                    String userId = currentUser.getUid();
                    setUpApp(userId);
                } else {
                    startActivityForResult(new Intent(MainActivity.this, SignUpActivity.class), REQ_SIGN_IN);
                }

            }
        };

        firebaseAuth.addAuthStateListener(authStateListener);


        //needs to be initialized to handle correctly a configuration change
        try {
            maModel = ViewModelProviders.of(this).get(ViewModelMA.class);
        } catch (IllegalStateException e) {
            Toast.makeText(this, "Error initialising the model!", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }

    public void setUpApp(final String userId) {
        maModel.init(userId);

        addApptBtn = findViewById(R.id.newAppointmentFB);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        //triggers onCreateOptionsMenu which hides the toolbar buttons
        invalidateOptionsMenu();

        drawerLayout = findViewById(R.id.nav_drawer);
        NavigationView navigationView = findViewById(R.id.nv);
        drawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.Open, R.string.Close);
        drawerLayout.addDrawerListener(drawerToggle);
        drawerToggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);

        //set view pager
        ViewPager viewPager = findViewById(R.id.viewPager);
        setupViewPager(viewPager);

        TabLayout tabLay = findViewById(R.id.tabLayout);
        tabLay.setupWithViewPager(viewPager);


        //when the activity regains focus you don't wont to inflate it again
        if (headerLayout == null) {
            headerLayout = navigationView.inflateHeaderView(R.layout.nav_header);
        }

        //  View headerLayout = navigationView.getHeaderView(0);
        userNameTV = headerLayout.findViewById(R.id.userNameTV);
        userEmailTV = headerLayout.findViewById(R.id.userEmailTV);
        String userName = "";
        String userEmail = "";
        //user info will be changed once the data is fetched from DB, this is a provisory trick to avoid NPE
        userInfo = new UserInfo(userName, userEmail);
        managerInfo = new ManagerInfo("", "");

        addModelChangedDataListeners();
        addUI_Listeners();
        updateUserInfo();
        checkForSmsPermission();

        createAlertDialog(R.string.provide_name_title, AlertDialogType.USER_NAME);
        createAlertDialog(R.string.title_reason_dialog, AlertDialogType.REASON_AD);
    }

    public List<String> getFdbkQuestions() {
        return fdbkQuestions;
    }

    private void createAlertDialog(int titleResource, final AlertDialogType dialogType) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        LayoutInflater layoutInflater = this.getLayoutInflater();

        final View inflatedView = layoutInflater.inflate(R.layout.alert_dialog_layout, null);

        builder.setView(inflatedView);

        builder.setTitle(titleResource);

        //doesn't allow the dialog to be canceled when user touches an area that is outside the dialog
        builder.setCancelable(false);


        builder.setPositiveButton(R.string.OK, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });

        if (AlertDialogType.REASON_AD == dialogType) {
            builder.setNegativeButton(R.string.Cancel, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    //make Toolbar buttons disappear
//                    dialogInterface.dismiss();

                    selectedAppt = null;
                    appointmentState = null;
                    invalidateOptionsMenu();
                }
            });
        }

        if (dialogType == AlertDialogType.REASON_AD) {
            reasonAlertDialog = builder.create();

        } else {
            usernameAlertDialog = builder.create();


        }
    }

    private void updateUserInfo() {
        if (userNameTV != null) {
            userNameTV.setText(userInfo.user_name);
            userEmailTV.setText(userInfo.email);
        }
    }

    private void checkForSmsPermission() {
        if (ActivityCompat.checkSelfPermission(this,
                Manifest.permission.SEND_SMS) !=
                PackageManager.PERMISSION_GRANTED) {
            // Permission not yet granted. Use requestPermissions().
            // MY_PERMISSIONS_REQUEST_SEND_SMS is an
            // app-defined int constant. The callback method gets the
            // result of the request.
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.SEND_SMS},
                    REQ_SMS_PERMISSION);
        } else {
            // Permission already granted. Enable the SMS button.
            // enableSmsButton();
            smsPermissionGranted = true;
        }
    }


    /* When using ActionBarDrawerToggle these the following two methods have to be called
     */
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        drawerToggle.syncState();
    }

    @Override
    public void onPostCreate(@Nullable Bundle savedInstanceState, @Nullable PersistableBundle persistentState) {
        super.onPostCreate(savedInstanceState, persistentState);
        drawerToggle.syncState();
    }

    private void addUI_Listeners() {
        addApptBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, NewAppointmentActivity.class);
                startActivityForResult(intent, NEW_APPT_CODE);

            }
        });

    }


//    //used only for testing
//    public CountingIdlingResource getSingInResource() {
//
//        if (singInResource == null) {
//            singInResource = new CountingIdlingResource("SING_IN_RESOURCE");
//        }
//        return singInResource;
//    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case NEW_APPT_CODE:
                if (resultCode == RESULT_OK) {
                    Appointment newAppt = data.getExtras().getParcelable(NewAppointmentActivity.NEW_APPOINTMENT_TAG);
                    maModel.addAppointment(newAppt);
                }

                break;

            case REQ_SIGN_IN:
                if (resultCode == RESULT_OK) {
                    String email = data.getExtras().getString(SignUpActivity.EMAIL_TAG);
                    String password = data.getExtras().getString(SignUpActivity.PASS_TAG);

                    if (loginPB == null) {
                        loginPB = findViewById(R.id.indeterminatePB);
                    }
                    loginPB.setVisibility(View.VISIBLE);
//                    singInResource.increment();

                    firebaseAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (!task.isSuccessful()) {
                                Toast.makeText(MainActivity.this, "Failed to sign in!\n Please try again!", Toast.LENGTH_SHORT).show();
                                startActivityForResult(new Intent(MainActivity.this, SignUpActivity.class), REQ_SIGN_IN);
                            } else {
                                //Succesful login
                                MainActivity.this.loginPB.setVisibility(View.GONE);

                                //Strictly used for testing login with Espresso
//                                singInResource.decrement();
                            }
                        }
                    });

                }


                break;

            case EDIT_APPT_CODE:
                if (resultCode == RESULT_OK) {
                    Appointment possibleNewAppt = data.getExtras().getParcelable(NewAppointmentActivity.NEW_APPOINTMENT_TAG);

                    if (!possibleNewAppt.equals(selectedAppt)) {
                        maModel.removeAppointmnet(selectedAppt, appointmentState);

                        maModel.addAppointment(possibleNewAppt);
                    }

                }


                selectedAppt = null;
                appointmentState = null;
                invalidateOptionsMenu(); //hide action bar buttons

                break;

            case PROVID_FDBK_CODE:
                if (resultCode == RESULT_OK) {
                    String[] answers = data.getExtras().getStringArray(ANSWERS_ARRAY_CODE);
                    maModel.apptFeedbackProvided(selectedAppt, appointmentState, answers);

                }

                selectedAppt = null;
                appointmentState = null;
                invalidateOptionsMenu();

                break;

            case PARTICIPANT_PROVID_FDBK_CODE:
                if (resultCode == RESULT_OK) {
                    String participantMessage = data.getExtras().getString(PARTICIPANT_FEEDBACK);
                    maModel.participantFdbkProvided(selectedAppt, participantMessage);
                }

                selectedAppt = null;
                appointmentState = null;
                invalidateOptionsMenu();

            default:
                Log.i(APP_TAG, "Invalid code, no activity with this code was launched: " + requestCode);

        }
    }


    private void addModelChangedDataListeners() {
        maModel.getProgressAppts().observe(this, new Observer<List<Appointment>>() {
            @Override
            public void onChanged(@Nullable List<Appointment> appointments) {
                //send the new list to the active fragment

                Fragment fragment = fragments.get(PROGRESS_FRAG_INDEX);

                if (fragment.isVisible()) {
                    ((AppointmentsListReceiver) fragment).onAppoinmentsListChanged(appointments);
                }

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

        maModel.getUserInfoLD().observe(this, new Observer<UserInfo>() {
            @Override
            public void onChanged(@Nullable final UserInfo userInfo) {
                if (userInfo.user_name == null) { // first login for the user, ask for his name
                    usernameAlertDialog.show();

                    usernameAlertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            EditText editText;

                            editText = usernameAlertDialog.findViewById(R.id.dialogET);

                            String textValue = editText.getText().toString();

                            if (EditTextHelper.isET_empty(editText, textValue, "Please fill in information!")) {
                                return;
                            }

                            maModel.saveNewUserName(textValue);
                            MainActivity.this.userInfo = userInfo;
                            updateUserInfo();


                            editText.setText("");
                            usernameAlertDialog.dismiss();
                        }
                    });

                } else {
                    MainActivity.this.userInfo = userInfo;
                    updateUserInfo();
                }
            }
        });

        maModel.getManagerInfoLD().observe(this, new Observer<ManagerInfo>() {
            @Override
            public void onChanged(@Nullable ManagerInfo managerInfo) {
                MainActivity.this.managerInfo = managerInfo;
            }
        });

        maModel.getFdbkQuestionsLD().observe(this, new Observer<List<String>>() {
            @Override
            public void onChanged(@Nullable List<String> fdbkQuestions) {
                MainActivity.this.fdbkQuestions = fdbkQuestions;
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //add the edit and reaso buttons to Main activity Toolbar
        getMenuInflater().inflate(R.menu.toolbar_items, menu);

        MenuItem editButton = menu.findItem(R.id.editApptB);
        MenuItem reasonAppt = menu.findItem(R.id.prvidReasoB);
        MenuItem provideFeedback = menu.findItem(R.id.participantFeedReasB);

        if (selectedAppt == null) {
            editButton.setVisible(false);
            reasonAppt.setVisible(false);
            provideFeedback.setVisible(false);
        } else {
            editButton.setVisible(true);
            editButton.setVisible(true);
            provideFeedback.setVisible(true);
        }

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public void onAttachFragment(Fragment fragment) {
        super.onAttachFragment(fragment);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (drawerToggle.onOptionsItemSelected(item))
            return true;


        switch (item.getItemId()) {
            case R.id.editApptB:

                Intent intent = new Intent(this, NewAppointmentActivity.class);
                intent.putExtra(EDIT_APPOINTMENT, selectedAppt);
                startActivityForResult(intent, EDIT_APPT_CODE);

                return true;

            case R.id.prvidReasoB:
                reasonAlertDialog.show();

                if (listenerWasntAttached) {
                    reasonAlertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            EditText editText;

                            editText = reasonAlertDialog.findViewById(R.id.dialogET);

                            String textValue = editText.getText().toString();

                            if (EditTextHelper.isET_empty(editText, textValue, "Please fill in information!")) {
                                return;
                            }

                            maModel.createNotFinishedAppt(selectedAppt, appointmentState, textValue);
                            selectedAppt = null;
                            appointmentState = null;
                            invalidateOptionsMenu();


                            editText.setText("");
                            reasonAlertDialog.dismiss();
                        }
                    });

                    listenerWasntAttached = false;
                }

                return true;

            case R.id.participantFeedReasB:
                Intent intent1 = new Intent(this, ParticipantFdbkActivity.class);
                startActivityForResult(intent1, PARTICIPANT_PROVID_FDBK_CODE);

                return true;

            default:
                Toast.makeText(this, "Not a valid option!", Toast.LENGTH_SHORT).show();

                return super.onOptionsItemSelected(item);
        }

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

//        viewPager.setOffscreenPageLimit(0);

        viewPager.setAdapter(sectionsPageAdapter);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        int itemId = menuItem.getItemId();

        switch (itemId) {
            case R.id.sosButton:

                if (smsPermissionGranted) {

                    if (!TextUtils.isEmpty(managerInfo.m_phone_no)) {
                        SmsManager smsManager = SmsManager.getDefault();
                        String message = "Hi,\nI'm " + userInfo.user_name + " and I have a problem with my volunteering session!";
                        smsManager.sendTextMessage(managerInfo.m_phone_no, null, message, null, null);
                        Toast.makeText(this, "Message was sent!", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(this, "Failed to send message to manager!", Toast.LENGTH_SHORT).show();
                    }

                } else {
                    //ask again the user to enable the permission
                    checkForSmsPermission();
                }

                break;

            case R.id.resetPassB:
                //get email
                if (!TextUtils.isEmpty(userInfo.email)) {
                    firebaseAuth.sendPasswordResetEmail(userInfo.email).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Toast.makeText(MainActivity.this, "Reset password email sent!", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(MainActivity.this, "Cannot reset password\n! Contact administrator!", Toast.LENGTH_SHORT).show();

                            }
                        }
                    });
                }

                break;

            case R.id.signOutB:
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        firebaseAuth.signOut();
                    }
                }).start();
                break;

            default:
                return true;
        }

        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == REQ_SMS_PERMISSION) {
            if (permissions[0].equalsIgnoreCase(Manifest.permission.SEND_SMS) &&
                    grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                smsPermissionGranted = true;
            } else {
                Toast.makeText(this, "App won't be able to send SOS Message!", Toast.LENGTH_SHORT).show();
            }
        }
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
        InternetDialogHelper icHelper = InternetDialogHelper.getInstance(this);

        if (isConnected) {

            if (icHelper.isDialogShowing()) {
                icHelper.dismissDialog();
            }


        } else {

            icHelper.showDialog();
        }

    }

    //Needed to update the lists that are displayed in fragments when the user moves between them -- called from onResume of fragment
    public List<Appointment> getAppointments(AppointmentState fragmentType) {
        List<Appointment> appointments;

        switch (fragmentType) {
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


    /**
     * Set selected appt to be edited or marked as not complete
     */
    public void setSelectedAppt(Appointment appt, AppointmentState appointmentState) {
        selectedAppt = appt;
        this.appointmentState = appointmentState;
    }

    @Override
    protected void onStart() {
        super.onStart();


    }

    @Override
    protected void onStop() {
        super.onStop();

//        if (authStateListener != null) {
//            firebaseAuth.removeAuthStateListener(authStateListener);
//        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        firebaseAuth.removeAuthStateListener(authStateListener);
//        Log.i(APP_TAG, "onDestroy called");
    }
}
