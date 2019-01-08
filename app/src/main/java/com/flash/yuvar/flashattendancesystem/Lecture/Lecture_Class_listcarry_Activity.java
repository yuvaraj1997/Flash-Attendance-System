package com.flash.yuvar.flashattendancesystem.Lecture;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.flash.yuvar.flashattendancesystem.Database.students_registered_class;
import com.flash.yuvar.flashattendancesystem.R;
import com.flash.yuvar.flashattendancesystem.UserProfile;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class Lecture_Class_listcarry_Activity extends AppCompatActivity {

    Button gen, scan;
    DatabaseReference registeredclass;
    private List<String> registeredlist;
    private List<String> registeredlist2;
    ArrayAdapter<String> adapter;
    students_registered_class retrieve;
    ListView listView;
    String classcodecarry,registeredclassidcarry;
    private String lecturename;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate (savedInstanceState);
        setContentView (R.layout.activity_lecture__class_listcarry_);


        registeredclass = FirebaseDatabase.getInstance ().getReference ("student_registered_class");


        retrieve = new students_registered_class ();
        listView = (ListView) findViewById (R.id.list_view);

        registeredlist = new ArrayList<> ();
        registeredlist2 = new ArrayList<> ();
        adapter = new ArrayAdapter<String> (this,R.layout.subject_info,R.id.subname,registeredlist);


        FirebaseUser user = FirebaseAuth.getInstance ().getCurrentUser ();
        String userid = user.getUid ();

        final FirebaseDatabase database = FirebaseDatabase.getInstance ();
        DatabaseReference myref = database.getReference ("users");

        myref.child (userid).addListenerForSingleValueEvent (new ValueEventListener ( ) {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                UserProfile userProfile = dataSnapshot.getValue (UserProfile.class);
                setLecturename(userProfile.getUserName());



            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText (Lecture_Class_listcarry_Activity.this,databaseError.getCode (),Toast.LENGTH_SHORT ).show ();

            }
        });


        registeredclass.addValueEventListener (new ValueEventListener ( ) {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                adapter.clear ();
                for(DataSnapshot ds: dataSnapshot.getChildren ()){


                    retrieve = ds.getValue (students_registered_class.class);

                    if(lecturename.equals(retrieve.getLecture_name())){
                        registeredlist.add(retrieve.getClass_name ().toString ());
                        registeredlist2.add (retrieve.getRegisteredclassID ().toString ());



                    }



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

                String classcode = registeredlist.get (position);
                String registeredclasscode = registeredlist2.get (position);

                Intent i = new Intent (getApplicationContext (),Lecture_Class_Detail.class);

                i.putExtra ("CarriedClassName",classcode);
                i.putExtra ("CarriedRegisteredClassID",registeredclasscode);
                startActivity (i);


            }
        });
    }

    private void setLecturename(String userName) {
        this.lecturename = userName;
    }
}
