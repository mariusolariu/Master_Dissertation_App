package com.sibot.mentorapp.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.droidnet.DroidListener;
import com.droidnet.DroidNet;
import com.sibot.mentorapp.R;
import com.sibot.mentorapp.util.EditTextHelper;

public class SignUpActivity extends AppCompatActivity implements DroidListener {
    public static final String EMAIL_TAG = "email";
    public static final String PASS_TAG = "pass";

    private EditText emailET;
    private EditText passwordET;

    private boolean activWasntSetUp = true;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        DroidNet.init(this);
        DroidNet mDroidNet = DroidNet.getInstance();
        mDroidNet.addInternetConnectivityListener(this);
    }

    private void setUp() {
        emailET = findViewById(R.id.emailET);
        passwordET = findViewById(R.id.passwordET);
        Button signInButton = findViewById(R.id.loginB);

        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String emailText = emailET.getText().toString();

                if (EditTextHelper.isET_empty(emailET, emailText, "Please fill in the email address!")) {
                    return;
                }

                String passwordText = passwordET.getText().toString();

                if (EditTextHelper.isET_empty(passwordET, passwordText, "Please fill in the password!")) {
                    return;
                }

                //send data back to main activity

                Intent intent = new Intent();
                intent.putExtra(EMAIL_TAG, emailText);
                intent.putExtra(PASS_TAG, passwordText);
                setResult(RESULT_OK, intent);
                finish();
            }

        });

        activWasntSetUp = false;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
//        setResult(RESULT_CANCELED);
//        finish();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        DroidNet.getInstance().removeAllInternetConnectivityChangeListeners();
    }

    @Override
    public void onInternetConnectivityChanged(boolean isConnected) {

        if (isConnected) {

            if (activWasntSetUp) {
                setUp();
            }
        } else {
            Toast.makeText(this, "Turn on the Interent!", Toast.LENGTH_SHORT).show();
        }

    }
}
