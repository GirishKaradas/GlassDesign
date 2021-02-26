package com.example.android.glass.glassdesign;

import androidx.core.widget.ContentLoadingProgressBar;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.widget.Toast;

public class SplashActivity extends BaseActivity {

    ContentLoadingProgressBar bar;
    public static int color_code;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

   //     bar=findViewById(R.id.activity_splash_bar);
     //   bar.show();
        SharedPreferences sharedPreferences = getSharedPreferences("glass_prefs", Context.MODE_PRIVATE);

        if (sharedPreferences == null){
            SharedPreferences.Editor editor= sharedPreferences.edit();
            editor.putInt("theme_code", 1);
            editor.putInt("color_code", 1);
            editor.commit();
            editor.apply();
            color_code = 1;
        }else {
                 color_code = sharedPreferences.getInt("color_code", 1);
        }



        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                //Fixed activity is this
                Intent i = new Intent(SplashActivity.this, ScanActivity.class);
                startActivity(i);

                //For testing purpose only
         //       Intent intent    = new Intent(SplashActivity.this, MainActivity.class);
           //     intent.putExtra("menu_key", R.menu.menu_maintenance);
             //   startActivity(intent);

                finish();
            }
        }, 3000);
    }

}