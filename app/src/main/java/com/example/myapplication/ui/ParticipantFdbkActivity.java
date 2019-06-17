package com.example.myapplication.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.droidnet.DroidListener;
import com.droidnet.DroidNet;
import com.example.myapplication.R;
import com.example.myapplication.util.EditTextHelper;

public class ParticipantFdbkActivity extends AppCompatActivity implements DroidListener {
    private boolean activityNotSetUp = true;
    public static final String SATISFIED = "Satisfied";
    public static final String NEUTRAL = "Neutral";
    public static final String DISSATISFIED = "Dissatisied";

    private EditText participantMessET;
    private ImageButton satisifedButton;
    private ImageButton neutralButton;
    private ImageButton dissatisfiedButton;
    private Button saveBtn;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.participant_fdbk_layout);

        DroidNet.init(this);
        DroidNet.getInstance().addInternetConnectivityListener(this);
    }


    private void setUp() {
        participantMessET = findViewById(R.id.participantMessET);
        satisifedButton = findViewById(R.id.satisfiedB);
        neutralButton = findViewById(R.id.neutralB);
        dissatisfiedButton = findViewById(R.id.dissatisifedB);
        saveBtn = findViewById(R.id.saveParticipantFdbkBtn);

        satisifedButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                participantMessET.setText(SATISFIED);
            }
        });

        neutralButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                participantMessET.setText(NEUTRAL);
            }
        });

        dissatisfiedButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                participantMessET.setText(DISSATISFIED);
            }
        });


        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String participantMessage = participantMessET.getText().toString();

                if (EditTextHelper.emptyET(participantMessET, participantMessage, "Provide feedback about meeting...")) {
                    return;
                }

                //send the text back to the main activity
                Intent intent = new Intent();
                intent.putExtra(MainActivity.PARTICIPANT_FEEDBACK, participantMessage);
                setResult(RESULT_OK, intent);
                finish();

            }
        });
    }

    @Override
    public void onInternetConnectivityChanged(boolean isConnected) {
        if (isConnected) {
            if (activityNotSetUp) {
                setUp();
            }
        } else {
            Toast.makeText(this, "Turn on the Interent!", Toast.LENGTH_SHORT).show();
        }

    }


    @Override
    public void onBackPressed() {
        setResult(RESULT_CANCELED);
        finish();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            setResult(RESULT_CANCELED);
            finish();
        }
        return true;
    }
}
