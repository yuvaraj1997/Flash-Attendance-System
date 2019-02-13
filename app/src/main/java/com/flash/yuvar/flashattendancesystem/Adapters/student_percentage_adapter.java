package com.flash.yuvar.flashattendancesystem.Adapters;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.flash.yuvar.flashattendancesystem.Database.admin_profile_detail;
import com.flash.yuvar.flashattendancesystem.Database.student_registered_list;
import com.flash.yuvar.flashattendancesystem.Database.students_registered_class;
import com.flash.yuvar.flashattendancesystem.Lecture.Lecture_Email_Intent;
import com.flash.yuvar.flashattendancesystem.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

public class student_percentage_adapter extends RecyclerView.Adapter<student_percentage_adapter.MyViewHolder> {


    List<student_registered_list> requestList;
    private Context ctx;


    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView student_name,student_percentage;
        public Button but_send_Warning,delete;
        public String adminpass;

        public MyViewHolder(View view) {
            super(view);
            but_send_Warning= (Button) view.findViewById(R.id.but_send_warning_issue);
            student_name = (TextView) view.findViewById(R.id.student_name_percentage);
            student_percentage = (TextView) view.findViewById(R.id.student_percentage);
            delete = (Button) view.findViewById(R.id.but_remove_student);



        }
    }

    public student_percentage_adapter(List<student_registered_list> requestList) {



        this.requestList = requestList;
    }


    @Override
    public student_percentage_adapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.adapter_student_percentage, parent, false);

        this.ctx = parent.getContext ();

        return new student_percentage_adapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final student_percentage_adapter.MyViewHolder holder, final int position) {

        final student_registered_list list = requestList.get(position);




        holder.student_name.setText(list.getName().trim());



        //holder.student_percentage.setText(list.getCount1().toString() + "%");
        holder.student_percentage.setText(String.format("%.2f ", list.getCount1())+"%");

        FirebaseUser user = FirebaseAuth.getInstance ().getCurrentUser ();


        final String userid = user.getUid ();

        holder.but_send_Warning.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ctx, Lecture_Email_Intent.class);


                intent.putExtra("uID", list.getuID());


                ctx.startActivity(intent);
            }
        });





        Toast.makeText(ctx,holder.adminpass,Toast.LENGTH_LONG).show();







        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatabaseReference classname = FirebaseDatabase.getInstance().getReference("student_registered_class").child(list.getCarriedregisteredid());

                classname.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                            final students_registered_class classname = dataSnapshot.getValue(students_registered_class.class);

                        DatabaseReference retrievelecturepass = FirebaseDatabase.getInstance().getReference("users").child(userid);

                        retrievelecturepass.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                                admin_profile_detail admin = dataSnapshot.getValue(admin_profile_detail.class);

                                final String adminpass = admin.getPass();


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



                                        if(m_Text.compareTo(adminpass)==0){

                                            removestudentregisteredclass(classname.getClass_name(),list.getuID(),list.getCarriedregisteredid());
                                            removefromusers(classname.getClass_name(),list.getuID());




                                            AlertDialog.Builder builder = new AlertDialog.Builder(ctx,R.style.AlertDialogStyle);
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
                                            AlertDialog.Builder builder = new AlertDialog.Builder(ctx,R.style.AlertDialogStyle);
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













                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });





                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }
        });









    }

    private void removefromusers(String class_name, final String getuID) {

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("users").child(getuID).child("subjects");

        ref.orderByChild("subject_code").equalTo(class_name).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot ds : dataSnapshot.getChildren()){

                    DatabaseReference del = FirebaseDatabase.getInstance().getReference("users").child(getuID).child("subjects").child(ds.getKey());

                    del.removeValue();

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void removestudentregisteredclass(String class_name, final String getuID, final String carriedregisteredid) {

        DatabaseReference removestudentlist = FirebaseDatabase.getInstance().getReference("student_registered_class").child(carriedregisteredid).child("student_list");

        Query studentlisid = removestudentlist.orderByChild("uID").equalTo(getuID);

        studentlisid.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot ds : dataSnapshot.getChildren()){

                    DatabaseReference delstudentlist = FirebaseDatabase.getInstance().getReference("student_registered_class").child(carriedregisteredid).child("student_list")
                            .child(ds.getKey());

                    delstudentlist.removeValue();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        DatabaseReference removeattendeeslist = FirebaseDatabase.getInstance().getReference("student_registered_class").child(carriedregisteredid).child("attendance_list");

        removeattendeeslist.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot ds : dataSnapshot.getChildren()){

                    DatabaseReference checkinsideid = FirebaseDatabase.getInstance().getReference("student_registered_class").child(carriedregisteredid).child("attendance_list").child(ds.getKey())
                            .child("attendees");

                    final String attendeesid = ds.getKey();

                    checkinsideid.orderByChild("uID").equalTo(getuID).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            for(DataSnapshot ds : dataSnapshot.getChildren()){

                                DatabaseReference removeinsideattendees = FirebaseDatabase.getInstance().getReference("student_registered_class").child(carriedregisteredid).child("attendance_list").child(attendeesid)
                                        .child("attendees").child(ds.getKey());

                                Toast.makeText(ctx,ds.getKey(),Toast.LENGTH_LONG).show();

                                removeinsideattendees.removeValue();

                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });






    }


    @Override
    public int getItemCount() {

        return requestList.size();
    }

}
