package com.flash.yuvar.flashattendancesystem.QRCode;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.google.zxing.Result;

import me.dm7.barcodescanner.zxing.ZXingScannerView;

public class ScanCode_Activity extends AppCompatActivity implements ZXingScannerView.ResultHandler {

    ZXingScannerView ScannerView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate (savedInstanceState);
        ScannerView = new ZXingScannerView (this);
        setContentView (ScannerView);




    }

    @Override
    public void handleResult(Result result) {

        QRCode_Capture_Activity.resultTextView.setText (result.getText ());
        onBackPressed ();

    }

    @Override
    protected void onPause() {
        super.onPause ( );

        ScannerView.stopCamera ();
    }

    @Override
    protected void onResume() {
        super.onResume ( );

        ScannerView.setResultHandler (this);
        ScannerView.startCamera ();
    }

}
