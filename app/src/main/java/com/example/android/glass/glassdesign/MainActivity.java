/*
 * Copyright 2019 Google LLC
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.android.glass.glassdesign;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;

import com.example.android.glass.glassdesign.menu.MenuActivity;
import com.example.glass.ui.GlassGestureDetector.Gesture;

/**
 * Main activity of the application. It provides viewPager to move between fragments.
 */
public class MainActivity extends BaseActivity {

    private String MENU_KEY="menu_key";
    protected static final int REQUEST_CODE = 200;
    private int menu;

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
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        menu = getIntent().getIntExtra(MENU_KEY, 0);
        Log.e("Mainactivity: ", String.valueOf(menu));

    }

    @Override
    public boolean onGesture(Gesture gesture) {
        switch (gesture) {
            case TAP:
                if (!hasPermissions(this, PERMISSIONS)) {
                    ActivityCompat.requestPermissions(this, PERMISSIONS, PERMISSION_ALL);
                }else{
                    Intent intent = new Intent(MainActivity.this, MenuActivity.class);
                    intent.putExtra(MENU_KEY, menu);
                    startActivityForResult(intent, REQUEST_CODE );
                }
                return true;
            default:
                return super.onGesture(gesture);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE && resultCode == RESULT_OK && data != null) {
            final int id = data.getIntExtra(MenuActivity.EXTRA_MENU_ITEM_ID_KEY,
                    MenuActivity.EXTRA_MENU_ITEM_DEFAULT_VALUE);
            String selectedOption = "";
            switch (id) {
                case R.id.bManual:
                    selectedOption = getString(R.string.lyo_manual);
                    startActivity(new Intent(this.getApplicationContext(), LyoManualActivity.class));
                    break;
                case R.id.bMonitor:
                    selectedOption = getString(R.string.monitor);
                    startActivity(new Intent(this.getApplicationContext(), MonitorActivity.class));
                    break;
                case R.id.bVideoCall:
                    selectedOption = getString(R.string.video_call);
                    Intent intent = new Intent(this.getApplicationContext(), VideoCallActivity.class);
                    intent.putExtra("step", "");
                    intent.putExtra("manual", "General");
                    startActivity(intent);
                    break;
                case R.id.bFirebase:
                    selectedOption = getString(R.string.tap_count);
                    startActivity(new Intent(this.getApplicationContext(), WebCallActivity.class));
                    break;

                case R.id.bHelp:
                    selectedOption = getString(R.string.help);
                    startActivity(new Intent(this.getApplicationContext(), TutorialActivity.class));
                    break;
                case R.id.bPlayVideo:
                    selectedOption = getString(R.string.play_video);
                    startActivity(new Intent(this.getApplicationContext(), VideoActivity.class));
                    break;
                case R.id.bScanner:
                    selectedOption = getString(R.string.qr_scanner);
                    startActivity(new Intent(this.getApplicationContext(), ScannerActivity.class));
                    break;

                case R.id.bJob:
                    selectedOption = "Jobs Module";
                    startActivity(new Intent(this.getApplicationContext(), JobActivity.class));
                    break;

                case R.id.bIQ:
                    selectedOption = "Quality Module";
                    startActivity(new Intent(this.getApplicationContext(), DQActivity.class));
                    break;

                case R.id.bDQ:
                    selectedOption = "Quality Module";
                    startActivity(new Intent(this.getApplicationContext(), DQActivity.class));
                    break;

                case R.id.bOQ:
                    selectedOption = "Quality Module";
                    startActivity(new Intent(this.getApplicationContext(), DQActivity.class));
                    break;
           /*     case R.id.bTracker:
                    selectedOption = "Live Tracking";
                    startActivity(new Intent(this.getApplicationContext(), SimpleImageTrackingActivity.class));
                    break;



            */
                case R.id.bTheme:
                    selectedOption = "Theme";
                    startActivity(new Intent(this.getApplicationContext(), ThemeActivity.class));
                    break;
            }
            Toast.makeText(this.getApplicationContext(),  "Opening "+selectedOption, Toast.LENGTH_SHORT)
                    .show();
        }
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
