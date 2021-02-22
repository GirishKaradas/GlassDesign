package com.example.android.glass.glassdesign;

import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.example.android.glass.glassdesign.util.DropDownAlert;
import com.google.zxing.Result;

import me.dm7.barcodescanner.zxing.ZXingScannerView;

public class ScanActivity extends BaseActivity implements ZXingScannerView.ResultHandler {

    private ZXingScannerView mScannerView;
    private DropDownAlert dropDownAlert;
    int PERMISSION_ALL = 1;
    String[] PERMISSIONS = {
            Manifest.permission.INTERNET,
            Manifest.permission.ACCESS_NETWORK_STATE,
            android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            android.Manifest.permission.CAMERA,
            Manifest.permission.RECORD_AUDIO,
            Manifest.permission.MODIFY_AUDIO_SETTINGS,
            Manifest.permission.BLUETOOTH
    };



    @Override
    public void onCreate(Bundle state) {
        super.onCreate(state);
        // Programmatically initialize the scanner view
        if (!hasPermissions(this, PERMISSIONS)) {
            ActivityCompat.requestPermissions(this, PERMISSIONS, PERMISSION_ALL);
        }
        mScannerView = new ZXingScannerView(this);
        // Set the scanner view as the content view
        setContentView(mScannerView);
        dropDownAlert = new DropDownAlert(this);
        dropDownAlert.setText("Scan Role QR Code");
        dropDownAlert.setTextWeight(0.5f);
        //  dropDownAlert.addImages("surfer.png", "bike.png");
        dropDownAlert.show();
    }

    @Override
    public void onResume() {
        super.onResume();
        // Register ourselves as a handler for scan results.
        mScannerView.setResultHandler(this);
        // Start camera on resume
        mScannerView.startCamera();
    }

    @Override
    public void onPause() {
        super.onPause();
        // Stop camera on pause
        mScannerView.stopCamera();
    }

    @Override
    public void handleResult(Result rawResult) {
        // Do something with the result here
        // Prints scan results

        Log.e("result", rawResult.getText());
        // Prints the scan format (qrcode, pdf417 etc.)
        Log.e("result", rawResult.getBarcodeFormat().toString());
        //If you would like to resume scanning, call this method below:
        //  mScannerView.resumeCameraPreview(this);
        String result = rawResult.getText();

    /*    Intent intent = new Intent(ScannerActivity.this, ScanResultActivity.class);
        intent.putExtra("key", rawResult.getText());
        setResult(RESULT_OK, intent);
        startActivity(intent);

     */

        Intent intent = new Intent(ScanActivity.this, MainActivity.class);

        switch (result) {
            case "Operator":
                intent.putExtra("menu_key", R.menu.menu_operator);
                Toast.makeText(this, "Operator Module", Toast.LENGTH_SHORT).show();
                startActivity(intent);
                break;
            case "Supervisor":
                intent.putExtra("menu_key", R.menu.menu_supervisor);
                Toast.makeText(this, "Supervisor Module", Toast.LENGTH_SHORT).show();
                startActivity(intent);
                break;
            case "Trainee":
                intent.putExtra("menu_key", R.menu.menu_trainee);
                Toast.makeText(this, "Trainee Module", Toast.LENGTH_SHORT).show();
                startActivity(intent);
                break;
            case "Validator":
                intent.putExtra("menu_key", R.menu.menu_validator);
                Toast.makeText(this, "Validator Module", Toast.LENGTH_SHORT).show();
                startActivity(intent);
                break;
            case "Maintenance":
                intent.putExtra("menu_key", R.menu.menu_maintenance);
                Toast.makeText(this, "Maintenance Module", Toast.LENGTH_SHORT).show();
                startActivity(intent);
                break;
            default:
                Toast.makeText(this, "No user Found", Toast.LENGTH_SHORT).show();
                Intent intent1 = getIntent();
                finish();
                startActivity(intent1);
                break;
        }
        finish();
    }

    public static boolean hasPermissions(Context context, String... permissions) {
        if (context != null && permissions != null) {
            for (String permission : permissions) {
                if (ActivityCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                    return false;
                }
            }
        }
        return true;
    }
}