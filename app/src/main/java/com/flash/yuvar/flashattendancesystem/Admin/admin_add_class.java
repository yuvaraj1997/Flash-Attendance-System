package com.flash.yuvar.flashattendancesystem.Admin;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import com.flash.yuvar.flashattendancesystem.Database.Subject_code;
import com.flash.yuvar.flashattendancesystem.Database.students_registered_class;
import com.flash.yuvar.flashattendancesystem.LoginActivity;
import com.flash.yuvar.flashattendancesystem.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class admin_add_class extends AppCompatActivity {

    private EditText subject_code;
    private Button add_class;
    private FirebaseAuth firebaseAuth;
    private ProgressBar loading;
    private Spinner spinner;
    private String lecturename;


    DatabaseReference databaseReference;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate (savedInstanceState);
        setContentView (R.layout.activity_admin_add_class);

        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance ( );

        if (firebaseAuth.getCurrentUser ()==null){
            finish ();
            startActivity (new Intent(this,LoginActivity.class));
        }


        databaseReference = FirebaseDatabase.getInstance ().getReference ("subject_code");


        subject_code = (EditText) findViewById (R.id.subject_code);
        add_class = (Button) findViewById (R.id.add);
        loading = findViewById (R.id.loading);
        spinner = (Spinner) findViewById(R.id.spinner);

        DatabaseReference fDatabaseRoot = FirebaseDatabase.getInstance().getReference("users");

        String type = "lecture";

        fDatabaseRoot.orderByChild("type").equalTo(type).addListenerForSingleValueEvent(new ValueEventListener() {


            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                final List<String> names = new ArrayList<String>();
                if(dataSnapshot.exists()){

                    for(DataSnapshot ds: dataSnapshot.getChildren()){
                        String Name = ds.child("name").getValue(String.class);
                        names.add(Name);

                    }




                    Collections.sort(names);
                    final ArrayAdapter<String> areasAdapter = new ArrayAdapter<String>(admin_add_class.this, android.R.layout.simple_spinner_item, names);
                    areasAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinner.setAdapter(areasAdapter);

                    spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                            setSpinnerValue(names.get(position).toString());

                            Toast.makeText(admin_add_class.this,names.get(position).toString(),Toast.LENGTH_LONG).show();

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












        add_class.setOnClickListener (new View.OnClickListener ( ) {
            @Override
            public void onClick(View v) {
                loading.setVisibility (View.VISIBLE);
                addclass();

            }

        });













    }

    private void setSpinnerValue(String s) {
        this.lecturename = s.toString().trim();


    }

    private void addclass() {
        String subjectcode = subject_code.getText ().toString ().trim ();



        if(!TextUtils.isEmpty (subjectcode)){
            loading.setVisibility (View.GONE);
            String id = databaseReference.push ().getKey ();
            final String random = randomString(4);

            int password = Integer.parseInt(random);

            Subject_code subject_code = new Subject_code (id,subjectcode,lecturename,password);
            databaseReference.child(id).setValue (subject_code);

            DatabaseReference databaseReference = FirebaseDatabase.getInstance ().getReference ("student_registered_class");
            final String registered_class_id = databaseReference.push ().getKey ();

            students_registered_class registered_class = new students_registered_class (subjectcode,id,registered_class_id,lecturename);



            databaseReference.child(registered_class_id).setValue (registered_class);




            Toast.makeText (this,"Subject Added", Toast.LENGTH_LONG).show ();

        }else{
            loading.setVisibility (View.GONE);
            Toast.makeText (this,"You should Enter Subject Code" ,Toast.LENGTH_LONG).show ();
        }


    }


    private void pushregisteredclass(String classcode, String classid, String registeredclassID,String lecture_name) {



        students_registered_class registered_class = new students_registered_class (classcode,classid,registeredclassID,lecture_name);



        databaseReference.child(registeredclassID).setValue (registered_class);




    }


    public static final String DATA = "0123456789";
    public static Random RANDOM = new Random();

    public static String randomString(int len) {
        StringBuilder sb = new StringBuilder(len);

        for (int i = 0; i < len; i++) {
            sb.append(DATA.charAt(RANDOM.nextInt(DATA.length())));
        }

        return sb.toString();
    }
}
