package com.flash.yuvar.flashattendancesystem.Adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.flash.yuvar.flashattendancesystem.Database.Request_access;
import com.flash.yuvar.flashattendancesystem.Database.student_registered_list;
import com.flash.yuvar.flashattendancesystem.Database.students_registered_class;
import com.flash.yuvar.flashattendancesystem.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

public class custom_request_adapter extends  RecyclerView.Adapter<custom_request_adapter.MyViewHolder> {

    private List<Request_access>requestList;
    private DatabaseReference databaseReference;
    private String classcode;
    private students_registered_class registered_class;
    private student_registered_list registered_student_list;
    private int i = 1;



    private Context ctx;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView classcode, student_name, student_id;
        public Button but_accept,but_delete;

        public MyViewHolder(View view) {
            super(view);
            classcode= (TextView) view.findViewById(R.id.classcode);
            student_name = (TextView) view.findViewById(R.id.stud_name);
            student_id = (TextView) view.findViewById(R.id.student_id);
            but_delete = (Button) view.findViewById (R.id.button_delete);
            but_accept = (Button) view.findViewById (R.id.button_accept);


        }
    }


    public custom_request_adapter(List<Request_access>requestList) {



        this.requestList = requestList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.adapter_view_layout, parent, false);

        this.ctx = parent.getContext ();

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {


        final Request_access request_access =requestList.get(position);




        DatabaseReference jLoginDatabase = FirebaseDatabase.getInstance ( ).getReference ( ).child ("users").child (request_access.getUserID ());
        holder.classcode.setText(request_access.getClass_Code ());
        holder.student_name.setText(request_access.getUserID ());









        jLoginDatabase.addValueEventListener (new ValueEventListener ( ) {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String userType = dataSnapshot.child("name").getValue().toString();
                String studentid = dataSnapshot.child("student_id").getValue().toString();
                holder.student_name.setText ("Name: " + userType);
                holder.student_id.setText("ID : " + studentid);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        holder.but_delete.setOnClickListener (new View.OnClickListener ( ) {
            @Override
            public void onClick(View v) {

                Request_access request_access =requestList.get(position);
                DatabaseReference jLoginDatabase = FirebaseDatabase.getInstance ( ).getReference ( ).child ("request_access").child (request_access.getRequest_id ());
                jLoginDatabase.removeValue ();
                requestList.remove (position);
                notifyDataSetChanged ();
                requestList.clear ();


            }
        });



        holder.but_accept.setOnClickListener (new View.OnClickListener ( ) {
            private String registeredID;

            @Override
            public void onClick(View v) {

                databaseReference = FirebaseDatabase.getInstance ().getReference ("student_registered_class");


                Request_access request_access =requestList.get(position);
                final String registered_class_id = databaseReference.push ().getKey ();
                final String class_Code = request_access.getClass_Code ();
                final String class_id = request_access.getClassID ();
                final String uID = request_access.getUserID ();
                final String lecture_name = request_access.getLecture_name();






                databaseReference.orderByChild ("classID").equalTo (class_id).addListenerForSingleValueEvent (new ValueEventListener ( ) {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if(dataSnapshot.exists()){

                            DatabaseReference jLoginDatabase = FirebaseDatabase.getInstance ( ).getReference ( ).child ("users").child (uID);
                            jLoginDatabase.addValueEventListener (new ValueEventListener ( ) {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    final String uID = dataSnapshot.getKey ().toString ();
                                    final String student_name = dataSnapshot.child("name").getValue().toString();

                                    DatabaseReference getregisteredclass = FirebaseDatabase.getInstance ( ).getReference ( ).child ("student_registered_class");

                                    getregisteredclass.addListenerForSingleValueEvent (new ValueEventListener ( ) {
                                        private String datasnapshotregisteredid;

                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                            for(DataSnapshot ds: dataSnapshot.getChildren ()){
                                                Toast.makeText(ctx,registeredID,Toast.LENGTH_LONG).show();

                                                registered_class = ds.getValue (students_registered_class.class);


                                                if(class_id .equals (registered_class.getClassID ())){
                                                    this.datasnapshotregisteredid = registered_class.getRegisteredclassID ();
                                                    pushstudent(uID,student_name,datasnapshotregisteredid);



                                                }


                                            }

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
                        else{
                            pushregisteredclass (class_Code,class_id,registered_class_id,lecture_name);

                            DatabaseReference jLoginDatabase = FirebaseDatabase.getInstance ( ).getReference ( ).child ("users").child (uID);
                            jLoginDatabase.addValueEventListener (new ValueEventListener ( ) {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    String uID = dataSnapshot.getKey ().toString ();
                                    String student_name = dataSnapshot.child("name").getValue().toString();
                                    pushstudent(uID,student_name,registered_class_id);
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





                Request_access request_access2 =requestList.get(position);
                DatabaseReference remove = FirebaseDatabase.getInstance ( ).getReference ( ).child ("request_access").child (request_access2.getRequest_id ());
                remove.removeValue ();
                requestList.remove (position);
                notifyDataSetChanged ();
                requestList.clear ();












            }
        });


    }


    private void pushstudent(String uID, final String student_name, final String registeredID) {

        final student_registered_list registered_list = new student_registered_list (uID,student_name,0);

        DatabaseReference extranode = FirebaseDatabase.getInstance ().getReference ("student_registered_class");






        extranode.orderByChild ("registeredclassID").equalTo (registeredID).addListenerForSingleValueEvent (new ValueEventListener ( ) {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists ()){

                    DatabaseReference checkduplicatedata = FirebaseDatabase.getInstance ().getReference ("student_registered_class").child(registeredID).child("student_list");
                    checkduplicatedata.addListenerForSingleValueEvent (new ValueEventListener ( ) {
                        String text = "empty";
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            for(DataSnapshot ds: dataSnapshot.getChildren ()){

                                registered_student_list = ds.getValue (student_registered_list.class);

                                if(student_name.equals (registered_student_list.getName ())){

                                    this.text = registered_student_list.getName ().toString ();

                                }




                            }
                            if(text.equals ("empty")){
                                DatabaseReference ref = FirebaseDatabase.getInstance ().getReference ("student_registered_class").child(registeredID).child("student_list").push ();

                                ref.setValue (registered_list);


                                Toast.makeText(ctx, "SuccessFull! Added" ,Toast.LENGTH_SHORT).show();

                            }
                            else{

                                Toast.makeText(ctx, "Student Exist! " + registered_student_list.getName (),Toast.LENGTH_SHORT).show();
                            }


                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });





                }
                else{
                    Toast.makeText(ctx, "Class ID not Exist!" ,Toast.LENGTH_SHORT).show();



                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        //int firstSpace = student_name.indexOf(" "); // detect the first space character
       // String firstName = student_name.substring(0, firstSpace);  // get everything upto the first space character

        //final String push_student_id = extranode.push ().getKey ();


        //extranode.child(push_student_id).setValue (registered_list);










    }

    private void pushregisteredclass(String classcode, String classid, String registeredclassID,String lecture_name) {



        students_registered_class registered_class = new students_registered_class (classcode,classid,registeredclassID,lecture_name);
        this.classcode=classcode;


        databaseReference.child(registeredclassID).setValue (registered_class);




    }

    @Override
    public int getItemCount() {

        return requestList.size();
    }
}
