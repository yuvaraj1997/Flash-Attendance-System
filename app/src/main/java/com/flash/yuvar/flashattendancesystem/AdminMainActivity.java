package com.flash.yuvar.flashattendancesystem;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.flash.yuvar.flashattendancesystem.Admin.ClassSide.Admin_view_class;
import com.flash.yuvar.flashattendancesystem.Admin.StudentSide.CourseList;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


public class AdminMainActivity extends AppCompatActivity {
    private TextView noofclass,noofstudents,nooflectures;
    private Button logout;
    private FirebaseAuth firebaseAuth;

    public static final String TAG = "Code";
    CountDownTimer timer;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate (savedInstanceState);
        setContentView (R.layout.activity_admin_main);



        firebaseAuth = FirebaseAuth.getInstance();

        if (firebaseAuth.getCurrentUser ()==null){
            finish ();
            startActivity (new Intent (this,LoginActivity.class));
        }

        noofclass = (TextView)findViewById(R.id.admin_all_registered_class);
        noofstudents = (TextView)findViewById(R.id.admin_all_students);
        nooflectures = (TextView)findViewById(R.id.admin_all_lectures);






        logout = findViewById (R.id.logout);

















        noofclass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewclass();
            }
        });

        noofstudents.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                adminstudentadd();
            }
        });








        logout.setOnClickListener (new View.OnClickListener ( ) {
            @Override
            public void onClick(View v) {
                Logout();
            }
        });



        timer = new CountDownTimer(100, 1000) {
            @Override
            public void onTick(final long millSecondsLeftToFinish) {

            }

            @Override
            public void onFinish() {

                callclasscount();
                callstudentcount();




                timer.start();
            }
        };
        timer.start();





    }

    private void adminstudentadd() {

        startActivity(new Intent (AdminMainActivity.this, CourseList.class));

    }

    private void callstudentcount() {

        DatabaseReference student = FirebaseDatabase.getInstance().getReference("users");



        student.addListenerForSingleValueEvent(new ValueEventListener() {
            Integer count = 0;
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for(DataSnapshot ds : dataSnapshot.getChildren()){
                    UserProfile type = ds.getValue(UserProfile.class);

                    if(type.getType().compareTo("student")==0){
                        count++;

                    }
                }



                noofstudents.setText(count.toString());
            }



            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void callclasscount() {
        DatabaseReference classcount = FirebaseDatabase.getInstance().getReference("subject_code");

        classcount.addListenerForSingleValueEvent(new ValueEventListener() {

            Integer count=0;
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                noofclass.animate();
                for(DataSnapshot ds:dataSnapshot.getChildren()){
                    count++;

                }
                noofclass.setText(count.toString());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }


    private void viewclass() {

        startActivity(new Intent (AdminMainActivity.this, Admin_view_class.class));

    }



    private void Logout(){
        firebaseAuth.signOut();
        finish();
        startActivity(new Intent (AdminMainActivity.this, LoginActivity.class));
    }
}
