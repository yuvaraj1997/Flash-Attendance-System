package com.flash.yuvar.flashattendancesystem.Adapters;

import android.content.Context;
import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.flash.yuvar.flashattendancesystem.Database.lecture_profile_detail;
import com.flash.yuvar.flashattendancesystem.Database.student_registered_list;
import com.flash.yuvar.flashattendancesystem.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class student_absentclass_adapter extends  RecyclerView.Adapter<student_absentclass_adapter.MyViewHolder> {

    private List<student_registered_list> absentclassList;


    private Context ctx;







    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView student_name;
        CheckBox mc,latetoclass,absentwithreason;




        MyViewHolder(View view) {
            super(view);

            student_name = (TextView) view.findViewById(R.id.adapter_absent_name);
            mc = (CheckBox)view.findViewById(R.id.checkBox);
            latetoclass = (CheckBox)view.findViewById(R.id.checkBoxLateToClass);
            absentwithreason = (CheckBox)view.findViewById(R.id.checkBoxOtherReason);



        }
    }





    @NonNull
    @Override
    public student_absentclass_adapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.adapter_absentclass, viewGroup, false);

        this.ctx = viewGroup.getContext ();

        return new student_absentclass_adapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull final student_absentclass_adapter.MyViewHolder myViewHolder, int i) {

        final student_registered_list list =absentclassList.get(i);

        myViewHolder.student_name.setText(list.getName());

        FirebaseUser user = FirebaseAuth.getInstance ().getCurrentUser ();


        final String userid = user.getUid ();








        myViewHolder.mc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                DatabaseReference retrievelecturepass = FirebaseDatabase.getInstance().getReference("users").child(userid);

                retrievelecturepass.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                        lecture_profile_detail lecture = dataSnapshot.getValue(lecture_profile_detail.class);

                        final String lecturepass = lecture.getPass();


                        AlertDialog.Builder builder = new AlertDialog.Builder(ctx,R.style.AlertDialogStyle);
                        builder.setTitle("Password");

                        // Set up the input
                        final EditText input = new EditText(ctx);
                        // Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
                        input.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_VARIATION_PASSWORD);
                        builder.setView(input);

                        // Set up the buttons
                        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                String m_Text = input.getText().toString();



                                if(m_Text.compareTo(lecturepass)==0){
                                    String enddate = new SimpleDateFormat("EEE, d MMM yyyy HH:mm:ss ", Locale.getDefault()).format(new Date());
                                    DatabaseReference addreason = FirebaseDatabase.getInstance().getReference("student_registered_class").child(list.getCarriedregisteredid()).child("attendance_list")
                                            .child(list.getCarriedAttendeeID()).child("attendees").push();

                                    student_registered_list add = new student_registered_list(list.getuID(),list.getName(),enddate,"MC");
                                    addreason.setValue(add);

                                    DatabaseReference addcount = FirebaseDatabase.getInstance().getReference("student_registered_class").child(list.getCarriedregisteredid()).child("student_list");

                                    addcount.orderByChild("name").equalTo(list.getName()).addListenerForSingleValueEvent(new ValueEventListener() {
                                        Integer count =0;
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                            if(dataSnapshot.exists()){
                                                for(DataSnapshot ds: dataSnapshot.getChildren()){
                                                    student_registered_list list2 = ds.getValue(student_registered_list.class);

                                                    this.count = list2.getCount();

                                                    count++;

                                                    DatabaseReference update = FirebaseDatabase.getInstance().getReference("student_registered_class").child(list.getCarriedregisteredid()).child("student_list")
                                                            .child(ds.getKey().toString());

                                                    student_registered_list push = new student_registered_list(list.getuID(),list.getName(),count);

                                                    update.setValue(push);

                                                    Toast.makeText(ctx,"Success",Toast.LENGTH_LONG).show();




                                                }
                                            }
                                        }

                                        @Override
                                        public void onCancelled(@NonNull DatabaseError databaseError) {

                                        }
                                    });


                                    AlertDialog.Builder builder = new AlertDialog.Builder(ctx,R.style.AlertDialogStyle);
                                    builder.setTitle("Success");
                                    builder.setPositiveButton("Okay", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {

                                            dialog.cancel();

                                        }
                                    });

                                    builder.setMessage("MC Added" );
                                    AlertDialog alert1 = builder.create();
                                    alert1.show();

                                }else{
                                    AlertDialog.Builder builder = new AlertDialog.Builder(ctx,R.style.AlertDialogStyle);
                                    builder.setTitle("Failed");
                                    builder.setPositiveButton("End", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            dialog.cancel();
                                            myViewHolder.mc.setChecked(false);

                                        }
                                    });

                                    myViewHolder.mc.setChecked(false);
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
                                myViewHolder.mc.setChecked(false);
                            }
                        });
                        builder.show();
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });


            }
        });

        myViewHolder.absentwithreason.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                DatabaseReference retrievelecturepass = FirebaseDatabase.getInstance().getReference("users").child(userid);

                retrievelecturepass.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                        lecture_profile_detail lecture = dataSnapshot.getValue(lecture_profile_detail.class);

                        final String lecturepass = lecture.getPass();


                        AlertDialog.Builder builder = new AlertDialog.Builder(ctx,R.style.AlertDialogStyle);
                        builder.setTitle("Password");

                        // Set up the input
                        final EditText input = new EditText(ctx);
                        // Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
                        input.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_VARIATION_PASSWORD);
                        builder.setView(input);

                        // Set up the buttons
                        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                String m_Text = input.getText().toString();



                                if(m_Text.compareTo(lecturepass)==0){



                                    String enddate = new SimpleDateFormat("EEE, d MMM yyyy HH:mm:ss ", Locale.getDefault()).format(new Date());
                                    DatabaseReference addreason = FirebaseDatabase.getInstance().getReference("student_registered_class").child(list.getCarriedregisteredid()).child("attendance_list")
                                            .child(list.getCarriedAttendeeID()).child("attendees").push();

                                    student_registered_list add = new student_registered_list(list.getuID(),list.getName(),enddate,"Absent With Reason");
                                    addreason.setValue(add);

                                    DatabaseReference addcount = FirebaseDatabase.getInstance().getReference("student_registered_class").child(list.getCarriedregisteredid()).child("student_list");

                                    addcount.orderByChild("name").equalTo(list.getName()).addListenerForSingleValueEvent(new ValueEventListener() {
                                        Integer count =0;
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                            if(dataSnapshot.exists()){
                                                for(DataSnapshot ds: dataSnapshot.getChildren()){
                                                    student_registered_list list2 = ds.getValue(student_registered_list.class);

                                                    this.count = list2.getCount();

                                                    count++;

                                                    DatabaseReference update = FirebaseDatabase.getInstance().getReference("student_registered_class").child(list.getCarriedregisteredid()).child("student_list")
                                                            .child(ds.getKey().toString());

                                                    student_registered_list push = new student_registered_list(list.getuID(),list.getName(),count);

                                                    update.setValue(push);

                                                    Toast.makeText(ctx,"Success",Toast.LENGTH_LONG).show();




                                                }
                                            }
                                        }

                                        @Override
                                        public void onCancelled(@NonNull DatabaseError databaseError) {

                                        }
                                    });


                                    AlertDialog.Builder builder = new AlertDialog.Builder(ctx,R.style.AlertDialogStyle);
                                    builder.setTitle("Success");
                                    builder.setPositiveButton("Okay", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {

                                            dialog.cancel();

                                        }
                                    });

                                    builder.setMessage("Absent With Reason Added" );
                                    AlertDialog alert1 = builder.create();
                                    alert1.show();

                                }else{
                                    AlertDialog.Builder builder = new AlertDialog.Builder(ctx,R.style.AlertDialogStyle);
                                    builder.setTitle("Failed");
                                    builder.setPositiveButton("End", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            dialog.cancel();
                                            myViewHolder.absentwithreason.setChecked(false);

                                        }
                                    });

                                    myViewHolder.absentwithreason.setChecked(false);
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
                                myViewHolder.absentwithreason.setChecked(false);
                            }
                        });
                        builder.show();
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });


            }
        });

        myViewHolder.latetoclass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                DatabaseReference retrievelecturepass = FirebaseDatabase.getInstance().getReference("users").child(userid);

                retrievelecturepass.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                        lecture_profile_detail lecture = dataSnapshot.getValue(lecture_profile_detail.class);

                        final String lecturepass = lecture.getPass();


                        AlertDialog.Builder builder = new AlertDialog.Builder(ctx,R.style.AlertDialogStyle);
                        builder.setTitle("Password");

                        // Set up the input
                        final EditText input = new EditText(ctx);
                        // Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
                        input.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_VARIATION_PASSWORD);
                        builder.setView(input);

                        // Set up the buttons
                        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                String m_Text = input.getText().toString();



                                if(m_Text.compareTo(lecturepass)==0){
                                    String enddate = new SimpleDateFormat("EEE, d MMM yyyy HH:mm:ss ", Locale.getDefault()).format(new Date());
                                    DatabaseReference addreason = FirebaseDatabase.getInstance().getReference("student_registered_class").child(list.getCarriedregisteredid()).child("attendance_list")
                                            .child(list.getCarriedAttendeeID()).child("attendees").push();

                                    student_registered_list add = new student_registered_list(list.getuID(),list.getName(),enddate,"Late To Class");
                                    addreason.setValue(add);

                                    DatabaseReference addcount = FirebaseDatabase.getInstance().getReference("student_registered_class").child(list.getCarriedregisteredid()).child("student_list");

                                    addcount.orderByChild("name").equalTo(list.getName()).addListenerForSingleValueEvent(new ValueEventListener() {
                                        Integer count =0 ;
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                            if(dataSnapshot.exists()){
                                                for(DataSnapshot ds: dataSnapshot.getChildren()){
                                                    student_registered_list list2 = ds.getValue(student_registered_list.class);

                                                    this.count = list2.getCount();

                                                    count++;

                                                    DatabaseReference update = FirebaseDatabase.getInstance().getReference("student_registered_class").child(list.getCarriedregisteredid()).child("student_list")
                                                            .child(ds.getKey().toString());

                                                    student_registered_list push = new student_registered_list(list.getuID(),list.getName(),count);

                                                    update.setValue(push);

                                                    Toast.makeText(ctx,"Success",Toast.LENGTH_LONG).show();




                                                }
                                            }
                                        }

                                        @Override
                                        public void onCancelled(@NonNull DatabaseError databaseError) {

                                        }
                                    });


                                    AlertDialog.Builder builder = new AlertDialog.Builder(ctx,R.style.AlertDialogStyle);
                                    builder.setTitle("Success");
                                    builder.setPositiveButton("Okay", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {

                                            dialog.cancel();

                                        }
                                    });

                                    builder.setMessage("Late To Class Added" );
                                    AlertDialog alert1 = builder.create();
                                    alert1.show();

                                }else{
                                    AlertDialog.Builder builder = new AlertDialog.Builder(ctx,R.style.AlertDialogStyle);
                                    builder.setTitle("Failed");
                                    builder.setPositiveButton("End", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            dialog.cancel();
                                            myViewHolder.latetoclass.setChecked(false);

                                        }
                                    });

                                    myViewHolder.latetoclass.setChecked(false);
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
                                myViewHolder.latetoclass.setChecked(false);
                            }
                        });
                        builder.show();
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });


            }
        });









    }

    @Override
    public int getItemCount() {
        return absentclassList.size();
    }


    public student_absentclass_adapter(List<student_registered_list> absentclassList) {



        this.absentclassList = absentclassList;
    }



}
