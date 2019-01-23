package com.flash.yuvar.flashattendancesystem.Student;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import com.flash.yuvar.flashattendancesystem.Database.student_registered_list;
import com.flash.yuvar.flashattendancesystem.Database.students_registered_class;
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
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class Student_Class_Detail extends AppCompatActivity {

    private TextView classcode,studentcount,classescount,ID;



    private PieChart chart;

    private SwipeRefreshLayout swipeRefreshLayout;
    private CountDownTimer timer;
    private Integer originalcount;

    private student_registered_list retrieve;

    private Double percentagetotal=0.0;
    private Integer countingstudenttotal=0;

    private String carriedclasscode,carriedregisteredid;





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student__class__detail);

        carriedclasscode = getIntent().getExtras().getString("CarriedClassName");









        classcode = (TextView)findViewById(R.id.student_class_detail_classcode);
        studentcount = (TextView)findViewById(R.id.student_class_detail_student_Registered);
        classescount = (TextView)findViewById(R.id.student_class_detail_classes);
        chart = (PieChart)findViewById(R.id.student_class_piechart);

        swipeRefreshLayout=(SwipeRefreshLayout)findViewById(R.id.student_class_detail_swipe);

        classcode.setText(carriedclasscode);




        getregisteredid();
        callstudentCount();

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        swipeRefreshLayout.setRefreshing(false);
                        getregisteredid();
                        callstudentCount();




                    }
                },4000);
            }
        });

        timer = new CountDownTimer(100, 1000) {
            @Override
            public void onTick(final long millSecondsLeftToFinish) {

            }

            @Override
            public void onFinish() {
                getregisteredid();
                callstudentCount();


                timer.start();
            }
        };
        timer.start();



        DatabaseReference retrievesubjectpass = FirebaseDatabase.getInstance().getReference("student_registered_class");

        retrievesubjectpass.orderByChild("class_name").equalTo(carriedclasscode).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    for(DataSnapshot ds : dataSnapshot.getChildren()){
                        students_registered_class get = ds.getValue(students_registered_class.class);

                        carriedregisteredid = get.getRegisteredclassID();

                        FirebaseUser user = FirebaseAuth.getInstance ().getCurrentUser ();
                        String userid = user.getUid ();

                        DatabaseReference count = FirebaseDatabase.getInstance().getReference("student_registered_class").child(carriedregisteredid).child("student_list");

                        count.orderByChild("uID").equalTo(userid).addListenerForSingleValueEvent(new ValueEventListener() {
                            Integer count =0;
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                for(DataSnapshot ds : dataSnapshot.getChildren()){
                                    student_registered_list registered_list = ds.getValue(student_registered_list.class);

                                    count = registered_list.getCount();


                                }

                                chart.setUsePercentValues(true);
                                chart.getDescription().setEnabled(false);
                                chart.setExtraOffsets(5,10,5,5);

                                chart.setDragDecelerationFrictionCoef(0.99f);

                                chart.setDrawHoleEnabled(false);
                                chart.setHoleColor(Color.WHITE);
                                chart.setTransparentCircleRadius(61f);

                                ArrayList<PieEntry> values = new ArrayList<>();

                                Integer classc = Integer.parseInt(classescount.getText().toString());

                                Double present = (double)(count)*100/classc;
                                Double absent = 100 - present;

                                //Toast.makeText(Student_Class_Detail.this,present.toString(), Toast.LENGTH_LONG).show();


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
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        classescount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent (getApplicationContext (), Student_attendance_View.class);
                i.putExtra ("CarriedClassName",carriedclasscode);

                startActivity (i);


            }
        });

        studentcount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = new Intent (getApplicationContext (), Student_registered_student_View.class);
                i.putExtra ("CarriedClassName",carriedclasscode);

                startActivity (i);

            }
        });



        //Toast.makeText(Student_Class_Detail.this, carriedregisteredid, Toast.LENGTH_SHORT).show();




    }

    private void getregisteredid() {

        DatabaseReference retrievesubjectpass = FirebaseDatabase.getInstance().getReference("student_registered_class");

        retrievesubjectpass.orderByChild("class_name").equalTo(carriedclasscode).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    for(DataSnapshot ds : dataSnapshot.getChildren()){
                        students_registered_class get = ds.getValue(students_registered_class.class);

                        carriedregisteredid = get.getRegisteredclassID();

                        DatabaseReference classcount = FirebaseDatabase.getInstance().getReference("student_registered_class").child(carriedregisteredid).child("attendance_list");

                        classcount.addListenerForSingleValueEvent(new ValueEventListener() {
                            Integer classnumber =0;
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                                for(DataSnapshot ds: dataSnapshot.getChildren()){
                                    classnumber++;

                                }

                                //Toast.makeText(Student_Class_Detail.this, classnumber.toString(), Toast.LENGTH_SHORT).show();
                                classescount.setText(classnumber.toString());
                            }
                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });





                    }
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }




    private void callstudentCount() {

        DatabaseReference retrievesubjectpass = FirebaseDatabase.getInstance().getReference("student_registered_class");

        retrievesubjectpass.orderByChild("class_name").equalTo(carriedclasscode).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    for(DataSnapshot ds : dataSnapshot.getChildren()){
                        students_registered_class get = ds.getValue(students_registered_class.class);

                        carriedregisteredid = get.getRegisteredclassID();

                        DatabaseReference classcount = FirebaseDatabase.getInstance().getReference("student_registered_class").child(carriedregisteredid).child("student_list");

                        classcount.addListenerForSingleValueEvent(new ValueEventListener() {
                            Integer studentnumber =0;
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                                for(DataSnapshot ds: dataSnapshot.getChildren()){
                                    studentnumber++;

                                }

                                //Toast.makeText(Student_Class_Detail.this, classnumber.toString(), Toast.LENGTH_SHORT).show();
                                studentcount.setText(studentnumber.toString());
                            }
                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });





                    }
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });








    }
}
