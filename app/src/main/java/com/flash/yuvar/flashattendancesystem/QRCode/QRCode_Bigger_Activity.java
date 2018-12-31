package com.flash.yuvar.flashattendancesystem.QRCode;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TextView;

import com.flash.yuvar.flashattendancesystem.R;
import com.squareup.picasso.Picasso;

public class QRCode_Bigger_Activity extends AppCompatActivity {

    public TextView classcodedetail;
    public ImageView qrcodedetail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qrcode__bigger_);

        String imageUrl = getIntent().getStringExtra("image_url");
        String classcode = getIntent().getStringExtra("classcode");

        classcodedetail = (TextView) findViewById(R.id.class_big);
        qrcodedetail =(ImageView)findViewById(R.id.qr_code_big);

        classcodedetail.setText(classcode);
        Picasso.get()
                .load(imageUrl)
                .placeholder(R.drawable.error)
                .fit()
                .centerInside()
                .into(qrcodedetail);
    }
}
