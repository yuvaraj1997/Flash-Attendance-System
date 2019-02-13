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
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.flash.yuvar.flashattendancesystem.Database.admin_profile_detail;
import com.flash.yuvar.flashattendancesystem.Database.student_registered_list;
import com.flash.yuvar.flashattendancesystem.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

public class student_inclass_adapter extends  RecyclerView.Adapter<student_inclass_adapter.MyViewHolder>  {


    private List<student_registered_list>inclassList;


    private Context ctx;







    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView student_name;
        public Button remove;


        public MyViewHolder(View view) {
            super(view);

            student_name = (TextView) view.findViewById(R.id.adapter_attendees_list);
            remove = (Button)view.findViewById(R.id.but_remove_inclass);


        }
    }





    @NonNull
    @Override
    public student_inclass_adapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.adapter_inclass, viewGroup, false);

        this.ctx = viewGroup.getContext ();

        return new student_inclass_adapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull student_inclass_adapter.MyViewHolder myViewHolder, int i) {

        final student_registered_list list =inclassList.get(i);

        myViewHolder.student_name.setText(list.getName());


        FirebaseUser user = FirebaseAuth.getInstance ().getCurrentUser ();


        final String userid = user.getUid ();

        myViewHolder.remove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                DatabaseReference retrievelecturepass = FirebaseDatabase.getInstance().getReference("users").child(userid);

                retrievelecturepass.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                        admin_profile_detail admin = dataSnapshot.getValue(admin_profile_detail.class);

                        final String pass = admin.getPass();

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


                                if (m_Text.compareTo(pass) == 0) {

                                    removefromusers(list.getCarriedregisteredid(), list.getCarriedAttendeeID(), list.getuID());


                                    AlertDialog.Builder builder = new AlertDialog.Builder(ctx,R.style.AlertDialogStyle);
                                    builder.setTitle("Success");
                                    builder.setPositiveButton("Okay", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {


                                        }
                                    });

                                    builder.setMessage("Deleted");
                                    AlertDialog alert1 = builder.create();
                                    alert1.show();

                                } else {
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
        });





    }

    private void removefromusers(final String carriedregisteredid, final String carriedAttendeeID, final String getuID) {

        DatabaseReference remove = FirebaseDatabase.getInstance().getReference("student_registered_class").child(carriedregisteredid)
                .child("attendance_list").child(carriedAttendeeID).child("attendees");

        remove.orderByChild("uID").equalTo(getuID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot ds: dataSnapshot.getChildren()){

                    DatabaseReference del = FirebaseDatabase.getInstance().getReference("student_registered_class").child(carriedregisteredid)
                            .child("attendance_list").child(carriedAttendeeID).child("attendees").child(ds.getKey());

                    del.removeValue();

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        DatabaseReference addcount = FirebaseDatabase.getInstance().getReference("student_registered_class").child(carriedregisteredid).child("student_list");

        addcount.orderByChild("uID").equalTo(getuID).addListenerForSingleValueEvent(new ValueEventListener() {
            Integer count =0;
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    for(DataSnapshot ds: dataSnapshot.getChildren()){
                        student_registered_list list2 = ds.getValue(student_registered_list.class);

                        this.count = list2.getCount();

                        count--;

                        DatabaseReference update = FirebaseDatabase.getInstance().getReference("student_registered_class").child(carriedregisteredid).child("student_list")
                                .child(ds.getKey().toString());

                        student_registered_list push = new student_registered_list(getuID,list2.getName(),count);

                        update.setValue(push);

                        Toast.makeText(ctx,"Success",Toast.LENGTH_LONG).show();




                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    @Override
    public int getItemCount() {
        return inclassList.size();
}

    public student_inclass_adapter(List<student_registered_list> inclasslist) {



        this.inclassList = inclasslist;
    }
}
