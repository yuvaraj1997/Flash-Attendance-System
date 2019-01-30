package com.flash.yuvar.flashattendancesystem.Admin.StudentSide;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ExpandableListView;
import android.widget.TextView;

import com.flash.yuvar.flashattendancesystem.Adapters.ExpandableListViewAdapter;
import com.flash.yuvar.flashattendancesystem.R;
import com.flash.yuvar.flashattendancesystem.UserProfile;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class Admin_student_add extends AppCompatActivity {


    private List<String> year = new ArrayList<String>();
    private List<String> names = new ArrayList<String>();
    private List<String> sem = new ArrayList<String>();

    ExpandableListView listview;

    String coursename;

    TextView textViewcoursename;

    ArrayAdapter<String> adapter;

    FloatingActionButton add;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_student_add);

        coursename = getIntent ().getExtras ().getString ("coursename");

        textViewcoursename = (TextView)findViewById(R.id.textviewcoursename);
        listview = (ExpandableListView)findViewById(R.id.expandable_list_student);
        add = (FloatingActionButton)findViewById(R.id.button_admin_floating_add_student);
        textViewcoursename.setText(coursename);

        DatabaseReference addcategoryname = FirebaseDatabase.getInstance().getReference("users");

        addcategoryname.orderByChild("course").equalTo(coursename).addListenerForSingleValueEvent(new ValueEventListener() {

            Integer count=0;


            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                year.clear();


                for(DataSnapshot ds :dataSnapshot.getChildren()){
                    UserProfile student = ds.getValue(UserProfile.class);

                    String years = "Year " + student.getYear();
                    if(!year.contains(years)){
                        year.add(years);


                        count++;
                    }






                }
                ExpandableListViewAdapter adapter = new ExpandableListViewAdapter(Admin_student_add.this,coursename,count,year);

                listview.setAdapter(adapter);
                //Toast.makeText(Admin_student_add.this,count.toString(),Toast.LENGTH_LONG).show();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });






        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addStudent();
            }
        });













    }

    private void addStudent() {

        Intent i = new Intent (getApplicationContext (), AdminRegisterStudent.class);



        startActivity (i);
    }
}