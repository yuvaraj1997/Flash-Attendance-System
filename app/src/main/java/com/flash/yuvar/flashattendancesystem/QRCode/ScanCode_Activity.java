package com.flash.yuvar.flashattendancesystem.QRCode;

import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.hardware.Camera;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import com.flash.yuvar.flashattendancesystem.Database.attendance_list_push_qr;
import com.flash.yuvar.flashattendancesystem.Database.student_registered_list;
import com.flash.yuvar.flashattendancesystem.Database.students_registered_class;
import com.flash.yuvar.flashattendancesystem.R;
import com.flash.yuvar.flashattendancesystem.UserProfile;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.zxing.Result;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import me.dm7.barcodescanner.zxing.ZXingScannerView;

import static android.Manifest.permission.CAMERA;

public class ScanCode_Activity extends AppCompatActivity implements ZXingScannerView.ResultHandler {

    private static final int REQUEST_CAMERA = 1;
    private ZXingScannerView scannerView;
    private static int camId = Camera.CameraInfo.CAMERA_FACING_BACK;
    private student_registered_list registered_student_list;
    private String name;
    private String attendance_id;
    private String attendance_list = "attendance_list";

    public String end;
    public String dateandtimeid;
    public String attendeedpushedid;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        scannerView = new ZXingScannerView(this);
        setContentView(scannerView);
        int currentApiVersion = Build.VERSION.SDK_INT;

