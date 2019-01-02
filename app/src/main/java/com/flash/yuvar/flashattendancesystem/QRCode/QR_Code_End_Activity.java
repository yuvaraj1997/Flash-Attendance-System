package com.flash.yuvar.flashattendancesystem.QRCode;

import android.graphics.Bitmap;
import android.graphics.Point;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.Display;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.flash.yuvar.flashattendancesystem.Database.students_registered_class;
import com.flash.yuvar.flashattendancesystem.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.zxing.WriterException;

import java.util.Random;

import androidmads.library.qrgenearator.QRGContents;
import androidmads.library.qrgenearator.QRGEncoder;

public class QR_Code_End_Activity extends AppCompatActivity {

    public String dateandtime, classcode, registrationid;
    public TextView classcodedetail,countdown;
    public ImageView qrcodedetail;
    private Bitmap bitmap;
    public String end;
    CountDownTimer timer;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qr__code__end_);

        dateandtime = getIntent().getStringExtra("dateandtime");
        classcode = getIntent().getStringExtra("classcode");

        classcodedetail = (TextView) findViewById(R.id.class_end);
        qrcodedetail = (ImageView) findViewById(R.id.qr_code_end);
        countdown = (TextView) findViewById(R.id.countdown);

        classcodedetail.setText(classcode);

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("student_registered_class");

        ref.orderByChild("class_name").equalTo(classcode).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    for (final DataSnapshot child : dataSnapshot.getChildren()) {




                        final String edit = child.getKey().substring(0,8);
                        final String edit2 = child.getKey().substring(8,child.getKey().length());



                        final String random = randomString(3);


                        final String original = child.getKey();
                        DatabaseReference secretcode = FirebaseDatabase.getInstance().getReference("student_registered_class").child(original);

                        secretcode.addListenerForSingleValueEvent(new ValueEventListener() {
                            String total;
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                this.total = edit + random + edit2;




                                students_registered_class retrieve =dataSnapshot.getValue(students_registered_class.class);

                                DatabaseReference secretcode = FirebaseDatabase.getInstance().getReference("student_registered_class").child(original
                                ).child("secret_code");

                                students_registered_class update = new students_registered_class(random);

                                secretcode.setValue(update);

                                String end = "End" + classcode + " " + total + "/" + dateandtime ;
                                WindowManager manager = (WindowManager) getSystemService(WINDOW_SERVICE);
                                Display display = manager.getDefaultDisplay();
                                Point point = new Point();
                                display.getSize(point);
                                int width = point.x;
                                int height = point.y;
                                int smallerDimension = width < height ? width : height;
                                smallerDimension = smallerDimension * 3 / 4;
                                QRGEncoder qrgEncoder = new QRGEncoder(
                                        end, null,
                                        QRGContents.Type.TEXT,
                                        smallerDimension);
                                try {


                                    bitmap = qrgEncoder.encodeAsBitmap();
                                    qrcodedetail.setImageBitmap(bitmap);
                                } catch (WriterException e) {

                                }

                            }
                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });

                    }

                } else {
                    Toast.makeText(QR_Code_End_Activity.this, "Error", Toast.LENGTH_SHORT).show();
                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });




        timer = new CountDownTimer(10000, 1000) {
            @Override
            public void onTick(final long millSecondsLeftToFinish) {
                String time = String.valueOf(millSecondsLeftToFinish / 1000);
                countdown.setText(time);
            }

            @Override
            public void onFinish() {
                DatabaseReference ref = FirebaseDatabase.getInstance().getReference("student_registered_class");

                ref.orderByChild("class_name").equalTo(classcode).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()) {
                            for (final DataSnapshot child : dataSnapshot.getChildren()) {




                                final String edit = child.getKey().substring(0,8);
                                final String edit2 = child.getKey().substring(8,child.getKey().length());



                                final String random = randomString(3);


                                final String original = child.getKey();
                                DatabaseReference secretcode = FirebaseDatabase.getInstance().getReference("student_registered_class").child(original);

                                secretcode.addListenerForSingleValueEvent(new ValueEventListener() {
                                    String total;
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                        this.total = edit + random + edit2;




                                        students_registered_class retrieve =dataSnapshot.getValue(students_registered_class.class);

                                        DatabaseReference secretcode = FirebaseDatabase.getInstance().getReference("student_registered_class").child(original
                                        ).child("secret_code");

                                        students_registered_class update = new students_registered_class(random);

                                        secretcode.setValue(update);

                                        String end = "End" + classcode + " " + total + "/" + dateandtime ;
                                        WindowManager manager = (WindowManager) getSystemService(WINDOW_SERVICE);
                                        Display display = manager.getDefaultDisplay();
                                        Point point = new Point();
                                        display.getSize(point);
                                        int width = point.x;
                                        int height = point.y;
                                        int smallerDimension = width < height ? width : height;
                                        smallerDimension = smallerDimension * 3 / 4;
                                        QRGEncoder qrgEncoder = new QRGEncoder(
                                                end, null,
                                                QRGContents.Type.TEXT,
                                                smallerDimension);
                                        try {


                                            bitmap = qrgEncoder.encodeAsBitmap();
                                            qrcodedetail.setImageBitmap(bitmap);
                                        } catch (WriterException e) {

                                        }

                                    }
                                    @Override
                                    public void onCancelled(@NonNull DatabaseError databaseError) {

                                    }
                                });

                            }

                        } else {
                            Toast.makeText(QR_Code_End_Activity.this, "Error", Toast.LENGTH_SHORT).show();
                        }


                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });



                timer.start();
            }
        };
        timer.start();








    }


    public static final String DATA = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz|!£$%&/=@#";
    public static Random RANDOM = new Random();

    public static String randomString(int len) {
        StringBuilder sb = new StringBuilder(len);

        for (int i = 0; i < len; i++) {
            sb.append(DATA.charAt(RANDOM.nextInt(DATA.length())));
        }

        return sb.toString();
    }
}