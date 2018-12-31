package com.flash.yuvar.flashattendancesystem.QRCode;

import android.graphics.Bitmap;
import android.graphics.Point;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.Display;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.flash.yuvar.flashattendancesystem.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.zxing.WriterException;

import androidmads.library.qrgenearator.QRGContents;
import androidmads.library.qrgenearator.QRGEncoder;

public class QR_Code_End_Activity extends AppCompatActivity {

    public String dateandtime, classcode, registrationid;
    public TextView classcodedetail;
    public ImageView qrcodedetail;
    private Bitmap bitmap;
    public String end;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qr__code__end_);

        dateandtime = getIntent().getStringExtra("dateandtime");
        classcode = getIntent().getStringExtra("classcode");


        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("student_registered_class");

        ref.orderByChild("class_name").equalTo(classcode).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    for (DataSnapshot child : dataSnapshot.getChildren()) {

                        String end = "End" + classcode + " " + child.getKey() + "/" + dateandtime;
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

                } else {
                    Toast.makeText(QR_Code_End_Activity.this, "Error", Toast.LENGTH_SHORT).show();
                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        classcodedetail = (TextView) findViewById(R.id.class_end);
        qrcodedetail = (ImageView) findViewById(R.id.qr_code_end);


        classcodedetail.setText(classcode);


    }

}


