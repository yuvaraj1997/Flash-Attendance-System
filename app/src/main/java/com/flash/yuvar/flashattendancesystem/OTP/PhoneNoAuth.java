package com.flash.yuvar.flashattendancesystem.OTP;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;

import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.ErrorCodes;
import com.firebase.ui.auth.IdpResponse;
import com.firebase.ui.auth.ResultCodes;
import com.flash.yuvar.flashattendancesystem.LoginActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Arrays;

public class PhoneNoAuth extends AppCompatActivity {

    String email;
    private static final int RC_SIGN_IN = 123;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {

        email  = getIntent().getStringExtra("email");
        super.onCreate(savedInstanceState);

        startActivityForResult(
                AuthUI.getInstance()
                        .createSignInIntentBuilder()
                        .setAvailableProviders(
                                Arrays.asList(
                                        new AuthUI.IdpConfig.Builder(AuthUI.PHONE_VERIFICATION_PROVIDER).build()
                                ))
                        .build(),
                RC_SIGN_IN);
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // RC_SIGN_IN is the request code
        if (requestCode == RC_SIGN_IN) {
            IdpResponse response = IdpResponse.fromResultIntent(data);
// Successfully signed in
            if (resultCode == ResultCodes.OK) {



                FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();

                FirebaseAuth.getInstance().signOut();


                Intent intentStudent = new Intent(PhoneNoAuth.this, LoginActivity.class);
                intentStudent.putExtra("email", email);
                intentStudent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intentStudent);
                finish();
                return;
            }
            // Sign in failed
            else {
                // User pressed back button
                if (response == null) {
                    ShowAlertBox("Login canceled by User");
                    return;
                }
                //No internet connection on the device
                if (response.getErrorCode() == ErrorCodes.NO_NETWORK) {
                    ShowAlertBox("No Internet Connection");
                    return;
                }
                //other errors
                if (response.getErrorCode() == ErrorCodes.UNKNOWN_ERROR) {
                    ShowAlertBox("Unknown Error");
                    return;
                }
            }
            ShowAlertBox("Unknown sign in response");
        }
    }
    //Creating a custom alert box
    protected void ShowAlertBox(String msg){
        AlertDialog.Builder builder1 = new AlertDialog.Builder(PhoneNoAuth.this);
        builder1.setMessage(msg);
        builder1.setCancelable(true);
        builder1.setPositiveButton(
                "Ok",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
        AlertDialog alert11 = builder1.create();
        alert11.show();
    }
}