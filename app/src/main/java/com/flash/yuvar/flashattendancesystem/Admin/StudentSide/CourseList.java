package com.flash.yuvar.flashattendancesystem.Admin.StudentSide;

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
import android.widget.Toast;

import com.flash.yuvar.flashattendancesystem.Database.admin_course;
import com.flash.yuvar.flashattendancesystem.Database.admin_profile_detail;
import com.flash.yuvar.flashattendancesystem.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class CourseList extends AppCompatActivity {

    private List<String> course = new ArrayList<String>();



    FloatingActionButton add;

    String adminpass;



    ArrayAdapter<String> adapter;
    ListView list;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course_list);

        list = (ListView) findViewById (R.id.listview_admin_courselist);
        add = (FloatingActionButton)findViewById(R.id.button_admin_floating_add_course);


        DatabaseReference getcourse = FirebaseDatabase.getInstance().getReference("course");

        adapter = new ArrayAdapter<String> (this,R.layout.subject_info,R.id.subname,course);

        getcourse.orderByChild("course").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {


                adapter.clear ();
                course.clear();

                for(DataSnapshot ds : dataSnapshot.getChildren()){
                    admin_course retrievecourse= ds.getValue(admin_course.class);

                    course.add(retrievecourse.getCourse());

                }

                adapter.notifyDataSetChanged();
                list.setAdapter (adapter);



                //Toast.makeText(CourseList.this, String.format(course.get(0) + course.size()),Toast.LENGTH_LONG).show();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        registerForContextMenu(list);

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent i = new Intent (getApplicationContext (), Admin_student_add.class);

                i.putExtra ("coursename",course.get(position));

                startActivity (i);
            }
        });



        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = new Intent (getApplicationContext (), Admin_add_course.class);

                startActivity (i);

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

    private void Delete(final int position) {
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

        AlertDialog.Builder builder = new AlertDialog.Builder(CourseList.this);
        builder.setTitle("Password");

        // Set up the input
        final EditText input = new EditText(CourseList.this);
        // Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
        input.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_VARIATION_PASSWORD);
        builder.setView(input);

        // Set up the buttons
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String m_Text = input.getText().toString();



                if(m_Text.compareTo(adminpass)==0){


                    DatabaseReference delete = FirebaseDatabase.getInstance().getReference("course");

                    delete.orderByChild("course").equalTo(course.get(position)).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            if(dataSnapshot.exists()){
                                for(DataSnapshot ds: dataSnapshot.getChildren()){
                                    DatabaseReference remove = FirebaseDatabase.getInstance().getReference("course").child(ds.getKey());

                                    remove.removeValue().addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Toast.makeText(CourseList.this,"Failed",Toast.LENGTH_LONG).show();
                                        }
                                    });


                                }
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });






                    AlertDialog.Builder builder = new AlertDialog.Builder(CourseList.this);
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
                    AlertDialog.Builder builder = new AlertDialog.Builder(CourseList.this);
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



    }
}
