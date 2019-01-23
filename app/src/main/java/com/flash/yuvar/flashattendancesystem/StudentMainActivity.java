package com.flash.yuvar.flashattendancesystem;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.flash.yuvar.flashattendancesystem.QRCode.ScanCode_Activity;
import com.flash.yuvar.flashattendancesystem.Student.Student_Class_List;
import com.flash.yuvar.flashattendancesystem.Student.Student_join_class;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


public class StudentMainActivity extends AppCompatActivity {

    private TextView profileName, profileId, profileCourse;
    private FirebaseAuth firebaseAuth;

    private TextView classcount1;
    private String uid;

    private Button logout, link_joinclass, btn_scan;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_main);

        logout = (Button) findViewById(R.id.logout);
        profileId = findViewById(R.id.stu_id);
        profileName = findViewById(R.id.name);
        profileCourse = findViewById(R.id.course);
        link_joinclass = findViewById(R.id.joinclass);
        btn_scan = findViewById(R.id.scanning);
        classcount1 = findViewById(R.id.student_profile_registered_class);

        firebaseAuth = FirebaseAuth.getInstance();


        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        final String userid = user.getUid();
        uid = userid;

        if (firebaseAuth.getCurrentUser() == null) {


            finish();
            startActivity(new Intent(this, LoginActivity.class));
        }

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myref = database.getReference("users");





        myref.child(userid).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                UserProfile userProfile = dataSnapshot.getValue(UserProfile.class);
                profileId.setText(userProfile.getUserId());
                profileName.setText(userProfile.getUserName());
                profileCourse.setText(userProfile.getUserCourse());


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(StudentMainActivity.this, databaseError.getCode(), Toast.LENGTH_SHORT).show();

            }
        });

        final DatabaseReference classcount = FirebaseDatabase.getInstance().getReference("users").child(userid).child("subjects");

        classcount.addListenerForSingleValueEvent(new ValueEventListener() {
            Integer count =0;
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot ds:dataSnapshot.getChildren()){
                    count++;

                }
                classcount1.setText(count.toString());

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        classcount1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                classlist();

            }
        });

        btn_scan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                scanclass();
            }
        });


        link_joinclass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                joinclass();
            }
        });


        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //DatabaseReference check = FirebaseDatabase.getInstance().getReference("users").child(userid).child("status");
                //check.removeValue();
                Logout();




            }
        });
    }

    private void classlist() {
        startActivity(new Intent(getApplicationContext(), Student_Class_List.class));
    }


    private void scanclass() {
        startActivity(new Intent(getApplicationContext(), ScanCode_Activity.class));
    }

    private void joinclass() {

        startActivity(new Intent(StudentMainActivity.this, Student_join_class.class));

    }

    private void Logout() {
        firebaseAuth.signOut();
        finish();
        startActivity(new Intent(StudentMainActivity.this, LoginActivity.class));
    }


}




