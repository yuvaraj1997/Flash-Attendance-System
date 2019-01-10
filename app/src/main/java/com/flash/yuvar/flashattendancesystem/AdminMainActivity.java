package com.flash.yuvar.flashattendancesystem;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.flash.yuvar.flashattendancesystem.Admin.Admin_view_class;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class AdminMainActivity extends AppCompatActivity {
    private TextView noofclass,noofstudents,nooflectures;
    private Button logout;
    private FirebaseAuth firebaseAuth;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate (savedInstanceState);
        setContentView (R.layout.activity_admin_main);


        firebaseAuth = FirebaseAuth.getInstance();

        if (firebaseAuth.getCurrentUser ()==null){
            finish ();
            startActivity (new Intent (this,LoginActivity.class));
        }

        noofclass = (TextView)findViewById(R.id.admin_all_registered_class);
        noofstudents = (TextView)findViewById(R.id.admin_all_students);
        nooflectures = (TextView)findViewById(R.id.admin_all_lectures);






        logout = findViewById (R.id.logout);



        DatabaseReference classcount = FirebaseDatabase.getInstance().getReference("subject_code");

        classcount.addListenerForSingleValueEvent(new ValueEventListener() {

            Integer count=0;
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                noofclass.animate();
                for(DataSnapshot ds:dataSnapshot.getChildren()){
                    count++;

                }
                noofclass.setText(count.toString());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });






        noofclass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewclass();
            }
        });








        logout.setOnClickListener (new View.OnClickListener ( ) {
            @Override
            public void onClick(View v) {
                Logout();
            }
        });









    }



    private void viewclass() {

        startActivity(new Intent (AdminMainActivity.this, Admin_view_class.class));

    }



    private void Logout(){
        firebaseAuth.signOut();
        finish();
        startActivity(new Intent (AdminMainActivity.this, LoginActivity.class));
    }
}
