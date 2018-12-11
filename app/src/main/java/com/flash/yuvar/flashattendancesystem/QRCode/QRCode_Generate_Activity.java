package com.flash.yuvar.flashattendancesystem.QRCode;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.flash.yuvar.flashattendancesystem.R;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.journeyapps.barcodescanner.BarcodeEncoder;

public class QRCode_Generate_Activity extends AppCompatActivity {
    EditText text;
    Button gen_btn;
    ImageView image;
    String text2Qr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate (savedInstanceState);
        setContentView (R.layout.activity_qrcode__generate_);

        final String carriedclasscode = getIntent ().getExtras ().getString ("CarriedClassName");
        String carriedregisteredid = getIntent ().getExtras ().getString ("CarriedRegisteredClassID");

        final String total = carriedclasscode + " " + carriedregisteredid;

        text = (EditText) findViewById (R.id.text);
        gen_btn = (Button) findViewById (R.id.gen_btn);
        image = (ImageView) findViewById (R.id.image);
        gen_btn.setOnClickListener (new View.OnClickListener ( ) {
            @Override
            public void onClick(View view) {
                text2Qr = text.getText ( ).toString ( ).trim ( );
                MultiFormatWriter multiFormatWriter = new MultiFormatWriter ( );
                try {
                    BitMatrix bitMatrix = multiFormatWriter.encode (total, BarcodeFormat.QR_CODE, 200, 200);
                    BarcodeEncoder barcodeEncoder = new BarcodeEncoder ( );
                    Bitmap bitmap = barcodeEncoder.createBitmap (bitMatrix);
                    image.setImageBitmap (bitmap);
                } catch (WriterException e) {
                    e.printStackTrace ( );
                }
            }
        });
    }
}

