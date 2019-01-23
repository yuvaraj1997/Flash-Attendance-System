package com.flash.yuvar.flashattendancesystem.Lecture;

import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import com.flash.yuvar.flashattendancesystem.Adapters.student_absentclass_adapter;
import com.flash.yuvar.flashattendancesystem.Adapters.student_inclass_adapter;
import com.flash.yuvar.flashattendancesystem.Database.student_registered_list;
import com.flash.yuvar.flashattendancesystem.R;
import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class Lecture_Class_listAttendees_Activity extends AppCompatActivity {
    DatabaseReference studentlist;
    String carriedAttendeeID,carriedregisteredid;


    CountDownTimer timer;

    private List<student_registered_list> inclasslist = new ArrayList<>();
    private List<student_registered_list> absentlist = new ArrayList<>();


    private List<String> name = new ArrayList<>();

    private student_inclass_adapter InclassAdapter;
    private student_absentclass_adapter AbsentAdapter;

    private RecyclerView recyclerViewabsent,recyclerViewinclass;

    student_registered_list retrieve;
    private PieChart chart;
    private Integer totalstudent=0;

    private Integer totalstudentcount=0;
    private Integer mccount=0;
    private Integer othercount=0;
    private Integer latecount=0;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate (savedInstanceState);
        setContentView (R.layout.activity_lecture__class_list_attendees_);

        carriedAttendeeID = getIntent ().getExtras ().getString ("CarriedAttendeessID");
        carriedregisteredid = getIntent ().getExtras ().getString ("CarriedRegisteredClassID");



        retrieve = new student_registered_list ();




        recyclerViewinclass = (RecyclerView) findViewById (R.id.recycler_view_attendance);
        recyclerViewabsent = (RecyclerView) findViewById (R.id.recycler_view_absent);
        chart = (PieChart)findViewById(R.id.inclassstatistic) ;


        recyclerViewinclass.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));
        recyclerViewabsent.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));




        inClass();
        absentclass();
        chart();












    }

    private void chart() {


        DatabaseReference studentcount = FirebaseDatabase.getInstance ().getReference ("student_registered_class").child(carriedregisteredid).child("student_list");
        Query query1 = studentcount.orderByChild("name");
        query1.addValueEventListener (new ValueEventListener( ) {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot ds: dataSnapshot.getChildren ()){
                    totalstudent++;
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });


        studentlist = FirebaseDatabase.getInstance ().getReference ("student_registered_class").child(carriedregisteredid).child("attendance_list")
                .child (carriedAttendeeID).child("attendees");
        Query query = studentlist.orderByChild("name");
        query.addValueEventListener (new ValueEventListener( ) {






            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for(DataSnapshot ds: dataSnapshot.getChildren ()){
                    retrieve = ds.getValue(student_registered_list.class);

                    if(retrieve.getReason().compareTo("MC")==0){
                        mccount++;



                    }else if(retrieve.getReason().compareTo("Absent With Reason")==0){
                        othercount++;
                    }
                    else if(retrieve.getReason().compareTo("Present")==0){
                        totalstudentcount++;
                    }
                    else if(retrieve.getReason().compareTo("Late To Class")==0){
                        latecount++;
                    }




                }

                Double mc = (double) (mccount)*100 /totalstudent;
                Double Late = (double)(latecount)*100/totalstudent;
                Double other = (double)(othercount)*100/totalstudent;
                Double Presentwithoutreason = (double)(totalstudentcount)*100/totalstudent;
                Double Absent = 100 - mc - Late - other - Presentwithoutreason;


                Toast.makeText(Lecture_Class_listAttendees_Activity.this, mc.toString(),Toast.LENGTH_LONG).show();

                chart.setUsePercentValues(true);
                chart.getDescription().setEnabled(false);
                chart.setExtraOffsets(5,10,5,5);

                chart.setDragDecelerationFrictionCoef(0.99f);

                chart.setDrawHoleEnabled(false);
                chart.setHoleColor(Color.WHITE);
                chart.setTransparentCircleRadius(61f);


                ArrayList<PieEntry> values = new ArrayList<>();

                values.add(new PieEntry(mc.floatValue(),"MC"));
                values.add(new PieEntry(Late.floatValue(),"Late To Class"));
                values.add(new PieEntry(other.floatValue(),"Other Reason"));
                values.add(new PieEntry(Presentwithoutreason.floatValue(),"Present"));
                values.add(new PieEntry(Absent.floatValue(),"Absent"));

                chart.animateY(1000, Easing.EasingOption.EaseInOutCubic);

                PieDataSet dataSet = new PieDataSet(values,"");
                dataSet.setSliceSpace(3f);
                dataSet.setSelectionShift(5f);
                dataSet.setColors(ColorTemplate.PASTEL_COLORS);

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

    private void absentclass() {

        DatabaseReference all = FirebaseDatabase.getInstance ().getReference ("student_registered_class").child(carriedregisteredid).child("student_list");
        Query query2 = all.orderByChild("name");
        query2.addValueEventListener (new ValueEventListener( ) {

            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                absentlist.clear();

                for(DataSnapshot ds: dataSnapshot.getChildren ()){


                    retrieve = ds.getValue (student_registered_list.class);


                    final student_registered_list inclass= new student_registered_list (retrieve.getuID(),retrieve.getName(),carriedregisteredid,carriedAttendeeID,0);

                    String check = "";
                    for(int i =0;i<name.size();i++){
                        if(name.get(i).equals(retrieve.getName())){
                            check = "got";


                        }
                    }

                    if(check.equals("")){
                        absentlist.add(inclass);

                    }

                }

                AbsentAdapter = new student_absentclass_adapter(absentlist);
                RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager (getApplicationContext());
                recyclerViewabsent.setLayoutManager(mLayoutManager);
                recyclerViewabsent.setItemAnimator(new DefaultItemAnimator());
                recyclerViewabsent.setAdapter(AbsentAdapter);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    private void inClass() {



        studentlist = FirebaseDatabase.getInstance ().getReference ("student_registered_class").child(carriedregisteredid).child("attendance_list")
                .child (carriedAttendeeID).child("attendees");
        Query query = studentlist.orderByChild("name");
        query.addValueEventListener (new ValueEventListener( ) {

            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                inclasslist.clear();



                for(DataSnapshot ds: dataSnapshot.getChildren ()){


                    retrieve = ds.getValue (student_registered_list.class);


                    student_registered_list inclass= new student_registered_list (retrieve.getuID(),retrieve.getName(),carriedregisteredid,carriedAttendeeID,0);

                    inclasslist.add(inclass);
                    name.add(retrieve.getName());

                }

                InclassAdapter = new student_inclass_adapter(inclasslist);
                RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager (getApplicationContext());
                recyclerViewinclass.setLayoutManager(mLayoutManager);
                recyclerViewinclass.setItemAnimator(new DefaultItemAnimator());
                recyclerViewinclass.setAdapter(InclassAdapter);


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
