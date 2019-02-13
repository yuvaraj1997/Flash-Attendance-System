package com.flash.yuvar.flashattendancesystem.Admin.ClassSide;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.flash.yuvar.flashattendancesystem.Database.Request_access;
import com.flash.yuvar.flashattendancesystem.Database.Subject_code;
import com.flash.yuvar.flashattendancesystem.Database.admin_profile_detail;
import com.flash.yuvar.flashattendancesystem.Database.student_registered_list;
import com.flash.yuvar.flashattendancesystem.Database.students_registered_class;
import com.flash.yuvar.flashattendancesystem.Lecture.Lecture_class_listDate_Activity;
import com.flash.yuvar.flashattendancesystem.Lecture.Lecture_request_acceptance;
import com.flash.yuvar.flashattendancesystem.QRCode.QRCode_Generate_Activity;
import com.flash.yuvar.flashattendancesystem.R;
import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class Admin_Class_Detail extends AppCompatActivity {

    private String carriedclasscode,carriedregisteredid;

    private TextView requested,classcode,studentcount,classescount;

    private Button viewclasses,classpassword;

    private String Classcode,adminpass,classpass;

    private PieChart chart;

    private SwipeRefreshLayout swipeRefreshLayout;
    CountDownTimer timer;
    private Integer originalcount;

    private student_registered_list retrieve;

    private Double percentagetotal=0.0;
    private Integer countingstudenttotal=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin__class__detail);


        carriedclasscode = getIntent ().getExtras ().getString ("CarriedClassName");

        carriedregisteredid = getIntent ().getExtras ().getString ("CarriedRegisteredClassID");

        requested = (TextView)findViewById(R.id.admin_class_detail__requested);
        classcode = (TextView)findViewById(R.id.admin_class_detail__classcode);
        studentcount = (TextView)findViewById(R.id.admin_class_detail__student_Registered);
        classescount = (TextView)findViewById(R.id.admin_class_detail__classes);
        chart = (PieChart)findViewById(R.id.admin_class_detail_classdetailchart);

        swipeRefreshLayout=(SwipeRefreshLayout)findViewById(R.id.admin_class_detail_swipe);

        classpassword = (Button)findViewById(R.id.button_admin_class_detail__view_password);
        viewclasses = (Button)findViewById(R.id.button_admin_class_detail__view_classes);






        Piechart();
        callclassdetail();
        callclassCount();
        callstudentCount();
        callRequestCount();










        requested.setOnClickListener (new View.OnClickListener ( ) {
            @Override
            public void onClick(View v) {
                requestlist();

            }
        });

        classescount.setOnClickListener (new View.OnClickListener ( ) {
            @Override
            public void onClick(View v) {
                Intent i = new Intent (getApplicationContext (), Lecture_class_listDate_Activity.class);

                i.putExtra ("CarriedClassName",carriedclasscode);
                i.putExtra ("CarriedRegisteredClassID",carriedregisteredid);
                startActivity (i);

            }
        });


        viewclasses.setOnClickListener (new View.OnClickListener ( ) {
            @Override
            public void onClick(View v) {
                Intent i = new Intent (getApplicationContext (), QRCode_Generate_Activity.class);
                i.putExtra ("CarriedClassName",carriedclasscode);
                i.putExtra ("CarriedRegisteredClassID",carriedregisteredid);
                startActivity (i);

            }
        });


        studentcount.setOnClickListener (new View.OnClickListener ( ) {
            @Override
            public void onClick(View v) {
                DatabaseReference retrieve = FirebaseDatabase.getInstance().getReference("student_registered_class").child(carriedregisteredid)
                        .child("student_list");

                Intent i = new Intent (getApplicationContext (), Admin_Student_List.class);

                i.putExtra ("CarriedRegisteredClassID",carriedregisteredid);
                startActivity (i);





            }
        });

        classpassword.setOnClickListener (new View.OnClickListener ( ) {
            @Override
            public void onClick(View v) {

                FirebaseUser user = FirebaseAuth.getInstance ().getCurrentUser ();
                String userid = user.getUid ();





                DatabaseReference retrieveadminpass = FirebaseDatabase.getInstance().getReference("users").child(userid);

                retrieveadminpass.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                        admin_profile_detail admin = dataSnapshot.getValue(admin_profile_detail.class);

                        adminpass = admin.getPass();


                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

                DatabaseReference retrievesubjectpass = FirebaseDatabase.getInstance().getReference("subject_code");

                retrievesubjectpass.orderByChild("subject_code").equalTo(carriedclasscode).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if(dataSnapshot.exists()){
                            for(DataSnapshot ds : dataSnapshot.getChildren()){
                                Subject_code subjectCode = ds.getValue(Subject_code.class);
                                Integer pass = subjectCode.getPassword();

                                classpass = pass.toString();

                            }
                        }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });



                AlertDialog.Builder builder = new AlertDialog.Builder(Admin_Class_Detail.this,R.style.AlertDialogStyle);
                builder.setTitle("Password");

                // Set up the input
                final EditText input = new EditText(Admin_Class_Detail.this);
                // Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
                input.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_VARIATION_PASSWORD);
                builder.setView(input);

                // Set up the buttons
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String m_Text = input.getText().toString();



                        if(m_Text.compareTo(adminpass)==0){


                            AlertDialog.Builder builder = new AlertDialog.Builder(Admin_Class_Detail.this,R.style.AlertDialogStyle);
                            builder.setTitle("Success");
                            builder.setPositiveButton("Okay", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {


                                }
                            });

                            builder.setMessage("Class Password = " + classpass);
                            AlertDialog alert1 = builder.create();
                            alert1.show();

                        }else{
                            AlertDialog.Builder builder = new AlertDialog.Builder(Admin_Class_Detail.this,R.style.AlertDialogStyle);
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


        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        swipeRefreshLayout.setRefreshing(false);
                        callclassdetail();
                        callclassCount();
                        callstudentCount();
                        callRequestCount();




                    }
                },4000);
            }
        });





    }

    private void Piechart() {

        retrieve  = new student_registered_list();



        DatabaseReference ref2 = FirebaseDatabase.getInstance().getReference("student_registered_class").child(carriedregisteredid).child("attendance_list");

        ref2.addListenerForSingleValueEvent(new ValueEventListener() {
            Integer count=0;
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot ds: dataSnapshot.getChildren()){

                    count++;
                }

                setOriginalCount(count);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("student_registered_class").child(carriedregisteredid).child("student_list");

        Query query = ref.orderByChild("name");
        query.addValueEventListener (new ValueEventListener( ) {
            Double percentagedecimal = 0.0;
            Integer countingstudent = 0;

            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for(DataSnapshot ds: dataSnapshot.getChildren ()){

                    retrieve = ds.getValue (student_registered_list.class);

                    Double percentage = ((double) retrieve.getCount()/originalcount) * 100;

                    student_registered_list list= new student_registered_list (retrieve.getuID(), retrieve.getName(),percentage,carriedregisteredid);


                    this.percentagedecimal += (double)(percentage/100);


                    countingstudent++;



                }

                chart.setUsePercentValues(true);
                chart.getDescription().setEnabled(false);
                chart.setExtraOffsets(5,10,5,5);

                chart.setDragDecelerationFrictionCoef(0.99f);

                chart.setDrawHoleEnabled(false);
                chart.setHoleColor(Color.WHITE);
                chart.setTransparentCircleRadius(61f);

                ArrayList<PieEntry> values = new ArrayList<>();

                Double present = (percentagedecimal/countingstudent)*100;
                Double absent = 100 - present;

                //Toast.makeText(Admin_Class_Detail.this,present.toString(),Toast.LENGTH_LONG).show();


                values.add(new PieEntry(present.floatValue(),"Present"));
                values.add(new PieEntry(absent.floatValue(),"Absent"));

                chart.animateY(1000, Easing.EasingOption.EaseInOutCubic);

                PieDataSet dataSet = new PieDataSet(values,"Performance Of The Class");
                dataSet.setSliceSpace(3f);
                dataSet.setSelectionShift(5f);
                dataSet.setColors(ColorTemplate.JOYFUL_COLORS);

                PieData data = new PieData((dataSet));
                data.setValueTextSize(10f);
                data.setValueTextColor(Color.YELLOW);

                chart.setData(data);




            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });





    }



    private void setOriginalCount(Integer count) {
        this.originalcount = count;
    }

    private void callRequestCount() {
        DatabaseReference requestaccess = FirebaseDatabase.getInstance().getReference ("request_access");

        requestaccess.addListenerForSingleValueEvent(new ValueEventListener() {
            Integer request =0;
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot ds: dataSnapshot.getChildren()){
                    Request_access retrieve = ds.getValue (Request_access.class);
                    if(classcode.getText().toString().equals(retrieve.getClass_Code())){
                        request++;
                    }
                }
                requested.setText(request.toString());
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });

    }

    private void callstudentCount() {

        DatabaseReference students = FirebaseDatabase.getInstance().getReference("student_registered_class").child(carriedregisteredid).child("student_list");

        students.addListenerForSingleValueEvent(new ValueEventListener() {
            Integer studentnumber =0;
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot ds:dataSnapshot.getChildren()){

                    studentnumber++;

                }

                studentcount.setText(studentnumber.toString());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    private void callclassCount() {
        DatabaseReference classcount = FirebaseDatabase.getInstance().getReference("student_registered_class").child(carriedregisteredid).child("attendance_list");

        classcount.addListenerForSingleValueEvent(new ValueEventListener() {
            Integer classnumber =0;
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot ds: dataSnapshot.getChildren()){
                    classnumber++;
                }
                classescount.setText(classnumber.toString());
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void callclassdetail() {
        DatabaseReference classdetail = FirebaseDatabase.getInstance().getReference("student_registered_class").child(carriedregisteredid);

        classdetail.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){

                    students_registered_class registered = dataSnapshot.getValue(students_registered_class.class);

                    classcode.setText(registered.getClass_name());

                    setClasscode(classcode.toString());


                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void setClasscode(String s) {
        this.Classcode = s;
    }

    private void requestlist() {
        Intent i = new Intent (getApplicationContext (), Lecture_request_acceptance.class);
        i.putExtra ("CarriedClassName",carriedclasscode);
        startActivity (i);
    }
}
