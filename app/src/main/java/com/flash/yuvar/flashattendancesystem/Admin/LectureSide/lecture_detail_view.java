package com.flash.yuvar.flashattendancesystem.Admin.LectureSide;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.flash.yuvar.flashattendancesystem.Database.admin_profile_detail;
import com.flash.yuvar.flashattendancesystem.Database.deleteduser;
import com.flash.yuvar.flashattendancesystem.Database.lecture_profile_detail;
import com.flash.yuvar.flashattendancesystem.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class lecture_detail_view extends AppCompatActivity {



    String lecturenameold;

    EditText name,email;



    Button update,delete;
    private ProgressBar loading;

    String pass;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lecture_detail_view);

        lecturenameold = getIntent ().getExtras ().getString ("lecturename");

        name = (EditText)findViewById(R.id.admin_retrieve_lecturename);

        email = (EditText)findViewById(R.id.admin_retrieve_lectureemailaddress);

        update = (Button)findViewById(R.id.button_admin_update_lecture_submit);
        delete = (Button)findViewById(R.id.button_admin_delete_lecture_submit);

        loading = findViewById(R.id.admin_lecture_retrieve_loading);

        final DatabaseReference getdetails = FirebaseDatabase.getInstance().getReference("users");


        getdetails.orderByChild("name").equalTo(lecturenameold).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot ds : dataSnapshot.getChildren()){
                    lecture_profile_detail lecture = ds.getValue(lecture_profile_detail.class);

                    name.setText(lecture.getName());

                    email.setText(lecture.getEmail());




                    email.setEnabled(false);




                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        FirebaseUser user = FirebaseAuth.getInstance ().getCurrentUser ();
        final String userid = user.getUid ();

        final FirebaseDatabase database = FirebaseDatabase.getInstance ();
        DatabaseReference myref = database.getReference ("users").child(userid);


        myref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {


                if(dataSnapshot.exists()){

                    admin_profile_detail admin = dataSnapshot.getValue(admin_profile_detail.class);


                    pass = admin.getPass();


                }



            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText (lecture_detail_view.this,databaseError.getCode (),Toast.LENGTH_SHORT ).show ();

            }
        });

        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatabaseReference getid = FirebaseDatabase.getInstance().getReference("users");

                getid.orderByChild("name").equalTo(lecturenameold).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for(DataSnapshot ds : dataSnapshot.getChildren()){
                            DatabaseReference swaplecturenameuser = FirebaseDatabase.getInstance().getReference("users").child(ds.getKey());

                            swaplecturenameuser.child("name").setValue(name.getText().toString());


                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

                DatabaseReference getsubjectcode = FirebaseDatabase.getInstance().getReference("subject_code");

                getsubjectcode.orderByChild("lecture_name").equalTo(lecturenameold).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for(DataSnapshot ds : dataSnapshot.getChildren()){
                            DatabaseReference swaplecturenamesubjectcode = FirebaseDatabase.getInstance().getReference("subject_code").child(ds.getKey());

                            swaplecturenamesubjectcode.child("lecture_name").setValue(name.getText().toString());


                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

                DatabaseReference getstudent_registered_class = FirebaseDatabase.getInstance().getReference("student_registered_class");

                getstudent_registered_class.orderByChild("lecture_name").equalTo(lecturenameold).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for(DataSnapshot ds : dataSnapshot.getChildren()){
                            DatabaseReference swaplecturenamestudent_registered_class = FirebaseDatabase.getInstance().getReference("student_registered_class").child(ds.getKey());

                            swaplecturenamestudent_registered_class.child("lecture_name").setValue(name.getText().toString());


                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

                DatabaseReference getstudent_requestaccess = FirebaseDatabase.getInstance().getReference("request_access");

                getstudent_requestaccess.orderByChild("lecture_name").equalTo(lecturenameold).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for(DataSnapshot ds : dataSnapshot.getChildren()){
                            DatabaseReference swaplecturenamestudent_requestaccess = FirebaseDatabase.getInstance().getReference("request_access").child(ds.getKey());

                            swaplecturenamestudent_requestaccess.child("lecture_name").setValue(name.getText().toString());


                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

                AlertDialog.Builder builder = new AlertDialog.Builder(lecture_detail_view.this,R.style.AlertDialogStyle);
                builder.setTitle("Success");
                builder.setPositiveButton("Okay", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();


                    }
                });
                builder.setMessage("Updated");
                AlertDialog alert1 = builder.create();
                alert1.show();




            }
        });

        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(lecture_detail_view.this,R.style.AlertDialogStyle);
                builder.setTitle("Password");

                // Set up the input
                final EditText input = new EditText(lecture_detail_view.this);
                // Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
                input.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                builder.setView(input);

                // Set up the buttons
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String m_Text = input.getText().toString();



                        if(m_Text.compareTo(pass)==0){


                            DatabaseReference getuserid = FirebaseDatabase.getInstance().getReference("users");

                            getuserid.orderByChild("name").equalTo(name.getText().toString()).addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    for(DataSnapshot ds : dataSnapshot.getChildren()){
                                        DatabaseReference deleteuesrs = FirebaseDatabase.getInstance().getReference("users").child(ds.getKey());
                                        deleteduser delete = new deleteduser(email.getText().toString());

                                        DatabaseReference adddeleteduser = FirebaseDatabase.getInstance().getReference("deleteduser").push();
                                        adddeleteduser.setValue(delete);

                                        deleteuesrs.removeValue();
                                    }


                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                }
                            });

                            DatabaseReference getsubjectcodeid = FirebaseDatabase.getInstance().getReference("subject_code");

                            getsubjectcodeid.orderByChild("lecture_name").equalTo(name.getText().toString()).addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    for(DataSnapshot ds : dataSnapshot.getChildren()){
                                        DatabaseReference deletesubjectcodeid = FirebaseDatabase.getInstance().getReference("subject_code").child(ds.getKey());
                                        deletesubjectcodeid.removeValue();
                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                }
                            });

                            DatabaseReference getstudentregisteredclassid = FirebaseDatabase.getInstance().getReference("student_registered_class");

                            getstudentregisteredclassid.orderByChild("lecture_name").equalTo(name.getText().toString()).addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    for(DataSnapshot ds : dataSnapshot.getChildren()){
                                        DatabaseReference deletestudentregisteredclassid = FirebaseDatabase.getInstance().getReference("student_registered_class").child(ds.getKey());
                                        deletestudentregisteredclassid.removeValue();
                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                }
                            });

                            DatabaseReference getstudentrequestaccess = FirebaseDatabase.getInstance().getReference("request_access");

                            getstudentrequestaccess.orderByChild("lecture_name").equalTo(name.getText().toString()).addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    for(DataSnapshot ds : dataSnapshot.getChildren()){
                                        DatabaseReference deletestudentrequestaccess = FirebaseDatabase.getInstance().getReference("request_access").child(ds.getKey());
                                        deletestudentrequestaccess.removeValue();
                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                }
                            });








                            AlertDialog.Builder builder = new AlertDialog.Builder(lecture_detail_view.this,R.style.AlertDialogStyle);
                            builder.setTitle("Success");
                            builder.setPositiveButton("Okay", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                 finish();


                                }
                            });
                            builder.setMessage("Deleted");
                            AlertDialog alert1 = builder.create();
                            alert1.show();


                        }
                        else{
                        AlertDialog.Builder builder = new AlertDialog.Builder(lecture_detail_view.this,R.style.AlertDialogStyle);
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
        });


    }
}
