package com.flash.yuvar.flashattendancesystem.QRCode;

import android.graphics.Bitmap;
import android.graphics.Point;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.flash.yuvar.flashattendancesystem.Database.attendance_list_push_qr;
import com.flash.yuvar.flashattendancesystem.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.zxing.WriterException;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import androidmads.library.qrgenearator.QRGContents;
import androidmads.library.qrgenearator.QRGEncoder;

public class QRCode_Generate_Activity extends AppCompatActivity {
    String TAG = "GenerateQRCode";
    private final String tag = "QRCGEN";

    Button gen_btn,del_btn,previosu_btn;


    public String total = null;
    private ImageView imgResult;
    public String carriedclasscode,carriedregisteredid;
    private Bitmap bitmap;
    String savePath = Environment.getExternalStorageDirectory().getPath();





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate (savedInstanceState);
        setContentView (R.layout.activity_qrcode__generate_);

        carriedclasscode = getIntent ().getExtras ().getString ("CarriedClassName");
        carriedregisteredid = getIntent ().getExtras ().getString ("CarriedRegisteredClassID");









        gen_btn = (Button) findViewById (R.id.gen_btn);
        imgResult = (ImageView) findViewById (R.id.image);




        //new Class
        gen_btn.setOnClickListener (new View.OnClickListener ( ) {
            @Override
            public void onClick(View v) {


                String date = new SimpleDateFormat ("EEE, d MMM yyyy HH:mm:ss ", Locale.getDefault()).format(new Date ());

                total = carriedclasscode + " " + carriedregisteredid + "/" + date;

                WindowManager manager = (WindowManager) getSystemService(WINDOW_SERVICE);
                Display display = manager.getDefaultDisplay();
                Point point = new Point();
                display.getSize(point);
                int width = point.x;
                int height = point.y;
                int smallerDimension = width < height ? width : height;
                smallerDimension = smallerDimension * 3 / 4;

                QRGEncoder qrgEncoder = new QRGEncoder (
                        total, null,
                        QRGContents.Type.TEXT,
                        smallerDimension);
                try {
                    bitmap = qrgEncoder.encodeAsBitmap();
                    attendancesheet(total,carriedclasscode,date,carriedregisteredid);
                    imgResult.setImageBitmap(bitmap);
                } catch (WriterException e) {
                    Log.v(TAG, e.toString());
                }















                //Toast.makeText (QRCode_Generate_Activity.this,"Image Saved",Toast.LENGTH_LONG ).show ();


            }
        });


    }

    private void attendancesheet(String total, String carriedclasscode, String date, final String carriedregisteredid) {

        total = carriedclasscode + " "+date;

        DatabaseReference extranode = FirebaseDatabase.getInstance ().getReference ("student_registered_class");

        final String finalTotal = total;
        extranode.orderByChild ("registeredclassID").equalTo (carriedregisteredid).addListenerForSingleValueEvent (new ValueEventListener ( ) {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists ()){

                    DatabaseReference ref = FirebaseDatabase.getInstance ().getReference ("student_registered_class").child(carriedregisteredid).child("attendance_list").push ();
                    String attendance_id = ref.getKey ();
                    final attendance_list_push_qr attendance_list = new attendance_list_push_qr (finalTotal,attendance_id);
                    ref.setValue (attendance_list );


                    Toast.makeText(QRCode_Generate_Activity.this, "SuccessFull! Generated" + finalTotal,Toast.LENGTH_SHORT).show();
                }



                else{
                    Toast.makeText(QRCode_Generate_Activity.this, "Class ID not Exist!" ,Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }


}




