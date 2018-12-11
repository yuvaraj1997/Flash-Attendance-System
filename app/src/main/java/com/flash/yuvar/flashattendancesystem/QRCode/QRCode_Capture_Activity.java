package com.flash.yuvar.flashattendancesystem.QRCode;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.flash.yuvar.flashattendancesystem.R;

public class QRCode_Capture_Activity extends AppCompatActivity {

    public static TextView resultTextView;
    Button scan_btn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate (savedInstanceState);
        setContentView (R.layout.activity_qrcode__capture_);



        resultTextView=(TextView)findViewById (R.id.result_text);
        scan_btn = (Button)findViewById (R.id.btn_scan);

        scan_btn.setOnClickListener (new View.OnClickListener ( ) {
            @Override
            public void onClick(View v) {
                startActivity (new Intent (getApplicationContext (),ScanCode_Activity.class));
            }
        });

    }
}
