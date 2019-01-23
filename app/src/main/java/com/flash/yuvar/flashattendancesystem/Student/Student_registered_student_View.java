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
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class Student_registered_student_View extends AppCompatActivity {

    String carriedclasscode;
    private List<String> names;

    ArrayAdapter<String> adapter;
    ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_registered_student__view);

        carriedclasscode = getIntent ().getExtras ().getString ("CarriedClassName");


        listView = (ListView) findViewById (R.id.student_listview_studentlist);


        names = new ArrayList<> ();

        adapter = new ArrayAdapter<String> (this,R.layout.subject_info,R.id.subname,names);

        DatabaseReference getid = FirebaseDatabase.getInstance().getReference("student_registered_class");

        getid.orderByChild("class_name").equalTo(carriedclasscode).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {


                for(DataSnapshot ds : dataSnapshot.getChildren()){

                    DatabaseReference getstudent = FirebaseDatabase.getInstance().getReference("student_registered_class").child(ds.getKey()).child("student_list");

                    Query query =  getstudent.orderByChild("name");

                    query.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                            adapter.clear ();
                            names.clear();

                            for(DataSnapshot ds : dataSnapshot.getChildren()){
                                student_registered_list list = ds.getValue(student_registered_list.class);

                                names.add(list.getName());
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
    }
}
