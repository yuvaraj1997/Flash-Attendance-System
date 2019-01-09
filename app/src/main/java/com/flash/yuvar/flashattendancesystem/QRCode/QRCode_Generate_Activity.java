package com.flash.yuvar.flashattendancesystem.QRCode;

import android.graphics.Bitmap;
import android.graphics.Point;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.flash.yuvar.flashattendancesystem.Adapters.QR_Code_Generator_adapter;
import com.flash.yuvar.flashattendancesystem.Database.attendance_list_push_qr;
import com.flash.yuvar.flashattendancesystem.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.google.zxing.WriterException;
import com.wang.avi.AVLoadingIndicatorView;

import java.io.ByteArrayOutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Random;

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


    private StorageReference mStorage;
    private DatabaseReference mDatabse;
    private String finalTotal ;
    private Uri downloaduri;


    private RecyclerView mRecyclerView;
    private QR_Code_Generator_adapter mAdapter;

    private List<attendance_list_push_qr> mUploads;

    public ProgressBar progressBar;
    private String Url;
    private String date;

    public String TOTAL;

    private AVLoadingIndicatorView avi;

    private String lecturename;









    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate (savedInstanceState);
        setContentView (R.layout.activity_qrcode__generate_);

        carriedclasscode = getIntent ().getExtras ().getString ("CarriedClassName");
        carriedregisteredid = getIntent ().getExtras ().getString ("CarriedRegisteredClassID");


        avi= (AVLoadingIndicatorView) findViewById(R.id.avi);
        gen_btn = (Button) findViewById (R.id.gen_btn);
        imgResult = (ImageView) findViewById (R.id.image);



        mRecyclerView = findViewById(R.id.recycler_view_qr);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        mUploads = new ArrayList<>();

        mDatabse = FirebaseDatabase.getInstance().getReference ("student_registered_class").child(carriedregisteredid).child("attendance_list");



        mDatabse.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                mUploads.clear();
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    attendance_list_push_qr upload = postSnapshot.getValue(attendance_list_push_qr.class);


                    mUploads.add(upload);
                }
                avi.hide();

                mAdapter = new QR_Code_Generator_adapter(QRCode_Generate_Activity.this, mUploads);

                mRecyclerView.setAdapter(mAdapter);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(QRCode_Generate_Activity.this, databaseError.getMessage(), Toast.LENGTH_SHORT).show();

            }
        });






        //new Class
        gen_btn.setOnClickListener (new View.OnClickListener ( ) {
            @Override
            public void onClick(View v) {

                avi.show();



                date = new SimpleDateFormat ("EEE, d MMM yyyy HH:mm:ss ", Locale.getDefault()).format(new Date ());

                TOTAL = carriedclasscode + " " + carriedregisteredid + "/" + date;


                WindowManager manager = (WindowManager) getSystemService(WINDOW_SERVICE);
                Display display = manager.getDefaultDisplay();
                Point point = new Point();
                display.getSize(point);
                int width = point.x;
                int height = point.y;
                int smallerDimension = width < height ? width : height;
                smallerDimension = smallerDimension * 3 / 4;

                QRGEncoder qrgEncoder = new QRGEncoder (
                        TOTAL, null,
                        QRGContents.Type.TEXT,
                        smallerDimension);
                try {


                    bitmap = qrgEncoder.encodeAsBitmap();

                    if(bitmap!=null){

                        saveBitmap(bitmap);



                    }
                    else{
                        return;
                    }





                    //imgResult.setImageBitmap(bitmap);
                } catch (WriterException e) {
                    Log.v(TAG, e.toString());
                }

















                //Toast.makeText (QRCode_Generate_Activity.this,"Image Saved",Toast.LENGTH_LONG ).show ();


            }
        });


    }

    private void setLecturename(String userName) {
        this.lecturename = userName;
    }

    private void saveBitmap(Bitmap bitmap) {

        String total = carriedclasscode + " "+date;


        mStorage = FirebaseStorage.getInstance().getReferenceFromUrl("gs://flash-attendance-system.appspot.com/"+carriedclasscode+"/"+total);


        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] data = baos.toByteArray();

        UploadTask uploadTask = mStorage.putBytes(data);

        uploadTask.addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                task.getResult().getStorage().getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        Url = uri.toString();
                        attendancesheet(TOTAL,carriedclasscode,date,carriedregisteredid,Url);


                    }
                });

            }
        });






    }



    private void attendancesheet(String total, String carriedclasscode, String date, final String carriedregisteredid, final String Url) {

        total = carriedclasscode + " "+date;

        DatabaseReference extranode = FirebaseDatabase.getInstance ().getReference ("student_registered_class");

        finalTotal=total;




        extranode.orderByChild ("registeredclassID").equalTo (carriedregisteredid).addListenerForSingleValueEvent (new ValueEventListener ( ) {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists ()){

                    if(Url!=null){
                        DatabaseReference ref = FirebaseDatabase.getInstance ().getReference ("student_registered_class").child(carriedregisteredid).child("attendance_list").push ();
                        String attendance_id = ref.getKey ();
                        final String password = randomString(4);



                        final attendance_list_push_qr attendance_list = new attendance_list_push_qr (finalTotal,attendance_id,Url,password);
                        ref.setValue (attendance_list );


                    }
                    else{


                        Toast.makeText(QRCode_Generate_Activity.this,"Try AGAIN "+Url,Toast.LENGTH_LONG).show();

                    }





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

    public static final String DATA = "0123456789";
    public static Random RANDOM = new Random();

    public static String randomString(int len) {
        StringBuilder sb = new StringBuilder(len);

        for (int i = 0; i < len; i++) {
            sb.append(DATA.charAt(RANDOM.nextInt(DATA.length())));
        }

        return sb.toString();
    }




}




