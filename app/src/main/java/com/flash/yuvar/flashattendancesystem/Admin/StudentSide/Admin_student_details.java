package com.flash.yuvar.flashattendancesystem.Admin.StudentSide;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
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

import com.flash.yuvar.flashattendancesystem.Database.deleteduser;
import com.flash.yuvar.flashattendancesystem.R;
import com.flash.yuvar.flashattendancesystem.UserProfile;
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
    String course;

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

        coursespinner = (Spinner)findViewById(R.id.admin_retrieve_spinner_studentcourse);

        gender = (RadioGroup)findViewById(R.id.genderradioretrieve);

        update = (Button)findViewById(R.id.button_admin_update_student_submit);
        delete = (Button)findViewById(R.id.button_admin_delete_student_submit);

        loading = findViewById(R.id.admin_student_retrieve_loading);


        DatabaseReference getdetails = FirebaseDatabase.getInstance().getReference("users");


        getdetails.orderByChild("name").equalTo(studentnameold).addListenerForSingleValueEvent(new ValueEventListener() {
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

                    email.setEnabled(false);


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







            }
        });


        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatabaseReference getid = FirebaseDatabase.getInstance().getReference("users");

                getid.orderByChild("name").equalTo(studentnameold).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for(DataSnapshot ds : dataSnapshot.getChildren()){

                            DatabaseReference adddeleteduser = FirebaseDatabase.getInstance().getReference("deleteduser").push();


                            deleteduser delete = new deleteduser(email.getText().toString());

                            adddeleteduser.setValue(delete);

                            DatabaseReference remove  = FirebaseDatabase.getInstance().getReference("users").child(ds.getKey());

                            remove.removeValue();


                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }
        });









        Toast.makeText(Admin_student_details.this, studentnameold, Toast.LENGTH_SHORT).show();
    }

    private void setSpinnerValue(String toString) {
        this.course = toString;

    }
}
