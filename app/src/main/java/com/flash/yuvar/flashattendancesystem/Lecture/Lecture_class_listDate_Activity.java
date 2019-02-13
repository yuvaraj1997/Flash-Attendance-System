package com.flash.yuvar.flashattendancesystem.Lecture;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
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

import com.flash.yuvar.flashattendancesystem.Database.attendance_list_push_qr;
import com.flash.yuvar.flashattendancesystem.Database.lecture_profile_detail;
import com.flash.yuvar.flashattendancesystem.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class Lecture_class_listDate_Activity extends AppCompatActivity {

    String carriedclasscode,carriedregisteredid;

    DatabaseReference attendancelist;
    private List<String> attendancelistid;
    private List<String> dateandtime;
    ArrayAdapter<String> adapter;
    ListView listView;
    attendance_list_push_qr retrieve;
    private String lecturepass;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate (savedInstanceState);
        setContentView (R.layout.activity_lecture_class_list_date_);

        carriedclasscode = getIntent ().getExtras ().getString ("CarriedClassName");
        carriedregisteredid = getIntent ().getExtras ().getString ("CarriedRegisteredClassID");

        attendancelist = FirebaseDatabase.getInstance ().getReference ("student_registered_class").child(carriedregisteredid).child("attendance_list");

        retrieve = new attendance_list_push_qr ();
        listView = (ListView) findViewById (R.id.list_view_date);

        attendancelistid = new ArrayList<> ();
        dateandtime = new ArrayList<> ();

        adapter = new ArrayAdapter<String> (this,R.layout.subject_info,R.id.subname,dateandtime);

        attendancelist.addValueEventListener (new ValueEventListener ( ) {

            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                adapter.clear ();
                attendancelistid.clear();
                dateandtime.clear();

                for(DataSnapshot ds: dataSnapshot.getChildren ()){


                    retrieve = ds.getValue (attendance_list_push_qr.class);
                    attendancelistid.add(retrieve.getAttendance_list_id ().toString ());
                    Integer position = retrieve.getDateandtime().indexOf(" ");
                    dateandtime.add (retrieve.getDateandtime ().substring(position+ 1,retrieve.getDateandtime().length()));

                }

                adapter.notifyDataSetChanged ();
                listView.setAdapter (adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        registerForContextMenu(listView);

        listView.setOnItemClickListener (new AdapterView.OnItemClickListener ( ) {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                String attendees_id = attendancelistid.get (position);


                Intent i = new Intent (getApplicationContext (),Lecture_Class_listAttendees_Activity.class);
                i.putExtra ("CarriedAttendeessID",attendees_id);

                i.putExtra ("CarriedRegisteredClassID",carriedregisteredid);
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

                lecture_profile_detail lecture = dataSnapshot.getValue(lecture_profile_detail.class);

                lecturepass = lecture.getPass();


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        AlertDialog.Builder builder = new AlertDialog.Builder(Lecture_class_listDate_Activity.this,R.style.AlertDialogStyle);
        builder.setTitle("Password");

        // Set up the input
        final EditText input = new EditText(Lecture_class_listDate_Activity.this);
        // Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
        input.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_VARIATION_PASSWORD);
        builder.setView(input);

        // Set up the buttons
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String m_Text = input.getText().toString();



                if(m_Text.compareTo(lecturepass)==0){


                    DatabaseReference delete = FirebaseDatabase.getInstance().getReference("student_registered_class").child(carriedregisteredid).child("attendance_list").child(attendancelistid.get(position));

                    delete.removeValue();




                    AlertDialog.Builder builder = new AlertDialog.Builder(Lecture_class_listDate_Activity.this,R.style.AlertDialogStyle);
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
                    AlertDialog.Builder builder = new AlertDialog.Builder(Lecture_class_listDate_Activity.this,R.style.AlertDialogStyle);
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
