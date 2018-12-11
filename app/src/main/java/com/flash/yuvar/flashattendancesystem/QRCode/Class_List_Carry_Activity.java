package com.flash.yuvar.flashattendancesystem.QRCode;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import com.flash.yuvar.flashattendancesystem.Database.students_registered_class;
import com.flash.yuvar.flashattendancesystem.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class Class_List_Carry_Activity extends AppCompatActivity {
    Button gen, scan;
    DatabaseReference registeredclass;
    private List<String> registeredlist;
    private List<String> registeredlist2;
    ArrayAdapter<String> adapter;
    students_registered_class retrieve;
    ListView listView;
    String classcodecarry,registeredclassidcarry;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate (savedInstanceState);
        setContentView (R.layout.activity_class__list__carry_);
        //gen = (Button) findViewById(R.id.button_open);


        registeredclass = FirebaseDatabase.getInstance ().getReference ("student_registered_class");


        retrieve = new students_registered_class ();
        listView = (ListView) findViewById (R.id.recycler_view2);

        registeredlist = new ArrayList<> ();
        registeredlist2 = new ArrayList<> ();
        adapter = new ArrayAdapter<String> (this,R.layout.subject_info,R.id.subname,registeredlist);

        registeredclass.addValueEventListener (new ValueEventListener ( ) {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot ds: dataSnapshot.getChildren ()){


                    retrieve = ds.getValue (students_registered_class.class);
                    registeredlist.add(retrieve.getClass_name ().toString ());
                    registeredlist2.add (retrieve.getRegisteredclassID ().toString ());

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

                Intent  i = new Intent (getApplicationContext (),QRCode_Generate_Activity.class);
                i.putExtra ("CarriedClassName",classcode);
                i.putExtra ("CarriedRegisteredClassID",registeredclasscode);
                startActivity (i);


            }
        });









    }
}
