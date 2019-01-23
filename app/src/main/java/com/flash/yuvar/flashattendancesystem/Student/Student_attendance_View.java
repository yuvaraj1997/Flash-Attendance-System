package com.flash.yuvar.flashattendancesystem.Student;

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
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class Student_attendance_View extends AppCompatActivity {

    String carriedclasscode;
    private List<String> date;
    String carriedregisteredid;
    private List<String> dateid;

    ArrayAdapter<String> adapter;
    ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_attendance__view);

        carriedclasscode = getIntent ().getExtras ().getString ("CarriedClassName");


        listView = (ListView) findViewById (R.id.student_listview_attendancelist);


        date = new ArrayList<>();

        dateid = new ArrayList<>();

        adapter = new ArrayAdapter<String> (this,R.layout.subject_info,R.id.subname,date);

        DatabaseReference getid = FirebaseDatabase.getInstance().getReference("student_registered_class");

        getid.orderByChild("class_name").equalTo(carriedclasscode).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {


                for(DataSnapshot ds : dataSnapshot.getChildren()){

                    carriedregisteredid = ds.getKey();
                    DatabaseReference getstudent = FirebaseDatabase.getInstance().getReference("student_registered_class").child(ds.getKey()).child("attendance_list");

                    Query query =  getstudent.orderByChild("dateandtime");

                    query.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                            adapter.clear ();
                            date.clear();

                            for(DataSnapshot ds : dataSnapshot.getChildren()){
                                attendance_list_push_qr list = ds.getValue(attendance_list_push_qr.class);

                                Integer position = list.getDateandtime().indexOf(" ");
                                String trim = list.getDateandtime().substring(position,list.getDateandtime().length());

                                dateid.add(list.getAttendance_list_id());

                                date.add(trim);
                            }

                            adapter.notifyDataSetChanged ();
                            listView.setAdapter (adapter);

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String attendees_id = dateid.get (position);


                Intent i = new Intent (getApplicationContext (), Student_Class_listAttendees_Activity.class);


                i.putExtra ("CarriedAttendeessID",attendees_id);

                i.putExtra ("CarriedRegisteredClassID",carriedregisteredid);
                startActivity (i);

            }
        });
    }
}
