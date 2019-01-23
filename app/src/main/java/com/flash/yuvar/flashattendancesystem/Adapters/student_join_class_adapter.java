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

import com.flash.yuvar.flashattendancesystem.Database.Request_access;
import com.flash.yuvar.flashattendancesystem.Database.Subject_code;
import com.flash.yuvar.flashattendancesystem.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

public class student_join_class_adapter extends  RecyclerView.Adapter<student_join_class_adapter.MyViewHolder>{

    private List<Subject_code> list;
    private int i = 1;
    private DatabaseReference databaseReference;

    private Context ctx;
    private Integer password = 0;
    private Integer verification =0;
    private String m_Text = "";


    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView classname, lecture_name;
        public Button but_join;

        public MyViewHolder(View view) {
            super(view);
            classname= (TextView) view.findViewById(R.id.join_class_name);
            lecture_name = (TextView) view.findViewById(R.id.join_lecture_name);
            but_join = (Button) view.findViewById (R.id.but_join);


        }
    }

    public student_join_class_adapter(List<Subject_code> list) {

        this.list = list;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.adapter_join_class_layout, parent, false);

        this.ctx = parent.getContext ();

        return new MyViewHolder(itemView);
    }



    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {

        final Subject_code subject_code =list.get(position);

        DatabaseReference jLoginDatabase = FirebaseDatabase.getInstance ().getReference("subject_code").child(subject_code.getSubject_id());
        holder.classname.setText(subject_code.getSubject_code());
        holder.lecture_name.setText(subject_code.getLecture_name());


        jLoginDatabase.addValueEventListener (new ValueEventListener( ) {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String classname = dataSnapshot.child("subject_code").getValue().toString();
                String lecturename = dataSnapshot.child("lecture_name").getValue().toString();
                holder.classname.setText ("Class Code : " + classname);
                holder.lecture_name.setText("Lecture Name  : " + lecturename);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        FirebaseUser user = FirebaseAuth.getInstance ().getCurrentUser ();
        String userid = user.getUid ();

        DatabaseReference check = FirebaseDatabase.getInstance ().getReference("request_access");

        check.orderByChild("userID").equalTo(userid).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    for(DataSnapshot ds : dataSnapshot.getChildren()){

                        if(subject_code.getSubject_code().compareTo(ds.child("class_Code").getValue().toString())==0){

                            holder.but_join.setText("Requested");
                            holder.but_join.setClickable(false);




                        }
                    }
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });




        holder.but_join.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {




                databaseReference = FirebaseDatabase.getInstance ().getReference ("request_access");
                FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
                final String request_id = databaseReference.push ().getKey ();
                final String RegisteredUserID = currentUser.getUid();

                final Subject_code subject_code1 =list.get(position);

                AlertDialog.Builder builder = new AlertDialog.Builder(ctx);
                builder.setTitle("Password");

                // Set up the input
                final EditText input = new EditText(ctx);
                // Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
                input.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                builder.setView(input);

                // Set up the buttons
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        m_Text = input.getText().toString();
                        password = Integer.parseInt(m_Text);
                        verification = subject_code1.getPassword();





                        if(password.compareTo(verification)==0){
                            refresh();
                            pushrequestaccess (subject_code1.getSubject_code(),RegisteredUserID,request_id,subject_code1.getSubject_id(),subject_code1.getLecture_name());
                            AlertDialog.Builder builder = new AlertDialog.Builder(ctx);
                            builder.setTitle("Success");
                            builder.setPositiveButton("Okay", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {


                                }
                            });
                            builder.setMessage("Requested");
                            AlertDialog alert1 = builder.create();
                            alert1.show();

                        }else{
                            AlertDialog.Builder builder = new AlertDialog.Builder(ctx);
                            builder.setTitle("Failed");
                            builder.setPositiveButton("End", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {


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

            private void pushrequestaccess(String ClassCode,String RegisteredUserID,String request_id,String classID,String lecture_name) {

                Request_access request_access = new Request_access (ClassCode,RegisteredUserID,request_id,classID,lecture_name);
                databaseReference.child(request_id).setValue (request_access);

            }
        });




    }

    private void refresh() {
        super.notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return list.size();
    }





}
