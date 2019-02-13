package com.flash.yuvar.flashattendancesystem.Admin;

import android.content.DialogInterface;
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

import com.flash.yuvar.flashattendancesystem.Database.admin_profile_detail;
import com.flash.yuvar.flashattendancesystem.Database.deleteduser;
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



public class DeletedList extends AppCompatActivity {

    private List<String> deleteduser;
    ArrayAdapter<String> adapter;
    com.flash.yuvar.flashattendancesystem.Database.deleteduser retrieve;
    ListView list;

    String adminpass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_deleted_list);

        list = (ListView)findViewById(R.id.deleted_list);

        retrieve = new deleteduser();

        deleteduser = new ArrayList<>();

        adapter = new ArrayAdapter<String> (this,R.layout.subject_info,R.id.subname,deleteduser);


        final DatabaseReference deleteduserretrieve = FirebaseDatabase.getInstance ().getReference ("deleteduser");
        deleteduserretrieve.addValueEventListener (new ValueEventListener( ) {

            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                adapter.clear ();
                deleteduser.clear();


                for(DataSnapshot ds: dataSnapshot.getChildren ()){


                    retrieve = ds.getValue (deleteduser.class);
                    deleteduser.add(retrieve.getEmail());


                }

                adapter.notifyDataSetChanged ();
                list.setAdapter (adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        registerForContextMenu(list);


    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        menu.setHeaderTitle("Choose your option");

        getMenuInflater().inflate(R.menu.listview_menu_update, menu);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {

        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo)item.getMenuInfo();


        switch (item.getItemId()) {
            case R.id.option_done:
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

        AlertDialog.Builder builder = new AlertDialog.Builder(DeletedList.this);
        builder.setTitle("Password");

        // Set up the input
        final EditText input = new EditText(DeletedList.this);
        // Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
        input.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_VARIATION_PASSWORD);
        builder.setView(input);

        // Set up the buttons
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String m_Text = input.getText().toString();



                if(m_Text.compareTo(adminpass)==0){


                    DatabaseReference delete = FirebaseDatabase.getInstance().getReference("deleteduser");

                    delete.orderByChild("email").equalTo(deleteduser.get(position)).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            for(DataSnapshot ds : dataSnapshot.getChildren()){
                                DatabaseReference remove = FirebaseDatabase.getInstance().getReference("deleteduser").child(ds.getKey());
                                remove.removeValue();
                            }

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });






                    AlertDialog.Builder builder = new AlertDialog.Builder(DeletedList.this);
                    builder.setTitle("Success");
                    builder.setPositiveButton("Okay", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {


                        }
                    });

                    builder.setMessage("Done" );
                    AlertDialog alert1 = builder.create();
                    alert1.show();

                }else{
                    AlertDialog.Builder builder = new AlertDialog.Builder(DeletedList.this);
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
