package com.flash.yuvar.flashattendancesystem;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.flash.yuvar.flashattendancesystem.Database.Subject_code;
import com.flash.yuvar.flashattendancesystem.Database.lecture_profile_detail;
import com.flash.yuvar.flashattendancesystem.Lecture.Lecture_Attendance_Table;
import com.flash.yuvar.flashattendancesystem.Lecture.Lecture_Class_listcarry_Activity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class LectureMainActivity extends AppCompatActivity {

    private TextView lecture_id,lecture_pass,numberofclass,lecture_name;
    private Button logout,attendancetable;
    private FirebaseAuth firebaseAuth;

    private String lecturename;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate (savedInstanceState);
        setContentView (R.layout.activity_lecture_main);

        numberofclass = (TextView)findViewById(R.id.lecture_profile_registered_class);
        lecture_name = (TextView)findViewById(R.id.lecture_name);



        logout = findViewById (R.id.logout);
        attendancetable = findViewById (R.id.attendancetable);


        firebaseAuth = FirebaseAuth.getInstance();

        if (firebaseAuth.getCurrentUser ()==null){
            finish ();
            startActivity (new Intent (this,LoginActivity.class));
        }

        FirebaseDatabase database = FirebaseDatabase.getInstance ();
        DatabaseReference myref = database.getReference ("users");

        FirebaseUser user = FirebaseAuth.getInstance ().getCurrentUser ();
        String userid = user.getUid ();

        myref.child (userid).addListenerForSingleValueEvent (new ValueEventListener( ) {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                lecture_profile_detail lecture = dataSnapshot.getValue (lecture_profile_detail.class);
                lecture_name.setText(lecture.getName());
                lecturename=lecture.getName();


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText (LectureMainActivity.this,databaseError.getCode (),Toast.LENGTH_SHORT ).show ();

            }
        });



        DatabaseReference noofclass = FirebaseDatabase.getInstance().getReference("subject_code");

        noofclass.addListenerForSingleValueEvent(new ValueEventListener() {
            Integer count = 0;
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    for(DataSnapshot ds:dataSnapshot.getChildren()){

                        Subject_code code = ds.getValue(Subject_code.class);

                        if(code.getLecture_name().compareTo(lecturename)==0){
                            count++;
                        }





                    }
                }
                numberofclass.setText(count.toString());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        numberofclass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewClass();
            }
        });









        logout.setOnClickListener (new View.OnClickListener ( ) {
            @Override
            public void onClick(View v) {
                Logout();
            }
        });

        attendancetable.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                attendancetableopen();
            }
        });






    }

    private void attendancetableopen() {
        Intent  i = new Intent (getApplicationContext (), Lecture_Attendance_Table.class);
        startActivity (i);
    }

    private void viewClass() {
        Intent  i = new Intent (getApplicationContext (),Lecture_Class_listcarry_Activity.class);
        startActivity (i);
    }





    private void Logout() {
        firebaseAuth.signOut();
        finish();
        startActivity(new Intent (LectureMainActivity.this, LoginActivity.class));
    }
}
