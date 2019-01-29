package com.flash.yuvar.flashattendancesystem.Lecture;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.flash.yuvar.flashattendancesystem.Adapters.custom_request_adapter;
import com.flash.yuvar.flashattendancesystem.Database.Request_access;
import com.flash.yuvar.flashattendancesystem.Database.Subject_code;
import com.flash.yuvar.flashattendancesystem.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class Lecture_request_acceptance extends AppCompatActivity {

    ListView listView;

    FirebaseDatabase database;
    DatabaseReference ref;

    private List<Request_access> requestList = new ArrayList<>();
    private RecyclerView recyclerView;
    private custom_request_adapter mAdapter;

    Request_access retrieve;

    public Button button_delete;
    private String lecturename;
    private String carriedclasscode;

    DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate (savedInstanceState);
        setContentView (R.layout.activity_lecture_request_acceptance);
        carriedclasscode = getIntent ().getExtras ().getString ("CarriedClassName");

        retrieve = new Request_access ();
        recyclerView = (RecyclerView) findViewById (R.id.recycler_view);


        database = FirebaseDatabase.getInstance ();
        ref = database.getReference ().child ("request_access");

        FirebaseUser user = FirebaseAuth.getInstance ().getCurrentUser ();
        String userid = user.getUid ();

        final FirebaseDatabase database = FirebaseDatabase.getInstance ();
        DatabaseReference myref = database.getReference ("subject_code");

        myref.orderByChild("subject_code").equalTo(carriedclasscode).addListenerForSingleValueEvent (new ValueEventListener ( ) {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for(DataSnapshot ds : dataSnapshot.getChildren()){


                    Subject_code subjectCode = ds.getValue (Subject_code.class);
                    setLecturename(subjectCode.getLecture_name());


                }




            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText (Lecture_request_acceptance.this,databaseError.getCode (),Toast.LENGTH_SHORT ).show ();

            }
        });


        recyclerView.addItemDecoration(new DividerItemDecoration (this, LinearLayoutManager.VERTICAL));

        Query query = ref.orderByChild("class_Code");

        query.addValueEventListener (new ValueEventListener ( ) {

            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {



                for(DataSnapshot ds: dataSnapshot.getChildren ()){


                    retrieve = ds.getValue (Request_access.class);


                    Request_access request_access= new Request_access (retrieve.getClass_Code (),retrieve.getUserID (),retrieve.getRequest_id (),retrieve.getClassID (),retrieve.getLecture_name());

                    if(lecturename.equals(retrieve.getLecture_name()) && carriedclasscode.equals(retrieve.getClass_Code())){
                        requestList.add(request_access);

                    }









                }



                mAdapter = new custom_request_adapter (requestList);
                RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager (getApplicationContext());
                recyclerView.setLayoutManager(mLayoutManager);
                recyclerView.setItemAnimator(new DefaultItemAnimator ());




                recyclerView.setAdapter(mAdapter);





            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });



    }

    private void setLecturename(String userName) {
        this.lecturename = userName;
    }
}
