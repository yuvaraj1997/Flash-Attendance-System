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

import com.flash.yuvar.flashattendancesystem.R;
import com.flash.yuvar.flashattendancesystem.UserProfile;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class AdminRegisterStudent extends AppCompatActivity {

    EditText name,id,email,password,phone,year,sem;
    Spinner coursespinner;
    RadioGroup gender;

    RadioButton radioButton;

    Button submit;
    private ProgressBar loading;
    String course;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_register_student);

        name = (EditText)findViewById(R.id.admin_register_studentname);
        id = (EditText)findViewById(R.id.admin_register_studentid);
        email = (EditText)findViewById(R.id.admin_register_studentemailaddress);
        password = (EditText)findViewById(R.id.admin_register_studentpassword);
        phone = (EditText)findViewById(R.id.admin_register_studentphonenumber);
        year = (EditText)findViewById(R.id.admin_register_studentyear);
        sem = (EditText)findViewById(R.id.admin_register_studentsem);

        coursespinner = (Spinner)findViewById(R.id.admin_register_spinner_studentcourse);

        gender = (RadioGroup)findViewById(R.id.genderradio);

        submit = (Button)findViewById(R.id.button_admin_register_student_submit);

        loading = findViewById(R.id.admin_student_register_loading);


        password.setText("test1234");
        password.setEnabled(false);



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
                    final ArrayAdapter<String> areasAdapter = new ArrayAdapter<String>(AdminRegisterStudent.this, android.R.layout.simple_spinner_item, course);
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


        final FirebaseAuth mauth = FirebaseAuth.getInstance();




        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loading.setVisibility (View.VISIBLE);
                final String studentname = name.getText().toString().trim();
                final String studentid = id.getText().toString().trim();
                final String studentemail = email.getText().toString().trim();
                String studentpassword = password.getText().toString().trim();
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

                Toast.makeText(AdminRegisterStudent.this,
                        course, Toast.LENGTH_SHORT).show();


                mauth.createUserWithEmailAndPassword(studentemail,studentpassword).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                    @Override
                    public void onSuccess(AuthResult authResult) {

                        String userid = mauth.getUid();

                        DatabaseReference push = FirebaseDatabase.getInstance().getReference("users").child(userid);

                        UserProfile student = new UserProfile(studentid,course,studentname,studentphone,studentemail,radioButton.getText().toString(),studentsem,"student",studentyear);

                        push.setValue(student);

                        mauth.signOut();
                        Toast.makeText(AdminRegisterStudent.this,mauth.getUid().toString(),Toast.LENGTH_LONG).show();
                        
                    }
                });







            }
        });











    }

    private void setSpinnerValue(String toString) {
        this.course = toString;

    }
}
