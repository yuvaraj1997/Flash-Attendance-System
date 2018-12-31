package com.flash.yuvar.flashattendancesystem.Student;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.flash.yuvar.flashattendancesystem.Database.Request_access;
import com.flash.yuvar.flashattendancesystem.Database.retrieve_subject_code;
import com.flash.yuvar.flashattendancesystem.LoginActivity;
import com.flash.yuvar.flashattendancesystem.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

public class Student_join_class extends AppCompatActivity {

    ListView listView;

    FirebaseDatabase database;
    DatabaseReference ref;

    ArrayList<String> list;
    ArrayList<String> list1;
    ArrayAdapter<String> adapter;
    retrieve_subject_code retrieve;

    DatabaseReference databaseReference;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate (savedInstanceState);
        setContentView (R.layout.activity_student_join_class);

        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance ( );

        if (firebaseAuth.getCurrentUser ()==null){
            finish ();
            startActivity (new Intent (this,LoginActivity.class));
        }

        retrieve = new retrieve_subject_code ();
        listView = (ListView) findViewById (R.id.listView);

        database = FirebaseDatabase.getInstance ();
        ref = database.getReference ("subject_code");
        list = new ArrayList<> ();
        list1 = new ArrayList<> ();
        adapter = new ArrayAdapter<String> (this,R.layout.subject_info,R.id.subname,list);

        ref.addValueEventListener (new ValueEventListener ( ) {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                adapter.clear ();

                for(DataSnapshot ds: dataSnapshot.getChildren ()){


                    retrieve = ds.getValue (retrieve_subject_code.class);
                    list.add(retrieve.getSubject_code ().toString ());
                    list1.add (retrieve.getSubject_id ().toString ());
                }
                Collections.sort(list);


                listView.setAdapter (adapter);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });



        listView.setOnItemClickListener (new AdapterView.OnItemClickListener ( ) {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                databaseReference = FirebaseDatabase.getInstance ().getReference ("request_access");
                FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
                String request_id = databaseReference.push ().getKey ();
                String RegisteredUserID = currentUser.getUid();

                String ClassCode = list.get (position);
                String classID = list1.get (position);


                pushrequestaccess (ClassCode,RegisteredUserID,request_id,classID);





                Toast.makeText (Student_join_class.this, "Request Successfull",Toast.LENGTH_LONG ).show ();
            }
        });



    }



    private void pushrequestaccess(String ClassCode,String RegisteredUserID,String request_id,String classID) {

        Request_access request_access = new Request_access (ClassCode,RegisteredUserID,request_id,classID);
        databaseReference.child(request_id).setValue (request_access);

    }
}