        if (currentApiVersion >= Build.VERSION_CODES.M) {
            if (checkPermission()) {
                Toast.makeText(getApplicationContext(), "Permission already granted!", Toast.LENGTH_LONG).show();
            } else {
                requestPermission();
            }
        }


    }

    private boolean checkPermission() {
        return (ContextCompat.checkSelfPermission(getApplicationContext(), CAMERA) == PackageManager.PERMISSION_GRANTED);
    }

    private void requestPermission() {
        ActivityCompat.requestPermissions(this, new String[]{CAMERA}, REQUEST_CAMERA);
    }

    @Override
    public void onResume() {
        super.onResume();

        int currentapiVersion = android.os.Build.VERSION.SDK_INT;
        if (currentapiVersion >= android.os.Build.VERSION_CODES.M) {
            if (checkPermission()) {
                if (scannerView == null) {
                    scannerView = new ZXingScannerView(this);
                    setContentView(scannerView);
                }
                scannerView.setResultHandler(this);
                scannerView.startCamera();
            } else {
                requestPermission();
            }
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        scannerView.stopCamera();
    }

    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case REQUEST_CAMERA:
                if (grantResults.length > 0) {

                    boolean cameraAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                    if (cameraAccepted) {
                        Toast.makeText(getApplicationContext(), "Permission Granted, Now you can access camera", Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(getApplicationContext(), "Permission Denied, You cannot access and camera", Toast.LENGTH_LONG).show();
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                            if (shouldShowRequestPermissionRationale(CAMERA)) {
                                showMessageOKCancel("You need to allow access to both the permissions",
                                        new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                                    requestPermissions(new String[]{CAMERA},
                                                            REQUEST_CAMERA);
                                                }
                                            }
                                        });
                                return;
                            }
                        }
                    }
                }
                break;
        }
    }

    private void showMessageOKCancel(String message, DialogInterface.OnClickListener okListener) {
        new android.support.v7.app.AlertDialog.Builder(ScanCode_Activity.this,R.style.AlertDialogStyle)
                .setMessage(message)
                .setPositiveButton("OK", okListener)
                .setNegativeButton("Cancel", null)
                .create()
                .show();
    }

    @Override
    public void handleResult(final Result result) {
        final String myResult = result.getText();

        if (myResult.contains("End")) {
            EndClass(myResult);

        } else {
            int point = myResult.indexOf(" ");
            int point2 = myResult.indexOf("/");

            final String classcode = myResult.substring(0, point);
            final String registered_id = myResult.substring(point + 1, (point2));
            final String date = myResult.substring((point2 + 1), myResult.length());

            final String total = classcode + " " + date;


            String [] splitStr = date.trim().split("\\s+");

            String hour = splitStr[4].substring(0,2);

            Integer clock = Integer.parseInt(hour);

            clock = clock +3;

            String newhour = new SimpleDateFormat("HH", Locale.getDefault()).format(new Date());

            Integer newhourInt = Integer.parseInt(newhour);

            String bigHourStatus;

            //Toast.makeText(ScanCode_Activity.this,newhourInt + clock,Toast.LENGTH_LONG).show();




            if(newhourInt.compareTo(clock)>0){
                bigHourStatus = "Big";
            }else{
                bigHourStatus = "Small";

            }

            String trimDate = splitStr[1] + " " + splitStr[2] + " " + splitStr[3];

            String todaydate = new SimpleDateFormat("d MMM yyyy", Locale.getDefault()).format(new Date());






            String bigDateStatus;

            if(todaydate.compareTo(trimDate)>0){

                bigDateStatus ="Big";

            }
            else if (todaydate.compareTo(trimDate)==0){
                bigDateStatus ="Same Day";

            }
            else{
                bigDateStatus ="Small";


            }

            String status;


            if(bigHourStatus .equals("Big") || bigDateStatus.equals("Big")){

                status = "Fail";


            }else{
                status = "Success";



            }




            Toast.makeText(ScanCode_Activity.this,trimDate+todaydate,Toast.LENGTH_LONG).show();





            if(status.equals("Fail")) {
                AlertDialog.Builder builder = new AlertDialog.Builder(ScanCode_Activity.this, R.style.AlertDialogStyle);
                builder.setTitle("Scan Result");
                builder.setPositiveButton("End", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        ScanCode_Activity.this.onBackPressed();

                    }
                });

                builder.setMessage("Class Has Been Locked");

                AlertDialog alert1 = builder.create();

                alert1.show();




            }
            else{
                final FirebaseDatabase database = FirebaseDatabase.getInstance();
                DatabaseReference myref = database.getReference("users");

                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                final String userid = user.getUid();


                myref.child(userid).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        UserProfile userProfile = dataSnapshot.getValue(UserProfile.class);


                        setName(userProfile.getUserName().toString());


                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        Toast.makeText(ScanCode_Activity.this, databaseError.getCode(), Toast.LENGTH_SHORT).show();

                    }
                });


                DatabaseReference extranode = FirebaseDatabase.getInstance().getReference("student_registered_class");

                extranode.orderByChild("class_name").equalTo(classcode).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()) {

                            final DatabaseReference checkduplicatedata = FirebaseDatabase.getInstance().getReference("student_registered_class").child(registered_id).child("student_list");
                            checkduplicatedata.addListenerForSingleValueEvent(new ValueEventListener() {
                                String text = "empty";

                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                                    for (DataSnapshot ds : dataSnapshot.getChildren()) {

                                        registered_student_list = ds.getValue(student_registered_list.class);

                                        if (name.equals(registered_student_list.getName())) {

                                            this.text = registered_student_list.getName();


                                        }


                                    }
                                    if (text.equals("empty")) {
                                        Log.d("QRCodeScanner", result.getText());
                                        Log.d("QRCodeScanner", result.getBarcodeFormat().toString());

                                        AlertDialog.Builder builder = new AlertDialog.Builder(ScanCode_Activity.this,R.style.AlertDialogStyle);
                                        builder.setTitle("Scan Result");
                                        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                ScanCode_Activity.this.onBackPressed();

                                            }
                                        });

                                        builder.setMessage("You Didnt Enroll In This Class : " + classcode);
                                        AlertDialog alert1 = builder.create();
                                        alert1.show();


                                    } else {


                                        DatabaseReference attendees = FirebaseDatabase.getInstance().getReference("student_registered_class").child(registered_id).child("attendance_list");

                                        attendees.orderByChild("dateandtime").equalTo(total).addListenerForSingleValueEvent(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                                                if (dataSnapshot.exists()) {




                                                    for (DataSnapshot ds : dataSnapshot.getChildren()) {

                                                        attendance_list_push_qr attendanceListPushQr = ds.getValue(attendance_list_push_qr.class);
                                                        setAttendance_id(attendanceListPushQr.getAttendance_list_id());


                                                    }


                                                    //Check First DUPLICATE DATE
                                                    DatabaseReference checking = FirebaseDatabase.getInstance().getReference("student_registered_class").child(registered_id).child("attendance_list")
                                                            .child(attendance_id).child("attendees");

                                                    checking.addListenerForSingleValueEvent(new ValueEventListener() {
                                                        @Override
                                                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                            String check_taken = "Not Exist";
                                                            for (DataSnapshot ds : dataSnapshot.getChildren()) {
                                                                student_registered_list check;

                                                                check = ds.getValue(student_registered_list.class);

                                                                if (name.equals(check.getName())) {
                                                                    check_taken = "Exist";

                                                                    DatabaseReference lock = FirebaseDatabase.getInstance().getReference("student_registered_class").child(registered_id).child("attendance_list")
                                                                            .child(attendance_id);


                                                                }


                                                            }

                                                            if (check_taken.equals("Not Exist")) {
                                                                DatabaseReference ref = FirebaseDatabase.getInstance().getReference("student_registered_class").child(registered_id).child("attendance_list")
                                                                        .child(attendance_id).child("attendees").push();

                                                                final student_registered_list student_registered_list = new student_registered_list(userid, name,"Present",0);
                                                                ref.setValue(student_registered_list);


                                                                Log.d("QRCodeScanner", result.getText());
                                                                Log.d("QRCodeScanner", result.getBarcodeFormat().toString());

                                                                AlertDialog.Builder builder = new AlertDialog.Builder(ScanCode_Activity.this,R.style.AlertDialogStyle);
                                                                builder.setTitle("Scan Result");
                                                                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                                                    @Override
                                                                    public void onClick(DialogInterface dialog, int which) {
                                                                        ScanCode_Activity.this.onBackPressed();

                                                                    }
                                                                });

                                                                builder.setMessage("Attendance Taken For The Class : " + classcode + "  Date/Time : " + date);
                                                                AlertDialog alert1 = builder.create();
                                                                alert1.show();


                                                            } else {
                                                                Log.d("QRCodeScanner", result.getText());
                                                                Log.d("QRCodeScanner", result.getBarcodeFormat().toString());

                                                                AlertDialog.Builder builder = new AlertDialog.Builder(ScanCode_Activity.this,R.style.AlertDialogStyle);
                                                                builder.setTitle("Scan Result");
                                                                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                                                    @Override
                                                                    public void onClick(DialogInterface dialog, int which) {
                                                                        ScanCode_Activity.this.onBackPressed();

                                                                    }
                                                                });

                                                                builder.setMessage("Attendance Already Taken :  " + classcode + " Mr " + name);
                                                                AlertDialog alert1 = builder.create();
                                                                //Button bq = alert1.getButton(DialogInterface.BUTTON_NEGATIVE);
                                                                //bq.setBackgroundColor(Color.BLUE);
                                                                alert1.show();


                                                            }
                                                        }

                                                        @Override
                                                        public void onCancelled(@NonNull DatabaseError databaseError) {

                                                        }
                                                    });
                                                } else {

                                                }

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


                        } else {

                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

            }













        }


    }

    private void EndClass(String myResult) {

        int ori = myResult.indexOf("d");
        String original = myResult.substring(ori + 1, myResult.length());

        int point = original.indexOf(" ");
        int point2 = original.indexOf("/");

        final String classcode = original.substring(0, point);
        final String include_secretcode = original.substring(point + 1, (point2));
        final String date = original.substring((point2 + 1), original.length());

        final String total = date;


        String edit = include_secretcode.substring(0,8);
        final String secretcode = include_secretcode.substring(8,11);
        String edit3 = include_secretcode.substring(11,include_secretcode.length());

        final String registered_id = edit + edit3;




        String [] splitStr = date.trim().split("\\s+");

        String hour = splitStr[5].substring(0,2);

        Integer clock = Integer.parseInt(hour);

        clock = clock +3;

        String newhour = new SimpleDateFormat("HH", Locale.getDefault()).format(new Date());

        Integer newhourInt = Integer.parseInt(newhour);

        String bigHourStatus;

        //Toast.makeText(ScanCode_Activity.this,newhourInt + clock,Toast.LENGTH_LONG).show();



        if(newhourInt.compareTo(clock)>0){
            bigHourStatus = "Big";
        }else{
            bigHourStatus = "Small";

        }

        String trimDate = splitStr[2] + " " + splitStr[3] + " " + splitStr[4];

        String todaydate = new SimpleDateFormat("d MMM yyyy", Locale.getDefault()).format(new Date());



        String bigDateStatus;

        if(todaydate.compareTo(trimDate)>0){

            bigDateStatus ="Big";

        }
        else if (todaydate.compareTo(trimDate)==0){
            bigDateStatus ="Same Day";

        }
        else{
            bigDateStatus ="Small";


        }

        String status;


        if(bigHourStatus .equals("Big") || bigDateStatus.equals("Big")){

            status = "Fail";


        }else{
            status = "Success";



        }



        if(status.equals("Fail")){



            DatabaseReference retrieve = FirebaseDatabase.getInstance().getReference("student_registered_class").child(registered_id).child("attendance_list");



            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
            final String userid = user.getUid();


            retrieve.orderByChild("dateandtime").equalTo(total).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                    if (dataSnapshot.exists()) {




                        for (DataSnapshot ds : dataSnapshot.getChildren()) {

                            attendance_list_push_qr attendanceListPushQr = ds.getValue(attendance_list_push_qr.class);
                            setAttendance_id(attendanceListPushQr.getAttendance_list_id());


                        }

                        DatabaseReference remove = FirebaseDatabase.getInstance().getReference("student_registered_class").child(registered_id).child("attendance_list").child(attendance_id).child("attendees");

                        remove.orderByChild("uID").equalTo(userid).addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                if(dataSnapshot.exists()){

                                    dataSnapshot.getRef().removeValue();



                                    Toast.makeText(ScanCode_Activity.this,dataSnapshot.getKey(),Toast.LENGTH_LONG);
                                    AlertDialog.Builder builder = new AlertDialog.Builder(ScanCode_Activity.this,R.style.AlertDialogStyle);
                                    builder.setTitle("Scan Result");
                                    builder.setPositiveButton("End", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            ScanCode_Activity.this.onBackPressed();

                                        }
                                    });
                                    builder.setMessage("Class Has Been Locked");
                                    AlertDialog alert1 = builder.create();
                                    alert1.show();
                                }
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








        }

        else{
            DatabaseReference checksecretcode = FirebaseDatabase.getInstance().getReference("student_registered_class").child(registered_id).child("secret_code");

            checksecretcode.addListenerForSingleValueEvent(new ValueEventListener() {
                String check;
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    students_registered_class retrieve = dataSnapshot.getValue(students_registered_class.class);


                    if(secretcode.equals(retrieve.getSecretkey())){
                        this.check = "Success";

                    }
                    else{
                        this.check = "Fail";
                    }
                    if(check.equals("Success")){

                        final FirebaseDatabase database = FirebaseDatabase.getInstance();
                        DatabaseReference myref = database.getReference("users");

                        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                        final String userid = user.getUid();

                        myref.child(userid).addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                UserProfile userProfile = dataSnapshot.getValue(UserProfile.class);


                                setName(userProfile.getUserName().toString());


                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {
                                Toast.makeText(ScanCode_Activity.this, databaseError.getCode(), Toast.LENGTH_SHORT).show();

                            }
                        });

                        DatabaseReference extranode = FirebaseDatabase.getInstance().getReference("student_registered_class");

                        extranode.orderByChild("class_name").equalTo(classcode).addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                if (dataSnapshot.exists()) {

                                    final DatabaseReference checkduplicatedata = FirebaseDatabase.getInstance().getReference("student_registered_class").child(registered_id).child("student_list");
                                    checkduplicatedata.addListenerForSingleValueEvent(new ValueEventListener() {
                                        String text = "empty";

                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                                            for (DataSnapshot ds : dataSnapshot.getChildren()) {

                                                registered_student_list = ds.getValue(student_registered_list.class);

                                                if (name.equals(registered_student_list.getName())) {

                                                    this.text = registered_student_list.getName();


                                                }


                                            }
                                            if (text.equals("empty")) {

                                                AlertDialog.Builder builder = new AlertDialog.Builder(ScanCode_Activity.this,R.style.AlertDialogStyle);
                                                builder.setTitle("Scan Result");
                                                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                                    @Override
                                                    public void onClick(DialogInterface dialog, int which) {
                                                        ScanCode_Activity.this.onBackPressed();

                                                    }
                                                });

                                                builder.setMessage("You Didnt Enroll In This Class : " + classcode);
                                                AlertDialog alert1 = builder.create();
                                                alert1.show();


                                            } else {


                                                DatabaseReference attendees = FirebaseDatabase.getInstance().getReference("student_registered_class").child(registered_id).child("attendance_list");

                                                attendees.orderByChild("dateandtime").equalTo(total).addListenerForSingleValueEvent(new ValueEventListener() {
                                                    @Override
                                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                                                        if (dataSnapshot.exists()) {


                                                            for (DataSnapshot ds : dataSnapshot.getChildren()) {
                                                                setDateandtimeid(ds.getKey());

                                                                attendance_list_push_qr attendanceListPushQr = ds.getValue(attendance_list_push_qr.class);
                                                                setAttendance_id(attendanceListPushQr.getAttendance_list_id());


                                                            }


                                                            //Check First DUPLICATE DATE
                                                            DatabaseReference checking = FirebaseDatabase.getInstance().getReference("student_registered_class").child(registered_id).child("attendance_list")
                                                                    .child(attendance_id).child("attendees");

                                                            checking.addListenerForSingleValueEvent(new ValueEventListener() {
                                                                @Override
                                                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                                    String check_taken = "Not Exist";
                                                                    for (DataSnapshot ds : dataSnapshot.getChildren()) {
                                                                        student_registered_list check;

                                                                        check = ds.getValue(student_registered_list.class);

                                                                        if (name.equals(check.getName())) {
                                                                            setAttendeesPushedId(ds.getKey());
                                                                            check_taken = "Exist";
                                                                        }


                                                                    }

                                                                    if (check_taken.equals("Not Exist")) {




                                                                        AlertDialog.Builder builder = new AlertDialog.Builder(ScanCode_Activity.this,R.style.AlertDialogStyle);
                                                                        builder.setTitle("Scan Result");
                                                                        builder.setPositiveButton("Exit", new DialogInterface.OnClickListener() {
                                                                            @Override
                                                                            public void onClick(DialogInterface dialog, int which) {
                                                                                ScanCode_Activity.this.onBackPressed();

                                                                            }
                                                                        });

                                                                        builder.setMessage("Late to class please refer to Lecure");
                                                                        AlertDialog alert1 = builder.create();
                                                                        alert1.show();


                                                                    } else {




                                                                        DatabaseReference checkend= FirebaseDatabase.getInstance().getReference("student_registered_class").child(registered_id).child("attendance_list")
                                                                                .child(dateandtimeid).child("attendees").child(attendeedpushedid);

                                                                        checkend.child("endDate").addListenerForSingleValueEvent(new ValueEventListener() {
                                                                            private String end;

                                                                            @Override
                                                                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                                                                                if(dataSnapshot.exists()){
                                                                                    this.end = "Exist";
                                                                                }
                                                                                else{
                                                                                    this.end = "Not Exist";
                                                                                }

                                                                                if(end.equals("Not Exist")){

                                                                                    final DatabaseReference retrievecount = FirebaseDatabase.getInstance().getReference("student_registered_class").child(registered_id).child("student_list");


                                                                                    retrievecount.addListenerForSingleValueEvent(new ValueEventListener() {
                                                                                        Integer count ;
                                                                                        @Override
                                                                                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                                                            for (DataSnapshot ds : dataSnapshot.getChildren()) {


                                                                                                registered_student_list = ds.getValue(student_registered_list.class);

                                                                                                if (name.equals(registered_student_list.getName())) {





                                                                                                    this.count = registered_student_list.getCount();

                                                                                                    count++;
                                                                                                    DatabaseReference update = FirebaseDatabase.getInstance().getReference("student_registered_class").child(registered_id).child("student_list")
                                                                                                            .child(ds.getKey().toString());

                                                                                                    student_registered_list push = new student_registered_list(userid,name,count);
                                                                                                    update.setValue(push);

                                                                                                    DatabaseReference updateattendees= FirebaseDatabase.getInstance().getReference("student_registered_class").child(registered_id).child("attendance_list")
                                                                                                            .child(dateandtimeid).child("attendees").child(attendeedpushedid);

                                                                                                    String enddate = new SimpleDateFormat("EEE, d MMM yyyy HH:mm:ss ", Locale.getDefault()).format(new Date());

                                                                                                    student_registered_list pushupdated = new student_registered_list(userid,name,enddate,"Present");

                                                                                                    updateattendees.setValue(pushupdated);





                                                                                                    AlertDialog.Builder builder = new AlertDialog.Builder(ScanCode_Activity.this,R.style.AlertDialogStyle);
                                                                                                    builder.setTitle("Scan Result");
                                                                                                    builder.setPositiveButton("End", new DialogInterface.OnClickListener() {
                                                                                                        @Override
                                                                                                        public void onClick(DialogInterface dialog, int which) {
                                                                                                            ScanCode_Activity.this.onBackPressed();

                                                                                                        }
                                                                                                    });
                                                                                                    builder.setMessage("Attendance Complete ");
                                                                                                    AlertDialog alert1 = builder.create();
                                                                                                    alert1.show();


                                                                                                }

                                                                                            }

                                                                                        }

                                                                                        @Override
                                                                                        public void onCancelled(@NonNull DatabaseError databaseError) {

                                                                                        }
                                                                                    });



                                                                                } else{
                                                                                    AlertDialog.Builder builder = new AlertDialog.Builder(ScanCode_Activity.this,R.style.AlertDialogStyle);
                                                                                    builder.setTitle("Scan Result");
                                                                                    builder.setPositiveButton("Exit", new DialogInterface.OnClickListener() {
                                                                                        @Override
                                                                                        public void onClick(DialogInterface dialog, int which) {
                                                                                            ScanCode_Activity.this.onBackPressed();

                                                                                        }
                                                                                    });

                                                                                    builder.setMessage("Class Already Ended");
                                                                                    AlertDialog alert1 = builder.create();
                                                                                    alert1.show();


                                                                                }

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
                                                        } else {

                                                        }

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

                                } else {

                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });

                    }

                    else{
                        AlertDialog.Builder builder = new AlertDialog.Builder(ScanCode_Activity.this,R.style.AlertDialogStyle);
                        builder.setTitle("Scan Result");
                        builder.setPositiveButton("End", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                ScanCode_Activity.this.onBackPressed();

                            }
                        });
                        builder.setMessage("Class Locked");
                        AlertDialog alert1 = builder.create();
                        alert1.show();

                    }
                }



                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });


        }














    }



    private void setAttendeesPushedId(String key) {
        this.attendeedpushedid =key;
    }

    private void setDateandtimeid(String key) {
        this.dateandtimeid = key;
    }

    private void setEnd(String endDate) {
        this.end = endDate;
    }


    private void setAttendance_id(String key) {
        this.attendance_id=key;
    }

    private void setName(String s) {
        this.name = s;
    }


}
