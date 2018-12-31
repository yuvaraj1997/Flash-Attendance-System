package com.flash.yuvar.flashattendancesystem.Admin;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.flash.yuvar.flashattendancesystem.Database.retrieve_subject_code;
import com.flash.yuvar.flashattendancesystem.LoginActivity;
import com.flash.yuvar.flashattendancesystem.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class Admin_view_class extends AppCompatActivity {

    ListView listView;

    FirebaseDatabase database;
    DatabaseReference ref;

    ArrayList<String> list;
    ArrayAdapter<String> adapter;
    retrieve_subject_code retrieve;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate (savedInstanceState);
        setContentView (R.layout.activity_admin_view_class);


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
        adapter = new ArrayAdapter<String> (this,R.layout.subject_info,R.id.subname,list);

        ref.addValueEventListener (new ValueEventListener ( ) {

            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                adapter.clear ();
                for(DataSnapshot ds: dataSnapshot.getChildren ()){


                    retrieve = ds.getValue (retrieve_subject_code.class);
                    list.add(retrieve.getSubject_code ().toString ());
                }

                listView.setAdapter (adapter);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
