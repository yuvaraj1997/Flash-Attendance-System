package com.flash.yuvar.flashattendancesystem.Admin.StudentSide;

import android.content.DialogInterface;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import com.flash.yuvar.flashattendancesystem.Database.admin_profile_detail;
import com.flash.yuvar.flashattendancesystem.Database.deleteduser;
import com.flash.yuvar.flashattendancesystem.R;
import com.flash.yuvar.flashattendancesystem.UserProfile;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Admin_student_details extends AppCompatActivity {

    String studentnameold;

    EditText name,id,email,phone,year,sem;
    Spinner coursespinner;
    RadioGroup gender;

    RadioButton radioButton;

    Button update,delete;
    private ProgressBar loading;
    RadioButton male,female;
    String course;
    String adminpass;

    CountDownTimer timer;
    String firstcomplete;
    String secondcomplete;
    String thirdcomplete;
    String forthcomplete;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_student_details);

        studentnameold = getIntent ().getExtras ().getString ("studentname");

        name = (EditText)findViewById(R.id.admin_retrieve_studentname);
        id = (EditText)findViewById(R.id.admin_retrieve_studentid);
        email = (EditText)findViewById(R.id.admin_retrieve_studentemailaddress);
        phone = (EditText)findViewById(R.id.admin_retrieve_studentphonenumber);
        year = (EditText)findViewById(R.id.admin_retrieve_studentyear);
        sem = (EditText)findViewById(R.id.admin_retrieve_studentsem);

        loading = (ProgressBar)findViewById(R.id.indeterminateBar) ;

        loading.setVisibility(View.INVISIBLE);


        male = (RadioButton)findViewById(R.id.admin_retrieve_studentgendermale);
        female = (RadioButton)findViewById(R.id.admin_retrieve_studentgenderFemale);

        coursespinner = (Spinner)findViewById(R.id.admin_retrieve_spinner_studentcourse);

        gender = (RadioGroup)findViewById(R.id.genderradioretrieve);

        update = (Button)findViewById(R.id.button_admin_update_student_submit);
        delete = (Button)findViewById(R.id.button_admin_delete_student_submit);



        DatabaseReference getdetails = FirebaseDatabase.getInstance().getReference("users");


        getdetails.orderByChild("name").equalTo(studentnameold).addListenerForSingleValueEvent(new ValueEventListener() {

            String coursename;
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot ds : dataSnapshot.getChildren()){
                    UserProfile student = ds.getValue(UserProfile.class);

                    name.setText(student.getName());
                    id.setText(student.getUserId());
                    email.setText(student.getEmail());
                    phone.setText(student.getContact_number());
                    year.setText(student.getYear());
                    sem.setText(student.getSem());

                    coursename = student.getCourse();

                    email.setEnabled(false);

                    if(student.getGender().equals("Male")){
                        Toast.makeText(Admin_student_details.this,"Male",Toast.LENGTH_LONG).show();
                        male.setChecked(true);

                    }else{
                        female.setChecked(true);

                    }




                    DatabaseReference fDatabaseRoot = FirebaseDatabase.getInstance().getReference("course");



                    fDatabaseRoot.addListenerForSingleValueEvent(new ValueEventListener() {


                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                            final List<String> course = new ArrayList<String>();
                            if(dataSnapshot.exists()){

                                for(DataSnapshot ds: dataSnapshot.getChildren()){
                                    String Name = ds.child("course").getValue(String.class);
                                    course.add(Name);

                                }
                                Collections.sort(course);

                                for(int i =0;i<course.size();i++){
                                    if(course.get(i).compareTo(coursename)==0){



                                        String firstvalue = course.get(0);

                                        Toast.makeText(Admin_student_details.this,firstvalue,Toast.LENGTH_LONG).show();

                                        course.set(0,coursename);

                                       course.set(i,firstvalue);

                                    }
                                }

                                final ArrayAdapter<String> areasAdapter = new ArrayAdapter<String>(Admin_student_details.this, android.R.layout.simple_spinner_item, course);
                                areasAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);


                                coursespinner.setAdapter(areasAdapter);



                                coursespinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                    @Override
                                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                        setSpinnerValue(course.get(position).toString());

                                        //Toast.makeText(AdminRegisterStudent.this,course.get(position).toString(),Toast.LENGTH_LONG).show();

                                    }

                                    @Override
                                    public void onNothingSelected(AdapterView<?> parent) {

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

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String studentname = name.getText().toString().trim();
                final String studentid = id.getText().toString().trim();
                final String studentemail = email.getText().toString().trim();
                final String studentphone = phone.getText().toString().trim();
                final String studentyear = year.getText().toString().trim();
                final String studentsem = sem.getText().toString().trim();

                // get selected radio button from radioGroup
                int selectedId = gender.getCheckedRadioButtonId();

                // find the radiobutton by returned id
                radioButton = (RadioButton) findViewById(selectedId);


                if(radioButton.getText().toString().isEmpty()){

                    gender.requestFocus ();
                    loading.setVisibility (View.GONE);
                    return;

                }



                if(studentemail.isEmpty()){
                    email.setError ("Email Required");
                    email.requestFocus ();
                    loading.setVisibility (View.GONE);
                    return;
                }

                if(studentname.isEmpty()){
                    name.setError ("Name Required");
                    name.requestFocus ();
                    loading.setVisibility (View.GONE);
                    return;
                }

                if(studentid.isEmpty()){
                    id.setError ("ID Required");
                    id.requestFocus ();
                    loading.setVisibility (View.GONE);
                    return;
                }

                if(studentphone.isEmpty()){
                    phone.setError ("Phone Number Required");
                    phone.requestFocus ();
                    loading.setVisibility (View.GONE);
                    return;
                }
                if(studentyear.isEmpty()){
                    year.setError ("Programme Year Required");
                    year.requestFocus ();
                    loading.setVisibility (View.GONE);
                    return;
                }
                if(studentsem.isEmpty()){
                    sem.setError ("Trimester Required");
                    sem.requestFocus ();
                    loading.setVisibility (View.GONE);
                    return;
                }

                DatabaseReference update = FirebaseDatabase.getInstance().getReference("users");


                update.orderByChild("name").equalTo(studentnameold).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for(DataSnapshot ds : dataSnapshot.getChildren()){


                            DatabaseReference push = FirebaseDatabase.getInstance().getReference("users").child(ds.getKey());

                            UserProfile student = new UserProfile(studentid,course,studentname,studentphone,studentemail,radioButton.getText().toString(),studentsem,"student",studentyear);

                            push.setValue(student);

                            Toast.makeText(Admin_student_details.this,
                                    "Success", Toast.LENGTH_SHORT).show();

                                finish();







                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

                DatabaseReference getstudentregisteredid = FirebaseDatabase.getInstance().getReference("student_registered_class");

                getstudentregisteredid.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for(DataSnapshot ds : dataSnapshot.getChildren()){
                            final String id = ds.getKey();
                            DatabaseReference getid = FirebaseDatabase.getInstance().getReference("student_registered_class").child(id).child("student_list");


                            getid.orderByChild("name").equalTo(studentnameold).addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    for(DataSnapshot ds : dataSnapshot.getChildren()){
                                        DatabaseReference swap = FirebaseDatabase.getInstance().getReference("student_registered_class").child(id).child("student_list").child(ds.getKey());

                                        swap.child("name").setValue(studentname);

                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                }
                            });


                            DatabaseReference attendancelist = FirebaseDatabase.getInstance().getReference("student_registered_class").child(id).child("attendance_list");

                            attendancelist.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    for(DataSnapshot ds : dataSnapshot.getChildren()){
                                        final String attendancelistid = ds.getKey();

                                        DatabaseReference getattendancelistid = FirebaseDatabase.getInstance().getReference("student_registered_class").child(id).child("attendance_list").child(attendancelistid).child("attendees");

                                        getattendancelistid.orderByChild("name").equalTo(studentnameold).addListenerForSingleValueEvent(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                for(DataSnapshot ds : dataSnapshot.getChildren()){
                                                    DatabaseReference swap = FirebaseDatabase.getInstance().getReference("student_registered_class").child(id).child("attendance_list").child(attendancelistid).child("attendees").child(ds.getKey());
                                                    swap.child("name").setValue(studentname);

                                                }
                                            }

                                            @Override
                                            public void onCancelled(@NonNull DatabaseError databaseError) {

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

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });







            }
        });


        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {



                FirebaseUser user = FirebaseAuth.getInstance ().getCurrentUser ();
                String userid = user.getUid ();





                DatabaseReference retrieveadminpass = FirebaseDatabase.getInstance().getReference("users").child(userid);

                retrieveadminpass.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                        admin_profile_detail admin = dataSnapshot.getValue(admin_profile_detail.class);

                        adminpass = admin.getPass();


                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

                AlertDialog.Builder builder = new AlertDialog.Builder(Admin_student_details.this,R.style.AlertDialogStyle);
                builder.setTitle("Password");

                // Set up the input
                final EditText input = new EditText(Admin_student_details.this);
                // Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
                input.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_VARIATION_PASSWORD);
                builder.setView(input);

                // Set up the buttons
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String m_Text = input.getText().toString();



                        if(m_Text.compareTo(adminpass)==0){



                            DatabaseReference getid = FirebaseDatabase.getInstance().getReference("users");

                            getid.orderByChild("name").equalTo(studentnameold).addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    for(DataSnapshot ds : dataSnapshot.getChildren()){

                                        DatabaseReference adddeleteduser = FirebaseDatabase.getInstance().getReference("deleteduser").push();

                                        DatabaseReference getid = FirebaseDatabase.getInstance().getReference("users");

                                        getid.orderByChild("email").equalTo(email.getText().toString()).addListenerForSingleValueEvent(new ValueEventListener() {
                                            String uid;
                                            @Override
                                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                                                for(DataSnapshot ds : dataSnapshot.getChildren()){
                                                    uid = ds.getKey();

                                                    DatabaseReference requestaccesscheck = FirebaseDatabase.getInstance().getReference("request_access");

                                                    requestaccesscheck.orderByChild("userID").equalTo(uid).addListenerForSingleValueEvent(new ValueEventListener() {
                                                        @Override
                                                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                            for(DataSnapshot ds : dataSnapshot.getChildren()){
                                                                DatabaseReference requestaccessdelete = FirebaseDatabase.getInstance().getReference("request_access").child(ds.getKey());
                                                                requestaccessdelete.removeValue();
                                                                firstcomplete = "PASS";


                                                            }
                                                        }

                                                        @Override
                                                        public void onCancelled(@NonNull DatabaseError databaseError) {

                                                        }
                                                    });

                                                    DatabaseReference studentregisteredall = FirebaseDatabase.getInstance().getReference("student_registered_class");

                                                    studentregisteredall.addListenerForSingleValueEvent(new ValueEventListener() {
                                                        final List<String> id = new ArrayList<String>();


                                                        @Override
                                                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                            for(DataSnapshot ds : dataSnapshot.getChildren()){



                                                                final String registereddetailid = ds.getKey();


                                                                DatabaseReference studentregisteredattendancelist = FirebaseDatabase.getInstance().getReference("student_registered_class")
                                                                        .child(registereddetailid).child("attendance_list");





                                                                studentregisteredattendancelist.addListenerForSingleValueEvent(new ValueEventListener() {

                                                                    @Override
                                                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                                                                        for(DataSnapshot ds : dataSnapshot.getChildren()){
                                                                            final String generatedclassid = ds.getKey();
                                                                            id.add(generatedclassid);

                                                                            DatabaseReference studentregisteredattendees = FirebaseDatabase.getInstance().getReference("student_registered_class")
                                                                                    .child(registereddetailid).child("attendance_list").child(generatedclassid).child("attendees");



                                                                            Toast.makeText(Admin_student_details.this,generatedclassid,Toast.LENGTH_LONG).show();
                                                                            Toast.makeText(Admin_student_details.this,registereddetailid,Toast.LENGTH_LONG).show();

                                                                            studentregisteredattendees.orderByChild("uID").equalTo(uid).addValueEventListener(new ValueEventListener() {

                                                                                @Override
                                                                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                                                                                    for(DataSnapshot ds : dataSnapshot.getChildren()){
                                                                                        DatabaseReference removestudentregisteredattendees = FirebaseDatabase.getInstance().getReference("student_registered_class")
                                                                                                .child(registereddetailid).child("attendance_list").child(generatedclassid).child("attendees").child(ds.getKey());




                                                                                        removestudentregisteredattendees.removeValue();
                                                                                        secondcomplete = "PASS";

                                                                                    }



                                                                                }

                                                                                @Override
                                                                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                                                                }
                                                                            });

                                                                        }
                                                                    }

                                                                    @Override
                                                                    public void onCancelled(@NonNull DatabaseError databaseError) {

                                                                    }
                                                                });




                                                                DatabaseReference studentregisteredstudentlistlist = FirebaseDatabase.getInstance().getReference("student_registered_class")
                                                                        .child(registereddetailid).child("student_list");

                                                                studentregisteredstudentlistlist.orderByChild("uID").equalTo(uid).addListenerForSingleValueEvent(new ValueEventListener() {
                                                                    @Override
                                                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                                        for(DataSnapshot ds : dataSnapshot.getChildren()){
                                                                            DatabaseReference removetudentregisteredstudentlistlist = FirebaseDatabase.getInstance().getReference("student_registered_class")
                                                                                    .child(registereddetailid).child("student_list").child(ds.getKey());

                                                                            //Toast.makeText(Admin_student_details.this,"Success2",Toast.LENGTH_LONG).show();

                                                                            removetudentregisteredstudentlistlist.removeValue();
                                                                            thirdcomplete = "PASS";


                                                                        }
                                                                    }

                                                                    @Override
                                                                    public void onCancelled(@NonNull DatabaseError databaseError) {

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

                                            @Override
                                            public void onCancelled(@NonNull DatabaseError databaseError) {

                                            }
                                        });


                                        DatabaseReference getrequestaccess = FirebaseDatabase.getInstance().getReference("request_access");

                                        getrequestaccess.orderByChild("userID").equalTo(ds.getKey()).addListenerForSingleValueEvent(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                for(DataSnapshot ds : dataSnapshot.getChildren()){
                                                    DatabaseReference removerequestaccess = FirebaseDatabase.getInstance().getReference("request_access").child(ds.getKey());
                                                    removerequestaccess.removeValue();
                                                }
                                            }

                                            @Override
                                            public void onCancelled(@NonNull DatabaseError databaseError) {

                                            }
                                        });




                                        deleteduser delete = new deleteduser(email.getText().toString());

                                        adddeleteduser.setValue(delete);

                                        DatabaseReference remove  = FirebaseDatabase.getInstance().getReference("users").child(ds.getKey());


                                        remove.removeValue();

                                        forthcomplete = "PASS";


                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                }
                            });









                            AlertDialog.Builder builder = new AlertDialog.Builder(Admin_student_details.this,R.style.AlertDialogStyle);
                            builder.setTitle("Success");
                            builder.setPositiveButton("Okay", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    finish();


                                }
                            });

                            builder.setMessage("Successfully Deleted");
                            AlertDialog alert1 = builder.create();
                            alert1.show();







                        }else{
                            AlertDialog.Builder builder = new AlertDialog.Builder(Admin_student_details.this,R.style.AlertDialogStyle);
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









        Toast.makeText(Admin_student_details.this, studentnameold, Toast.LENGTH_SHORT).show();
    }

    private void setSpinnerValue(String toString) {
        this.course = toString;

    }
}
