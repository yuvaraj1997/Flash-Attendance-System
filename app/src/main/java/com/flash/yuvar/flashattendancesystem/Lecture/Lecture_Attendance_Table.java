package com.flash.yuvar.flashattendancesystem.Lecture;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import com.evrencoskun.tableview.TableView;
import com.flash.yuvar.flashattendancesystem.Adapters.TableViewAdapter;
import com.flash.yuvar.flashattendancesystem.Database.TableModal;
import com.flash.yuvar.flashattendancesystem.Database.student_registered_list;
import com.flash.yuvar.flashattendancesystem.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class Lecture_Attendance_Table extends AppCompatActivity {
    TableView tableView ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lecture__attendance__table);

        final RecyclerView recyclerView = findViewById(R.id.recyclerViewDeliveryProductList);

        final List<TableModal> StudentList = new ArrayList<>();
        DatabaseReference student  = FirebaseDatabase.getInstance().getReference("student_registered_class").child("-LVlowhI2WyahbJyIBU8").child("student_list");

        student.addListenerForSingleValueEvent(new ValueEventListener() {
            Integer i = 0;

            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                StudentList.clear();
                for(DataSnapshot ds : dataSnapshot.getChildren()){
                    i++;
                    final student_registered_list completelist = ds.getValue(student_registered_list.class);




                    DatabaseReference student1  = FirebaseDatabase.getInstance().getReference("student_registered_class").child("-LVlowhI2WyahbJyIBU8").child("attendance_list");

                    student1.addListenerForSingleValueEvent(new ValueEventListener() {

                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            String attendanceid;
                            for(DataSnapshot ds : dataSnapshot.getChildren()){

                                DatabaseReference student2  = FirebaseDatabase.getInstance().getReference("student_registered_class").child("-LVlowhI2WyahbJyIBU8").child("attendance_list")
                                        .child(ds.getKey()).child("attendees");

                                student2.addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                        for(DataSnapshot ds : dataSnapshot.getChildren()){

                                            student_registered_list list = ds.getValue(student_registered_list.class);

                                            if(list.getName().equals(completelist.getName())){

                                             
                                                StudentList.add(new TableModal("Present","Present",list.getName() ,i ));
                                                Toast.makeText(Lecture_Attendance_Table.this, completelist.getName(), Toast.LENGTH_SHORT).show();
                                                return;
                                            }
                                            else{
                                                StudentList.add(new TableModal("Absent","Absent",list.getName() ,i ));
                                                return;

                                            }
                                        }
                                        TableViewAdapter adapter = new TableViewAdapter(StudentList);

                                        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(Lecture_Attendance_Table.this);
                                        recyclerView.setLayoutManager(linearLayoutManager);

                                        recyclerView.setAdapter(adapter);
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


            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }


}
