package com.flash.yuvar.flashattendancesystem.Lecture;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import com.flash.yuvar.flashattendancesystem.Database.lecture_profile_detail;
import com.flash.yuvar.flashattendancesystem.Database.student_registered_list;
import com.flash.yuvar.flashattendancesystem.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class Lecture_add_reason extends AppCompatActivity {

    String carriedAttendeeID,carriedregisteredid;


    private EditText reason_detail;
    private Button add_reason;
    private ProgressBar loading;
    private Spinner spinner;
    private String studentname,uid;
    private String lecturepass;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lecture_add_reason);

        carriedAttendeeID = getIntent ().getExtras ().getString ("attendeesid");
        carriedregisteredid = getIntent ().getExtras ().getString ("registrationid");

        reason_detail = (EditText)findViewById(R.id.reason_detail);
        add_reason = (Button)findViewById(R.id.add_reason);
        loading = (ProgressBar)findViewById(R.id.loading_reason);
        spinner = (Spinner)findViewById(R.id.spinner_student_exceptions);

        DatabaseReference studentlist = FirebaseDatabase.getInstance().getReference("student_registered_class").child(carriedregisteredid).child("student_list");


        studentlist.addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                final List<String> names = new ArrayList<String>();
                for(DataSnapshot ds: dataSnapshot.getChildren()){
                    String Name = ds.child("name").getValue(String.class);
                    names.add(Name);

                }

                Collections.sort(names);
                final ArrayAdapter<String> areasAdapter = new ArrayAdapter<String>(Lecture_add_reason.this, android.R.layout.simple_spinner_item, names);
                areasAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinner.setAdapter(areasAdapter);

                spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        setSpinnerValue(names.get(position).toString());

                        Toast.makeText(Lecture_add_reason.this,names.get(position).toString(),Toast.LENGTH_LONG).show();

                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });




        add_reason.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loading.setVisibility(View.VISIBLE);
                addReason();
            }
        });





    }

    private void addReason() {
        DatabaseReference getuid = FirebaseDatabase.getInstance().getReference("users");



        getuid.orderByChild("name").equalTo(studentname).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){

                    String completevalue = dataSnapshot.getValue().toString();

                    uid = completevalue.substring(1,29);



                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });



        if(!TextUtils.isEmpty (reason_detail.getText().toString())){

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


            AlertDialog.Builder builder = new AlertDialog.Builder(Lecture_add_reason.this,R.style.AlertDialogStyle);
            builder.setTitle("Password");

            // Set up the input
            final EditText input = new EditText(Lecture_add_reason.this);
            // Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
            input.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_VARIATION_PASSWORD);
            builder.setView(input);

            // Set up the buttons
            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    String m_Text = input.getText().toString();



                    if(m_Text.compareTo(lecturepass)==0){




                        String enddate = new SimpleDateFormat("EEE, d MMM yyyy HH:mm:ss ", Locale.getDefault()).format(new Date());
                        DatabaseReference addreason = FirebaseDatabase.getInstance().getReference("student_registered_class").child(carriedregisteredid).child("attendance_list")
                                .child(carriedAttendeeID).child("attendees").push();

                        student_registered_list add = new student_registered_list(uid,studentname,enddate,reason_detail.getText().toString());
                        addreason.setValue(add);

                        DatabaseReference addcount = FirebaseDatabase.getInstance().getReference("student_registered_class").child(carriedregisteredid).child("student_list");

                        addcount.orderByChild("name").equalTo(studentname).addListenerForSingleValueEvent(new ValueEventListener() {
                            Integer count ;
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                if(dataSnapshot.exists()){
                                    for(DataSnapshot ds: dataSnapshot.getChildren()){
                                        student_registered_list list = ds.getValue(student_registered_list.class);

                                        this.count = list.getCount();

                                        count++;

                                        DatabaseReference update = FirebaseDatabase.getInstance().getReference("student_registered_class").child(carriedregisteredid).child("student_list")
                                                .child(ds.getKey().toString());

                                        student_registered_list push = new student_registered_list(uid,studentname,count);

                                        update.setValue(push);

                                        Toast.makeText(Lecture_add_reason.this,"Success",Toast.LENGTH_LONG).show();




                                    }
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });


                        AlertDialog.Builder builder = new AlertDialog.Builder(Lecture_add_reason.this,R.style.AlertDialogStyle);
                        builder.setTitle("Success");
                        builder.setPositiveButton("Okay", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {








                            }
                        });

                        builder.setMessage("Attendance Taken and Reason Added" );
                        AlertDialog alert1 = builder.create();
                        alert1.show();

                    }else{
                        AlertDialog.Builder builder = new AlertDialog.Builder(Lecture_add_reason.this,R.style.AlertDialogStyle);
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


            loading.setVisibility (View.GONE);


        }else{
            loading.setVisibility (View.GONE);
            Toast.makeText (this,"You should Enter Reason" ,Toast.LENGTH_LONG).show ();
        }


        //Toast.makeText(Lecture_add_reason.this,reason_detail.getText().toString(),Toast.LENGTH_LONG).show();







    }

    private void setSpinnerValue(String s) {

        this.studentname = s;
    }
}
