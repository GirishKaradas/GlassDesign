package com.example.android.glass.glassdesign;

import androidx.core.widget.ContentLoadingProgressBar;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

public class SplashActivity extends BaseActivity {

    ContentLoadingProgressBar bar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

   //     bar=findViewById(R.id.activity_splash_bar);
     //   bar.show();


        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                //Fixed activity is this
                Intent i = new Intent(SplashActivity.this, ScanActivity.class);
                startActivity(i);

                //For testing purpose only
             //   Intent intent    = new Intent(SplashActivity.this, MainActivity.class);
             //   intent.putExtra("menu_key", R.menu.menu_trainee);
              //  startActivity(intent);



                finish();
            }
        }, 3000);
    }

}