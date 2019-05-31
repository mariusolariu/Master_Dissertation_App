package com.example.myapplication.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.droidnet.DroidListener;
import com.droidnet.DroidNet;
import com.example.myapplication.R;
import com.example.myapplication.util.EditTextHelper;

public class ProvideFdbkActivity extends AppCompatActivity implements DroidListener {
    private boolean activityNotSetUp = true;
    private EditText[] answersET;
    private Button saveBtn;
    private static final int NUMBER_OF_ANSWERS = 5;
    private static final String ANSWER_NOT_FILLED_MESSAGE = "Please fill in the answer...";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.feedback_activity);

        DroidNet.init(this);
        DroidNet.getInstance().addInternetConnectivityListener(this);
    }

    private void setUp() {
        answersET = new EditText[NUMBER_OF_ANSWERS];
        answersET[0] = findViewById(R.id.q1ET);
        answersET[1] = findViewById(R.id.q2ET);
        answersET[2] = findViewById(R.id.q3ET);
        answersET[3] = findViewById(R.id.q4ET);
        answersET[4] = findViewById(R.id.q5ET);

        saveBtn = findViewById(R.id.saveFdbkBtn);

        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String[] answers = new String[NUMBER_OF_ANSWERS];

                for (int i = 0; i < answers.length; i++) {
                    answers[i] = answersET[i].getText().toString();

                    if (EditTextHelper.emptyET(answersET[i], answers[i], ANSWER_NOT_FILLED_MESSAGE))
                        return;

                }

                Intent intent = new Intent();
                intent.putExtra(MainActivity.ANSWERS_ARRAY_CODE, answers);
                setResult(RESULT_OK, intent);
                finish();
            }
        });

        activityNotSetUp = false;
    }

    @Override
    public void onInternetConnectivityChanged(boolean isConnected) {

        if (isConnected) {
//            Toast.makeText(this, "Internet is on!", Toast.LENGTH_SHORT).show();
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
