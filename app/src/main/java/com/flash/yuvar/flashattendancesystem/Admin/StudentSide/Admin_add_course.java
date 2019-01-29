package com.flash.yuvar.flashattendancesystem.Admin.StudentSide;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.flash.yuvar.flashattendancesystem.Database.admin_course;
import com.flash.yuvar.flashattendancesystem.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Admin_add_course extends AppCompatActivity {

    Button addcourse;

    EditText coursetext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_add_course);

        addcourse = (Button)findViewById(R.id.button_add_course_push);
        coursetext = (EditText)findViewById(R.id.admin_add_course);






        addcourse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(coursetext.getText().toString().equals("")){
                    Toast.makeText(Admin_add_course.this,"Fill In Blanks",Toast.LENGTH_LONG).show();

                }
                else{

                    DatabaseReference check = FirebaseDatabase.getInstance().getReference("course");

                    check.orderByChild("course").equalTo(coursetext.getText().toString()).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            if(dataSnapshot.exists()){
                                Toast.makeText(Admin_add_course.this,"Already Exist",Toast.LENGTH_LONG).show();

                            }
                            else{
                                DatabaseReference course = FirebaseDatabase.getInstance().getReference("course").push();
                                admin_course c = new admin_course(coursetext.getText().toString());
                                course.setValue(c).addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        finish();


                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(Admin_add_course.this,"Failed",Toast.LENGTH_LONG).show();

                                    }
                                });

                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });


                }

            }
        });
    }
}
