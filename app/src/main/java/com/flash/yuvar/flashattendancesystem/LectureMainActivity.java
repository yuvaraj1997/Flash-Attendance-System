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

import com.flash.yuvar.flashattendancesystem.Lecture.Lecture_Class_listcarry_Activity;
import com.flash.yuvar.flashattendancesystem.Lecture.Lecture_request_acceptance;
import com.flash.yuvar.flashattendancesystem.QRCode.Class_List_Carry_Activity;
import com.google.firebase.auth.FirebaseAuth;

public class LectureMainActivity extends AppCompatActivity {

    private TextView lecture_id,lecture_pass;
    private Button logout,but_request,but_generate,but_viewclass;
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
        but_generate = findViewById (R.id.button_generate);
        but_viewclass=findViewById (R.id.button_viewclass);
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

        but_generate.setOnClickListener (new View.OnClickListener ( ) {
            @Override
            public void onClick(View v) {
                generate();

            }
        });

        but_viewclass.setOnClickListener (new View.OnClickListener ( ) {
            @Override
            public void onClick(View v) {
                viewClass();
            }
        });



    }

    private void viewClass() {
        Intent  i = new Intent (getApplicationContext (),Lecture_Class_listcarry_Activity.class);
        startActivity (i);
    }

    private void generate() {
        Intent  i = new Intent (getApplicationContext (),Class_List_Carry_Activity.class);
        startActivity (i);

    }

    private void requestlist() {

        Intent  i = new Intent (getApplicationContext (),Lecture_request_acceptance.class);
        startActivity (i);

    }

    private void Logout() {
        firebaseAuth.signOut();
        finish();
        startActivity(new Intent (LectureMainActivity.this, LoginActivity.class));
    }
}
