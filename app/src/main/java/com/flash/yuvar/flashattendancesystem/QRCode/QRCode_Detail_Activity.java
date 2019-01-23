package com.flash.yuvar.flashattendancesystem.QRCode;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.flash.yuvar.flashattendancesystem.Database.lecture_profile_detail;
import com.flash.yuvar.flashattendancesystem.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;


public class QRCode_Detail_Activity extends AppCompatActivity implements View.OnClickListener {

    public TextView classcodedetail,dateandtimedetail;
    public ImageView qrcodedetail;
    public String classcode;

    public String imageUrl,dateandtime;

    Button endclass;
    private String registrationid;
    private String verification;
    private String lecturepass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qrcode__detail_);


        classcodedetail = (TextView) findViewById(R.id.class_code_detail_view);
        dateandtimedetail = (TextView) findViewById(R.id.date_and_time_detail_view);
        qrcodedetail = (ImageView) findViewById(R.id.qr_code_detail_view);
        endclass = (Button)findViewById(R.id.button_end);


        imageUrl = getIntent().getStringExtra("image_url");
        dateandtime = getIntent().getStringExtra("dateandtime");



        int position = dateandtime.indexOf(" ");

        classcode = dateandtime.substring(0,position);
        String date = dateandtime.substring(position,dateandtime.length());



        dateandtimedetail.setText(date);
        classcodedetail.setText(classcode);

        Picasso.get()
                .load(imageUrl)
                .placeholder(R.drawable.error)
                .fit()
                .centerInside()
                .into(qrcodedetail);


        qrcodedetail.setOnClickListener(this);

        endclass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                FirebaseUser user = FirebaseAuth.getInstance ().getCurrentUser ();
                final String userid = user.getUid ();

                final FirebaseDatabase database = FirebaseDatabase.getInstance ();
                DatabaseReference myref = database.getReference ("users").child(userid);


                myref.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {


                        if(dataSnapshot.exists()){

                            lecture_profile_detail lecture = dataSnapshot.getValue(lecture_profile_detail.class);


                            setPassword(lecture.getPass());


                        }



                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        Toast.makeText (QRCode_Detail_Activity.this,databaseError.getCode (),Toast.LENGTH_SHORT ).show ();

                    }
                });


                AlertDialog.Builder builder = new AlertDialog.Builder(QRCode_Detail_Activity.this);
                builder.setTitle("Password");

                // Set up the input
                final EditText input = new EditText(QRCode_Detail_Activity.this);
                // Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
                input.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                builder.setView(input);

                // Set up the buttons
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String m_Text = input.getText().toString();



                        if(m_Text.compareTo(lecturepass)==0){


                            AlertDialog.Builder builder = new AlertDialog.Builder(QRCode_Detail_Activity.this);
                            builder.setTitle("Success");
                            builder.setPositiveButton("Okay", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    Intent intent = new Intent(QRCode_Detail_Activity.this, QR_Code_End_Activity.class);


                                    intent.putExtra("registrationid", registrationid);
                                    intent.putExtra("dateandtime", dateandtime);
                                    intent.putExtra("classcode", classcode);

                                    startActivity(intent);


                                }
                            });
                            builder.setMessage("Access Granted");
                            AlertDialog alert1 = builder.create();
                            alert1.show();

                        }else{
                            AlertDialog.Builder builder = new AlertDialog.Builder(QRCode_Detail_Activity.this);
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

    private void setPassword(String pass) {
        this.lecturepass = pass;
    }


    @Override
    public void onClick(View v) {

        Intent intent = new Intent(this, QRCode_Bigger_Activity.class);
        intent.putExtra("image_url", imageUrl);
        intent.putExtra("classcode", classcode);

        startActivity(intent);

    }
}
