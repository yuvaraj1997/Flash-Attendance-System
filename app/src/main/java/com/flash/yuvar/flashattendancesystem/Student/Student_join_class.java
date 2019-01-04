package com.flash.yuvar.flashattendancesystem.Student;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.flash.yuvar.flashattendancesystem.Adapters.student_join_class_adapter;
import com.flash.yuvar.flashattendancesystem.Database.Subject_code;
import com.flash.yuvar.flashattendancesystem.LoginActivity;
import com.flash.yuvar.flashattendancesystem.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class Student_join_class extends AppCompatActivity {

    RecyclerView recyclerView;

    FirebaseDatabase database;
    DatabaseReference ref;

    private student_join_class_adapter adapter;
    Subject_code subject_code;

    private List<Subject_code> list = new ArrayList<>();

    DatabaseReference databaseReference;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate (savedInstanceState);
        setContentView (R.layout.activity_student_join_class);

        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance ( );

        if (firebaseAuth.getCurrentUser ()==null){
            finish ();
            startActivity (new Intent (this,LoginActivity.class));
        }

        subject_code = new Subject_code();
        recyclerView = (RecyclerView) findViewById (R.id.recycler_view_joinclass);

        database = FirebaseDatabase.getInstance ();
        ref = database.getReference ("subject_code");


        recyclerView.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));

        ref.addValueEventListener (new ValueEventListener ( ) {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                list.clear();

                for(DataSnapshot ds: dataSnapshot.getChildren ()){


                    subject_code = ds.getValue (Subject_code.class);
                    Subject_code retrieve = new Subject_code(subject_code.getSubject_id(),subject_code.getSubject_code(),subject_code.getLecture_name(),subject_code.getPassword());
                    list.add(retrieve);


                }

                adapter = new student_join_class_adapter(list);
                RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager (getApplicationContext());
                recyclerView.setLayoutManager(mLayoutManager);
                recyclerView.setItemAnimator(new DefaultItemAnimator());





                recyclerView.setAdapter(adapter);


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });












                //Toast.makeText (Student_join_class.this, "Request Successfull",Toast.LENGTH_LONG ).show ();




    }



}

