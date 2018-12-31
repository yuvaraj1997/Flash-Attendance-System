package com.flash.yuvar.flashattendancesystem.QRCode;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.flash.yuvar.flashattendancesystem.R;
import com.squareup.picasso.Picasso;


public class QRCode_Detail_Activity extends AppCompatActivity implements View.OnClickListener {

    public TextView classcodedetail,dateandtimedetail;
    public ImageView qrcodedetail;
    public String classcode;

    public String imageUrl,dateandtime;

    Button endclass;
    String registrationid;

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

                Intent intent = new Intent(QRCode_Detail_Activity.this, QR_Code_End_Activity.class);


                intent.putExtra("registrationid", registrationid);
                intent.putExtra("dateandtime", dateandtime);
                intent.putExtra("classcode", classcode);

                startActivity(intent);


            }
        });






    }


    @Override
    public void onClick(View v) {

        Intent intent = new Intent(this, QRCode_Bigger_Activity.class);
        intent.putExtra("image_url", imageUrl);
        intent.putExtra("classcode", classcode);

        startActivity(intent);

    }
}
