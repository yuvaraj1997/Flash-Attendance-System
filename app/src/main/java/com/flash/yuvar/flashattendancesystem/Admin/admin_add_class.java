package com.flash.yuvar.flashattendancesystem.Admin;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.firebase.client.Firebase;
import com.flash.yuvar.flashattendancesystem.Database.Subject_code;
import com.flash.yuvar.flashattendancesystem.LoginActivity;
import com.flash.yuvar.flashattendancesystem.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class admin_add_class extends AppCompatActivity {

    private EditText subject_code;
    private Button add_class;
    private FirebaseAuth firebaseAuth;
    private ProgressBar loading;


    DatabaseReference databaseReference;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate (savedInstanceState);
        setContentView (R.layout.activity_admin_add_class);

        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance ( );

        if (firebaseAuth.getCurrentUser ()==null){
            finish ();
            startActivity (new Intent (this,LoginActivity.class));
        }


        databaseReference = FirebaseDatabase.getInstance ().getReference ("subject_code");


        subject_code = (EditText) findViewById (R.id.subject_code);
        add_class = (Button) findViewById (R.id.add);
        loading = findViewById (R.id.loading);






        add_class.setOnClickListener (new View.OnClickListener ( ) {
            @Override
            public void onClick(View v) {
                loading.setVisibility (View.VISIBLE);
                addclass();

            }

        });













    }

    private void addclass() {
        String subjectcode = subject_code.getText ().toString ().trim ();

        if(!TextUtils.isEmpty (subjectcode)){
            loading.setVisibility (View.GONE);
            String id = databaseReference.push ().getKey ();

            Subject_code subject_code = new Subject_code (id,subjectcode);
            databaseReference.child(id).setValue (subject_code);

            Toast.makeText (this,"Subject Added", Toast.LENGTH_LONG).show ();

        }else{
            loading.setVisibility (View.GONE);
            Toast.makeText (this,"You should Enter Subject Code" ,Toast.LENGTH_LONG).show ();
        }


    }
}
