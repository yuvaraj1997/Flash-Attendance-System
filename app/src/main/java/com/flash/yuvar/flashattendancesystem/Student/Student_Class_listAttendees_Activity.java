package com.flash.yuvar.flashattendancesystem.Student;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
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
import java.util.List;

public class Student_Class_listAttendees_Activity extends AppCompatActivity {

    String carriedregisteredid,CarriedAttendeessID;

    private List<String> name;

    ArrayAdapter<String> adapter;
    ListView listView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student__class_list_attendees_);

        carriedregisteredid = getIntent ().getExtras ().getString ("CarriedRegisteredClassID");
        CarriedAttendeessID = getIntent ().getExtras ().getString ("CarriedAttendeessID");


        listView = (ListView) findViewById (R.id.student_attendees_listview_studentlist);


        name = new ArrayList<>();



        adapter = new ArrayAdapter<String> (this,R.layout.subject_info,R.id.subname,name);

        DatabaseReference getid = FirebaseDatabase.getInstance().getReference("student_registered_class").child(carriedregisteredid).child("attendance_list")
                .child(CarriedAttendeessID).child("attendees");

        getid.orderByChild("name").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                adapter.clear ();
                name.clear();
                for(DataSnapshot ds : dataSnapshot.getChildren()){

                    student_registered_list list = ds.getValue(student_registered_list.class);





                    name.add(list.getName());
                }
                adapter.notifyDataSetChanged ();
                listView.setAdapter (adapter);


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        //Toast.makeText(this,carriedregisteredid + CarriedAttendeessID , Toast.LENGTH_LONG).show();
    }
}
