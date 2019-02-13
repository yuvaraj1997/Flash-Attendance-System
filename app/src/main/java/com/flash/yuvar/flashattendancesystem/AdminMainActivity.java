package com.flash.yuvar.flashattendancesystem;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.flash.yuvar.flashattendancesystem.Admin.ClassSide.Admin_view_class;
import com.flash.yuvar.flashattendancesystem.Admin.DeletedList;
import com.flash.yuvar.flashattendancesystem.Admin.LectureSide.LectureList;
import com.flash.yuvar.flashattendancesystem.Admin.StudentSide.CourseList;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


public class AdminMainActivity extends AppCompatActivity {
    private TextView noofclass,noofstudents,nooflectures,noofdeleted;
    private Button logout;
    private FirebaseAuth firebaseAuth;

    public static final String TAG = "Code";
    CountDownTimer timer;

    private SwipeRefreshLayout swipeRefreshLayout;

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
        noofdeleted = (TextView)findViewById(R.id.admin_all_deleted);
        swipeRefreshLayout=(SwipeRefreshLayout)findViewById(R.id.adminswipe);





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

        noofdeleted.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteduser();
            }
        });

        nooflectures.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                lecturelist();
            }
        });








        logout.setOnClickListener (new View.OnClickListener ( ) {
            @Override
            public void onClick(View v) {
                Logout();
            }
        });


        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        swipeRefreshLayout.setRefreshing(false);
                        callclasscount();
                        callstudentcount();
                        calldeletedcount();
                        calllecturecount();




                    }
                },4000);
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
                calldeletedcount();
                calllecturecount();




                timer.start();
            }
        };
        timer.start();





    }

    private void calllecturecount() {

        DatabaseReference lecture = FirebaseDatabase.getInstance().getReference("users");



        lecture.orderByChild("type").equalTo("lecture").addListenerForSingleValueEvent(new ValueEventListener() {
            Integer count = 0;
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for(DataSnapshot ds : dataSnapshot.getChildren()){


                    count++;

                }


                nooflectures.setText(count.toString());
            }



            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void lecturelist() {
        startActivity(new Intent (AdminMainActivity.this, LectureList.class));
    }

    private void deleteduser() {
        startActivity(new Intent (AdminMainActivity.this, DeletedList.class));

    }

    private void calldeletedcount() {

        DatabaseReference student = FirebaseDatabase.getInstance().getReference("deleteduser");



        student.addListenerForSingleValueEvent(new ValueEventListener() {
            Integer count = 0;
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for(DataSnapshot ds : dataSnapshot.getChildren()){


                    count++;

                }



                noofdeleted.setText(count.toString());
            }



            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
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
