package com.flash.yuvar.flashattendancesystem.Lecture;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.flash.yuvar.flashattendancesystem.Adapters.student_percentage_adapter;
import com.flash.yuvar.flashattendancesystem.Database.student_registered_list;
import com.flash.yuvar.flashattendancesystem.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class Lecture_Student_List extends AppCompatActivity {

    String carriedregisteredid;
    private List<student_registered_list> requestList = new ArrayList<>();
    private RecyclerView recyclerView;

    student_registered_list retrieve;

    private Integer percentage,originalcount;
    private student_percentage_adapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lecture__student__list);

        carriedregisteredid = getIntent ().getExtras ().getString ("CarriedRegisteredClassID");

        retrieve = new student_registered_list();

        recyclerView = (RecyclerView) findViewById (R.id.recycler_view_studentlist_percentage);


        recyclerView.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));

        DatabaseReference ref2 = FirebaseDatabase.getInstance().getReference("student_registered_class").child(carriedregisteredid).child("attendance_list");

        ref2.addListenerForSingleValueEvent(new ValueEventListener() {
            Integer count=0;
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot ds: dataSnapshot.getChildren()){

                    count++;
                }



                setOriginalCount(count);



            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("student_registered_class").child(carriedregisteredid).child("student_list");

        ref.addValueEventListener (new ValueEventListener( ) {

            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                requestList.clear();

                for(DataSnapshot ds: dataSnapshot.getChildren ()){


                    retrieve = ds.getValue (student_registered_list.class);




                    Double percentage = ((double)retrieve.getCount()/originalcount) * 100;




     






                    student_registered_list list= new student_registered_list (retrieve.getuID(),retrieve.getName(),percentage);

                    requestList.add(list);














                }



                mAdapter = new student_percentage_adapter(requestList);
                RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager (getApplicationContext());
                recyclerView.setLayoutManager(mLayoutManager);
                recyclerView.setItemAnimator(new DefaultItemAnimator());




                recyclerView.setAdapter(mAdapter);





            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }

    private void setOriginalCount(Integer count) {
        this.originalcount = count;
    }

    private void setPercentage(Integer percentage) {
        this.percentage = percentage;
    }
}
