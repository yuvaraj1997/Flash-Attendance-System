package com.flash.yuvar.flashattendancesystem;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class LoginActivity extends AppCompatActivity {


    private FirebaseAuth firebaseAuth;
    private EditText studentID,studentpassword;
    private Button btn_signin;
    private ProgressBar loading;

    private int counter = 5;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate (savedInstanceState);
        setContentView (R.layout.activity_login);



        studentID = findViewById (R.id.student_id);
        studentpassword = findViewById (R.id.password);
        btn_signin = findViewById (R.id.login);
        loading = findViewById (R.id.loading);










        firebaseAuth = FirebaseAuth.getInstance();


        FirebaseUser user = firebaseAuth.getCurrentUser();


        
        btn_signin.setOnClickListener (new View.OnClickListener ( ) {
            @Override
            public void onClick(View v) {
                loading.setVisibility (View.VISIBLE);
                String email = studentID.getText ().toString ().trim ();
                String pass = studentpassword.getText ().toString ().trim ();
                if(email.isEmpty ()){
                    studentID.setError ("Email Required");
                    studentID.requestFocus ();
                    loading.setVisibility (View.GONE);
                    return;


                }

                if(pass.isEmpty()){
                    studentpassword.setError ("Password Required");
                    studentpassword.requestFocus ();
                    loading.setVisibility (View.GONE);
                    return;


                }
                validate(studentID.getText ().toString (),studentpassword.getText ().toString ());
            }
        });

    }

    private void validate(String id, String pass) {



        firebaseAuth.signInWithEmailAndPassword (id,pass).addOnCompleteListener (new OnCompleteListener<AuthResult> ( ) {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful ()){

                    FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
                    String RegisteredUserID = currentUser.getUid();

                    DatabaseReference jLoginDatabase = FirebaseDatabase.getInstance ( ).getReference ( ).child ("users").child (RegisteredUserID);

                    jLoginDatabase.addValueEventListener (new ValueEventListener ( ) {

                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            String userType = dataSnapshot.child("type").getValue().toString();
                            if(userType.equals("student")){
                                Intent intentStudent = new Intent(LoginActivity.this, StudentMainActivity.class);
                                intentStudent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                startActivity(intentStudent);
                                finish();
                            }else if(userType.equals("lecture")){
                                Intent intentLecture = new Intent(LoginActivity.this, LectureMainActivity.class);
                                intentLecture.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                startActivity(intentLecture);
                                finish();
                            }else if(userType.equals("admin")){
                                Intent intentAdmin = new Intent(LoginActivity.this, AdminMainActivity.class);
                                intentAdmin.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                startActivity(intentAdmin);
                                finish();
                            }else{
                                Toast.makeText(LoginActivity.this, "Failed Login. Please Try Again", Toast.LENGTH_SHORT).show();
                                return;
                            }

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });


                    //Toast.makeText(MainActivity.this, "Login Successful", Toast.LENGTH_SHORT).show();
                    //startActivity(new Intent(LoginActivity.this, StudentMainActivity.class));
                }else{
                    loading.setVisibility (View.GONE);
                    Toast.makeText(LoginActivity.this, "Login Failed", Toast.LENGTH_SHORT).show();


                    if(counter == 0){
                        btn_signin.setEnabled(false);
                    }
                }
            }
        });
    }

    private void checkEmailVerification() {
        FirebaseUser firebaseUser = firebaseAuth.getInstance().getCurrentUser();
        Boolean emailflag = firebaseUser.isEmailVerified();



//        if(emailflag){
//            finish();
//            startActivity(new Intent(MainActivity.this, SecondActivity.class));
//        }else{
//            Toast.makeText(this, "Verify your email", Toast.LENGTH_SHORT).show();
//            firebaseAuth.signOut();
//        }
    }


}













