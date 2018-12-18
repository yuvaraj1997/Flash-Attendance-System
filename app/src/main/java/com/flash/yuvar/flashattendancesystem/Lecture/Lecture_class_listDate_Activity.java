package com.flash.yuvar.flashattendancesystem.Lecture;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.flash.yuvar.flashattendancesystem.Database.attendance_list_push_qr;
import com.flash.yuvar.flashattendancesystem.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class Lecture_class_listDate_Activity extends AppCompatActivity {

    String carriedclasscode,carriedregisteredid;

    DatabaseReference attendancelist;
    private List<String> attendancelistid;
    private List<String> dateandtime;
    ArrayAdapter<String> adapter;
    ListView listView;
    attendance_list_push_qr retrieve;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate (savedInstanceState);
        setContentView (R.layout.activity_lecture_class_list_date_);

        carriedclasscode = getIntent ().getExtras ().getString ("CarriedClassName");
        carriedregisteredid = getIntent ().getExtras ().getString ("CarriedRegisteredClassID");

        attendancelist = FirebaseDatabase.getInstance ().getReference ("student_registered_class").child(carriedregisteredid).child("attendance_list");

        retrieve = new attendance_list_push_qr ();
        listView = (ListView) findViewById (R.id.list_view_date);

        attendancelistid = new ArrayList<> ();
        dateandtime = new ArrayList<> ();

        adapter = new ArrayAdapter<String> (this,R.layout.subject_info,R.id.subname,dateandtime);

        attendancelist.addValueEventListener (new ValueEventListener ( ) {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot ds: dataSnapshot.getChildren ()){


                    retrieve = ds.getValue (attendance_list_push_qr.class);
                    attendancelistid.add(retrieve.getAttendance_list_id ().toString ());
                    dateandtime.add (retrieve.getDateandtime ().toString ());

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

                String attendees_id = attendancelistid.get (position);


                Intent i = new Intent (getApplicationContext (),Lecture_Class_listAttendees_Activity.class);
                i.putExtra ("CarriedAttendeessID",attendees_id);
                i.putExtra ("CarriedRegisteredClassID",carriedregisteredid);
                startActivity (i);


            }
        });





    }
}
