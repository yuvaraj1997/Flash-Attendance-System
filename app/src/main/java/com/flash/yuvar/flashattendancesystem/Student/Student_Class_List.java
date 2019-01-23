package com.flash.yuvar.flashattendancesystem.Student;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.flash.yuvar.flashattendancesystem.Database.Subject_code;
import com.flash.yuvar.flashattendancesystem.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class Student_Class_List extends AppCompatActivity {

    private List<String> registeredlist;
    ArrayAdapter<String> adapter;
    ListView listView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student__class__list);


        FirebaseUser user = FirebaseAuth.getInstance ().getCurrentUser ();
        String userid = user.getUid ();

        listView = (ListView) findViewById (R.id.student_class_listview);

        registeredlist = new ArrayList<>();
        adapter = new ArrayAdapter<String> (this,R.layout.subject_info,R.id.subname,registeredlist);

        DatabaseReference registeredclass = FirebaseDatabase.getInstance ().getReference ("users").child(userid).child("subjects");

        Query query = registeredclass.orderByChild("subject_code");

        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                adapter.clear ();
                if(dataSnapshot.exists()){
                    for(DataSnapshot ds : dataSnapshot.getChildren()){

                        Subject_code subject = ds.getValue(Subject_code.class);
                        registeredlist.add(subject.getSubject_code().toString());
                    }
                }
                listView.setAdapter (adapter);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        listView.setOnItemClickListener (new AdapterView.OnItemClickListener ( ) {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                String classcode = registeredlist.get (position);


                Intent i = new Intent (getApplicationContext (), Student_Class_Detail.class);

                i.putExtra ("CarriedClassName",classcode);

                startActivity (i);


            }
        });




    }

}
