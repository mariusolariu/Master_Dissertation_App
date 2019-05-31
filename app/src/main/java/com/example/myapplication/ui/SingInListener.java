package com.example.myapplication.ui;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import static com.example.myapplication.ui.MainActivity.REQ_SIGN_IN;


/**
 * Custom auth state listener which needs to be attached on two scenarios: i) first sign in and ii) sign out and sing in again
 */

//NOT USED FOR THE MOMENT

public class SingInListener implements FirebaseAuth.AuthStateListener {
    private final MainActivity mainActivity;

    @Override
    public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
        FirebaseUser currentUser = firebaseAuth.getCurrentUser();

        if (currentUser != null) {
            //user is signed in
            Toast.makeText(mainActivity, "User signed in", Toast.LENGTH_SHORT).show();

            //FIXME this check shouldn't be here
            // if (mainActivity.loginPB != null) mainActivity.loginPB.setVisibility(View.GONE);

            String userId = currentUser.getUid();
            mainActivity.setUpApp(userId);
        } else {
            mainActivity.startActivityForResult(new Intent(mainActivity, SignUpActivity.class), REQ_SIGN_IN);
        }

    }
}
