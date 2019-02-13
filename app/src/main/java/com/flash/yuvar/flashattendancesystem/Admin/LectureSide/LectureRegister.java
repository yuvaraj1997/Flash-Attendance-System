package com.flash.yuvar.flashattendancesystem.Admin.LectureSide;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;

import com.flash.yuvar.flashattendancesystem.Database.admin_profile_detail;
import com.flash.yuvar.flashattendancesystem.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class LectureRegister extends AppCompatActivity {

    EditText name,email;


    Button submit;
    private ProgressBar loading;
    String password,type;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lecture_register);

        name = (EditText)findViewById(R.id.admin_register_lecturename);

        email = (EditText)findViewById(R.id.admin_register_lectureemailaddress);


        submit = (Button)findViewById(R.id.button_admin_register_lecture_submit);

        loading = findViewById(R.id.admin_lecture_register_loading);


        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loading.setVisibility (View.VISIBLE);
                final String lecturename = name.getText().toString().trim();

                final String lectureemail = email.getText().toString().trim();





                if(lectureemail.isEmpty()){
                    email.setError ("Email Required");
                    email.requestFocus ();
                    loading.setVisibility (View.GONE);
                    return;
                }

                if(lecturename.isEmpty()){
                    name.setError ("Name Required");
                    name.requestFocus ();
                    loading.setVisibility (View.GONE);
                    return;
                }
                final FirebaseAuth mauth = FirebaseAuth.getInstance();


                mauth.createUserWithEmailAndPassword(lectureemail,"test1234").addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                    @Override
                    public void onSuccess(AuthResult authResult) {

                        String userid = mauth.getUid();

                        DatabaseReference push = FirebaseDatabase.getInstance().getReference("users").child(userid);

                        admin_profile_detail admin = new admin_profile_detail(lectureemail,lecturename,"1234","lecture");

                        push.setValue(admin);

                        mauth.signOut();
                        finish();
                        //Toast.makeText(AdminRegisterStudent.this,mauth.getUid().toString(),Toast.LENGTH_LONG).show();

                    }
                });







            }
        });

    }
}
