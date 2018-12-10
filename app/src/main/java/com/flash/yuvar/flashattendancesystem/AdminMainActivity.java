package com.flash.yuvar.flashattendancesystem;

import android.content.Intent;
import android.os.Bundle;

import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;


import com.flash.yuvar.flashattendancesystem.Admin.Admin_view_class;
import com.flash.yuvar.flashattendancesystem.Admin.admin_add_class;
import com.google.firebase.auth.FirebaseAuth;

public class AdminMainActivity extends AppCompatActivity {
    private TextView admin_id,admin_pass;
    private Button logout,addclass,viewclass;
    private FirebaseAuth firebaseAuth;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate (savedInstanceState);
        setContentView (R.layout.activity_admin_main);


        firebaseAuth = FirebaseAuth.getInstance();

        if (firebaseAuth.getCurrentUser ()==null){
            finish ();
            startActivity (new Intent (this,LoginActivity.class));
        }

        logout = findViewById (R.id.logout);
        addclass = findViewById (R.id.addclass);
        viewclass = findViewById (R.id.viewclass);

        addclass.setOnClickListener (new View.OnClickListener ( ) {
            @Override
            public void onClick(View v) {
                addclass();
            }
        });


        viewclass.setOnClickListener (new View.OnClickListener ( ) {
            @Override
            public void onClick(View v) {

                viewclass();

            }
        });











        logout.setOnClickListener (new View.OnClickListener ( ) {
            @Override
            public void onClick(View v) {
                Logout();
            }
        });









    }

    private void viewclass() {

        startActivity(new Intent (AdminMainActivity.this, Admin_view_class.class));
        finish();
    }

    private void addclass() {


        startActivity(new Intent (AdminMainActivity.this, admin_add_class.class));
        finish();


    }

    private void Logout(){
        firebaseAuth.signOut();
        finish();
        startActivity(new Intent (AdminMainActivity.this, LoginActivity.class));
    }
}
