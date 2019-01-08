package com.flash.yuvar.flashattendancesystem.Lecture;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.flash.yuvar.flashattendancesystem.Database.Request_access;
import com.flash.yuvar.flashattendancesystem.Database.Subject_code;
import com.flash.yuvar.flashattendancesystem.Database.lecture_profile_detail;
import com.flash.yuvar.flashattendancesystem.Database.students_registered_class;
import com.flash.yuvar.flashattendancesystem.QRCode.QRCode_Generate_Activity;
import com.flash.yuvar.flashattendancesystem.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Lecture_Class_Detail extends AppCompatActivity {

    private String carriedclasscode,carriedregisteredid;

    private TextView requested,classcode,studentcount,classescount;

    private Button viewclasses,classpassword;

    private String Classcode,lecturepass,classpass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lecture__class__detail);

        carriedclasscode = getIntent ().getExtras ().getString ("CarriedClassName");
        carriedregisteredid = getIntent ().getExtras ().getString ("CarriedRegisteredClassID");

        requested = (TextView)findViewById(R.id.lecture_class_requested);
        classcode = (TextView)findViewById(R.id.lecture_class_detail_classcode);
        studentcount = (TextView)findViewById(R.id.lecture_class_detail_student_Registered);
        classescount = (TextView)findViewById(R.id.lecture_class_detail_classes);

        classpassword = (Button)findViewById(R.id.button_lecture_class_detail_view_password);
        viewclasses = (Button)findViewById(R.id.button_lecture_class_detail_view_classes);

        final DatabaseReference classdetail = FirebaseDatabase.getInstance().getReference("student_registered_class").child(carriedregisteredid);

        classdetail.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){

                    students_registered_class registered = dataSnapshot.getValue(students_registered_class.class);

                    classcode.setText(registered.getClass_name());

                    setClasscode(classcode.toString());


                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        final DatabaseReference classcount = FirebaseDatabase.getInstance().getReference("student_registered_class").child(carriedregisteredid).child("attendance_list");


        classcount.addListenerForSingleValueEvent(new ValueEventListener() {
            Integer classnumber =0;
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot ds: dataSnapshot.getChildren()){

                    classnumber++;



                }
                classescount.setText(classnumber.toString());


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        final DatabaseReference students = FirebaseDatabase.getInstance().getReference("student_registered_class").child(carriedregisteredid).child("student_list");

        students.addListenerForSingleValueEvent(new ValueEventListener() {
            Integer studentnumber =0;
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot ds:dataSnapshot.getChildren()){

                    studentnumber++;

                }

                studentcount.setText(studentnumber.toString());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

       DatabaseReference requestaccess = FirebaseDatabase.getInstance().getReference ("request_access");


       requestaccess.addListenerForSingleValueEvent(new ValueEventListener() {
           Integer request =0;
           @Override
           public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
               for(DataSnapshot ds: dataSnapshot.getChildren()){

                   Request_access retrieve = ds.getValue (Request_access.class);







                   if(classcode.getText().toString().equals(retrieve.getClass_Code())){
                       request++;




                   }


               }

               requested.setText(request.toString());

           }

           @Override
           public void onCancelled(@NonNull DatabaseError databaseError) {

           }
       });



        requested.setOnClickListener (new View.OnClickListener ( ) {
            @Override
            public void onClick(View v) {
                requestlist();

            }
        });

        classescount.setOnClickListener (new View.OnClickListener ( ) {
            @Override
            public void onClick(View v) {
                Intent i = new Intent (getApplicationContext (),Lecture_class_listDate_Activity.class);

                i.putExtra ("CarriedClassName",carriedclasscode);
                i.putExtra ("CarriedRegisteredClassID",carriedregisteredid);
                startActivity (i);

            }
        });


        viewclasses.setOnClickListener (new View.OnClickListener ( ) {
            @Override
            public void onClick(View v) {
                Intent i = new Intent (getApplicationContext (),QRCode_Generate_Activity.class);

                i.putExtra ("CarriedRegisteredClassID",carriedregisteredid);
                startActivity (i);

            }
        });


        studentcount.setOnClickListener (new View.OnClickListener ( ) {
            @Override
            public void onClick(View v) {
                DatabaseReference retrieve = FirebaseDatabase.getInstance().getReference("student_registered_class").child(carriedregisteredid)
                        .child("student_list");

                Intent i = new Intent (getApplicationContext (),Lecture_Student_List.class);

                i.putExtra ("CarriedRegisteredClassID",carriedregisteredid);
                startActivity (i);





            }
        });

        classpassword.setOnClickListener (new View.OnClickListener ( ) {
            @Override
            public void onClick(View v) {

                FirebaseUser user = FirebaseAuth.getInstance ().getCurrentUser ();
                String userid = user.getUid ();





                DatabaseReference retrievelecturepass = FirebaseDatabase.getInstance().getReference("users").child(userid);

                retrievelecturepass.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                        lecture_profile_detail lecture = dataSnapshot.getValue(lecture_profile_detail.class);

                        lecturepass = lecture.getPass();


                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

                DatabaseReference retrievesubjectpass = FirebaseDatabase.getInstance().getReference("subject_code");

                retrievesubjectpass.orderByChild("subject_code").equalTo(carriedclasscode).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if(dataSnapshot.exists()){
                            for(DataSnapshot ds : dataSnapshot.getChildren()){
                                Subject_code subjectCode = ds.getValue(Subject_code.class);
                                Integer pass = subjectCode.getPassword();

                                classpass = pass.toString();

                            }
                        }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });



                AlertDialog.Builder builder = new AlertDialog.Builder(Lecture_Class_Detail.this);
                builder.setTitle("Password");

                // Set up the input
                final EditText input = new EditText(Lecture_Class_Detail.this);
                // Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
                input.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                builder.setView(input);

                // Set up the buttons
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String m_Text = input.getText().toString();



                        if(m_Text.compareTo(lecturepass)==0){


                            AlertDialog.Builder builder = new AlertDialog.Builder(Lecture_Class_Detail.this);
                            builder.setTitle("Success");
                            builder.setPositiveButton("Okay", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {


                                }
                            });

                            builder.setMessage("Class Password = " + classpass);
                            AlertDialog alert1 = builder.create();
                            alert1.show();

                        }else{
                            AlertDialog.Builder builder = new AlertDialog.Builder(Lecture_Class_Detail.this);
                            builder.setTitle("Failed");
                            builder.setPositiveButton("End", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.cancel();



                                }
                            });
                            builder.setMessage("Failed");
                            AlertDialog alert1 = builder.create();
                            alert1.show();

                        }


                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

                builder.show();


            }
        });


    }

    private void setClasscode(String s) {
        this.Classcode = s;
    }

    private void requestlist() {
        Intent i = new Intent (getApplicationContext (),Lecture_request_acceptance.class);
        i.putExtra ("CarriedClassName",carriedclasscode);
        startActivity (i);
    }
}
