package com.flash.yuvar.flashattendancesystem.Lecture;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.flash.yuvar.flashattendancesystem.Database.student_registered_list;
import com.flash.yuvar.flashattendancesystem.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Lecture_Class_listAttendees_Activity extends AppCompatActivity {
    DatabaseReference studentlist;
    String carriedAttendeeID,carriedregisteredid;
    private List<String> uid;
    private List<String> name;
    ArrayAdapter<String> adapter;
    ListView listView;
    student_registered_list retrieve;

    private FloatingActionButton add;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate (savedInstanceState);
        setContentView (R.layout.activity_lecture__class_list_attendees_);

        carriedAttendeeID = getIntent ().getExtras ().getString ("CarriedAttendeessID");
        carriedregisteredid = getIntent ().getExtras ().getString ("CarriedRegisteredClassID");

        studentlist = FirebaseDatabase.getInstance ().getReference ("student_registered_class").child(carriedregisteredid).child("attendance_list")
        .child (carriedAttendeeID).child("attendees");

        retrieve = new student_registered_list ();
        listView = (ListView) findViewById (R.id.list_view_attendees);
        add = (FloatingActionButton)findViewById(R.id.floating_exception_button) ;

        uid = new ArrayList<> ();
        name = new ArrayList<> ();

        adapter = new ArrayAdapter<String> (this,R.layout.subject_info,R.id.subname,name);

        studentlist.addValueEventListener (new ValueEventListener ( ) {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                adapter.clear ();
                for(DataSnapshot ds: dataSnapshot.getChildren ()){


                    retrieve = ds.getValue (student_registered_list.class);
                    uid.add(retrieve.getuID ().toString ());
                    name.add (retrieve.getName ().toString ());

                }
                Collections.sort(name);

                listView.setAdapter (adapter);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(Lecture_Class_listAttendees_Activity.this, Lecture_add_reason.class);


                intent.putExtra("registrationid", carriedregisteredid);
                intent.putExtra("attendeesid", carriedAttendeeID);

                startActivity(intent);




            }
        });





    }
}
