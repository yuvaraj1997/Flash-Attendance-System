package com.flash.yuvar.flashattendancesystem.Admin.ClassSide;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;

import com.flash.yuvar.flashattendancesystem.Database.admin_profile_detail;
import com.flash.yuvar.flashattendancesystem.Database.students_registered_class;
import com.flash.yuvar.flashattendancesystem.LoginActivity;
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

public class Admin_view_class extends AppCompatActivity {

    ListView listView;

    FirebaseDatabase database;
    DatabaseReference ref;

    ArrayList<String> list;
    ArrayList<String> listid;
    ArrayAdapter<String> adapter;
    students_registered_class retrieve;

    String registeredid;



    String adminpass;

    private FloatingActionButton addclass;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate (savedInstanceState);
        setContentView (R.layout.activity_admin_view_class);


        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance ( );

        if (firebaseAuth.getCurrentUser ()==null){
            finish ();
            startActivity (new Intent (this,LoginActivity.class));
        }

        retrieve = new students_registered_class ();
        listView = (ListView) findViewById (R.id.listView);

        addclass = (FloatingActionButton)findViewById(R.id.button_floating_add_class) ;

        database = FirebaseDatabase.getInstance ();
        ref = database.getReference ("student_registered_class");
        list = new ArrayList<> ();
        listid = new ArrayList<> ();
        adapter = new ArrayAdapter<String> (this,R.layout.subject_info,R.id.subname,list);

        ref.addValueEventListener (new ValueEventListener ( ) {

            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                adapter.clear ();
                for(DataSnapshot ds: dataSnapshot.getChildren ()){


                    retrieve = ds.getValue (students_registered_class.class);

                    //Toast.makeText(Admin_view_class.this,retrieve.getRegisteredclassID(),Toast.LENGTH_LONG).show();

                    list.add(retrieve.getClass_name().toString ());
                    listid.add(retrieve.getRegisteredclassID().toString());
                }

                listView.setAdapter (adapter);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        registerForContextMenu(listView);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {



                Intent i = new Intent (getApplicationContext (), Admin_Class_Detail.class);
                i.putExtra ("CarriedClassName",list.get(position));
                i.putExtra ("CarriedRegisteredClassID",listid.get(position));
                startActivity (i);



                //Toast.makeText(Admin_view_class.this,registeredid,Toast.LENGTH_LONG).show();

            }
        });

        addclass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addclass();

            }
        });


    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        menu.setHeaderTitle("Choose your option");

        getMenuInflater().inflate(R.menu.listview_menu, menu);
    }
    @Override
    public boolean onContextItemSelected(MenuItem item) {

        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo)item.getMenuInfo();


        switch (item.getItemId()) {
            case R.id.option_delete:
                Delete(info.position);



                return true;
            default:
                return super.onContextItemSelected(item);
        }


    }

    private void Delete(int position) {

        final String classcode =list.get(position);


        FirebaseUser user = FirebaseAuth.getInstance ().getCurrentUser ();


        String userid = user.getUid ();



        DatabaseReference retrievelecturepass = FirebaseDatabase.getInstance().getReference("users").child(userid);

        retrievelecturepass.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                admin_profile_detail admin = dataSnapshot.getValue(admin_profile_detail.class);

                adminpass = admin.getPass();


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        AlertDialog.Builder builder = new AlertDialog.Builder(Admin_view_class.this);
        builder.setTitle("Password");

        // Set up the input
        final EditText input = new EditText(Admin_view_class.this);
        // Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
        input.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_VARIATION_PASSWORD);
        builder.setView(input);

        // Set up the buttons
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String m_Text = input.getText().toString();



                if(m_Text.compareTo(adminpass)==0){

                    DatabaseReference registered = FirebaseDatabase.getInstance().getReference("student_registered_class");

                    Query query = registered.orderByChild("class_name").equalTo(classcode);

                    query.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            for(DataSnapshot ds : dataSnapshot.getChildren()){
                                //Toast.makeText(Admin_view_class.this,ds.getKey(),Toast.LENGTH_LONG).show();
                                DatabaseReference removeregistered = FirebaseDatabase.getInstance().getReference("student_registered_class").child(ds.getKey());
                                removeregistered.removeValue();

                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });

                    DatabaseReference subjectcode = FirebaseDatabase.getInstance().getReference("subject_code");

                    Query query1 = subjectcode.orderByChild("subject_code").equalTo(classcode);

                    query1.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            for(DataSnapshot ds : dataSnapshot.getChildren()){
                                DatabaseReference removesubjectcode = FirebaseDatabase.getInstance().getReference("subject_code").child(ds.getKey());
                                removesubjectcode.removeValue();

                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });



                    DatabaseReference usersubject = FirebaseDatabase.getInstance().getReference("users");

                    Query query2 =  usersubject.orderByChild("type").equalTo("student");

                    query2.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            for(DataSnapshot ds : dataSnapshot.getChildren()){
                                final String userid = ds.getKey();

                                DatabaseReference insideuser = FirebaseDatabase.getInstance().getReference("users").child(ds.getKey()).child("subjects");

                                Query inside = insideuser.orderByChild("subject_code").equalTo(classcode);

                                inside.addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                        for(DataSnapshot ds : dataSnapshot.getChildren()){


                                            DatabaseReference removeusersubject = FirebaseDatabase.getInstance().getReference("users").child(userid).child("subjects")
                                                    .child(ds.getKey());
                                            removeusersubject.removeValue();
                                        }
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError databaseError) {

                                    }
                                });


                                //DatabaseReference removesubjectcode = FirebaseDatabase.getInstance().getReference("subject_code").child(ds.getKey());
                                //removesubjectcode.removeValue();

                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });





                    AlertDialog.Builder builder = new AlertDialog.Builder(Admin_view_class.this);
                    builder.setTitle("Success");
                    builder.setPositiveButton("Okay", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {


                        }
                    });

                    builder.setMessage("Deleted" );
                    AlertDialog alert1 = builder.create();
                    alert1.show();

                }else{
                    AlertDialog.Builder builder = new AlertDialog.Builder(Admin_view_class.this);
                    builder.setTitle("Failed");
                    builder.setPositiveButton("End", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();



                        }
                    });
                    builder.setMessage("Failed");
                    AlertDialog alert1 = builder.create();
                    alert1.show();

                }


            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        builder.show();






        //Toast.makeText(this,list.get(position),Toast.LENGTH_LONG).show();
    }

    private void addclass() {


        startActivity(new Intent (Admin_view_class.this, admin_add_class.class));



    }


}
