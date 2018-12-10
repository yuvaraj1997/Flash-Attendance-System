package com.flash.yuvar.flashattendancesystem;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;


import com.flash.yuvar.flashattendancesystem.Lecture.Lecture_request_acceptance;
import com.flash.yuvar.flashattendancesystem.Student.Student_join_class;
import com.google.firebase.auth.FirebaseAuth;

public class LectureMainActivity extends AppCompatActivity {

    private TextView lecture_id,lecture_pass;
    private Button logout,but_request;
    private FirebaseAuth firebaseAuth;
    private TabLayout tabLayout;
    private AppBarLayout appBarLayout;
    private ViewPager viewPager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate (savedInstanceState);
        setContentView (R.layout.activity_lecture_main);



        logout = findViewById (R.id.logout);
        but_request = findViewById (R.id.button_request);
        firebaseAuth = FirebaseAuth.getInstance();

        if (firebaseAuth.getCurrentUser ()==null){
            finish ();
            startActivity (new Intent (this,LoginActivity.class));
        }


        tabLayout = (TabLayout) findViewById (R.id.tablayout_id);
        viewPager = (ViewPager) findViewById (R.id.viewpager_id);

        but_request.setOnClickListener (new View.OnClickListener ( ) {
            @Override
            public void onClick(View v) {
                requestlist();
            }
        });



        logout.setOnClickListener (new View.OnClickListener ( ) {
            @Override
            public void onClick(View v) {
                Logout();
            }
        });



    }

    private void requestlist() {
        startActivity(new Intent (LectureMainActivity.this, Lecture_request_acceptance.class));
        finish();
    }

    private void Logout() {
        firebaseAuth.signOut();
        finish();
        startActivity(new Intent (LectureMainActivity.this, LoginActivity.class));
    }
}
