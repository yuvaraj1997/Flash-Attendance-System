package com.flash.yuvar.flashattendancesystem.Admin.LectureSide;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.flash.yuvar.flashattendancesystem.Admin.StudentSide.Admin_add_course;
import com.flash.yuvar.flashattendancesystem.Database.admin_profile_detail;
import com.flash.yuvar.flashattendancesystem.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class LectureList extends AppCompatActivity {


    private List<String> lecture;
    ArrayAdapter<String> adapter;
    admin_profile_detail retrieve;
    ListView list;

    FloatingActionButton add;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lecture_list);

        list = (ListView)findViewById(R.id.admin_lecture_list);

        retrieve = new admin_profile_detail();

        lecture = new ArrayList<>();

        adapter = new ArrayAdapter<String> (this,R.layout.subject_info,R.id.subname,lecture);

        add = (FloatingActionButton)findViewById(R.id.button_admin_floating_add_lecture);


        DatabaseReference lectureretrieve = FirebaseDatabase.getInstance ().getReference ("users");
        lectureretrieve.orderByChild("type").equalTo("lecture").addValueEventListener (new ValueEventListener( ) {

            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                adapter.clear ();
                lecture.clear();


                for(DataSnapshot ds: dataSnapshot.getChildren ()){


                    retrieve = ds.getValue (admin_profile_detail.class);
                    lecture.add(retrieve.getName());


                }

                adapter.notifyDataSetChanged ();
                list.setAdapter (adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = new Intent (getApplicationContext (), LectureRegister.class);

                startActivity (i);

            }
        });


        list.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
           @Override
           public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
               String lecturename = lecture.get (position);


               Intent i = new Intent (getApplicationContext (), lecture_detail_view.class);
               i.putExtra ("lecturename",lecturename);


               startActivity (i);
               return false;
           }
       });


    }
}
